package pl.wat.tim.mobile.infrastructure.integration.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewUserDto {
    private final String username;
    private final String password;
    private final String firstname;
    private final String lastname;
    @SerializedName(value="mail")
    private final String email;
}
