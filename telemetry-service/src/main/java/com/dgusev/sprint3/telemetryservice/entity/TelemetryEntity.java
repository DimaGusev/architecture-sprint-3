package com.dgusev.sprint3.telemetryservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_telemetry")
@Getter
@Setter
public class TelemetryEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "house_uuid")
    private String houseUuid;

    @Column(name = "device_uuid")
    private String deviceUuid;

    @Column(name = "metric_name")
    private String metricName;

    @Column(name = "metric_value")
    private String metricValue;

    @Column(name = "metric_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
