package com.siopa.partner_details.service;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.models.Store;
import com.siopa.partner_details.repositories.OwnerRepository;
import com.siopa.partner_details.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;

    public StoreService(StoreRepository storeRepository, OwnerRepository ownerRepository) {
        this.storeRepository = storeRepository;
        this.ownerRepository = ownerRepository;
    }

    /**
     * Retrieves all stores from the database.
     * @return List of all stores.
     */
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    /**
     * Retrieves a store by its ID.
     * @param id The ID of the store.
     * @return An optional Store object.
     */
    public Optional<Store> getStoreById(Long id) {
        return storeRepository.findById(id);
    }

    /**
     * Gets all stores belonging to a specific owner.
     * @param ownerId The ID of the owner.
     * @return List of stores owned by the given owner.
     */
    public List<Store> getStoresByOwnerId(Long ownerId) {
        return storeRepository.findByOwnerId(ownerId);
    }

    /**
     * Creates a new store and associates it with an existing owner.
     * @param store The store object to be created.
     * @param ownerId The ID of the owner to associate with the store.
     * @return The created store.
     */
    public Store createStore(Store store, Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + ownerId));

        store.setOwner(owner);
        return storeRepository.save(store);
    }

    /**
     * Updates an existing store with new details.
     * @param id The ID of the store to update.
     * @param updatedStore The new store details.
     * @return The updated store object.
     */
    public Store updateStore(Long id, Store updatedStore) {
        return storeRepository.findById(id)
                .map(store -> {
                    store.setName(updatedStore.getName());
                    store.setAddress(updatedStore.getAddress());
                    store.setPhoneNumber(updatedStore.getPhoneNumber());
                    store.setEmail(updatedStore.getEmail());
                    store.setActive(updatedStore.isActive());
                    return storeRepository.save(store);
                })
                .orElseThrow(() -> new RuntimeException("Store not found with ID: " + id));
    }

    /**
     * Deletes a store by ID.
     * @param id The ID of the store to delete.
     */
    public void deleteStore(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new RuntimeException("Store not found with ID: " + id);
        }
        storeRepository.deleteById(id);
    }
}