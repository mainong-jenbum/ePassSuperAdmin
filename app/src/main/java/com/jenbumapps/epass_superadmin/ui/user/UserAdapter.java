package com.jenbumapps.epass_superadmin.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jenbumapps.core.model.User;
import com.jenbumapps.epass_superadmin.R;

import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Vh> {

    public interface Listener {
        void onUserSelected(User user, int pos);
    }


    private Listener listener;
    private List<User> users;

    public UserAdapter(List<User> users, Listener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_item, parent, false);

        Vh vh = new Vh(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserSelected(users.get(vh.getAdapterPosition()), vh.getAdapterPosition());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

        User user = users.get(position);

        holder.tvUserName.setText(user.getName());
        holder.tvStatus.setText(user.getStatus().toString());
        holder.tvId.setText(String.format(Locale.ENGLISH, "#%d", user.getId()));
        holder.tvDesignationAbbr.setText(user.getDesignationAbbr());
        holder.tvDesignation.setText(user.getDesignation());

        holder.tvCityName.setText(user.getCity().getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvStatus;
        TextView tvId;
        TextView tvDesignationAbbr;
        TextView tvDesignation;
        TextView tvCityName;

        public Vh(@NonNull View view) {
            super(view);

            tvUserName = view.findViewById(R.id.tv_user_name);
            tvStatus = view.findViewById(R.id.tv_status);
            tvId = view.findViewById(R.id.tv_id);
            tvDesignationAbbr = view.findViewById(R.id.tv_designation_abbr);
            tvDesignation = view.findViewById(R.id.tv_designation);
            tvCityName = view.findViewById(R.id.tv_city_name);

        }
    }
}
