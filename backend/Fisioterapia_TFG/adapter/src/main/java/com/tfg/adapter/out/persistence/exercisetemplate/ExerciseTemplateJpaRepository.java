package com.tfg.adapter.out.persistence.exercisetemplate;

import com.tfg.port.out.persistence.ExerciseTemplateRepository;
import com.tfg.trainingsession.ExerciseTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ExerciseTemplateJpaRepository implements ExerciseTemplateRepository {

    private final ExerciseTemplateJpaDataRepository jpaDataRepository;

    public ExerciseTemplateJpaRepository(ExerciseTemplateJpaDataRepository jpaDataRepository) {
        this.jpaDataRepository = jpaDataRepository;
    }

    @Override
    @Transactional
    public void save(ExerciseTemplate template) {
        ExerciseTemplateJpaEntity entity = ExerciseTemplateJpaMapper.toJpaEntity(template);
        jpaDataRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseTemplate> findAll() {
        return jpaDataRepository.findAll().stream()
                .map(ExerciseTemplateJpaMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseTemplate> findById(int id) {
        return jpaDataRepository.findById(id)
                .map(ExerciseTemplateJpaMapper::toModel);
    }

    @Override
    public void deleteAll() {
        jpaDataRepository.deleteAll();
    }
}
