package pl.wat.tim.mobile.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.ActivityRegistrationBinding;
import pl.wat.tim.mobile.viewmodel.NewUserViewModel;
import pl.wat.tim.mobile.viewmodel.factory.NewUserViewModelFactory;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityRegistrationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        NewUserViewModel viewModel = ViewModelProviders.of(this, new NewUserViewModelFactory(this)).get(NewUserViewModel.class);

        binding.setNewUserViewModel(viewModel);

        viewModel.getBusy().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer obj) {
                binding.progressBar.setVisibility(obj);
            }
        });

        setErrorHandling(viewModel, binding);
    }

    private void setErrorHandling(NewUserViewModel userViewModel, final ActivityRegistrationBinding binding) {
        userViewModel.getEmailError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editEmail.setError(err);
                binding.editEmail.requestFocus();
            }
        });

        userViewModel.getPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editPassword.setError(err);
                binding.editPassword.requestFocus();
            }
        });

        userViewModel.getUsernameError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String err) {
                binding.editUsername.setError(err);
                binding.editUsername.requestFocus();
            }
        });
    }
}

