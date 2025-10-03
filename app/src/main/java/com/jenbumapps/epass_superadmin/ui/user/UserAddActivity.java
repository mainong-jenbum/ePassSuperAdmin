package com.jenbumapps.epass_superadmin.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.City;
import com.jenbumapps.core.model.User;
import com.jenbumapps.epass_superadmin.R;
import com.jenbumapps.epass_superadmin.utility.ImageCompression;
import com.jenbumapps.epass_superadmin.utility.Utility;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinCity;
    private EditText etAddress;
    private TextView tvSignature;
    private EditText etDesignation;
    private EditText etDesignationAbbr;
    private EditText etPassword;
    private EditText etPhone;
    private EditText etName;
    private Button btnRegister;
    private EasyImage easyImage;

    private User user;
    private boolean isNew = true;

    private List<City> cities = new ArrayList<>();
    private ArrayAdapter<City> cityArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        initView();
        initAdapter();
        initListener();

        user = Parcels.unwrap(getIntent().getParcelableExtra("KEY_USER"));
        if (user == null) {
            user = new User();
            isNew = true;
        }
        fetchCity();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        etDesignationAbbr = findViewById(R.id.et_designation_abbr);
        etDesignation = findViewById(R.id.et_designation);
        tvSignature = findViewById(R.id.tv_signature);
        etAddress = findViewById(R.id.et_address);
        spinCity = findViewById(R.id.spin_city);

        btnRegister = findViewById(R.id.btn_register);

    }

    private void initAdapter() {
        cityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinCity.setAdapter(cityArrayAdapter);
    }

    private void initListener() {
        tvSignature.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        spinCity.setOnItemSelectedListener(this);
    }

    private void fetchCity() {
        ApiManager.CITY.fetchActive().enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.isSuccessful()) {
                    cities.clear();
                    cities.addAll(response.body());
                    cityArrayAdapter.notifyDataSetChanged();

                    if (!isNew) {
                        setData();
                    }

                } else {
                    Toast.makeText(UserAddActivity.this, "Failed to fetch city data. Code " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Toast.makeText(UserAddActivity.this, "Failed to fetch city data. Message " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        if (user == null) return;
        spinCity.setSelection(cityArrayAdapter.getPosition(user.getCity()));
        etAddress.setText(user.getAddress());
        tvSignature.setText(user.getSignatureUrl());
        etDesignation.setText(user.getDesignation());
        etDesignationAbbr.setText(user.getDesignationAbbr());
        etPassword.setText(user.getPassword());
        etPhone.setText(String.valueOf(user.getPhone()));
        etName.setText(user.getName());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            if (validationSuccess()) {
                ApiManager.USER.add(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UserAddActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UserAddActivity.this, "Failed to create user. Code " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(UserAddActivity.this, "Failed to create user. Error - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else if (v.getId() == R.id.tv_signature) {
            easyImage = new EasyImage.Builder(UserAddActivity.this).build();
            easyImage.openChooser(this);
        }
    }

    private boolean validationSuccess() {

        if (etName.getText().toString().trim().length() < 3) {
            Toast.makeText(UserAddActivity.this, "Enter a valid Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setName(etName.getText().toString().trim());
        }

        if (etPhone.getText().toString().trim().length() != 10) {
            Toast.makeText(UserAddActivity.this, "Enter a valid phone", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setPhone(Long.parseLong(etPhone.getText().toString().trim()));
        }

        if (etPassword.getText().toString().trim().length() < 3) {
            Toast.makeText(UserAddActivity.this, "Enter a valid password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setPassword(etPassword.getText().toString().trim());
        }

        if (etDesignationAbbr.getText().toString().trim().length() < 2) {
            Toast.makeText(UserAddActivity.this, "Enter a valid designation ABBR", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setDesignationAbbr(etDesignationAbbr.getText().toString().trim());
        }

        if (etDesignationAbbr.getText().toString().trim().length() < 2) {
            Toast.makeText(UserAddActivity.this, "Enter a valid designation Abbr", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setDesignationAbbr(etDesignationAbbr.getText().toString().trim());
        }

        if (etDesignation.getText().toString().trim().length() < 2) {
            Toast.makeText(UserAddActivity.this, "Enter a valid designation", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setDesignation(etDesignation.getText().toString().trim());
        }

        if (etAddress.getText().toString().trim().length() < 2) {
            Toast.makeText(UserAddActivity.this, "Enter a valid Address", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            user.setAddress(etAddress.getText().toString().trim());
        }

        if (TextUtils.isEmpty(user.getSignatureUrl())) {
            Toast.makeText(this, "Upload signature", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (user.getCity() == null) {
            Toast.makeText(UserAddActivity.this, "Select a division", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                uploadImageFile(imageFiles);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    private void uploadImageFile(MediaFile[] imageFiles) {
        if (imageFiles.length > 0) {
            java.io.File f = imageFiles[0].getFile();
            Log.d("Personal Fragment ", "FILE PATH :" + f.getAbsolutePath());

            ImageCompression.compressImage(Uri.fromFile(f).getPath(), this);

            Utility.uploadFile(this, f, new Utility.ImageUploadListener() {
                @Override
                public void onSuccess(String url) {
                    user.setSignatureUrl(url);
                    tvSignature.setText(String.format("UPLOADED (%s)", url));
                }

                @Override
                public void onFailure(String reason) {
                    Toast.makeText(UserAddActivity.this, reason, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        user.setCity((City) parent.getSelectedItem());
        etAddress.setHint(user.getCity().getName().toUpperCase()+" :: CHANGLANG DISTRICT");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
