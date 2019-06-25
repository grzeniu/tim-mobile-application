package pl.wat.tim.mobile.infrastructure.integration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinanceDto {
    private final String description;
    private final String value;
    private final Integer categoryId;
}
