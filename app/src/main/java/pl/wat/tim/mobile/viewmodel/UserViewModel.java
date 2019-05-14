package pl.wat.tim.mobile.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import lombok.Getter;
import pl.wat.tim.mobile.RetrofitClientInstance;
import pl.wat.tim.mobile.integration.BackendAppClient;
import pl.wat.tim.mobile.integration.dto.AuthToken;
import pl.wat.tim.mobile.integration.dto.LoginUserDto;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.view.MainActivity;
import pl.wat.tim.mobile.view.RegistrationActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    public MediatorLiveData<String> username = new MediatorLiveData<>();
    public MediatorLiveData<String> password = new MediatorLiveData<>();

    @Getter
    private MediatorLiveData<String> usernameError = new MediatorLiveData<>();
    @Getter
    private MediatorLiveData<String> passwordError = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;

    private User user;
    private Context context;
    private BackendAppClient client;

    public UserViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
        this.client = RetrofitClientInstance.getRetrofitInstance().create(BackendAppClient.class);
    }

    public MutableLiveData<Integer> getBusy() {
        if (busy == null) {
            busy = new MediatorLiveData<>();
            busy.setValue(View.GONE);
        }
        return busy;
    }

    public void onLoginClick() {
        if (isValidCredentials()) {
            LoginUserDto userDto = createLoginUserDto();
            Call<AuthToken> call = client.generateToken(userDto);
            getBusy().setValue(View.VISIBLE);

            call.enqueue(new Callback<AuthToken>() {
                @Override
                public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                    if (response.code() == 201) {
                        user.setToken(response.body().getToken());
                        user.setUsername(response.body().getUsername());
                        Toasty.success(context, "Logged in successfully", Toast.LENGTH_SHORT, true).show();

                        getBusy().setValue(View.GONE);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("USER_OBJ", user);
                        context.startActivity(intent);
                        ((Activity) context).finish();
                    } else {
                        Toasty.error(context, "Incorrect credentials, try again", Toast.LENGTH_SHORT, true).show();
                        getBusy().setValue(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<AuthToken> call, Throwable t) {
                    Toasty.error(context, "Something went wrong!", Toast.LENGTH_SHORT, true).show();
                    getBusy().setValue(View.GONE);
                }
            });
        } else {
            Toasty.error(context, "Invalid credentials", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void onRegisterClick() {
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }

    private LoginUserDto createLoginUserDto() {
        return LoginUserDto.builder()
                .username(username.getValue())
                .password(password.getValue())
                .build();
    }

    private boolean isValidCredentials() {
        if (username.getValue() == null) {
            usernameError.setValue("Username cannot be empty");
            return false;
        }
        if (!UserFieldValidator.isValidPassword(password.getValue())) {
            passwordError.setValue("Password should be Minimum 6 Characters");
            return false;
        }
        return true;
    }
}