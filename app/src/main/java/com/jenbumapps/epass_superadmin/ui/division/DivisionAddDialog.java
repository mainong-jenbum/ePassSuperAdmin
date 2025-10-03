package com.jenbumapps.epass_superadmin.ui.division;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.City;
import com.jenbumapps.epass_superadmin.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DivisionAddDialog extends Dialog implements View.OnClickListener {

    private Button btnRegister;
    private EditText etName;

    public interface Listener {
        void onDivisionCreated();
    }

    private Context context;
    private Listener listener;

    public DivisionAddDialog(@NonNull Context context, Listener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_division_add);

        initView();
        initListener();
    }

    private void initView() {

        Window window = getWindow();
        if (window != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setLayout(width, height);
        }

        etName = findViewById(R.id.et_name);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void initListener() {
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            if (etName.getText().toString().trim().length() > 2) {
                City city = new City();
                city.setName(etName.getText().toString().trim());
                ApiManager.CITY.create(city).enqueue(new Callback<City>() {
                    @Override
                    public void onResponse(Call<City> call, Response<City> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "A new Division has been added", Toast.LENGTH_SHORT).show();
                            listener.onDivisionCreated();
                            dismiss();
                        } else {
                            Toast.makeText(context, "Failed to register a new Division. Code " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<City> call, Throwable t) {
                        Toast.makeText(context, "Failed to register a new Division. Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Enter a valid division name", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
