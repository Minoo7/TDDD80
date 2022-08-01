package com.vinga129.savolax.ui.profile.edit_profile;

import static com.vinga129.savolax.util.HelperUtil.parseHttpError;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vinga129.savolax.data.AddImageRepository;
import com.vinga129.savolax.data.Result;
import com.vinga129.savolax.data.Result.Success;
import com.vinga129.savolax.data.ResultHolder;
import com.vinga129.savolax.base.NetworkAndroidViewModel;
import com.vinga129.savolax.retrofit.rest_objects.Customer;
import com.vinga129.savolax.other.UserRepository;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class EditProfileViewModel extends NetworkAndroidViewModel {

    private final ObservableField<String> currentBio = new ObservableField<>();
    private final MutableLiveData<ResultHolder<?>> editProfileResult = new MutableLiveData<>();

    public ObservableField<String> getCurrentBio() {
        return currentBio;
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
        Single<Result> addImage = AddImageRepository.getInstance().uploadImage(getApplication(), bitmap);

        // Get image id uploaded image
        // Edit profile
        Completable editCustomer = addImage.flatMapCompletable(integerResult -> {
            int image_id = ((Success<Integer>) integerResult).getData();
            customer.setImage_id(image_id);
            // editProfile(customer);
            return restAPI.updateCustomer(UserRepository.getINSTANCE().getId(), customer);
        });

        editCustomer.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(onEditProfile);
    }

    private void getBioData() {
        restAPI.getCustomerProfile(user.getId())
                .doOnSuccess(customerProfile -> currentBio.set(customerProfile.getBio()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
