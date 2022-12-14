package com.mycompany.store.service;

import com.mycompany.store.domain.OrderItem;
import com.mycompany.store.domain.Product;
import com.mycompany.store.repository.OrderItemRepository;
import com.mycompany.store.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;

    public ProductService(ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Update a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product update(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getName() != null) {
                    existingProduct.setName(product.getName());
                }
                if (product.getDescription() != null) {
                    existingProduct.setDescription(product.getDescription());
                }
                if (product.getPrice() != null) {
                    existingProduct.setPrice(product.getPrice());
                }
                if (product.getSized() != null) {
                    existingProduct.setSized(product.getSized());
                }
                if (product.getImage() != null) {
                    existingProduct.setImage(product.getImage());
                }
                if (product.getImageContentType() != null) {
                    existingProduct.setImageContentType(product.getImageContentType());
                }
                if (product.getEhsanComment() != null) {
                    existingProduct.setEhsanComment(product.getEhsanComment());
                }
                if (product.getIsActive() != null) {
                    existingProduct.setIsActive(product.getIsActive());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    // return object instead of void from delete or deleteObject custom method
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        Optional<OrderItem> orderItem = orderItemRepository.findOneWithToOneRelationships(id);
        //Optional<Product> product = productRepository.findOneWithToOneRelationships(id);
        if (orderItem.isEmpty() != true) {
            //later set product.isActive = false
            productRepository.findById(id).map(existingProduct -> {
                existingProduct.setIsActive(false);
                return existingProduct;
            }).map(productRepository::save);
            log.debug("This product cant be deleted", id);
        }
        //query to check the count of orderItem related to this product
        else {
            productRepository.deleteById(id);
        }

    }

    public Optional<Product> disableProduct(Product product) {
        log.debug("Request to disable the Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {

                if (product.getIsActive() != null) {
                    existingProduct.setIsActive(product.getIsActive());
                }

                return existingProduct;
            })
            .map(productRepository::save);

    }

    /*List<Product> findAllWithEagerRelationships(Boolean isActive) {
        return this.getAllActiveProductsList(isActive);
    }

    @Query("select product from Product product where product.isActive =:isActive")
    Optional<Product> getAllActiveProductsList(@Param("id") Long id) {
        return null;
    }*/


}
