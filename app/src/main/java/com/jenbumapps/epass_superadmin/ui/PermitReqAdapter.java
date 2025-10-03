package com.jenbumapps.epass_superadmin.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PermitReqAdapter extends RecyclerView.Adapter<PermitReqAdapter.Vh> {



    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

    }

    class Vh extends RecyclerView.ViewHolder{

        public Vh(@NonNull View view) {
            super(view);
        }
    }
}
