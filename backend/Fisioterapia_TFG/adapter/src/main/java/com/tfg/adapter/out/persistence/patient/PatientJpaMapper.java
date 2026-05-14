package com.tfg.adapter.out.persistence.patient;

import com.tfg.patient.Patient;
import com.tfg.patient.PatientDNI;
import com.tfg.patient.PatientEmail;
import com.tfg.patient.PatientId;

public class PatientJpaMapper {

    public static PatientJpaEntity toJpaEntity(Patient patient) {
        PatientJpaEntity entity = new PatientJpaEntity();
        entity.setId(patient.getId().value());
        entity.setLegalName(patient.getLegalName());
        entity.setNameToUse(patient.getNameToUse());
        entity.setSurname(patient.getSurname());
        entity.setSecondSurname(patient.getSecondSurname());
        entity.setGenderIdentity(patient.getGenderIdentity());
        entity.setClinicalUseSex(patient.getClinicalUseSex());
        entity.setAdministrativeSex(patient.getAdministrativeSex());
        entity.setDni(patient.getDni().value());
        entity.setEmail(patient.getEmail().value());
        entity.setPhoneNumber(patient.getPhoneNumber());
        entity.setPronouns(patient.getPronouns());
        entity.setDateOfBirth(patient.getDateOfBirth());
        entity.setPolarAccessToken(patient.getPolarAccessToken());
        entity.setPolarUserId(patient.getPolarUserId());
        return entity;
    }

    public static Patient toModelEntity(PatientJpaEntity entity) {
        return new Patient(
                new PatientId(entity.getId()),
                new PatientEmail(entity.getEmail()),
                new PatientDNI(entity.getDni()),
                entity.getGenderIdentity(),
                entity.getClinicalUseSex(),
                entity.getAdministrativeSex(),
                entity.getLegalName(),
                entity.getNameToUse(),
                entity.getSurname(),
                entity.getSecondSurname(),
                entity.getPronouns(),
                entity.getDateOfBirth(),
                entity.getPhoneNumber(),
                entity.getPolarAccessToken(),
                entity.getPolarUserId()
        );
    }
}
