package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n01.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n01.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.services.FruitService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FruitControllerTest {

    @Autowired
    private WebApplicationContext context;

    @org.junit.jupiter.api.BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private MockMvc mockMvc;

    @MockitoBean
    private FruitService fruitService;

    @Test
    @DisplayName("POST /fruits: hauria de retornar 201 amb la fruita creada")
    void createFruit_shouldReturn201() throws Exception {
        FruitResponseDTO response = new FruitResponseDTO(1L, "Poma", 5);
        when(fruitService.create(any(FruitRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/fruits")
                        .contentType("application/json")
                        .content("{\"name\":\"Poma\",\"weightInKilos\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Poma"))
                .andExpect(jsonPath("$.weightInKilos").value(5));
    }

    @Test
    @DisplayName("POST /fruits: hauria de retornar 400 si el nom es buit")
    void createFruit_shouldReturn400WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/fruits")
                        .contentType("application/json")
                        .content("{\"name\":\"\",\"weightInKilos\":5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /fruits: hauria de retornar 400 si el pes no es positiu")
    void createFruit_shouldReturn400WhenWeightIsNotPositive() throws Exception {
        mockMvc.perform(post("/fruits")
                        .contentType("application/json")
                        .content("{\"name\":\"Poma\",\"weightInKilos\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /fruits/{id}: hauria de retornar 200 amb la fruita")
    void getFruitById_shouldReturn200() throws Exception {
        FruitResponseDTO response = new FruitResponseDTO(1L, "Poma", 5);
        when(fruitService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/fruits/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Poma"));
    }

    @Test
    @DisplayName("GET /fruits/{id}: hauria de retornar 404 si no existeix")
    void getFruitById_shouldReturn404WhenNotFound() throws Exception {
        when(fruitService.getById(99L)).thenThrow(new FruitNotFoundException(99L));

        mockMvc.perform(get("/fruits/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /fruits: hauria de retornar 200 amb llista de fruites")
    void getAllFruits_shouldReturn200WithList() throws Exception {
        List<FruitResponseDTO> fruits = List.of(
                new FruitResponseDTO(1L, "Poma", 5),
                new FruitResponseDTO(2L, "Platan", 3)
        );
        when(fruitService.getAll()).thenReturn(fruits);

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /fruits: hauria de retornar 200 amb llista buida")
    void getAllFruits_shouldReturn200WithEmptyList() throws Exception {
        when(fruitService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("PUT /fruits/{id}: hauria de retornar 200 amb la fruita actualitzada")
    void updateFruit_shouldReturn200() throws Exception {
        FruitResponseDTO response = new FruitResponseDTO(1L, "Pera", 8);
        when(fruitService.update(eq(1L), any(FruitRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/fruits/1")
                        .contentType("application/json")
                        .content("{\"name\":\"Pera\",\"weightInKilos\":8}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pera"));
    }

    @Test
    @DisplayName("PUT /fruits/{id}: hauria de retornar 404 si l'ID no existeix")
    void updateFruit_shouldReturn404WhenNotFound() throws Exception {
        when(fruitService.update(eq(99L), any(FruitRequestDTO.class)))
                .thenThrow(new FruitNotFoundException(99L));

        mockMvc.perform(put("/fruits/99")
                        .contentType("application/json")
                        .content("{\"name\":\"Pera\",\"weightInKilos\":8}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /fruits/{id}: hauria de retornar 400 si les dades no son valides")
    void updateFruit_shouldReturn400WhenInvalidData() throws Exception {
        mockMvc.perform(put("/fruits/1")
                        .contentType("application/json")
                        .content("{\"name\":\"\",\"weightInKilos\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /fruits/{id}: hauria de retornar 204 si existeix")
    void deleteFruit_shouldReturn204() throws Exception {
        doNothing().when(fruitService).delete(1L);

        mockMvc.perform(delete("/fruits/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /fruits/{id}: hauria de retornar 404 si no existeix")
    void deleteFruit_shouldReturn404WhenNotFound() throws Exception {
        doThrow(new FruitNotFoundException(99L)).when(fruitService).delete(99L);

        mockMvc.perform(delete("/fruits/99"))
                .andExpect(status().isNotFound());
    }
}