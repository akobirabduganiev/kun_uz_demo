package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity{

    @Column(name = "name_uz",nullable = false, unique = true)
    private String nameUz;
    @Column(name = "name_ru",nullable = false, unique = true)
    private String nameRu;
    @Column(name = "name_en",nullable = false, unique = true)
    private String nameEn;

    @Column
    private Integer profileId;
    @Column(nullable = false, unique = true)
    private String key;

}
