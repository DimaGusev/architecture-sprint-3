package com.dgusev.sprint3.deviceservice.dto;

import lombok.Data;

@Data
public class RegisterDeviceDto {
    private String moduleUuid;
    private String houseUuid;
    private String name;
    private String serialNumber;
    private DeviceStatus status;
    private String url;
}
