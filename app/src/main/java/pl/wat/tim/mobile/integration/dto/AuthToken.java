package pl.wat.tim.mobile.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthToken {
    private final String token;
    private final String username;
}
