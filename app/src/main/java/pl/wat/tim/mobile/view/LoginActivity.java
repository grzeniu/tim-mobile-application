package pl.wat.tim.mobile.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.ActivityLoginBinding;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.viewmodel.UserViewModel;
import pl.wat.tim.mobile.viewmodel.factory.UserViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        UserViewModel userViewModel = ViewModelProviders.of(this, new UserViewModelFactory(this, new User())).get(UserViewModel.class);

        binding.setUserViewModel(userViewModel);

        //TODO refactor - move to another method
        userViewModel.getEmailError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editEmail.requestFocus();
                binding.editEmail.setError(err);
            }
        });

        userViewModel.getPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editPassword.requestFocus();
                binding.editPassword.setError(err);
            }
        });
    }
}
