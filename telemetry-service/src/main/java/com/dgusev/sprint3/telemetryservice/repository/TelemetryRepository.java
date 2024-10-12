package com.dgusev.sprint3.telemetryservice.repository;

import com.dgusev.sprint3.telemetryservice.entity.TelemetryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryEntity, String> {

    TelemetryEntity findFirstByDeviceUuidOrderByTimestampDesc(String deviceUuid);

    List<TelemetryEntity> findByDeviceUuidAndTimestampBetweenOrderByTimestamp(String deviceUuid, LocalDateTime from, LocalDateTime to);
}
