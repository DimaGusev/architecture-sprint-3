package com.dgusev.sprint3.telemetryservice.service;

import com.dgusev.sprint3.telemetryservice.dto.TelemetryDto;
import com.dgusev.sprint3.telemetryservice.entity.TelemetryEntity;
import com.dgusev.sprint3.telemetryservice.repository.TelemetryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TelemetryService {

    @Autowired
    private TelemetryRepository telemetryRepository;

    public void insertNew(TelemetryDto request) {
        TelemetryEntity telemetryEntity = new TelemetryEntity();
        telemetryEntity.setUuid(UUID.randomUUID().toString());
        telemetryEntity.setCreatedAt(LocalDateTime.now());
        telemetryEntity.setDeviceUuid(request.getDeviceUuid());
        telemetryEntity.setHouseUuid(request.getHouseUuid());
        telemetryEntity.setMetricName(request.getMetricName());
        telemetryEntity.setMetricValue(request.getMetricValue());
        telemetryEntity.setTimestamp(request.getTimestamp());
        telemetryRepository.save(telemetryEntity);
    }

    public TelemetryDto getLast(String deviceUuid) {
        TelemetryEntity last = telemetryRepository.findFirstByDeviceUuidOrderByTimestampDesc(deviceUuid);
        if (last != null) {
            return convert(last);
        } else {
            return null;
        }
    }


    public List<TelemetryDto> getAll(String deviceUuid, LocalDateTime from, LocalDateTime to) {
        return telemetryRepository.findByDeviceUuidAndTimestampBetweenOrderByTimestamp(deviceUuid, from, to)
                .stream()
                .map(this::convert)
                .toList();
    }


    private TelemetryDto convert(TelemetryEntity entity) {
        TelemetryDto telemetryDto = new TelemetryDto();
        telemetryDto.setDeviceUuid(entity.getDeviceUuid());
        telemetryDto.setHouseUuid(entity.getHouseUuid());
        telemetryDto.setMetricName(entity.getMetricName());
        telemetryDto.setMetricValue(entity.getMetricValue());
        telemetryDto.setTimestamp(entity.getTimestamp());
        return telemetryDto;
    }
}
