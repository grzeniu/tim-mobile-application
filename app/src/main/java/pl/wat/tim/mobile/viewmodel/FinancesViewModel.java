package pl.wat.tim.mobile.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import androidx.core.content.FileProvider;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import pl.wat.tim.mobile.BuildConfig;
import pl.wat.tim.mobile.RetrofitClientInstance;
import pl.wat.tim.mobile.integration.BackendAppClient;
import pl.wat.tim.mobile.user.User;
import pl.wat.tim.mobile.util.FileUtil;
import pl.wat.tim.mobile.view.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancesViewModel extends ViewModel {

    private static final String TOKEN_PREFIX = "Bearer ";

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

    public void onAddFinanceClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onGenerateReport() {
        Call<ResponseBody> call = client.generateReport(TOKEN_PREFIX + user.getToken());

        busy.setValue(View.VISIBLE);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String filename = response.headers().get("Content-Disposition").replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
                    File savedReport = FileUtil.saveToDisk(response.body(), filename);
                    Toasty.success(context, "Saved", Toast.LENGTH_SHORT, true).show();

                    if(savedReport!=null)
                        FileUtil.openReport(savedReport, context);
                } else {
                    Toasty.error(context, "Something went wrong while downloading your report", Toast.LENGTH_SHORT, true).show();
                }
                busy.setValue(View.GONE);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(context, "The report cannot be downloaded", Toast.LENGTH_SHORT, true).show();
                busy.setValue(View.GONE);
            }
        });
    }
}
