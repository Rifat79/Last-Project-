package com.example.mdibrahim.lastproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.location.Location;
import android.location.LocationListener;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;

import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    protected Button button;
    TextView text;
    private LocationManager locationManager;
    Location location;
    int k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);

        //Added

    }

    public void onClick(View view) {
        text = (TextView) findViewById(R.id.textView);


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        k  = cal.get(Calendar.ZONE_OFFSET);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        getHeroes(k,latitude,longitude);


    }

    //Added
    //This Portion is for Retrofit

    public class Hero {
        @SerializedName("altitude")
        double value;
        public double getValue() {
            return value;
        }
    }

    public interface Api {
        String BASE_URL = "http://shafayatsnest.com";
        @GET("/sunposition/get-altitude/")
        Call<Hero>getHeroes(@Query("timezone") int timezone,@Query("latitude") double latitude,@Query("longitude") double longitude);
    }

    public  void getHeroes( int altitude, double latitude, double longitude ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<Hero> call = api.getHeroes(k,latitude,longitude);

        call.enqueue(new Callback<Hero>() {
            @Override
            public void onResponse(Call<Hero> call, Response<Hero> response) {
                Hero hero = response.body();
                text.setText(String.valueOf(hero.getValue()));

            }

            @Override
            public void onFailure(Call<Hero> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }




}
