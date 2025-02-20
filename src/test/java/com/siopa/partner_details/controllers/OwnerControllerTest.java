package com.siopa.partner_details.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.service.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private OwnerController ownerController;

    private Owner owner1;
    private Owner owner2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner1 = new Owner(1L, "John Doe", "john.doe@example.com", "+1234567890", null);
        owner2 = new Owner(2L, "Jane Doe", "jane.doe@example.com", "+9876543210", null);
    }

    @Test
    void testGetAllOwners() {
        when(ownerService.getAllOwners()).thenReturn(Arrays.asList(owner1, owner2));

        ResponseEntity<List<Owner>> response = ownerController.getAllOwners();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(ownerService, times(1)).getAllOwners();
    }

    @Test
    void testGetOwnerById_Found() {
        when(ownerService.getOwnerById(1L)).thenReturn(Optional.of(owner1));

        ResponseEntity<Owner> response = ownerController.getOwnerById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(owner1.getName(), response.getBody().getName());
        verify(ownerService, times(1)).getOwnerById(1L);
    }

    @Test
    void testGetOwnerById_NotFound() {
        when(ownerService.getOwnerById(3L)).thenReturn(Optional.empty());

        ResponseEntity<Owner> response = ownerController.getOwnerById(3L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateOwner() {
        when(ownerService.createOwner(any(Owner.class))).thenReturn(owner1);

        ResponseEntity<Owner> response = ownerController.createOwner(owner1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(owner1.getEmail(), response.getBody().getEmail());
        verify(ownerService, times(1)).createOwner(any(Owner.class));
    }

    @Test
    void testUpdateOwner() {
        when(ownerService.updateOwner(eq(1L), any(Owner.class))).thenReturn(owner1);

        ResponseEntity<Owner> response = ownerController.updateOwner(1L, owner1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(owner1.getName(), response.getBody().getName());
        verify(ownerService, times(1)).updateOwner(eq(1L), any(Owner.class));
    }

    @Test
    void testDeleteOwner() {
        doNothing().when(ownerService).deleteOwner(1L);

        ResponseEntity<Void> response = ownerController.deleteOwner(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(ownerService, times(1)).deleteOwner(1L);
    }
}