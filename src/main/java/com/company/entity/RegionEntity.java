package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "region")
@Getter
@Setter
public class RegionEntity extends BaseEntity {

    @Column(name = "name_uz", nullable = false, unique = true)
    private String nameUz;
    @Column(name = "name_ru", nullable = false, unique = true)
    private String nameRu;
    @Column(name = "name_en", nullable = false, unique = true)
    private String nameEn;
    @Column
    private Boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;
    @Column(nullable = false, unique = true)
    private String key;


}
