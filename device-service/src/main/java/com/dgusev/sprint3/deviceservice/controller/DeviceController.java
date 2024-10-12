package com.dgusev.sprint3.deviceservice.controller;

import com.dgusev.sprint3.deviceservice.dto.DeviceCommand;
import com.dgusev.sprint3.deviceservice.dto.DeviceInfo;
import com.dgusev.sprint3.deviceservice.dto.RegisterDeviceDto;
import com.dgusev.sprint3.deviceservice.dto.UpdateStatusRequest;
import com.dgusev.sprint3.deviceservice.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PutMapping(value = "/devices/{deviceId}/status")
    public void updateStatus(@PathVariable(name = "deviceId") String deviceId, @RequestBody UpdateStatusRequest request) {
        log.info("Request to update status of device {} to {}", deviceId, request.getStatus());
        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        deviceService.updateStatus(deviceId, request);
    }

    @PostMapping(value = "/devices/register")
    public DeviceInfo registerDevice(@RequestBody RegisterDeviceDto registerDeviceDto) {
        log.info("Request to register device {}", registerDeviceDto);
        return deviceService.registerDevice(registerDeviceDto);
    }

    @GetMapping(value = "/devices/{deviceId}")
    public DeviceInfo getDevice(@PathVariable(name = "deviceId") String deviceId) {
        log.info("Request to get device {}", deviceId);
        return deviceService.getDevice(deviceId);
    }

    @PutMapping(value = "/devices/{deviceId}")
    public DeviceInfo updateDevice(@PathVariable(name = "deviceId") String deviceId, @RequestBody RegisterDeviceDto registerDeviceDto) {
        log.info("Request to update device {} with data {}", deviceId, registerDeviceDto);
        return deviceService.updateDevice(deviceId, registerDeviceDto);
    }

    @PostMapping(value = "/devices/{deviceId}/commands")
    public void sendCommands(@PathVariable(name = "deviceId") String deviceId, @RequestBody DeviceCommand commands) {
        log.info("Request to send command {} device {}", commands, deviceId);
        log.info("Send command {}", commands);
    }

}
