package com.company.repository;

import com.company.entity.CategoryEntity;
import com.company.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Integer> {
    List<RegionEntity> findAllByVisible(Boolean visible);


    Optional<RegionEntity> findByKey(String key);

    @Transactional
    @Modifying
    @Query("update RegionEntity set visible = :visible where id = :id")
    int updateVisible(@Param("visible") Boolean visible, @Param("id") Integer id);
}
