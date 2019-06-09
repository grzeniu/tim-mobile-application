package pl.wat.tim.mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.stetho.Stetho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import es.dmoral.toasty.Toasty;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.ActivityLoginBinding;
import pl.wat.tim.mobile.viewmodel.UserViewModel;
import pl.wat.tim.mobile.viewmodel.factory.UserViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        final ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        UserViewModel userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this)).get(UserViewModel.class);

        binding.setUserViewModel(userViewModel);

        userViewModel.getBusy().observe(this, binding.loginProgressBar::setVisibility);

        userViewModel.getUser().observe(this, user -> {
            userViewModel.getBusy().setValue(View.GONE);

            if (user == null)
                Toasty.error(this, "Incorrect credentials, try again").show();
            else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("USER_OBJ", user);
                startActivity(intent);
                finish();
            }
        });

        setErrorHandling(userViewModel, binding);
    }

    private void setErrorHandling(UserViewModel userViewModel, final ActivityLoginBinding binding) {
        userViewModel.getUsernameError().observe(this, err -> {
            binding.editUsername.setError(err);
            binding.editUsername.requestFocus();
        });

        userViewModel.getPasswordError().observe(this, err -> {
            binding.editPassword.setError(err);
            binding.editPassword.requestFocus();
        });
    }
}
