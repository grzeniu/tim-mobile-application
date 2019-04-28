package pl.wat.tim.mobile.integration;

import pl.wat.tim.mobile.integration.dto.AuthToken;
import pl.wat.tim.mobile.integration.dto.LoginUserDto;
import pl.wat.tim.mobile.integration.dto.NewUserDto;
import pl.wat.tim.mobile.integration.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendAppClient {

    @POST("token/signup")
    Call<UserDto> createNewUser(@Body NewUserDto newUserDto);

    @POST("token/generate-token")
    Call<AuthToken> generateToken(@Body LoginUserDto loginUserDto);
}
