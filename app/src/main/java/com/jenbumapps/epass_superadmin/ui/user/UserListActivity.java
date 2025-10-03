package com.jenbumapps.epass_superadmin.ui.user;

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
import com.jenbumapps.core.model.User;
import com.jenbumapps.core.model.codec.UserStatus;
import com.jenbumapps.epass_superadmin.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private List<User> users = new ArrayList<>();
    private UserAdapter mAdapter;

    private SwipeRefreshLayout srlLayout;
    private ExtendedFloatingActionButton eFabNew;
    private RecyclerView rvUsers;
    private TextView tvTotalInActive;
    private TextView tvTotalActive;
    private TextView tvTotalRegistered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initView();

        initListener();
        initAdapter();

        fetchData();

    }

    private void initView() {
        srlLayout = findViewById(R.id.swipe_refresh_layout);
        tvTotalRegistered = findViewById(R.id.tv_total_registered);
        tvTotalActive = findViewById(R.id.tv_total_active);
        tvTotalInActive = findViewById(R.id.tv_total_in_active);
        rvUsers = findViewById(R.id.rv_users);
        eFabNew = findViewById(R.id.efab_add);
    }

    private void initAdapter() {
        mAdapter = new UserAdapter(users, new UserAdapter.Listener() {
            @Override
            public void onUserSelected(User user, int pos) {

            }
        });

        rvUsers.setAdapter(mAdapter);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initListener() {
        srlLayout.setOnRefreshListener(this);
        eFabNew.setOnClickListener(this);
    }

    private void fetchData() {

        if (!srlLayout.isRefreshing()) srlLayout.setRefreshing(true);

        ApiManager.USER.fetchAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);

                if (response.isSuccessful()) {
                    users.clear();
                    users.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                    setData();
                } else {
                    Toast.makeText(UserListActivity.this, "Error Code - " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (srlLayout.isRefreshing()) srlLayout.setRefreshing(false);
                t.printStackTrace();
                Toast.makeText(UserListActivity.this, "Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        tvTotalRegistered.setText(String.valueOf(users.size()));

        List<User> activeUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                activeUsers.add(user);
            }
        }

        tvTotalActive.setText(String.valueOf(activeUsers.size()));
        tvTotalInActive.setText(String.valueOf(users.size() - activeUsers.size()));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.efab_add) {
            startActivity(new Intent(this, UserAddActivity.class));
        }
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
