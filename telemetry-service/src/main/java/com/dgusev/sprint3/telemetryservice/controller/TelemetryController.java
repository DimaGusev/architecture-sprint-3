package com.dgusev.sprint3.telemetryservice.controller;

import com.dgusev.sprint3.telemetryservice.dto.TelemetryDto;
import com.dgusev.sprint3.telemetryservice.service.TelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class TelemetryController {

    @Autowired
    private TelemetryService telemetryService;

    @PostMapping(value = "/telemetry")
    public void newTelemetry(@RequestBody TelemetryDto request) {
        log.info("New metric received {}", request);
        telemetryService.insertNew(request);
    }


    @GetMapping(value = "/devices/{deviceUuid}/telemetry/latest")
    public TelemetryDto getLast(@PathVariable(name = "deviceUuid") String deviceUuid) {
        log.info("Get last metric for {}", deviceUuid);
        return telemetryService.getLast(deviceUuid);
    }


    @GetMapping(value = "/devices/{deviceUuid}/telemetry")
    public List<TelemetryDto> getAll(@PathVariable(name = "deviceUuid") String deviceUuid,
                                     @RequestParam(name = "from") LocalDateTime from,
                                     @RequestParam(name = "to") LocalDateTime to) {
        log.info("Get all metric for {} from {} to {}", deviceUuid, from, to);
        return telemetryService.getAll(deviceUuid, from, to);
    }

}
