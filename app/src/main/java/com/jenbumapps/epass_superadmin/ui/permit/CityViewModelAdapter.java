package com.jenbumapps.epass_superadmin.ui.permit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jenbumapps.epass_superadmin.R;
import com.jenbumapps.core.model.viewmodel.CityViewModel;

import java.util.List;

public class CityViewModelAdapter extends RecyclerView.Adapter<CityViewModelAdapter.Vh> {

    public interface Listener {
        void onCityModelSelected(CityViewModel cvm, int pos);
    }

    private List<CityViewModel> cityViewModels;
    private Listener listener;

    public CityViewModelAdapter(List<CityViewModel> cityViewModels, Listener listener) {
        this.cityViewModels = cityViewModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_city_item, parent, false);

        Vh vh = new Vh(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCityModelSelected(cityViewModels.get(vh.getAdapterPosition()), vh.getAdapterPosition());
            }
        });

        return vh;
    }

    @Override
    public int getItemCount() {
        return cityViewModels.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        CityViewModel cvm = cityViewModels.get(position);

        holder.tvCityName.setText(cvm.getCity().getName());
        holder.tvActive.setText(String.valueOf(cvm.getActiveReq()));
        holder.tvPending.setText(String.valueOf(cvm.getPendingReq()));
        holder.tvExpired.setText(String.valueOf(cvm.getExpiredReq()));

    }

    class Vh extends RecyclerView.ViewHolder {

        TextView tvCityName;
        TextView tvActive;
        TextView tvPending;
        TextView tvExpired;

        public Vh(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_city);
            tvActive = itemView.findViewById(R.id.tv_active);
            tvPending = itemView.findViewById(R.id.tv_pending_req);
            tvExpired = itemView.findViewById(R.id.tv_expired_req);

        }
    }
}
