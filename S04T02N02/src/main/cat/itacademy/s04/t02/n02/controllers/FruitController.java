package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.dto.FruitRequestDTO;
import cat.itacademy.s04.t02.n02.dto.FruitResponseDTO;
import cat.itacademy.s04.t02.n02.services.FruitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
public class FruitController {

    private final FruitService fruitService;

    @PostMapping
    public ResponseEntity<FruitResponseDTO> create(@Valid @RequestBody FruitRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fruitService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fruitService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<FruitResponseDTO>> getAll(
            @RequestParam(required = false) Long providerId) {
        if (providerId != null) {
            return ResponseEntity.ok(fruitService.getByProviderId(providerId));
        }
        return ResponseEntity.ok(fruitService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FruitResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody FruitRequestDTO request) {
        return ResponseEntity.ok(fruitService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fruitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}