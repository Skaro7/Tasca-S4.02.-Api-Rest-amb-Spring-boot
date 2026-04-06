package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.exception.DuplicateProviderException;
import cat.itacademy.s04.t02.n02.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.Provider;
import cat.itacademy.s04.t02.n02.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    private Provider provider;
    private ProviderRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        provider = Provider.builder().id(1L).name("FruitesFresh").country("Spain").build();
        requestDTO = ProviderRequestDTO.builder().name("FruitesFresh").country("Spain").build();
    }

    @Test
    @DisplayName("create: hauria de crear i retornar el proveidor")
    void create_shouldSaveAndReturnProvider() {
        when(providerRepository.existsByName("FruitesFresh")).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(provider);

        ProviderResponseDTO result = providerService.create(requestDTO);

        assertThat(result.getName()).isEqualTo("FruitesFresh");
        assertThat(result.getCountry()).isEqualTo("Spain");
    }

    @Test
    @DisplayName("create: hauria de llançar DuplicateProviderException si el nom ja existeix")
    void create_shouldThrowWhenDuplicateName() {
        when(providerRepository.existsByName("FruitesFresh")).thenReturn(true);

        assertThatThrownBy(() -> providerService.create(requestDTO))
                .isInstanceOf(DuplicateProviderException.class);
    }

    @Test
    @DisplayName("getAll: hauria de retornar tots els proveïdors")
    void getAll_shouldReturnAllProviders() {
        when(providerRepository.findAll()).thenReturn(List.of(provider));

        List<ProviderResponseDTO> result = providerService.getAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("update: hauria d'actualitzar el proveïdor")
    void update_shouldUpdateProvider() {
        ProviderRequestDTO updateRequest = ProviderRequestDTO.builder()
                .name("NouNom").country("France").build();
        Provider updated = Provider.builder().id(1L).name("NouNom").country("France").build();

        when(providerRepository.findById(1L)).thenReturn(Optional.of(provider));
        when(providerRepository.existsByName("NouNom")).thenReturn(false);
        when(providerRepository.save(any(Provider.class))).thenReturn(updated);

        ProviderResponseDTO result = providerService.update(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("NouNom");
    }

    @Test
    @DisplayName("update: hauria de llançar ProviderNotFoundException si no existeix")
    void update_shouldThrowWhenNotFound() {
        when(providerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> providerService.update(99L, requestDTO))
                .isInstanceOf(ProviderNotFoundException.class);
    }

    @Test
    @DisplayName("delete: hauria d'eliminar el proveïdor si no té fruites")
    void delete_shouldDeleteWhenNoFruits() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.existsByProviderId(1L)).thenReturn(false);

        assertThatCode(() -> providerService.delete(1L)).doesNotThrowAnyException();
        verify(providerRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete: hauria de llançar ProviderHasFruitsException si té fruites")
    void delete_shouldThrowWhenHasFruits() {
        when(providerRepository.existsById(1L)).thenReturn(true);
        when(fruitRepository.existsByProviderId(1L)).thenReturn(true);

        assertThatThrownBy(() -> providerService.delete(1L))
                .isInstanceOf(ProviderHasFruitsException.class);
    }

    @Test
    @DisplayName("delete: hauria de llançar ProviderNotFoundException si no existeix")
    void delete_shouldThrowWhenNotFound() {
        when(providerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> providerService.delete(99L))
                .isInstanceOf(ProviderNotFoundException.class);
    }
}