package com.siopa.partner_details.service;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.repositories.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private Owner owner1;
    private Owner owner2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner1 = Owner.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();

        owner2 = Owner.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .phoneNumber("0987654321")
                .build();
    }

    /**
     * Tests retrieving all owners.
     */
    @Test
    void testGetAllOwners() {
        List<Owner> owners = Arrays.asList(owner1, owner2);
        when(ownerRepository.findAll()).thenReturn(owners);

        List<Owner> result = ownerService.getAllOwners();

        assertEquals(2, result.size());
        verify(ownerRepository, times(1)).findAll();
    }

    /**
     * Tests retrieving an owner by ID when the owner exists.
     */
    @Test
    void testGetOwnerById_Found() {
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner1));

        Optional<Owner> result = ownerService.getOwnerById(1L);

        assertTrue(result.isPresent());
        assertEquals(owner1.getName(), result.get().getName());
        verify(ownerRepository, times(1)).findById(1L);
    }

    /**
     * Tests retrieving an owner by ID when the owner does not exist.
     */
    @Test
    void testGetOwnerById_NotFound() {
        when(ownerRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Owner> result = ownerService.getOwnerById(3L);

        assertFalse(result.isPresent());
        verify(ownerRepository, times(1)).findById(3L);
    }

    /**
     * Tests creating a new owner.
     */
    @Test
    void testCreateOwner() {
        when(ownerRepository.save(owner1)).thenReturn(owner1);

        Owner createdOwner = ownerService.createOwner(owner1);

        assertEquals(owner1.getId(), createdOwner.getId());
        assertEquals(owner1.getEmail(), createdOwner.getEmail());
        verify(ownerRepository, times(1)).save(owner1);
    }

    /**
     * Tests updating an existing owner.
     */
    @Test
    void testUpdateOwner_Found() {
        Owner updatedData = Owner.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("1111111111")
                .build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner1));
        when(ownerRepository.save(any(Owner.class))).thenAnswer(i -> i.getArgument(0));

        Owner updatedOwner = ownerService.updateOwner(1L, updatedData);

        assertEquals("John Updated", updatedOwner.getName());
        assertEquals("john.updated@example.com", updatedOwner.getEmail());
        assertEquals("1111111111", updatedOwner.getPhoneNumber());
        verify(ownerRepository, times(1)).findById(1L);
        verify(ownerRepository, times(1)).save(owner1);
    }

    /**
     * Tests updating an owner that does not exist.
     */
    @Test
    void testUpdateOwner_NotFound() {
        Owner updatedData = Owner.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .phoneNumber("1111111111")
                .build();

        when(ownerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                ownerService.updateOwner(1L, updatedData));

        assertEquals("Owner not found with ID: 1", exception.getMessage());
        verify(ownerRepository, times(1)).findById(1L);
        verify(ownerRepository, never()).save(any(Owner.class));
    }

    /**
     * Tests deleting an existing owner.
     */
    @Test
    void testDeleteOwner_Found() {
        when(ownerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ownerRepository).deleteById(1L);

        ownerService.deleteOwner(1L);

        verify(ownerRepository, times(1)).existsById(1L);
        verify(ownerRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests deleting an owner that does not exist.
     */
    @Test
    void testDeleteOwner_NotFound() {
        when(ownerRepository.existsById(3L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                ownerService.deleteOwner(3L));

        assertEquals("Owner not found with ID: 3", exception.getMessage());
        verify(ownerRepository, times(1)).existsById(3L);
        verify(ownerRepository, never()).deleteById(anyLong());
    }
}