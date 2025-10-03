package com.jenbumapps.epass_superadmin.utility;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.jenbumapps.core.api.ApiManager;
import com.jenbumapps.core.model.time.LocalDateTime;
import com.jenbumapps.epass_superadmin.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {

    private static long exitTime = 0;

    public static void exitApp(Activity context) {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(context, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            context.finish();
        }
    }


    /**
     * @param mContext
     * @param file
     * @param listener
     */
    public static void uploadFile(FragmentActivity mContext, File file, final ImageUploadListener listener) {

        final Dialog dialog = new SpotsDialog.Builder()
                .setContext(mContext)
                .setMessage("Uploading image..")
                .build();
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));

//        int defaultFileUploadSize = Integer.parseInt(App.GLOBAL_PARAMS.get("KEY_FILE_UPLOAD_SIZE")!=null?
//                App.GLOBAL_PARAMS.get("KEY_FILE_UPLOAD_SIZE") : "2048");

        int defaultFileUploadSize = 5120;

        if (fileSize < defaultFileUploadSize) {
            dialog.show();

            ApiManager.FILE.saveImage(filePart).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        listener.onSuccess((String) response.body());
                    } else {
                        listener.onFailure("Failed to upload image. Try again");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    dialog.dismiss();
                    listener.onFailure("Failed to upload image. Try again");
                }
            });
        } else {
            listener.onFailure("File Size Too Large!!. Try again");
        }

    }


    public static long timeInMinutes(LocalDateTime fromTime, LocalDateTime toTime) {

        long timeInMillis = (toTime.getTime().getTime().getTimeInMillis() -
                fromTime.getTime().getTime().getTimeInMillis());
        return TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
    }

    public static long timeInDays(LocalDateTime fromTime, LocalDateTime toTime) {
        long timeInMillis = (toTime.getTime().getTime().getTimeInMillis() -
                fromTime.getTime().getTime().getTimeInMillis());
        return TimeUnit.MILLISECONDS.toDays(timeInMillis);
    }

    public static String time(LocalDateTime fromTime, LocalDateTime toTime) {

        String time = "";

        long days = timeInDays(fromTime, toTime);

        if (days < 1) {
            long minute = timeInMinutes(fromTime, toTime);
            time = minute + " minutes ago";
        } else if (days < 31) {
            time = days + " days ago";
        } else {
            int months = (int) (days / 30.417);
            time = months + " months ago";
        }

        return time;
    }

    public interface ImageUploadListener {
        void onSuccess(String url);
        void onFailure(String reason);
    }
}
