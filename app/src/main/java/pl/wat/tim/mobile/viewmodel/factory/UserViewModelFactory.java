package pl.wat.tim.mobile.viewmodel.factory;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import lombok.AllArgsConstructor;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.viewmodel.UserViewModel;

@AllArgsConstructor
public class UserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private User user;
    private Context context;

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new UserViewModel(context, user);
    }
}