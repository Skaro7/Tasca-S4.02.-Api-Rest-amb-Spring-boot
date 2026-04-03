package cat.itacademy.s04.t02.n01.dto;

import jakarta.validation.constraints.NotBlank;
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

    @Positive(message = "El pes ha de ser un número positiu.")
    private int weightInKilos;
}