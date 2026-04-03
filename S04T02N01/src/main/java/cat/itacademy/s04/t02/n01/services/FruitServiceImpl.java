package cat.itacademy.s04.t02.n01.services;

import cat.itacademy.s04.t02.n01.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n01.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n01.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.model.Fruit;
import cat.itacademy.s04.t02.n01.repository.FruitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FruitServiceImpl implements FruitService {

    private final FruitRepository fruitRepository;

    @Override
    public FruitResponseDTO create(FruitRequestDTO request) {
        Fruit fruit = Fruit.builder()
                .name(request.getName())
                .weightInKilos(request.getWeightInKilos())
                .build();
        Fruit saved = fruitRepository.save(fruit);
        return toResponseDTO(saved);
    }

    @Override
    public FruitResponseDTO getById(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        return toResponseDTO(fruit);
    }

    @Override
    public List<FruitResponseDTO> getAll() {
        return fruitRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Override
    public FruitResponseDTO update(Long id, FruitRequestDTO request) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new FruitNotFoundException(id));
        fruit.setName(request.getName());
        fruit.setWeightInKilos(request.getWeightInKilos());
        Fruit updated = fruitRepository.save(fruit);
        return toResponseDTO(updated);
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
                .build();
    }
}