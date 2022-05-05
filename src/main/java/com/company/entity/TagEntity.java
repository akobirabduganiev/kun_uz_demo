package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TagEntity.TABLE_NAME)
@Getter
@Setter
public class TagEntity extends BaseEntity {
    static final String TABLE_NAME = "tag";


    @Column(nullable = false, unique = true)
    private String key;

    @Column(name = "name_uz", nullable = false, unique = true)
    private String nameUz;
    @Column(name = "name_ru", nullable = false, unique = true)
    private String nameRu;
    @Column(name = "name_en", nullable = false, unique = true)
    private String nameEn;

    @Column
    private Boolean visible = true;

    @Column(name = "profile_id", nullable = false)
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;
}


