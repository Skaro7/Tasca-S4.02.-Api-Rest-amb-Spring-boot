package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;

import java.util.List;

public interface ProviderService {

    ProviderResponseDTO create(ProviderRequestDTO request);

    List<ProviderResponseDTO> getAll();

    ProviderResponseDTO update(Long id, ProviderRequestDTO request);

    void delete(Long id);
}