package com.dgusev.sprint3.deviceservice.service;

import com.dgusev.sprint3.deviceservice.dto.DeviceInfo;
import com.dgusev.sprint3.deviceservice.dto.RegisterDeviceDto;
import com.dgusev.sprint3.deviceservice.dto.UpdateStatusRequest;
import com.dgusev.sprint3.deviceservice.entity.DeviceEntity;
import com.dgusev.sprint3.deviceservice.entity.ModuleEntity;
import com.dgusev.sprint3.deviceservice.exceptions.DeviceNotFoundException;
import com.dgusev.sprint3.deviceservice.repository.DeviceRepository;
import com.dgusev.sprint3.deviceservice.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public void updateStatus(String deviceId, UpdateStatusRequest request) {
        DeviceEntity device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        device.setStatus(request.getStatus());
        deviceRepository.save(device);
        //TODO sent request to device
    }

    public DeviceInfo registerDevice(RegisterDeviceDto registerDeviceDto) {
        ModuleEntity moduleEntity = moduleRepository.findById(registerDeviceDto.getModuleUuid()).orElseThrow(() -> new IllegalArgumentException("Module not found"));
        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setUuid(UUID.randomUUID().toString());
        deviceEntity.setName(registerDeviceDto.getName());
        deviceEntity.setSerialNumber(registerDeviceDto.getSerialNumber());
        deviceEntity.setHouseUuid(registerDeviceDto.getHouseUuid());
        deviceEntity.setStatus(registerDeviceDto.getStatus());
        deviceEntity.setUrl(registerDeviceDto.getUrl());
        deviceEntity.setDeviceModule(moduleEntity);
        deviceRepository.save(deviceEntity);
        return convertToInfo(deviceEntity);
    }

    public DeviceInfo getDevice(String deviceId) {
        DeviceEntity device = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        return convertToInfo(device);
    }


    public DeviceInfo updateDevice(String deviceId, RegisterDeviceDto registerDeviceDto) {
        DeviceEntity deviceEntity = deviceRepository.findById(deviceId).orElseThrow(() -> new DeviceNotFoundException("Device not found"));
        deviceEntity.setName(registerDeviceDto.getName());
        deviceEntity.setSerialNumber(registerDeviceDto.getSerialNumber());
        deviceEntity.setUrl(registerDeviceDto.getUrl());
        deviceRepository.save(deviceEntity);
        return convertToInfo(deviceEntity);
    }


    private DeviceInfo convertToInfo(DeviceEntity device) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setUuid(device.getUuid());
        deviceInfo.setName(device.getName());
        deviceInfo.setHouseUuid(device.getHouseUuid());
        deviceInfo.setSerialNumber(device.getSerialNumber());
        deviceInfo.setUrl(device.getUrl());
        deviceInfo.setStatus(device.getStatus());
        DeviceInfo.Module module = new DeviceInfo.Module();
        module.setUuid(device.getDeviceModule().getUuid());
        module.setType(device.getDeviceModule().getType());
        module.setName(device.getDeviceModule().getName());
        module.setModel(device.getDeviceModule().getModel());
        module.setSupplier(device.getDeviceModule().getSupplier());
        module.setSite(device.getDeviceModule().getSite());
        deviceInfo.setModule(module);
        return deviceInfo;
    }
}
