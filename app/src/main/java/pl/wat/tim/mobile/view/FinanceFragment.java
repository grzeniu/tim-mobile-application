package pl.wat.tim.mobile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.FragmentFinancesBinding;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;

public class FinanceFragment extends Fragment {

    private FinanceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentFinancesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finances, container, false);
        FinancesViewModel viewModel = ViewModelProviders.of(getActivity()).get(FinancesViewModel.class);
        binding.setFinancesViewModel(viewModel);

        adapter = new FinanceAdapter(viewModel.getFinances().getValue());
        binding.financesRecyclerView.setAdapter(adapter);

        viewModel.getFinances().observe(this, finances -> {
            adapter = new FinanceAdapter(finances);
            binding.financesRecyclerView.setAdapter(adapter);
        });

        viewModel.getBusy().observe(this, binding.financesProgressBar::setVisibility);

        return binding.getRoot();
    }
}
