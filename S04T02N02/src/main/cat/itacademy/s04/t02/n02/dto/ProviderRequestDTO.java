package cat.itacademy.s04.t02.n02.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderRequestDTO {

    @NotBlank(message = "El nom del proveidor no pot estar buit.")
    private String name;

    @NotBlank(message = "El pais no pot estar buit.")
    private String country;
}