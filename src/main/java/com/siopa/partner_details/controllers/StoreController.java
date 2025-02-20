package com.siopa.partner_details.controllers;

import com.siopa.partner_details.models.Store;
import com.siopa.partner_details.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * API Endpoints for store entity.
 */
@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Gets all stores.
     * @return List of all stores.
     */
    @GetMapping
    public ResponseEntity<List<Store>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    /**
     * Gets a store by ID.
     * @param id The ID of the store.
     * @return The store if found, else 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        Optional<Store> store = storeService.getStoreById(id);
        return store.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gets all stores belonging to an owner.
     * @param ownerId The ID of the owner.
     * @return List of stores owned by the given owner.
     */
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Store>> getStoresByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(storeService.getStoresByOwnerId(ownerId));
    }

    /**
     * Creates a new store for an owner.
     * @param ownerId The ID of the owner.
     * @param store The store object to be created.
     * @return The created store.
     */
    @PostMapping("/owner/{ownerId}")
    public ResponseEntity<Store> createStore(@PathVariable Long ownerId, @RequestBody Store store) {
        return ResponseEntity.ok(storeService.createStore(store, ownerId));
    }

    /**
     * Updates an existing store.
     * @param id The ID of the store to update.
     * @param updatedStore The new store details.
     * @return The updated store if found, else 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store updatedStore) {
        return ResponseEntity.ok(storeService.updateStore(id, updatedStore));
    }

    /**
     * Deletes a store by ID.
     * @param id The ID of the store to delete.
     * @return Response status 204 No Content if successful, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}