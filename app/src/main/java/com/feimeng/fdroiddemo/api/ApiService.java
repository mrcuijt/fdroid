package com.feimeng.fdroiddemo.api;


import com.feimeng.fdroid.mvp.model.api.bean.Response;
import com.feimeng.fdroid.utils.RxJavas;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API接口
 * Created by feimeng on 2018/3/17.
 */
public interface ApiService {
    @POST("user/login")
    Observable<Response<String>> login(@Body RequestBody requestBody);

    @POST("user/register")
    Observable<Response<RxJavas.NULL>> register(@Body RequestBody requestBody);
}
