package com.mycompany.store.repository;

import com.mycompany.store.domain.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    default Optional<Product> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }


    default List<Product> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default List<Product> findAllWithEagerRelationships(Boolean isActive) {
        return this.getAllActiveProductsList(isActive);
    }

    /*default Optional<Product> findOneWithEagerRelationships(Long id) {

        return this.disableTheProduct(id);
    }*/

    default Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct product from Product product left join fetch product.productCategory",
        countQuery = "select count(distinct product) from Product product"
    )
    Page<Product> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct product from Product product left join fetch product.productCategory")
    List<Product> findAllWithToOneRelationships();

    @Query("select product from Product product left join fetch product.productCategory where product.id =:id")
    Optional<Product> findOneWithToOneRelationships(@Param("id") Long id);

/*
    @Query("select product from Product product left join fetch product.productCategory where product.id =:id")
    Optional<Product> disableTheProduct(@Param("id") Long id);
*/

    @Query("select product from Product product where product.isActive =:isActive")
    List<Product> getAllActiveProductsList(@Param("isActive") Boolean isActive);
}
