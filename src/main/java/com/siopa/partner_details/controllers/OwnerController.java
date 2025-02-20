package com.siopa.partner_details.controllers;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Gets all owners.
     * @return List of all owners.
     */
    @GetMapping
    public ResponseEntity<List<Owner>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    /**
     * Gets an owner by ID.
     * @param id The ID of the owner.
     * @return The owner if found, else 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Owner> getOwnerById(@PathVariable Long id) {
        Optional<Owner> owner = ownerService.getOwnerById(id);
        return owner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new owner.
     * @param owner The owner object to be created.
     * @return The created owner.
     */
    @PostMapping
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        return ResponseEntity.ok(ownerService.createOwner(owner));
    }

    /**
     * Updates an existing owner.
     * @param id The ID of the owner to update.
     * @param updatedOwner The new owner details.
     * @return The updated owner if found, else 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable Long id, @RequestBody Owner updatedOwner) {
        return ResponseEntity.ok(ownerService.updateOwner(id, updatedOwner));
    }

    /**
     * Deletes an owner by ID.
     * @param id The ID of the owner to delete.
     * @return Response status 204 No Content if successful, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}