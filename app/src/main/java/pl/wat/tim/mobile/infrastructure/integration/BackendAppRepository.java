package pl.wat.tim.mobile.infrastructure.integration;

import android.os.StrictMode;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MediatorLiveData;
import okhttp3.ResponseBody;
import pl.wat.tim.mobile.RetrofitClientInstance;
import pl.wat.tim.mobile.infrastructure.integration.dto.AuthToken;
import pl.wat.tim.mobile.infrastructure.integration.dto.FinanceResponseDto;
import pl.wat.tim.mobile.infrastructure.integration.dto.LoginUserDto;
import pl.wat.tim.mobile.infrastructure.integration.dto.NewUserDto;
import pl.wat.tim.mobile.infrastructure.integration.dto.UserDto;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.util.FileUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackendAppRepository {

    private static final String TOKEN_PREFIX = "Bearer ";

    private BackendAppClient client;

    public BackendAppRepository() {
        client = RetrofitClientInstance.getRetrofitInstance().create(BackendAppClient.class);
    }

    public MediatorLiveData<List<Finance>> getFinances(String token, MediatorLiveData<List<Finance>> data) {
        client.getFinances(TOKEN_PREFIX + token).enqueue(new Callback<List<FinanceResponseDto>>() {

            @Override
            public void onResponse(Call<List<FinanceResponseDto>> call, Response<List<FinanceResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Finance> finances = mapResponseToFinance(response.body());
                    data.setValue(finances);
                }
            }

            @Override
            public void onFailure(Call<List<FinanceResponseDto>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public File generateReport(String token) {
        Response<ResponseBody> response = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            response = client.generateReport(TOKEN_PREFIX + token).execute();
        } catch (IOException e) {
            Log.d("error", e.getMessage());
        }

        if (response != null && response.body() != null) {
            String filename = response.headers().get("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
            return FileUtil.saveToDisk(response.body(), filename);
        }
        return null;
    }

    public MediatorLiveData<Boolean> createNewUser(MediatorLiveData<Boolean> userCreated, NewUserDto newUserDto) {
        client.createNewUser(newUserDto).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.code() == 201) {
                    userCreated.setValue(true);
                } else {
                    userCreated.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                userCreated.setValue(false);
            }
        });
        return userCreated;
    }

    public MediatorLiveData<User> generateToken(MediatorLiveData<User> user, LoginUserDto userDto) {
        client.generateToken(userDto).enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                if (response.code() == 201 && response.body() != null) {
                    user.setValue(User.builder()
                            .username(response.body().getUsername())
                            .token(response.body().getToken())
                            .build());
                } else {
                    user.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                user.setValue(null);
            }
        });
        return user;
    }

    public void deleteFinance(String token, int id, MediatorLiveData<Boolean> itemDeleted) {
        client.deleteFinance(TOKEN_PREFIX+token, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204) {
                    itemDeleted.setValue(true);
                } else {
                    itemDeleted.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                itemDeleted.setValue(false);
            }
        });
    }

    private List<Finance> mapResponseToFinance(List<FinanceResponseDto> dtos) {
        return dtos.stream().map(it -> Finance.builder()
                .id(it.getId())
                .description(it.getDescription())
                .value(it.getValue())
                .financeType(it.getFinanceType())
                .createdDate(LocalDateTime.parse(it.getCreatedDate()))
                .category(it.getCategory().getName())
                .build())
                .collect(Collectors.toList());
    }
}
