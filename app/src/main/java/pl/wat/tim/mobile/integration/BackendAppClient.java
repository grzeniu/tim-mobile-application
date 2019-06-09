package pl.wat.tim.mobile.integration;

import java.util.List;

import okhttp3.ResponseBody;
import pl.wat.tim.mobile.integration.dto.AuthToken;
import pl.wat.tim.mobile.integration.dto.FinanceResponseDto;
import pl.wat.tim.mobile.integration.dto.LoginUserDto;
import pl.wat.tim.mobile.integration.dto.NewUserDto;
import pl.wat.tim.mobile.integration.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface BackendAppClient {

    @POST("token/signup")
    Call<UserDto> createNewUser(@Body NewUserDto newUserDto);

    @POST("token/generate-token")
    Call<AuthToken> generateToken(@Body LoginUserDto loginUserDto);

    @Streaming
    @GET("/users/reports/eng")
    Call<ResponseBody> generateReport(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/finances")
    Call<List<FinanceResponseDto>> getFinances(@Header("Authorization") String token);

}
