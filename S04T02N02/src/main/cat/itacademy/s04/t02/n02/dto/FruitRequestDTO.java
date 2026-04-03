package cat.itacademy.s04.t02.n02.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FruitRequestDTO {

    @NotBlank(message = "El nom de la fruita no pot estar buit.")
    private String name;

    @Positive(message = "El pes ha de ser un numero positiu.")
    private int weightInKilos;

    @NotNull(message = "Cal indicar l'ID del proveidor.")
    private Long providerId;
}