package com.dgusev.sprint3.deviceservice.entity;

import com.dgusev.sprint3.deviceservice.dto.DeviceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "device_module")
@Getter
@Setter
public class ModuleEntity {

    @Id
    @Column(name = "uuid")
    private String uuid;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "site")
    private String site;

}
