package com.tfg.adapter.in.scheduler.polar;

import com.tfg.port.in.polar.SyncPolarDataUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PolarDailyScheduler {
    private final SyncPolarDataUseCase syncPolarDataUseCase;

    public PolarDailyScheduler(SyncPolarDataUseCase syncPolarDataUseCase) {
        this.syncPolarDataUseCase = syncPolarDataUseCase;
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void runDailySync() {
        System.out.println("--- Iniciando Sincronización Polar ---");
        syncPolarDataUseCase.executeDailySync();
        System.out.println("--- Finalizada Sincronización Polar ---");
    }
}
