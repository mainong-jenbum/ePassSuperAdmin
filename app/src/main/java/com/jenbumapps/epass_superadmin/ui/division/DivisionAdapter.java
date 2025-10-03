package com.jenbumapps.epass_superadmin.ui.division;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jenbumapps.core.model.User;
import com.jenbumapps.core.model.viewmodel.CityViewModel;
import com.jenbumapps.epass_superadmin.R;

import java.util.List;
import java.util.Locale;

public class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.Vh> {

    public interface Listener {
        void onDivisionSelected(CityViewModel cvm, int pos);
    }


    private Listener listener;
    private List<CityViewModel> cityViewModels;
    private List<User> users;

    public DivisionAdapter(List<CityViewModel> cityViewModels, List<User> users, Listener listener) {
        this.cityViewModels = cityViewModels;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_division_item, parent, false);

        Vh vh = new Vh(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDivisionSelected(cityViewModels.get(vh.getAdapterPosition()), vh.getAdapterPosition());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

        CityViewModel cvm = cityViewModels.get(position);

        holder.tvDivisionName.setText(cvm.getCity().getName());
        holder.tvStatus.setText(cvm.getCity().isActive() ? "ACTIVE" : "INACTIVE");
        holder.tvId.setText(String.format(Locale.ENGLISH, "#%d", cvm.getCity().getId()));

        for (User user : users) {
            if (user.getCity().getId() == cvm.getCity().getId()) {
                holder.tvUserName.setText(user.getName());
                holder.tvDesignation.setText(user.getDesignation());
            }
        }
    }

    @Override
    public int getItemCount() {
        return cityViewModels.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView tvDivisionName;
        TextView tvStatus;
        TextView tvId;
        TextView tvUserName;
        TextView tvDesignation;

        public Vh(@NonNull View view) {
            super(view);

            tvDivisionName = view.findViewById(R.id.tv_division_name);
            tvStatus = view.findViewById(R.id.tv_status);
            tvId = view.findViewById(R.id.tv_id);
            tvUserName = view.findViewById(R.id.tv_auth_user);
            tvDesignation = view.findViewById(R.id.tv_designation);

        }
    }
}
