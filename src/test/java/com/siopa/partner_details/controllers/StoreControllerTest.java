package com.siopa.partner_details.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.models.Store;
import com.siopa.partner_details.service.StoreService;
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

class StoreControllerTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    private Store store1;
    private Store store2;
    private Owner owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        owner = new Owner(1L, "John Doe", "john.doe@example.com", "+1234567890", null);
        store1 = new Store(1L, "Tech World", "123 Tech Street",true,"+1987654321", "techworld@example.com", owner);
        store2 = new Store(2L, "Gadget Hub", "456 Market Street",true,"+1122334455", "gadgethub@example.com", owner);
    }

    @Test
    void testGetAllStores() {
        when(storeService.getAllStores()).thenReturn(Arrays.asList(store1, store2));

        ResponseEntity<List<Store>> response = storeController.getAllStores();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(storeService, times(1)).getAllStores();
    }

    @Test
    void testGetStoreById_Found() {
        when(storeService.getStoreById(1L)).thenReturn(Optional.of(store1));

        ResponseEntity<Store> response = storeController.getStoreById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(store1.getName(), response.getBody().getName());
        verify(storeService, times(1)).getStoreById(1L);
    }

    @Test
    void testGetStoreById_NotFound() {
        when(storeService.getStoreById(3L)).thenReturn(Optional.empty());

        ResponseEntity<Store> response = storeController.getStoreById(3L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateStore() {
        when(storeService.createStore(any(Store.class), eq(1L))).thenReturn(store1);

        ResponseEntity<Store> response = storeController.createStore(1L, store1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(store1.getEmail(), response.getBody().getEmail());
        verify(storeService, times(1)).createStore(any(Store.class), eq(1L));
    }

    @Test
    void testUpdateStore() {
        when(storeService.updateStore(eq(1L), any(Store.class))).thenReturn(store1);

        ResponseEntity<Store> response = storeController.updateStore(1L, store1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(store1.getName(), response.getBody().getName());
        verify(storeService, times(1)).updateStore(eq(1L), any(Store.class));
    }

    @Test
    void testDeleteStore() {
        doNothing().when(storeService).deleteStore(1L);

        ResponseEntity<Void> response = storeController.deleteStore(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(storeService, times(1)).deleteStore(1L);
    }
}