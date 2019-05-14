package pl.wat.tim.mobile.viewmodel.factory;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;

public class FinancesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private User user;
    private Context context;

    public FinancesViewModelFactory(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FinancesViewModel(context, user);
    }
}
