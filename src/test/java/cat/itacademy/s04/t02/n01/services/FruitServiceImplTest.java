package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n01.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n01.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
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
class FruitServiceImplTest {

    @Mock
    private FruitRepository fruitRepository;

    @InjectMocks
    private FruitServiceImpl fruitService;

    private Fruit fruit;
    private FruitRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        fruit = Fruit.builder().id(1L).name("Poma").weightInKilos(5).build();
        requestDTO = FruitRequestDTO.builder().name("Poma").weightInKilos(5).build();
    }

    @Test
    @DisplayName("create: hauria de desar i retornar la fruita creada")
    void create_shouldSaveAndReturnFruit() {
        when(fruitRepository.save(any(Fruit.class))).thenReturn(fruit);

        FruitResponseDTO result = fruitService.create(requestDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Poma");
        assertThat(result.getWeightInKilos()).isEqualTo(5);
        verify(fruitRepository, times(1)).save(any(Fruit.class));
    }

    @Test
    @DisplayName("getById: hauria de retornar la fruita quan l'ID existeix")
    void getById_shouldReturnFruitWhenExists() {
        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));

        FruitResponseDTO result = fruitService.getById(1L);

        assertThat(result.getName()).isEqualTo("Poma");
    }

    @Test
    @DisplayName("getById: hauria de llançar FruitNotFoundException quan l'ID no existeix")
    void getById_shouldThrowWhenNotFound() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fruitService.getById(99L))
                .isInstanceOf(FruitNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("getAll: hauria de retornar totes les fruites")
    void getAll_shouldReturnAllFruits() {
        Fruit fruit2 = Fruit.builder().id(2L).name("Platan").weightInKilos(3).build();
        when(fruitRepository.findAll()).thenReturn(List.of(fruit, fruit2));

        List<FruitResponseDTO> result = fruitService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("getAll: hauria de retornar una llista buida si no hi ha fruites")
    void getAll_shouldReturnEmptyList() {
        when(fruitRepository.findAll()).thenReturn(List.of());

        List<FruitResponseDTO> result = fruitService.getAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("update: hauria d'actualitzar i retornar la fruita")
    void update_shouldUpdateAndReturnFruit() {
        FruitRequestDTO updateRequest = FruitRequestDTO.builder().name("Pera").weightInKilos(8).build();
        Fruit updated = Fruit.builder().id(1L).name("Pera").weightInKilos(8).build();

        when(fruitRepository.findById(1L)).thenReturn(Optional.of(fruit));
        when(fruitRepository.save(any(Fruit.class))).thenReturn(updated);

        FruitResponseDTO result = fruitService.update(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("Pera");
        assertThat(result.getWeightInKilos()).isEqualTo(8);
    }

    @Test
    @DisplayName("update: hauria de llançar FruitNotFoundException si l'ID no existeix")
    void update_shouldThrowWhenNotFound() {
        when(fruitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fruitService.update(99L, requestDTO))
                .isInstanceOf(FruitNotFoundException.class);
    }

    @Test
    @DisplayName("delete: hauria d'eliminar la fruita si existeix")
    void delete_shouldDeleteFruitWhenExists() {
        when(fruitRepository.existsById(1L)).thenReturn(true);
        doNothing().when(fruitRepository).deleteById(1L);

        assertThatCode(() -> fruitService.delete(1L)).doesNotThrowAnyException();
        verify(fruitRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete: hauria de llançar FruitNotFoundException si l'ID no existeix")
    void delete_shouldThrowWhenNotFound() {
        when(fruitRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> fruitService.delete(99L))
                .isInstanceOf(FruitNotFoundException.class);
    }
}