package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.exception.DuplicateProviderException;
import cat.itacademy.s04.t02.n02.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.services.FruitService;
import cat.itacademy.s04.t02.n02.services.ProviderService;
import org.junit.jupiter.api.BeforeEach;
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
class ProviderControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    @MockitoBean
    private FruitService fruitService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("POST /providers: hauria de retornar 201")
    void createProvider_shouldReturn201() throws Exception {
        ProviderResponseDTO response = new ProviderResponseDTO(1L, "FruitesFresh", "Spain");
        when(providerService.create(any(ProviderRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/providers")
                        .contentType("application/json")
                        .content("{\"name\":\"FruitesFresh\",\"country\":\"Spain\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("FruitesFresh"));
    }

    @Test
    @DisplayName("POST /providers: hauria de retornar 400 si el nom es buit")
    void createProvider_shouldReturn400WhenNameBlank() throws Exception {
        mockMvc.perform(post("/providers")
                        .contentType("application/json")
                        .content("{\"name\":\"\",\"country\":\"Spain\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /providers: hauria de retornar 400 si el nom ja existeix")
    void createProvider_shouldReturn400WhenDuplicate() throws Exception {
        when(providerService.create(any(ProviderRequestDTO.class)))
                .thenThrow(new DuplicateProviderException("FruitesFresh"));

        mockMvc.perform(post("/providers")
                        .contentType("application/json")
                        .content("{\"name\":\"FruitesFresh\",\"country\":\"Spain\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /providers: hauria de retornar 200 amb llista")
    void getAllProviders_shouldReturn200() throws Exception {
        when(providerService.getAll()).thenReturn(List.of(
                new ProviderResponseDTO(1L, "FruitesFresh", "Spain")
        ));

        mockMvc.perform(get("/providers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("PUT /providers/{id}: hauria de retornar 200")
    void updateProvider_shouldReturn200() throws Exception {
        ProviderResponseDTO response = new ProviderResponseDTO(1L, "NouNom", "France");
        when(providerService.update(eq(1L), any(ProviderRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/providers/1")
                        .contentType("application/json")
                        .content("{\"name\":\"NouNom\",\"country\":\"France\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NouNom"));
    }

    @Test
    @DisplayName("PUT /providers/{id}: hauria de retornar 404 si no existeix")
    void updateProvider_shouldReturn404WhenNotFound() throws Exception {
        when(providerService.update(eq(99L), any(ProviderRequestDTO.class)))
                .thenThrow(new ProviderNotFoundException(99L));

        mockMvc.perform(put("/providers/99")
                        .contentType("application/json")
                        .content("{\"name\":\"NouNom\",\"country\":\"France\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /providers/{id}: hauria de retornar 204")
    void deleteProvider_shouldReturn204() throws Exception {
        doNothing().when(providerService).delete(1L);

        mockMvc.perform(delete("/providers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /providers/{id}: hauria de retornar 404 si no existeix")
    void deleteProvider_shouldReturn404WhenNotFound() throws Exception {
        doThrow(new ProviderNotFoundException(99L)).when(providerService).delete(99L);

        mockMvc.perform(delete("/providers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /providers/{id}: hauria de retornar 400 si te fruites")
    void deleteProvider_shouldReturn400WhenHasFruits() throws Exception {
        doThrow(new ProviderHasFruitsException(1L)).when(providerService).delete(1L);

        mockMvc.perform(delete("/providers/1"))
                .andExpect(status().isBadRequest());
    }
}