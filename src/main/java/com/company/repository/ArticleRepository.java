package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import com.company.mapper.ArticleSimpleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer> {

    Page<ArticleEntity> findByVisible(Boolean visible, Pageable pageable);

    Optional<ArticleEntity> findByTitle(String title);

    @Transactional
    @Modifying
    @Query("update ArticleEntity set visible = :visible where id = :id")
    int updateVisible(@Param("visible") Boolean visible, @Param("id") Integer id);

    public List<ArticleEntity> findTop5ByTypeIdAndStatus(Integer typeId, ArticleStatus status, Sort sort);

    /*@Query("SELECT new ArticleEntity(a.id,a.title,a.description,a.attach.id,a.publishedDate) " +
            " FROM ArticleEntity a  where  a.type.id =:typeId and status =:status order by createdDate desc")
    public List<ArticleEntity> getTypeId(@Param("typeId") Integer typeId, @Param("status") ArticleStatus status);*/


    @Query(value = "SELECT a.id,a.title,a.description,a.attach_id,a.published_date " +
            " FROM article AS a  where  a.type_id =:typeId and status =:status order by created_date desc Limit 5", nativeQuery = true)
    public List<ArticleSimpleMapper> getTypeId(@Param("typeId") Integer typeId, @Param("status") String status);


    public Page<ArticleEntity> findAllByTypeId(Integer typeId, Pageable pageable);

    public Optional<ArticleEntity> findByIdAndStatus(Integer id, ArticleStatus status);

    Page<ArticleEntity> findByRegionIdAndStatus(Integer regionId, Pageable pageable, ArticleStatus status);

    Page<ArticleEntity> findByCategoryIdAndStatus(Integer cId, Pageable pageable, ArticleStatus status);

    Page<ArticleEntity> findByTypeIdAndStatus(Integer tId, Pageable pageable, ArticleStatus status);


    @Query(value = "select a.id, a.title, a.description, a.attach_id, a.published_date from" +
            " article as a where a.status = :status order by a.created_date desc limit 4",
            nativeQuery = true)
    List<ArticleSimpleMapper> getLast4(@Param("status") String status);

    @Query(value = "select a.id, a.title, a.description, a.attach_id, a.published_date from" +
            " article as a where a.region_id = :regionId and a.status = :status order by a.created_date desc limit 4",
            nativeQuery = true)
    List<ArticleSimpleMapper> getByRegionIdLast4(@Param("regionId") Integer regionId, @Param("status") String status);

    @Query(value = "select a.id, a.title, a.description, a.attach_id, a.published_date from" +
            " article as a where a.category_id = :categoryId and a.status = :status order by a.created_date desc limit 4",
            nativeQuery = true)
    List<ArticleSimpleMapper> getByCategoryIdLast4(@Param("categoryId") Integer categoryId, @Param("status") String status);


    @Transactional
    @Modifying
    @Query("update ArticleEntity set status = :status where id = :id")
    int updateStatus(@Param("status") ArticleStatus status, @Param("id") Integer id);


    @Modifying
    @Transactional
    @Query(value = "update ArticleEntity set sharedCount= :shared where id=:id")
    void updateSharedCount(@Param("shared") Integer share, @Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "update ArticleEntity set sharedCount= sharedCount +1 where id=:id")
    void updateSharedCount(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(value = "update ArticleEntity a set a.viewCount = a.viewCount + 1 where a.id =:id")
    void updateViewCount(@Param("id") Integer id);
}
