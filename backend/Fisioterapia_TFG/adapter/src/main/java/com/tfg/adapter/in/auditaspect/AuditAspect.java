package com.tfg.adapter.in.auditaspect;

import com.tfg.auditlog.AuditLog;
import com.tfg.auditlog.AuditLogId;
import com.tfg.port.out.persistence.AuditLogRepository;
import com.tfg.physiotherapist.Physiotherapist;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    public AuditAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @AfterReturning("execution(* com.tfg.adapter.out.persistence.*.save(..))")
    public void logAfterSave(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        String entityType = entity.getClass().getSimpleName();
        String details = "Entity Saved";

        if (entity instanceof Physiotherapist p) {
            details = p.getEmail().value();
        }
        AuditLog log = new AuditLog(
                new AuditLogId(ThreadLocalRandom.current().nextInt(1_000_000)),
                "CREATE/UPDATE",
                entityType,
                LocalDateTime.now().toString(),
                details,
                "SYSTEM" // TODO: Extraer de SecurityContextHolder.getContext().getAuthentication().getName()
        );

        auditLogRepository.save(log);
    }
}
