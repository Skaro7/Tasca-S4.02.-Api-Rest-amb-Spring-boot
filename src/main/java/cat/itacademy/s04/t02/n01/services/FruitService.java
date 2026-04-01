package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n01.dto.FruitResponseDTO;

import java.util.List;

public interface FruitService {

    FruitResponseDTO create(FruitRequestDTO request);

    FruitResponseDTO getById(Long id);

    List<FruitResponseDTO> getAll();

    FruitResponseDTO update(Long id, FruitRequestDTO request);

    void delete(Long id);
}