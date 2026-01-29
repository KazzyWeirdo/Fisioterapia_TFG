package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.PatientGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PatientJpaDataRepository extends JpaRepository<PatientJpaEntity, Integer> {
    Optional<PatientJpaEntity> findByEmail(String email);
    Optional<PatientJpaEntity> findByDni(String dni);

    @Modifying
    @Query("UPDATE PatientJpaEntity p SET p.id = :id, p.email = :email, p.dni = :dni, p.gender = :gender, p.name = :name, p.surname = :surname, p.secondSurname = :secondSurname, p.dateOfBirth = :dateOfBirth, p.phoneNumber = :phoneNumber WHERE p.id = :id")
    void updatePatientById(
            @Param("id") Integer id,
            @Param("email") String email,
            @Param("dni") String dni,
            @Param("gender") PatientGender gender,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("secondSurname") String secondSurname,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("phoneNumber") int phoneNumber
    );
}
