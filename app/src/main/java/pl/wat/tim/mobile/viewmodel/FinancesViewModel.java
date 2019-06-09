package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.wat.tim.mobile.integration.BackendAppRepository;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.util.FileUtil;

public class FinancesViewModel extends ViewModel {

    private static final String TOKEN_PREFIX = "Bearer ";

    private MediatorLiveData<List<Finance>> finances = new MediatorLiveData<>();
    public MediatorLiveData<String> reportDate = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;
    private BackendAppRepository repository;
    private Context context;
    private User user;
    private boolean firstInitilization;

    public FinancesViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
        LocalDate localDate = LocalDate.now();
        this.reportDate.setValue(localDate.getMonth().name() + " " + localDate.getYear());
        this.firstInitilization = true;
        this.repository = new BackendAppRepository();
    }

    public MutableLiveData<Integer> getBusy() {
        if (busy == null) {
            busy = new MediatorLiveData<>();
            busy.setValue(View.GONE);
        }
        return busy;
    }

    public MediatorLiveData<List<Finance>> getFinances() {
        if (firstInitilization) {
            getBusy().setValue(View.VISIBLE);
            this.finances = repository.getFinances(user.getToken());
            firstInitilization = false;
            getBusy().setValue(View.GONE);
        }

        return finances;
    }

    public void onAddFinanceClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onGenerateReport() {
        getBusy().setValue(View.VISIBLE);

        File savedReport = repository.generateReport(user.getToken());
        if (savedReport != null)
            FileUtil.openReport(savedReport, context);
        getBusy().setValue(View.GONE);
    }
}
