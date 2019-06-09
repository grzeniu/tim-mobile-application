package pl.wat.tim.mobile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import es.dmoral.toasty.Toasty;
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

        viewModel.getBusy().observe(this, binding.progressBar::setVisibility);

        viewModel.getUserCreated().observe(this, created -> {
            viewModel.getBusy().setValue(View.GONE);

            if (created) {
                Toasty.success(getApplicationContext(), "New account created successfully!", Toast.LENGTH_SHORT, true).show();
                onBackPressed();
            } else {
                Toasty.error(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT, true).show();
            }
        });

        setErrorHandling(viewModel, binding);
    }

    private void setErrorHandling(NewUserViewModel userViewModel, final ActivityRegistrationBinding binding) {
        userViewModel.getEmailError().observe(this, err -> {
            binding.editEmail.setError(err);
            binding.editEmail.requestFocus();
        });

        userViewModel.getPasswordError().observe(this, err -> {
            binding.editPassword.setError(err);
            binding.editPassword.requestFocus();
        });

        userViewModel.getUsernameError().observe(this, err -> {
            binding.editUsername.setError(err);
            binding.editUsername.requestFocus();
        });
    }
}

