package cn.edu.jssvc.wanhongxu.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.edu.jssvc.wanhongxu.coolweather.gson.Forecast;
import cn.edu.jssvc.wanhongxu.coolweather.gson.HourlyForecast;
import cn.edu.jssvc.wanhongxu.coolweather.gson.Weather;
import cn.edu.jssvc.wanhongxu.coolweather.service.AutoUpdateService;
import cn.edu.jssvc.wanhongxu.coolweather.util.HttpUtil;
import cn.edu.jssvc.wanhongxu.coolweather.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView apiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    private String mWeatherId;
    private Button navButton;
    public DrawerLayout drawerLayout;
    public SwipeRefreshLayout swipeRefresh;
    private TextView dressText;
    private TextView influText;
    private TextView travelText;
    private TextView ultravioletText;
    private TextView qltyText;
    private TextView humidityText;
    private ImageView nowImg;
    private TextView directionText;
    private TextView powerText;
    private LinearLayout hourlyForecastLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if (Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        nowImg = (ImageView) findViewById(R.id.now_img);
        directionText = (TextView) findViewById(R.id.direction_text);
        powerText = (TextView) findViewById(R.id.power_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        hourlyForecastLayout = (LinearLayout) findViewById(R.id.hourlyforecast_layout);
        apiText = (TextView) findViewById(R.id.api_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        humidityText = (TextView) findViewById(R.id.humidity_text);
        qltyText = (TextView) findViewById(R.id.qlty_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        dressText = (TextView) findViewById(R.id.dress_text);
        influText = (TextView) findViewById(R.id.influ_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        travelText = (TextView) findViewById(R.id.travel_text);
        ultravioletText = (TextView) findViewById(R.id.ultraviolet_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=feeec04bd1594383a867d878f7ee51f1";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.e("adadawwwwwwwwwww","请求失败");
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        Glide.with(this).load("https://cdn.heweather.com/cond_icon/"+weather.now.more.nowcode+".png").into(nowImg);
        directionText.setText(weather.now.windy.direction);
        powerText.setText(weather.now.windy.windPower);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            ImageView forecastImg = (ImageView) view.findViewById(R.id.forecast_img);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            Glide.with(this).load("https://cdn.heweather.com/cond_icon/"+forecast.more.Forecastcode+".png").into(forecastImg);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        hourlyForecastLayout.removeAllViews();
        for (HourlyForecast hourlyForecast : weather.hourlyForecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.hourlyforecast_item,hourlyForecastLayout,false);
            TextView hourlyDateText = (TextView) view.findViewById(R.id.hourly_date_text);
            TextView hourlyInfoText = (TextView) view.findViewById(R.id.hourly_info_text);
            TextView hourlyText = (TextView) view.findViewById(R.id.hourly_text);
            ImageView hourlyForecastImg = (ImageView) view.findViewById(R.id.hourly_forecast_img);
            hourlyDateText.setText(hourlyForecast.date);
            Glide.with(this).load("https://cdn.heweather.com/cond_icon/"+hourlyForecast.more.Forecastcode+".png").into(hourlyForecastImg);
            hourlyInfoText.setText(hourlyForecast.more.info);
            hourlyText.setText(hourlyForecast.temperature);
            hourlyForecastLayout.addView(view);
        }
        if (weather.aqi != null){
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            qltyText.setText(weather.aqi.city.qlty);
        }
        humidityText.setText(weather.now.humidity+"%");
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String dress = "穿衣指数："+weather.suggestion.dress.info;
        String influ = "感冒指数："+weather.suggestion.influ.info;
        String sport = "运动指数："+weather.suggestion.sport.info;
        String travel = "旅游指数："+weather.suggestion.travel.info;
        String ultraviolet = "紫外线指数："+weather.suggestion.ultraviolet.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        dressText.setText(dress);
        influText.setText(influ);
        sportText.setText(sport);
        travelText.setText(travel);
        ultravioletText.setText(ultraviolet);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
