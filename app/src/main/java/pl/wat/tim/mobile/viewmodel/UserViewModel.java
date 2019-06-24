package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import lombok.Getter;
import pl.wat.tim.mobile.infrastructure.integration.BackendAppRepository;
import pl.wat.tim.mobile.infrastructure.integration.dto.LoginUserDto;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.view.RegistrationActivity;

public class UserViewModel extends ViewModel {

    public MediatorLiveData<String> username = new MediatorLiveData<>();
    public MediatorLiveData<String> password = new MediatorLiveData<>();

    @Getter
    private MediatorLiveData<String> usernameError = new MediatorLiveData<>();
    @Getter
    private MediatorLiveData<String> passwordError = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;

    @Getter
    private MediatorLiveData<User> user;

    private Context context;
    private BackendAppRepository repository;

    public UserViewModel(Context context) {
        this.user = new MediatorLiveData<>();
        this.context = context;
        this.repository = new BackendAppRepository();
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
            getBusy().setValue(View.VISIBLE);
            user = repository.generateToken(user, createLoginUserDto());
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