package com.siopa.partner_details.service;

import com.siopa.partner_details.models.Owner;
import com.siopa.partner_details.models.Store;
import com.siopa.partner_details.repositories.OwnerRepository;
import com.siopa.partner_details.repositories.StoreRepository;
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

class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private StoreService storeService;

    private Owner owner;
    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = Owner.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();

        store1 = Store.builder()
                .id(1L)
                .name("Store One")
                .address("123 Main St")
                .phoneNumber("1111111111")
                .email("storeone@example.com")
                .isActive(true)
                .owner(owner)
                .build();

        store2 = Store.builder()
                .id(2L)
                .name("Store Two")
                .address("456 Second St")
                .phoneNumber("2222222222")
                .email("storetwo@example.com")
                .isActive(true)
                .owner(owner)
                .build();
    }

    /**
     * Tests retrieving all stores.
     */
    @Test
    void testGetAllStores() {
        List<Store> stores = Arrays.asList(store1, store2);
        when(storeRepository.findAll()).thenReturn(stores);

        List<Store> result = storeService.getAllStores();

        assertEquals(2, result.size());
        verify(storeRepository, times(1)).findAll();
    }

    /**
     * Tests retrieving a store by ID when it exists.
     */
    @Test
    void testGetStoreById_Found() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store1));

        Optional<Store> result = storeService.getStoreById(1L);

        assertTrue(result.isPresent());
        assertEquals(store1.getName(), result.get().getName());
        verify(storeRepository, times(1)).findById(1L);
    }

    /**
     * Tests retrieving a store by ID when it does not exist.
     */
    @Test
    void testGetStoreById_NotFound() {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.getStoreById(99L);

        assertFalse(result.isPresent());
        verify(storeRepository, times(1)).findById(99L);
    }

    /**
     * Tests retrieving stores by owner ID.
     */
    @Test
    void testGetStoresByOwnerId() {
        List<Store> stores = Arrays.asList(store1, store2);
        when(storeRepository.findByOwnerId(owner.getId())).thenReturn(stores);

        List<Store> result = storeService.getStoresByOwnerId(owner.getId());

        assertEquals(2, result.size());
        verify(storeRepository, times(1)).findByOwnerId(owner.getId());
    }

    /**
     * Tests creating a store when the owner exists.
     */
    @Test
    void testCreateStore_Success() {
        Store newStore = Store.builder()
                .name("New Store")
                .address("789 New St")
                .phoneNumber("3333333333")
                .email("newstore@example.com")
                .isActive(true)
                .build();

        when(ownerRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
            Store s = invocation.getArgument(0);
            s.setId(3L); // Simulate generated ID
            return s;
        });

        Store createdStore = storeService.createStore(newStore, owner.getId());

        assertNotNull(createdStore.getId());
        assertEquals(owner, createdStore.getOwner());
        verify(ownerRepository, times(1)).findById(owner.getId());
        verify(storeRepository, times(1)).save(newStore);
    }

    /**
     * Tests creating a store when the owner does not exist.
     */
    @Test
    void testCreateStore_OwnerNotFound() {
        Store newStore = Store.builder()
                .name("New Store")
                .address("789 New St")
                .phoneNumber("3333333333")
                .email("newstore@example.com")
                .isActive(true)
                .build();

        when(ownerRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                storeService.createStore(newStore, 99L)
        );

        assertEquals("Owner not found with ID: 99", exception.getMessage());
        verify(ownerRepository, times(1)).findById(99L);
        verify(storeRepository, never()).save(any(Store.class));
    }

    /**
     * Tests updating an existing store.
     */
    @Test
    void testUpdateStore_Found() {
        Store updatedData = Store.builder()
                .name("Updated Store")
                .address("Updated Address")
                .phoneNumber("4444444444")
                .email("updated@example.com")
                .isActive(false)
                .build();

        when(storeRepository.findById(store1.getId())).thenReturn(Optional.of(store1));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Store updatedStore = storeService.updateStore(store1.getId(), updatedData);

        assertEquals("Updated Store", updatedStore.getName());
        assertEquals("Updated Address", updatedStore.getAddress());
        assertEquals("4444444444", updatedStore.getPhoneNumber());
        assertEquals("updated@example.com", updatedStore.getEmail());
        assertFalse(updatedStore.isActive());
        verify(storeRepository, times(1)).findById(store1.getId());
        verify(storeRepository, times(1)).save(store1);
    }

    /**
     * Tests updating a store that does not exist.
     */
    @Test
    void testUpdateStore_NotFound() {
        Store updatedData = Store.builder()
                .name("Updated Store")
                .address("Updated Address")
                .phoneNumber("4444444444")
                .email("updated@example.com")
                .isActive(false)
                .build();

        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                storeService.updateStore(99L, updatedData)
        );

        assertEquals("Store not found with ID: 99", exception.getMessage());
        verify(storeRepository, times(1)).findById(99L);
        verify(storeRepository, never()).save(any(Store.class));
    }

    /**
     * Tests deleting an existing store.
     */
    @Test
    void testDeleteStore_Found() {
        when(storeRepository.existsById(store1.getId())).thenReturn(true);
        doNothing().when(storeRepository).deleteById(store1.getId());

        storeService.deleteStore(store1.getId());

        verify(storeRepository, times(1)).existsById(store1.getId());
        verify(storeRepository, times(1)).deleteById(store1.getId());
    }

    /**
     * Tests deleting a store that does not exist.
     */
    @Test
    void testDeleteStore_NotFound() {
        when(storeRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () ->
                storeService.deleteStore(99L)
        );

        assertEquals("Store not found with ID: 99", exception.getMessage());
        verify(storeRepository, times(1)).existsById(99L);
        verify(storeRepository, never()).deleteById(anyLong());
    }
}