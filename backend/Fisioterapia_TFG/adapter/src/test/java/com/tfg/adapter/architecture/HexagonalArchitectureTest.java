package com.tfg.adapter.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "com.tfg")
class HexagonalArchitectureTest {

    // -------------------------------------------------------------------------
    // 1. Layer isolation
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule model_does_not_depend_on_application_or_adapter =
        noClasses()
            .that().resideInAnyPackage(
                "com.tfg.patient..",
                "com.tfg.trainingsession..",
                "com.tfg.indiba..",
                "com.tfg.pni..",
                "com.tfg.statistics..",
                "com.tfg.physiotherapist..",
                "com.tfg.passwordresettoken.."
            )
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.tfg.service..",
                "com.tfg.port..",
                "com.tfg.adapter..",
                "com.tfg.exceptions..",
                "com.tfg.pojos.."
            )
            .because("Domain classes must be pure Java with no dependency on application or adapter layers");

    @ArchTest
    static final ArchRule application_does_not_depend_on_adapter =
        noClasses()
            .that().resideInAnyPackage("com.tfg.service..", "com.tfg.port..")
            .should().dependOnClassesThat().resideInAPackage("com.tfg.adapter..")
            .because("Application layer must not know about adapter (infrastructure) implementations");

    @ArchTest
    static final ArchRule layered_architecture_is_respected =
        layeredArchitecture()
            .consideringAllDependencies()
            // com.tfg (root) covers SpringAppConfig; com.tfg.configuration covers security wiring classes
            .layer("Adapter").definedBy("com.tfg.adapter..", "com.tfg.configuration..", "com.tfg")
            .layer("Application").definedBy("com.tfg.service..", "com.tfg.port..", "com.tfg.exceptions..", "com.tfg.pojos..")
            .layer("Model").definedBy(
                "com.tfg.patient..", "com.tfg.trainingsession..", "com.tfg.indiba..",
                "com.tfg.pni..", "com.tfg.statistics..", "com.tfg.physiotherapist..", "com.tfg.passwordresettoken.."
            )
            .whereLayer("Adapter").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter")
            .whereLayer("Model").mayOnlyBeAccessedByLayers("Adapter", "Application")
            // test-support factories/tests live under com.tfg.model and are not production code
            .ignoreDependency(resideInAPackage("com.tfg.model.."), alwaysTrue());

    // -------------------------------------------------------------------------
    // 2. Framework-free domain
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule model_has_no_spring_annotations =
        noClasses()
            .that().resideInAnyPackage(
                "com.tfg.patient..",
                "com.tfg.trainingsession..",
                "com.tfg.indiba..",
                "com.tfg.pni..",
                "com.tfg.statistics..",
                "com.tfg.physiotherapist..",
                "com.tfg.passwordresettoken.."
            )
            .should().beAnnotatedWith("org.springframework.stereotype.Service")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Component")
            .orShould().beAnnotatedWith("org.springframework.stereotype.Repository")
            .because("Domain classes must not carry Spring stereotype annotations");

    @ArchTest
    static final ArchRule model_has_no_jpa_entity_annotation =
        noClasses()
            .that().resideInAnyPackage(
                "com.tfg.patient..",
                "com.tfg.trainingsession..",
                "com.tfg.indiba..",
                "com.tfg.pni..",
                "com.tfg.statistics..",
                "com.tfg.physiotherapist..",
                "com.tfg.passwordresettoken.."
            )
            .should().beAnnotatedWith("jakarta.persistence.Entity")
            .because("JPA @Entity must only appear in the adapter persistence layer, not in domain classes");

    // -------------------------------------------------------------------------
    // 3. Ports are interfaces
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule input_ports_are_interfaces =
        classes()
            .that().resideInAPackage("com.tfg.port.in..")
            .should().beInterfaces()
            .because("Input ports define use case contracts and must be interfaces");

    @ArchTest
    static final ArchRule output_ports_are_interfaces =
        classes()
            .that().resideInAPackage("com.tfg.port.out..")
            .and().areNotMemberClasses()
            .should().beInterfaces()
            .because("Output ports define infrastructure contracts and must be interfaces");

    // -------------------------------------------------------------------------
    // 4. Services implement only input ports
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule services_do_not_depend_on_adapter_out =
        noClasses()
            .that().resideInAPackage("com.tfg.service..")
            .should().dependOnClassesThat().resideInAPackage("com.tfg.adapter.out..")
            .because("Services must depend only on output port interfaces, never on concrete adapter implementations");

    @ArchTest
    static final ArchRule services_do_not_depend_on_adapter_in =
        noClasses()
            .that().resideInAPackage("com.tfg.service..")
            .should().dependOnClassesThat().resideInAPackage("com.tfg.adapter.in..")
            .because("Services must not know about REST controllers or input adapters");

    // -------------------------------------------------------------------------
    // 5. Thin controllers
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule rest_controllers_do_not_depend_on_persistence =
        noClasses()
            .that().resideInAPackage("com.tfg.adapter.in.rest..")
            .should().dependOnClassesThat().resideInAPackage("com.tfg.adapter.out..")
            .because("REST controllers must be thin: they call use cases via input ports, never persistence adapters directly");

    @ArchTest
    static final ArchRule rest_controllers_are_annotated =
        classes()
            .that().resideInAPackage("com.tfg.adapter.in.rest..")
            .and().haveSimpleNameEndingWith("Controller")
            .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController")
            .because("Every class named *Controller in the REST package must be a @RestController");

    // -------------------------------------------------------------------------
    // 6. No cyclic dependencies
    // -------------------------------------------------------------------------

    @ArchTest
    static final ArchRule no_cycles_between_slices =
        slices()
            .matching("com.tfg.(*)..")
            .should().beFreeOfCycles()
            .because("Cyclic dependencies between top-level packages indicate a broken design");
}
