package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import lombok.Getter;
import pl.wat.tim.mobile.infrastructure.integration.BackendAppRepository;
import pl.wat.tim.mobile.infrastructure.integration.dto.NewUserDto;

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
    @Getter
    private MediatorLiveData<Boolean> userCreated = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;

    private Context context;
    private BackendAppRepository repository;

    public NewUserViewModel(Context context) {
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

    public void onRegistrationClick() {
        if (isValidNewUser()) {
            getBusy().setValue(View.VISIBLE);
            repository.createNewUser(userCreated, createNewUserDto());
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
