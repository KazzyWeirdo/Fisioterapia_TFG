package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.auditlog.PageQuery;
import com.tfg.pojos.auditlog.PagedResponse;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class AuditLogMongoRepository implements AuditLogRepository {
    private final AuditLogMongoDataRepository auditLogMongoDataRepository;

    public AuditLogMongoRepository(AuditLogMongoDataRepository auditLogMongoDataRepository) {
        this.auditLogMongoDataRepository = auditLogMongoDataRepository;
    }

    @Override
    @Transactional
    public void save(AuditLog auditLog) {
        auditLogMongoDataRepository.save(AuditLogMongoMapper.toMongoEntity(auditLog));
    }

    @Override
    public void deleteAll() {
        auditLogMongoDataRepository.deleteAll();
    }

    @Override
    public PagedResponse<AuditLog> findAll(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());

        Page<AuditLogMongoEntity> page = auditLogMongoDataRepository.findAll(pageable);

        List<AuditLog> content = page.getContent().stream()
                .map(AuditLogMongoMapper::toDomainEntity)
                .toList();

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.isLast()
        );
    }
}
