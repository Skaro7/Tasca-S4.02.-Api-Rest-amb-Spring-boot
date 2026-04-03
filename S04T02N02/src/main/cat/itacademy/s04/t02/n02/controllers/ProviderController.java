package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.dto.ProviderRequestDTO;
import cat.itacademy.s04.t02.n02.dto.ProviderResponseDTO;
import cat.itacademy.s04.t02.n02.services.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    public ResponseEntity<ProviderResponseDTO> create(@Valid @RequestBody ProviderRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDTO>> getAll() {
        return ResponseEntity.ok(providerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequestDTO request) {
        return ResponseEntity.ok(providerService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}