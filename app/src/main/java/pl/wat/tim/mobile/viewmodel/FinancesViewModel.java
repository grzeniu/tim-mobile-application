package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.infrastructure.integration.BackendAppRepository;
import pl.wat.tim.mobile.infrastructure.integration.dto.FinanceDto;
import pl.wat.tim.mobile.infrastructure.persistence.DatabaseRepository;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.util.FileUtil;
import pl.wat.tim.mobile.view.FinanceFragment;
import pl.wat.tim.mobile.view.FragmentProvider;
import pl.wat.tim.mobile.view.NewFinanceFragment;

public class FinancesViewModel extends ViewModel {

    private static final String INCOME = "INCOME";
    private static final String EXPENSE = "EXPENSE";

    private MediatorLiveData<List<Finance>> finances = new MediatorLiveData<>();
    public MediatorLiveData<String> reportDate = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> itemDeleted;
    private MediatorLiveData<List<String>> categories;
    public MediatorLiveData<String> description = new MediatorLiveData<>();
    public MediatorLiveData<String> value = new MediatorLiveData<>();
    public MediatorLiveData<Finance> newFinance = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;
    private BackendAppRepository repository;
    private DatabaseRepository dbRepository;
    private FragmentProvider fragmentProvider;
    private Context context;
    private User user;
    private boolean firstInitilization;
    private Integer selectedCategoryId;
    private String selectedFinanceType;

    public FinancesViewModel(Context context, User user, FragmentProvider provider) {
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
        this.fragmentProvider = provider;
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

    public void fetchFinances() {
        repository.getFinances(user.getToken(), finances);
        getBusy().setValue(View.GONE);
    }

    public MediatorLiveData<Finance> getNewFinance(){
        if(newFinance==null){
            newFinance=new MediatorLiveData<>();
        }
        return newFinance;
    }

    public MediatorLiveData<List<String>> getCategories() {
        if (categories == null) {
            getBusy().setValue(View.VISIBLE);
            this.categories = repository.getCategories(user.getToken());
            getBusy().setValue(View.GONE);
        }
        return categories;
    }

    public MediatorLiveData<Boolean> getItemDeleted() {
        if (itemDeleted == null) {
            itemDeleted = new MediatorLiveData<>();
            itemDeleted.setValue(true);
        }
        return itemDeleted;
    }

    public void onSelectCategory(AdapterView<?> parent, View view, int pos, long id) {
        selectedCategoryId = pos+1;
    }

    public void onSelectFinanceType(AdapterView<?> parent, View view, int pos, long id) {
        selectedFinanceType = parent.getSelectedItem().toString().toUpperCase();
    }

    public void onAddFinance() {
        getBusy().setValue(View.VISIBLE);

        FinanceDto financeDto = FinanceDto.builder()
                .description(description.getValue())
                .value(value.getValue())
                .categoryId(selectedCategoryId)
                .build();

        if (selectedFinanceType.equals(INCOME)) {
            repository.addIncome(user.getToken(), financeDto, newFinance);
        } else if (selectedFinanceType.equals(EXPENSE)) {
            repository.addExpense(user.getToken(), financeDto, newFinance);
        }

        getBusy().setValue(View.GONE);
    }

    public void onBackToFinances() {
        fragmentProvider.showFragment(new FinanceFragment(), R.id.fragment_container);
    }

    public void onAddFinanceClick() {
        fragmentProvider.showFragment(new NewFinanceFragment(), R.id.fragment_container);
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

    public void clear(){
        categories=null;
        selectedFinanceType=null;
        selectedCategoryId=null;
        newFinance=null;
        description = new MediatorLiveData<>();
        value = new MediatorLiveData<>();
    }
}
