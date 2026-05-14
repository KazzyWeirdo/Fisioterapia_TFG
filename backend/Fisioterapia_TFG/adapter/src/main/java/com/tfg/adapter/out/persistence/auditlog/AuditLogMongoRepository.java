package com.tfg.adapter.out.persistence.auditlog;

import com.tfg.auditlog.AuditLog;
import com.tfg.pojos.pagedpojos.PageQuery;
import com.tfg.pojos.pagedpojos.PagedResponse;
import com.tfg.port.out.persistence.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditLogMongoRepository implements AuditLogRepository {
    private final AuditLogMongoDataRepository auditLogMongoDataRepository;

    public AuditLogMongoRepository(AuditLogMongoDataRepository auditLogMongoDataRepository) {
        this.auditLogMongoDataRepository = auditLogMongoDataRepository;
    }

    @Override
    public void save(AuditLog auditLog) {
        auditLogMongoDataRepository.save(AuditLogMongoMapper.toMongoEntity(auditLog));
    }

    @Override
    public void deleteAll() {
        auditLogMongoDataRepository.deleteAll();
    }

    @Override
    public PagedResponse<AuditLog> findAll(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size(),
                Sort.by(Sort.Direction.DESC, "timestamp"));

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
