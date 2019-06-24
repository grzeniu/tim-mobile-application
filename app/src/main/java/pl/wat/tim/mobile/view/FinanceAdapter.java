package pl.wat.tim.mobile.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import pl.wat.tim.mobile.R;
import pl.wat.tim.mobile.databinding.RowItemBinding;
import pl.wat.tim.mobile.model.Finance;
import pl.wat.tim.mobile.viewmodel.FinancesViewModel;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.CustomView> {

    private List<Finance> finances;
    private LayoutInflater layoutInflater;
    private FinancesViewModel viewModel;

    public FinanceAdapter(List<Finance> finances, FinancesViewModel viewModel) {
        if (finances != null) {
            this.finances = finances;
        } else {
            this.finances = new ArrayList<>();
        }

        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        RowItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_item, parent, false);

        return new CustomView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        Finance finance = finances.get(position);
        holder.binding.setFinance(finance);

        holder.binding.deleteButton.setOnClickListener(v -> {
            Finance financeToRemove = finances.get(position);
            viewModel.deleteFinance(financeToRemove.getId());
        });
    }

    @Override
    public int getItemCount() {
        return this.finances.size();
    }

    class CustomView extends RecyclerView.ViewHolder {
        private RowItemBinding binding;

        CustomView(RowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
