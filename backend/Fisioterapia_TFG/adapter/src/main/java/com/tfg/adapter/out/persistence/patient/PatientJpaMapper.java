package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;

public class PatientJpaMapper {

    public static PatientJpaEntity toJpaEntity(Patient patient) {
        PatientJpaEntity entity = new PatientJpaEntity();
        entity.setId(patient.getId().value());
        entity.setName(patient.getName());
        entity.setSurname(patient.getSurname());
        entity.setSecondSurname(patient.getSecondSurname());
        entity.setGender(patient.getGender().toString());
        entity.setDni(patient.getDni().value());
        entity.setEmail(patient.getEmail().value());
        entity.setPhoneNumber(patient.getPhoneNumber());
        entity.setDateOfBirth(patient.getDateOfBirth());
        return entity;
    }

    public static Patient toModelEntity(PatientJpaEntity entity) {
        return new Patient(
                new com.tfg.patient.PatientId(entity.getId()),
                new com.tfg.patient.PatientEmail(entity.getEmail()),
                new com.tfg.patient.PatientDNI(entity.getDni()),
                com.tfg.patient.PatientGender.valueOf(entity.getGender()),
                entity.getName(),
                entity.getSurname(),
                entity.getSecondSurname(),
                entity.getDateOfBirth(),
                entity.getPhoneNumber()
        );
    }
}
