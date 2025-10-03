package com.jenbumapps.epass_superadmin.ui.division;

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
import com.jenbumapps.core.model.City;
import com.jenbumapps.core.model.User;
import com.jenbumapps.core.model.viewmodel.CityViewModel;
import com.jenbumapps.epass_superadmin.R;
import com.jenbumapps.epass_superadmin.app.App;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisionActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<User> users = new ArrayList<>();
    private DivisionAdapter mAdapter;
    private SwipeRefreshLayout srlLayout;

    private ExtendedFloatingActionButton eFabNew;
    private RecyclerView rvDivision;
    private TextView tvTotalInActive;
    private TextView tvTotalActive;
    private TextView tvTotalRegistered;

    private List<City> cities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        initView();

        initListener();

        fetchCities();
    }

    private void initView() {
        srlLayout = findViewById(R.id.srl_main_activity);
        tvTotalRegistered = findViewById(R.id.tv_total_registered);
        tvTotalActive = findViewById(R.id.tv_total_active);
        tvTotalInActive = findViewById(R.id.tv_total_in_active);
        rvDivision = findViewById(R.id.rv_divisions);
        eFabNew = findViewById(R.id.efab_add);
    }

    private void initAdapter() {
        mAdapter = new DivisionAdapter(App.homeModel.getCityViewModels(), users, new DivisionAdapter.Listener() {
            @Override
            public void onDivisionSelected(CityViewModel cvm, int pos) {

            }
        });

        rvDivision.setAdapter(mAdapter);
        rvDivision.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        eFabNew.setOnClickListener(this);
        srlLayout.setOnRefreshListener(this);
    }

    private void fetchUsers() {

        if (!srlLayout.isRefreshing()) srlLayout.setRefreshing(true);

        ApiManager.USER.fetchAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);

                if (response.isSuccessful()) {
                    users.clear();
                    users.addAll(response.body());
                    initAdapter();
                } else {
                    Toast.makeText(DivisionActivity.this, "Error Code - " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DivisionActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCities() {
        if (!srlLayout.isRefreshing()) srlLayout.setRefreshing(true);
        ApiManager.CITY.fetchAll().enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);
                if (response.isSuccessful()) {

                    cities.clear();
                    cities.addAll(response.body());

                    tvTotalRegistered.setText(String.valueOf(cities.size()));

                    List<City> activeCities = new ArrayList<>();
                    for (City city : cities) {
                        if (city.isActive()) {
                            activeCities.add(city);
                        }
                    }
                    tvTotalActive.setText(String.valueOf(activeCities.size()));
                    tvTotalInActive.setText(String.valueOf(cities.size() - activeCities.size()));

                    fetchUsers();

                } else {
                    Toast.makeText(DivisionActivity.this, "Failed to fetch City Data. Error" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(DivisionActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_add) {
            new DivisionAddDialog(this, new DivisionAddDialog.Listener() {
                @Override
                public void onDivisionCreated() {
                    onRefresh();
                }
            }).show();
        }
    }

    @Override
    public void onRefresh() {
        fetchCities();
    }
}
