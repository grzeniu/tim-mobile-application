package pl.wat.tim.mobile.viewmodel.factory;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.viewmodel.NewUserViewModel;
import pl.wat.tim.mobile.viewmodel.UserViewModel;

public class NewUserViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;

    public NewUserViewModelFactory(Context context) {
        this.context = context;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new NewUserViewModel(context);
    }
}