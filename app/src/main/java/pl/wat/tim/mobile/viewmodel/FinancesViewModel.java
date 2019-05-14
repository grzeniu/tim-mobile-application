package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.lifecycle.ViewModel;
import pl.wat.tim.mobile.user.User;

public class FinancesViewModel extends ViewModel {

    private Context context;
    private User user;

    public FinancesViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
    }

    public void onAddFinanceClick(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
