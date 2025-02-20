package com.siopa.partner_details.service;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.repositories.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Gets all owners from the database.
     * @return List of all owners.
     */
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    /**
     * Retrieves an owner by their ID.
     * @param id The ID of the owner.
     * @return An optional Owner object.
     */
    public Optional<Owner> getOwnerById(Long id) {
        return ownerRepository.findById(id);
    }

    /**
     * Creates a new owner in the database.
     * @param owner The owner object to be saved.
     * @return The created owner.
     */
    public Owner createOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    /**
     * Updates an existing owner with new details.
     * @param id The ID of the owner to update.
     * @param updatedOwner The new owner details.
     * @return The updated owner object.
     */
    public Owner updateOwner(Long id, Owner updatedOwner) {
        return ownerRepository.findById(id)
                .map(owner -> {
                    owner.setName(updatedOwner.getName());
                    owner.setEmail(updatedOwner.getEmail());
                    owner.setPhoneNumber(updatedOwner.getPhoneNumber());
                    return ownerRepository.save(owner);
                })
                .orElseThrow(() -> new RuntimeException("Owner not found with ID: " + id));
    }

    /**
     * Deletes an owner by ID.
     * @param id The ID of the owner to delete.
     */
    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new RuntimeException("Owner not found with ID: " + id);
        }
        ownerRepository.deleteById(id);
    }
}