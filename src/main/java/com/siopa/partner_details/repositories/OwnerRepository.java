package com.siopa.partner_details.repositories;

import com.siopa.partner_details.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access interface for owner entity.
 */
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    // Find owner by email (ensuring unique emails)
    Optional<Owner> findByEmail(String email);

    // Check if an email already exists
    boolean existsByEmail(String email);
}