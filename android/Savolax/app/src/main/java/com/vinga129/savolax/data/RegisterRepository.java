package com.vinga129.savolax.data;

import androidx.annotation.NonNull;
import com.vinga129.savolax.data.model.RegisteredUser;
import com.vinga129.savolax.ui.retrofit.Controller;
import com.vinga129.savolax.ui.retrofit.rest_objects.Customer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterRepository() {}

    public static RegisterRepository getInstance() {
        if (instance == null)
            instance = new RegisterRepository();
        return instance;
    }

    public Single<Result<RegisteredUser>> register(Customer customer) {
        return RegisterDataSource.register(customer);
    }
}
