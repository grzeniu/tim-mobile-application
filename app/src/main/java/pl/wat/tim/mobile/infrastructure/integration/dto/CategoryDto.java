package pl.wat.tim.mobile.infrastructure.integration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private final Integer id;
    private final String name;
}
