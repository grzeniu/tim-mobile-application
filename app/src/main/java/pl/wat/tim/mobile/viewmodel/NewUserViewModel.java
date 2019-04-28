package pl.wat.tim.mobile.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import lombok.Getter;
import pl.wat.tim.mobile.RetrofitClientInstance;
import pl.wat.tim.mobile.integration.BackendAppClient;
import pl.wat.tim.mobile.integration.dto.NewUserDto;
import pl.wat.tim.mobile.integration.dto.UserDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewUserViewModel extends ViewModel {

    public MediatorLiveData<String> email = new MediatorLiveData<>();
    public MediatorLiveData<String> password = new MediatorLiveData<>();
    public MediatorLiveData<String> username = new MediatorLiveData<>();
    public MediatorLiveData<String> firstname = new MediatorLiveData<>();
    public MediatorLiveData<String> lastname = new MediatorLiveData<>();

    @Getter
    private MediatorLiveData<String> emailError = new MediatorLiveData<>();
    @Getter
    private MediatorLiveData<String> passwordError = new MediatorLiveData<>();
    @Getter
    private MediatorLiveData<String> usernameError = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;

    private Context context;
    private BackendAppClient client;

    public NewUserViewModel(Context context) {
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

    public void onRegistrationClick() {
        if (isValidNewUser()) {
            Call<UserDto> call = client.createNewUser(createNewUserDto());
            getBusy().setValue(View.VISIBLE);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    Toasty.success(context, "New account created successfully!", Toast.LENGTH_SHORT, true).show();
                    busy.setValue(View.GONE);
                    backToPreviousActivity();
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Toasty.error(context, "Something went wrong!", Toast.LENGTH_SHORT, true).show();
                    busy.setValue(View.GONE);
                }
            });
        } else {
            Toasty.error(context, "Invalid data", Toast.LENGTH_SHORT, true).show();
        }
    }

    private NewUserDto createNewUserDto() {
        return NewUserDto.builder()
                .username(username.getValue())
                .password(password.getValue())
                .firstname(firstname.getValue())
                .lastname(lastname.getValue())
                .email(email.getValue())
                .build();
    }

    private void backToPreviousActivity() {
        ((Activity) context).onBackPressed();
    }

    private boolean isValidNewUser() {
        if (username.getValue() == null) {
            usernameError.setValue("Username cannot be empty");
            return false;
        }
        if (!UserFieldValidator.isValidPassword(password.getValue())) {
            passwordError.setValue("Password should be Minimum 6 Characters");
            return false;
        }
        if (!UserFieldValidator.isValidEmail(email.getValue())) {
            emailError.setValue("Invalid Email");
            return false;
        }
        return true;
    }
}
