package com.dgusev.sprint3.telemetryservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TelemetryDto {
    private String houseUuid;
    private String deviceUuid;
    private String metricName;
    private String metricValue;
    private LocalDateTime timestamp;
}
