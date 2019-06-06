package pl.wat.tim.mobile.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Finance {

    private Integer id;

    private String description;

    private String value;

    private String financeType;

    private LocalDateTime createdDate;

    private String category;
}
