package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.PatientGender;
import com.tfg.patient.PatientSex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientJpaDataRepository extends JpaRepository<PatientJpaEntity, Integer> {
    Optional<PatientJpaEntity> findByEmail(String email);
    Optional<PatientJpaEntity> findByDni(String dni);

    @Modifying
    @Query("UPDATE PatientJpaEntity p SET p.id = :id, p.email = :email, p.dni = :dni, p.genderIdentity = :gender, p.administrativeSex = :administrativeSex, p.legalName = :legalName, p.nameToUse = :nameToUse, p.surname = :surname, p.secondSurname = :secondSurname, p.pronouns = :pronouns, p.dateOfBirth = :dateOfBirth, p.phoneNumber = :phoneNumber WHERE p.id = :id")
    void updatePatientById(
            @Param("id") Integer id,
            @Param("email") String email,
            @Param("dni") String dni,
            @Param("gender") PatientGender genderIdentity,
            @Param("administrativeSex") PatientSex administrativeSex,
            @Param("legalName") String legalName,
            @Param("nameToUse") String nameToUse,
            @Param("surname") String surname,
            @Param("secondSurname") String secondSurname,
            @Param("pronouns") String pronouns,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("phoneNumber") int phoneNumber
    );

    @Query("SELECT p FROM PatientJpaEntity p WHERE p.polarAccessToken IS NOT NULL")
    List<PatientJpaEntity> findAllWithPolarToken();

    @Query("SELECT new com.tfg.adapter.out.persistence.patient.PatientSummaryJpaProjection(p.id, p.nameToUse, p.surname) FROM PatientJpaEntity p")
    Page<PatientSummaryJpaProjection> findSummaries(Pageable pageable);
}
