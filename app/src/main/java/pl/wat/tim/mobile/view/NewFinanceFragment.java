package pl.wat.tim.mobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import es.dmoral.toasty.Toasty;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.FragmentNewFinancesBinding;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;

public class NewFinanceFragment extends Fragment {

    private FinancesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentNewFinancesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_finances, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(FinancesViewModel.class);
        binding.setFinancesViewModel(viewModel);

        viewModel.getCategories().observe(this, categories -> {
            if(categories!=null){
                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
                binding.spinnerCategory.setSelection(1);
                binding.spinnerFinancetype.setSelection(1);
                binding.setSpinnerAdapter(adapter);
            }
        });

        viewModel.getNewFinance().observe(this, newFinance -> {
            if(newFinance!= null){
                Toasty.success(this.getActivity(), "New Finance was added").show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FinanceFragment()).commit();
            } else {
                Toasty.error(this.getActivity(),"Error during adding an new finance").show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.clear();
    }
}
