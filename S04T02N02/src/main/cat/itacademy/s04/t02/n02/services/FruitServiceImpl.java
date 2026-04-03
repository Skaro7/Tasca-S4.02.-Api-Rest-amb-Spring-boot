package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.Fruit;
import cat.itacademy.s04.t02.n02.model.Provider;
import cat.itacademy.s04.t02.n02.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;
    private final ProviderRepository providerRepository;

    @Override
    public FruitResponseDTO create(FruitRequestDTO request) {
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ProviderNotFoundException(request.getProviderId()));
        Fruit fruit = Fruit.builder()
                .name(request.getName())
                .weightInKilos(request.getWeightInKilos())
                .provider(provider)
                .build();
        return toResponseDTO(fruitRepository.save(fruit));
    }

    @Override
    public FruitResponseDTO getById(Long id) {
        return toResponseDTO(fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id)));
    }

    @Override
    public List<FruitResponseDTO> getAll() {
        return fruitRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public List<FruitResponseDTO> getByProviderId(Long providerId) {
        if (!providerRepository.existsById(providerId)) {
            throw new ProviderNotFoundException(providerId);
        }
        return fruitRepository.findByProviderId(providerId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public FruitResponseDTO update(Long id, FruitRequestDTO request) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        Provider provider = providerRepository.findById(request.getProviderId())
                .orElseThrow(() -> new ProviderNotFoundException(request.getProviderId()));
        fruit.setName(request.getName());
        fruit.setWeightInKilos(request.getWeightInKilos());
        fruit.setProvider(provider);
        return toResponseDTO(fruitRepository.save(fruit));
    }

    @Override
    public void delete(Long id) {
        if (!fruitRepository.existsById(id)) {
            throw new FruitNotFoundException(id);
        }
        fruitRepository.deleteById(id);
    }

    private FruitResponseDTO toResponseDTO(Fruit fruit) {
        return FruitResponseDTO.builder()
                .id(fruit.getId())
                .name(fruit.getName())
                .weightInKilos(fruit.getWeightInKilos())
                .provider(ProviderResponseDTO.builder()
                        .id(fruit.getProvider().getId())
                        .name(fruit.getProvider().getName())
                        .country(fruit.getProvider().getCountry())
                        .build())
                .build();
    }
}