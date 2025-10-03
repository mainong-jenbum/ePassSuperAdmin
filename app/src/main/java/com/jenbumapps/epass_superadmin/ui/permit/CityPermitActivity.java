package com.jenbumapps.epass_superadmin.ui.permit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.EPass;
import com.jenbumapps.core.model.viewmodel.CityViewModel;
import com.jenbumapps.epass_superadmin.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityPermitActivity extends AppCompatActivity implements PermitAdapter.Listener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private TextView tvNoItem;
    private RecyclerView rvPermits;
    private TextView tvExpiredReq;
    private TextView tvNewReq;
    private TextView tvTotalActive;
    private TextView tvCityName;

    private PermitAdapter mAdapter;
    private List<EPass> permits = new ArrayList<>();
    private SwipeRefreshLayout srlPermit;

    private ExtendedFloatingActionButton eFab;
    private CityViewModel cvm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_permit);

        initView();
        initAdapter();
        initListener();
        setData();
        fetchPermitData();
    }

    private void initView() {
        tvNoItem = findViewById(R.id.tv_no_item);
        srlPermit = findViewById(R.id.srl_city_permit);
        tvCityName = findViewById(R.id.tv_city_name);
        tvTotalActive = findViewById(R.id.tv_total_active_permits);
        tvNewReq = findViewById(R.id.tv_total_new_requests);
        tvExpiredReq = findViewById(R.id.tv_total_expired_req);

        rvPermits = findViewById(R.id.rv_permits);

        eFab = findViewById(R.id.efab_menu);

    }

    private void initAdapter() {
        mAdapter = new PermitAdapter(permits, this);
        rvPermits.setLayoutManager(new LinearLayoutManager(this));
        rvPermits.setAdapter(mAdapter);
    }

    private void initListener() {
        srlPermit.setOnRefreshListener(this);
        eFab.setOnClickListener(this);
    }

    private void fetchPermitData() {
        if (cvm == null) finish();

        if (!srlPermit.isRefreshing()) srlPermit.setRefreshing(true);

        ApiManager.PASS.fetchNewRequestsByCity(cvm.getCity().getId()).enqueue(new Callback<List<EPass>>() {
            @Override
            public void onResponse(Call<List<EPass>> call, Response<List<EPass>> response) {
                if (srlPermit.isRefreshing()) srlPermit.setRefreshing(false);
                if (response.isSuccessful()) {
                    permits.clear();
                    permits.addAll(response.body());
                    if (permits.size() > 0) {
                        tvNewReq.setText(String.valueOf(permits.size()));
                        rvPermits.setVisibility(View.VISIBLE);
                        tvNoItem.setVisibility(View.GONE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        rvPermits.setVisibility(View.GONE);
                        tvNoItem.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvPermits.setVisibility(View.GONE);
                    tvNoItem.setVisibility(View.VISIBLE);
                    Toast.makeText(CityPermitActivity.this, "Failed to fetch permits for " + cvm.getCity().getId() + ". Error code - " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<EPass>> call, Throwable t) {
                if (srlPermit.isRefreshing()) srlPermit.setRefreshing(false);
                rvPermits.setVisibility(View.GONE);
                tvNoItem.setVisibility(View.VISIBLE);
                t.printStackTrace();
                Toast.makeText(CityPermitActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        cvm = Parcels.unwrap(getIntent().getParcelableExtra("CITY_MODEL"));
        if (cvm == null) {
            finish();
        }

        tvCityName.setText(cvm.getCity().getName());

        tvTotalActive.setText(String.valueOf(cvm.getActiveReq()));
        tvNewReq.setText(String.valueOf(cvm.getPendingReq()));
        tvExpiredReq.setText(String.valueOf(cvm.getExpiredReq()));

    }

    @Override
    public void onPermitSelected(EPass formData, int pos, View view) {
        // TODO
    }

    @Override
    public void onRefresh() {

        // NEW PERMIT DATA
        fetchPermitData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_menu) {
            Intent intent = new Intent(this, PermitListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("KEY_CITY", Parcels.wrap(cvm.getCity()));
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
