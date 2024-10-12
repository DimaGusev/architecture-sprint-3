package com.dgusev.sprint3.deviceservice.dto;

import com.dgusev.sprint3.deviceservice.entity.ModuleEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class DeviceInfo {

    private String uuid;
    private Module module;
    private String houseUuid;
    private String name;
    private String serialNumber;
    private DeviceStatus status;
    private String url;

    @Data
    public static class Module {
        private String uuid;
        private DeviceType type;
        private String name;
        private String model;
        private String supplier;
        private String site;
    }
}
