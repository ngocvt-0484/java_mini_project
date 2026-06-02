package com.example.employeemanagement.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SystemScheduler {
    // schedule: 30s
    @Scheduled(fixedRate = 30000)
    public void logSystemStatus() {
        log.info("System running");
    }
}
