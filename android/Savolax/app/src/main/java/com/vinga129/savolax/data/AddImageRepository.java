package com.vinga129.savolax.data;

import android.content.Context;
import android.graphics.Bitmap;
import com.vinga129.savolax.retrofit.Controller;
import com.vinga129.savolax.other.UserRepository;
import com.vinga129.savolax.util.HelperUtil;
import io.reactivex.Single;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddImageRepository {

    private static volatile AddImageRepository instance;

    private AddImageRepository() {
    }

    public static AddImageRepository getInstance() {
        if (instance == null)
            instance = new AddImageRepository();
        return instance;
    }

    public Single<Result> uploadImage(Context context, Bitmap bitmap) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = dateFormat.format(Calendar.getInstance().getTime());
        MultipartBody.Part body = buildImageBodyPart(context, fileName, bitmap);
        return AddImageDataSource.uploadImage(body);
    }

    private static MultipartBody.Part buildImageBodyPart(Context context, String fileName, Bitmap bitmap) {
        File file = HelperUtil.persistImage(context, bitmap, fileName);
        RequestBody reqFile = RequestBody.create(Objects.requireNonNull(file), MediaType.parse("image/*"));
        return MultipartBody.Part.createFormData("file", file.getName(), reqFile);
    }


    private static class AddImageDataSource {

        public static Single<Result> uploadImage(MultipartBody.Part body) {
            return Controller.getInstance().getRestAPI().uploadImage(UserRepository.getINSTANCE().getId(), body)
                    .flatMap(AddImageDataSource::parseResult);
        }

        private static Single<Result> parseResult(Map<String, String> responseMap) {
            return Single.just(new Result.Success<>(
                    Integer.valueOf(Objects.requireNonNull(responseMap.get("id"))))
            );
        }
    }
}
