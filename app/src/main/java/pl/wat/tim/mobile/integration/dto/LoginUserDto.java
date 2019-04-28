package pl.wat.tim.mobile.integration.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class LoginUserDto {
    @SerializedName("username")
    private final String username;
    @SerializedName("password")
    private final String password;
}
