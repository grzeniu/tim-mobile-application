package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import lombok.Getter;
import pl.wat.tim.mobile.user.User;

public class UserViewModel extends ViewModel {

    public MediatorLiveData<String> email = new MediatorLiveData<>();
    public MediatorLiveData<String> password = new MediatorLiveData<>();

    @Getter
    private MediatorLiveData<String> emailError = new MediatorLiveData<>();
    @Getter
    private MediatorLiveData<String> passwordError = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;

    private User user;
    private Context context;

    public UserViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
    }

    public MutableLiveData<Integer> getBusy() {
        if (busy == null) {
            busy = new MediatorLiveData<>();
            busy.setValue(View.GONE);
        }

        return busy;
    }

    public void onLoginClick() {
        user.setUserCredentials(email.getValue(), password.getValue());

        if (!user.isValidEmail()) {
            emailError.setValue("Invalid Email");
        } else if (!user.isValidPassword()) {
            passwordError.setValue("Password Should be Minimum 6 Characters");
        } else if (user.isValidCredential()) {
            getBusy().setValue(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    getBusy().setValue(View.GONE);

                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();

                        //TODO here switch activity after successful sign up
//                    Intent intent = new Intent(context, ProfileActivity.class);
//                    intent.putExtra("USER_OBJ", user);
//                    context.startActivity(intent);

//                    ((Activity) context).finish();
                }
            }, 500);
        } else {
            //TODO add TextView with error message to xml
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}