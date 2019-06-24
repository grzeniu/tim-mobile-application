package pl.wat.tim.mobile.infrastructure.integration.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinanceResponseDto {
    private final Integer id;
    private final String description;
    private final String value;
    private final String financeType;
    private final String createdBy;
    private final String createdDate;
    private final CategoryDto category;
}
