package com.dgusev.sprint3.deviceservice.repository;

import com.dgusev.sprint3.deviceservice.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, String> {
}
