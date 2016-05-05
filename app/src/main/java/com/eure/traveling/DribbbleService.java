package com.eure.traveling;

import com.eure.traveling.entity.Shot;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public final class DribbbleService extends IntentService {
    public static final String API_URL = "https://api.dribbble.com/v1/";

    public DribbbleService() {
        super("DribbbleService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        try {
            loadShotList(bundle.getString("typeName"), bundle.getInt("page"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface Dribbble {
        @GET("shots/?access_token=" + BuildConfig.DRIBBBLE_ACCESS_TOKEN)
        Call<List<Shot>> shots(@Query("list") String typeName, @Query("page") int page);
    }

    private void loadShotList(final String typeName, int page) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        Dribbble dribbble = retrofit.create(Dribbble.class);

        Call<List<Shot>> call = dribbble.shots(typeName.toLowerCase(), page);

        call.enqueue(new Callback<List<Shot>>() {
            @Override
            public void onResponse(Call<List<Shot>> call, final Response<List<Shot>> response) {
                final Realm realm = Realm.getDefaultInstance();
                try {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (Shot shot : response.body()) {
                                shot.setType(typeName);
                                realm.copyToRealm(shot);
                            }
                        }
                    });
                } finally {
                    realm.close();
                }
            }

            @Override
            public void onFailure(Call<List<Shot>> call, Throwable t) {
                Toast.makeText(DribbbleService.this, "通信に失敗しました", Toast.LENGTH_SHORT);
            }
        });
    }
}
