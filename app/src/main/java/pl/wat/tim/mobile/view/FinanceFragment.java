package pl.wat.tim.mobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.FragmentFinancesBinding;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;

public class FinanceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentFinancesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finances, container, false);
        FinancesViewModel viewModel = ViewModelProviders.of(getActivity()).get(FinancesViewModel.class);
        binding.setFinancesViewModel(viewModel);

        return binding.getRoot();
    }
}
