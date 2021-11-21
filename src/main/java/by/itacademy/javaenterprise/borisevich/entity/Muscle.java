package by.itacademy.javaenterprise.borisevich.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Muscle {
    private int muscleId;
    private String muscleName;
}
