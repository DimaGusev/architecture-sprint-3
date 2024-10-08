package com.dgusev.sprint3.deviceservice.dto;

import lombok.Data;

@Data
public class DeviceCommand {
    private String command;
    private String argument;
}
