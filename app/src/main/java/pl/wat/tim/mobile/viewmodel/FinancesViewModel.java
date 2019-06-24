package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.wat.tim.mobile.infrastructure.integration.BackendAppRepository;
import pl.wat.tim.mobile.infrastructure.persistence.DatabaseRepository;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.util.FileUtil;

public class FinancesViewModel extends ViewModel {

    private MediatorLiveData<List<Finance>> finances = new MediatorLiveData<>();
    public MediatorLiveData<String> reportDate = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> itemDeleted;

    private MediatorLiveData<Integer> busy;
    private BackendAppRepository repository;
    private DatabaseRepository dbRepository;
    private Context context;
    private User user;
    private boolean firstInitilization;

    public FinancesViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
        LocalDateTime localDate = LocalDateTime.now();
        this.user.setDateOfLogin(localDate);
        this.reportDate.setValue(localDate.getMonth().name() + " " + localDate.getYear());
        this.firstInitilization = true;
        this.repository = new BackendAppRepository();
        this.itemDeleted = null;
        this.dbRepository = new DatabaseRepository(context);
        this.dbRepository.addUser(user);
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
            this.finances = repository.getFinances(user.getToken(), new MediatorLiveData<>());
            getBusy().setValue(View.GONE);
        }

        return finances;
    }

    public void fetchFinances(){
        repository.getFinances(user.getToken(),finances);
        getBusy().setValue(View.GONE);
    }

    public MediatorLiveData<Boolean> getItemDeleted() {
        if (itemDeleted == null) {
            itemDeleted = new MediatorLiveData<>();
            itemDeleted.setValue(true);
        }
        return itemDeleted;
    }

    public void onAddFinanceClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void deleteFinance(int id) {
        getBusy().setValue(View.VISIBLE);
        repository.deleteFinance(user.getToken(), id, itemDeleted);
    }

    public void onGenerateReport() {
        getBusy().setValue(View.VISIBLE);

        File savedReport = repository.generateReport(user.getToken());
        if (savedReport != null)
            FileUtil.openReport(savedReport, context);
        getBusy().setValue(View.GONE);
    }
}
