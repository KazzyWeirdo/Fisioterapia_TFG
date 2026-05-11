package com.tfg.application.port.in.statistics;

import com.tfg.model.statistics.PathologyRehabilitationStats;
import java.util.List;

public interface GetPathologyRehabilitationStatsUseCase {
    List<PathologyRehabilitationStats> getPathologyRehabilitationStats();
}
