package com.siopa.partner_details.repositories;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.models.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Data access interface for store entity.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * Finds all stores associated with a specific owner.
     * @param ownerId The ID of the owner.
     * @return List of stores belonging to the specified owner.
     */
    List<Store> findByOwnerId(Long ownerId);

    /**
     * Retrieves all active stores.
     * @return List of active stores.
     */
    List<Store> findByIsActiveTrue();


}