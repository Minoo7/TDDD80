package com.vinga129.savolax.ui.profile.edit_profile;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vinga129.savolax.data.AddImageRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.retrofit.NetworkAndroidViewModel;
import com.vinga129.savolax.retrofit.NetworkViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.retrofit.rest_objects.CustomerProfile;
import com.vinga129.savolax.ui.later.UserRepository;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class EditProfileViewModel extends NetworkAndroidViewModel {

    private final MutableLiveData<ResultHolder<String>> bioResult = new MutableLiveData<>();
    private final MutableLiveData<ResultHolder<?>> editProfileResult = new MutableLiveData<>();

    public LiveData<ResultHolder<String>> getBioResult() {
        return bioResult;
    }

    public EditProfileViewModel(Application application) {
        super(application);
        getBioData();
    }

    public LiveData<ResultHolder<?>> getEditProfileResult() {
        return editProfileResult;
    }

    private final CompletableObserver onEditProfile = new CompletableObserver() {
        @Override
        public void onSubscribe(final Disposable d) {
        }

        @Override
        public void onComplete() {
            editProfileResult.setValue(new ResultHolder<>());
        }

        @Override
        public void onError(final Throwable e) {
            if (e instanceof HttpException) {
                editProfileResult.setValue(parseHttpError((HttpException) e));
            }
        }
    };

    public void editProfile(Customer customer) {
        restAPI.updateCustomer(UserRepository.getINSTANCE().getId(), customer).subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribeWith(onEditProfile);
    }

    public void editProfileWithImage(Bitmap bitmap, Customer customer) {
        // Upload image from bitmap
        Single<Result<Integer>> addImage = AddImageRepository.getInstance().uploadImage(getApplication(), bitmap);

        // Get image id uploaded image
        // Edit profile
        Completable editCustomer = addImage.flatMapCompletable(integerResult -> {
            int image_id = ((Success<Integer>) integerResult).getData();
            customer.setImage_id(image_id);
            // editProfile(customer);
            return restAPI.updateCustomer(UserRepository.getINSTANCE().getId(), customer);
        });

        editCustomer.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(onEditProfile);
    }

    private void getBioData() {
        restAPI.getCustomerProfile(UserRepository.getINSTANCE().getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                new DisposableSingleObserver<CustomerProfile>() {
                    @Override
                    public void onSuccess(final CustomerProfile value) {
                        bioResult.setValue(new ResultHolder<>(value.getBio()));
                    }

                    @Override
                    public void onError(final Throwable e) {
                        if (e instanceof HttpException) {
                            bioResult.setValue(parseHttpError((HttpException) e));
                        }
                    }
                });
    }
}
