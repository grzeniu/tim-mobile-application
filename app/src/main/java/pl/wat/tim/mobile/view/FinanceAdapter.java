package pl.wat.tim.mobile.view;

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

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.CustomView> {

    private List<Finance> finances;

    private LayoutInflater layoutInflater;

    FinanceAdapter(List<Finance> finances) {
        if(finances != null){
            this.finances = finances;
        } else {
            this.finances = new ArrayList<>();
        }
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
