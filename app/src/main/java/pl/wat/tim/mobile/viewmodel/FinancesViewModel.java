package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import pl.wat.tim.mobile.RetrofitClientInstance;
import pl.wat.tim.mobile.integration.BackendAppClient;
import pl.wat.tim.mobile.integration.dto.FinanceResponseDto;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.model.User;
import pl.wat.tim.mobile.util.FileUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancesViewModel extends ViewModel {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final MediatorLiveData<List<Finance>> finances = new MediatorLiveData<>();

    public MediatorLiveData<String> reportDate = new MediatorLiveData<>();

    private MediatorLiveData<Integer> busy;
    private BackendAppClient client;
    private Context context;
    private User user;

    public FinancesViewModel(Context context, User user) {
        this.user = user;
        this.context = context;
        LocalDate localDate = LocalDate.now();
        this.reportDate.setValue(localDate.getMonth().name() + " " + localDate.getYear());

        this.client = RetrofitClientInstance.getRetrofitInstance().create(BackendAppClient.class);
    }

    public MutableLiveData<Integer> getBusy() {
        if (busy == null) {
            busy = new MediatorLiveData<>();
            busy.setValue(View.GONE);
        }
        return busy;
    }

    public MediatorLiveData<List<Finance>> getFinances() {
        if (finances.getValue() == null) {
            getFinancesData();
        }

        return finances;
    }

    public void onAddFinanceClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onGenerateReport() {
        Call<ResponseBody> call = client.generateReport(TOKEN_PREFIX + user.getToken());

        getBusy().setValue(View.VISIBLE);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String filename = response.headers().get("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
                    File savedReport = FileUtil.saveToDisk(response.body(), filename);
                    Toasty.success(context, "Saved", Toast.LENGTH_SHORT, true).show();

                    if (savedReport != null)
                        FileUtil.openReport(savedReport, context);
                } else {
                    Toasty.error(context, "Something went wrong while downloading your report", Toast.LENGTH_SHORT, true).show();
                }
                getBusy().setValue(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(context, "The report cannot be downloaded", Toast.LENGTH_SHORT, true).show();
                getBusy().setValue(View.GONE);
            }
        });
    }

    private void getFinancesData() {
        Call<List<FinanceResponseDto>> incomesCall = client.getIncomes(TOKEN_PREFIX + user.getToken());
        Call<List<FinanceResponseDto>> expensesCall = client.getExpenses(TOKEN_PREFIX + user.getToken());

        getBusy().setValue(View.VISIBLE);
        callGetFiances(incomesCall);
        callGetFiances(expensesCall);
    }

    private void callGetFiances(Call<List<FinanceResponseDto>> call) {
        call.enqueue(new Callback<List<FinanceResponseDto>>() {

            @Override
            public void onResponse(Call<List<FinanceResponseDto>> call, Response<List<FinanceResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    if (finances.getValue() != null) {
                        finances.getValue().addAll(mapResponseToFinance(response.body()));
                        finances.setValue(finances.getValue());
                    } else {
                        finances.setValue(mapResponseToFinance(response.body()));
                    }
                }
                getBusy().setValue(View.GONE);
            }

            @Override
            public void onFailure(Call<List<FinanceResponseDto>> call, Throwable t) {
                Toasty.error(context, "The data cannot be downloaded", Toast.LENGTH_SHORT, true).show();
                getBusy().setValue(View.GONE);
            }
        });
    }

    private List<Finance> mapResponseToFinance(List<FinanceResponseDto> dtos) {
        return dtos.stream().map(it -> Finance.builder()
                .id(it.getId())
                .description(it.getDescription())
                .value(it.getValue())
                .financeType(it.getFinanceType())
                .createdDate(LocalDateTime.parse(it.getCreatedDate()))
                .category(it.getCategory().getName())
                .build())
                .collect(Collectors.toList());
    }
}
