package cat.itacademy.s04.t02.n01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FruitResponseDTO {

    private Long id;
    private String name;
    private int weightInKilos;
}