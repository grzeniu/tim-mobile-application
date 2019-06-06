package pl.wat.tim.mobile.view;

import android.os.Bundle;

import com.facebook.stetho.Stetho;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.ActivityLoginBinding;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.viewmodel.UserViewModel;
import pl.wat.tim.mobile.viewmodel.factory.UserViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);

        final ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        UserViewModel userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this, new User())).get(UserViewModel.class);

        binding.setUserViewModel(userViewModel);

        userViewModel.getBusy().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer obj) {
                binding.loginProgressBar.setVisibility(obj);
            }
        });

        setErrorHandling(userViewModel, binding);
    }

    private void setErrorHandling(UserViewModel userViewModel, final ActivityLoginBinding binding){
        userViewModel.getUsernameError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editUsername.setError(err);
                binding.editUsername.requestFocus();
            }
        });

        userViewModel.getPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editPassword.setError(err);
                binding.editPassword.requestFocus();
            }
        });
    }
}
