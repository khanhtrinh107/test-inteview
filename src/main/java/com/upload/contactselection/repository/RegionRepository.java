package com.upload.contactselection.repository;

import com.upload.contactselection.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, String> {

    List<Region> findByDeleteFlagOrderByDisplayOrder(Integer deleteFlag);

}