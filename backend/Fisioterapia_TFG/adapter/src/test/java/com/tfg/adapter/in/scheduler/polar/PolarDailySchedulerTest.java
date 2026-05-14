package com.tfg.adapter.in.scheduler.polar;

import com.tfg.port.in.polar.SyncPolarDataUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PolarDailySchedulerTest {

    @Mock
    private SyncPolarDataUseCase syncPolarDataUseCase;

    @InjectMocks
    private PolarDailyScheduler polarDailyScheduler;

    @Test
    void testRunDailySync() {
        polarDailyScheduler.runDailySync();

        verify(syncPolarDataUseCase, times(1)).executeDailySync();
    }
}
