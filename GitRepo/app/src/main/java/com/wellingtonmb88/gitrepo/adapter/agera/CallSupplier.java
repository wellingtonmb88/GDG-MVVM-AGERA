package com.wellingtonmb88.gitrepo.adapter.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import java.io.IOException;

import me.drakeet.retrofit2.adapter.agera.HttpException;
import retrofit2.Call;
import retrofit2.Response;

import static com.google.android.agera.Preconditions.checkNotNull;

/**
 * Created by wbarbosa on 14/06/2016.
 */
class CallSupplier<T> implements Supplier<Result<T>> {

    private final Call<T> call;


    CallSupplier(@NonNull final Call<T> call) {
        this.call = checkNotNull(call);
    }


    @NonNull
    @Override public Result<T> get() {
        Result<T> result;
        try {
            Response<T> response = call.clone().execute();
            if (response.isSuccessful()) {
                result = Result.success(response.body());
            } else {
                result = Result.failure(new HttpException(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = Result.failure(e);
        }
        return result;
    }
}
