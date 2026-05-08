package com.tfg.adapter.out.persistence.trainingsession;

import com.tfg.adapter.out.persistence.exercisetemplate.ExerciseTemplateJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ExercisesJpaEntity {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_template_id")
    private ExerciseTemplateJpaEntity exerciseTemplate;
    private String name;
    @ElementCollection
    @CollectionTable(
            name = "exercise_sets",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<ExerciseSetJpaEmbeddable> sets = new ArrayList<>();

    public void addSet(ExerciseSetJpaEmbeddable set) {
        this.sets.add(set);
    }
}
