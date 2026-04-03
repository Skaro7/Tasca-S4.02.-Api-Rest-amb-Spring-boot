package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.exception.DuplicateProviderException;
import cat.itacademy.s04.t02.n02.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.Provider;
import cat.itacademy.s04.t02.n02.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final FruitRepository fruitRepository;

    @Override
    public ProviderResponseDTO create(ProviderRequestDTO request) {
        if (providerRepository.existsByName(request.getName())) {
            throw new DuplicateProviderException(request.getName());
        }
        Provider provider = Provider.builder()
                .name(request.getName())
                .country(request.getCountry())
                .build();
        return toResponseDTO(providerRepository.save(provider));
    }

    @Override
    public List<ProviderResponseDTO> getAll() {
        return providerRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public ProviderResponseDTO update(Long id, ProviderRequestDTO request) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException(id));
        if (!provider.getName().equals(request.getName()) &&
                providerRepository.existsByName(request.getName())) {
            throw new DuplicateProviderException(request.getName());
        }
        provider.setName(request.getName());
        provider.setCountry(request.getCountry());
        return toResponseDTO(providerRepository.save(provider));
    }

    @Override
    public void delete(Long id) {
        if (!providerRepository.existsById(id)) {
            throw new ProviderNotFoundException(id);
        }
        if (fruitRepository.existsByProviderId(id)) {
            throw new ProviderHasFruitsException(id);
        }
        providerRepository.deleteById(id);
    }

    private ProviderResponseDTO toResponseDTO(Provider provider) {
        return ProviderResponseDTO.builder()
                .id(provider.getId())
                .name(provider.getName())
                .country(provider.getCountry())
                .build();
    }
}