package com.tfg.adapter.in.auditaspect;

import com.tfg.auditlog.AuditLog;
import com.tfg.auditlog.AuditLogId;
import com.tfg.indiba.IndibaSession;
import com.tfg.patient.Patient;
import com.tfg.physiotherapist.Physiotherapist;
import com.tfg.pni.PniReport;
import com.tfg.port.out.persistence.AuditLogRepository;
import com.tfg.trainingsession.TrainingSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditLogRepository auditLogRepository;

    public AuditAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @AfterReturning(
            "execution(* com.tfg.adapter.out.persistence..*.save(..))" +
            " && !within(com.tfg.adapter.out.persistence.auditlog..*)"
    )
    public void logAfterSave(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) return;
        Object entity = args[0];
        if (!isDomainEntity(entity)) return;
        try {
            writeLog(entity, "CREATE");
        } catch (Exception e) {
            log.error("Audit log failed for save on {}: {}", entity.getClass().getSimpleName(), e.getMessage());
        }
    }

    @AfterReturning("execution(* com.tfg.adapter.out.persistence..*.update(..))")
    public void logAfterUpdate(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length < 2) return;
        try {
            writeLog(args[1], "UPDATE");
        } catch (Exception e) {
            log.error("Audit log failed for update: {}", e.getMessage());
        }
    }

    private void writeLog(Object entity, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null) ? auth.getName() : "system";

        auditLogRepository.save(new AuditLog(
                new AuditLogId(ThreadLocalRandom.current().nextInt(1_000_000)),
                entity.getClass().getSimpleName(),
                action,
                LocalDateTime.now().toString(),
                buildDetails(entity),
                user
        ));
    }

    private boolean isDomainEntity(Object entity) {
        return entity instanceof Patient
                || entity instanceof IndibaSession
                || entity instanceof PniReport
                || entity instanceof TrainingSession
                || entity instanceof Physiotherapist;
    }

    private String buildDetails(Object entity) {
        if (entity instanceof Patient p) {
            return p.getLegalName() + " " + p.getSurname();
        }
        if (entity instanceof IndibaSession s) {
            return "Patient: " + s.getPatient().getLegalName() + " " + s.getPatient().getSurname()
                    + " | Area: " + s.getTreatedArea();
        }
        if (entity instanceof PniReport r) {
            return "Patient: " + r.getPatient().getLegalName() + " " + r.getPatient().getSurname();
        }
        if (entity instanceof TrainingSession t) {
            return "Patient: " + t.getPatient().getLegalName() + " " + t.getPatient().getSurname();
        }
        if (entity instanceof Physiotherapist ph) {
            return ph.getEmail().value();
        }
        return entity.getClass().getSimpleName();
    }
}
