package com.dgusev.sprint3.deviceservice.entity;

import com.dgusev.sprint3.deviceservice.dto.DeviceStatus;
import com.dgusev.sprint3.deviceservice.dto.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "device_module")
@Getter
@Setter
public class DeviceEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @JoinColumn(name = "module_uuid")
    @ManyToOne
    private ModuleEntity deviceModule;

    @Column(name = "house_uuid")
    private String houseUuid;

    @Column(name = "name")
    private String name;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private DeviceStatus status;

    @Column(name = "url")
    private String url;
}
