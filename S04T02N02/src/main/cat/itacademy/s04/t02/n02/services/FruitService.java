package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.dto.FruitResponseDTO;

import java.util.List;

public interface FruitService {

    FruitResponseDTO create(FruitRequestDTO request);

    FruitResponseDTO getById(Long id);

    List<FruitResponseDTO> getAll();

    List<FruitResponseDTO> getByProviderId(Long providerId);

    FruitResponseDTO update(Long id, FruitRequestDTO request);

    void delete(Long id);
}