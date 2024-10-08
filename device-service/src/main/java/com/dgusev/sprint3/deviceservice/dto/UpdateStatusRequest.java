package com.dgusev.sprint3.deviceservice.dto;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private DeviceStatus status;
}
