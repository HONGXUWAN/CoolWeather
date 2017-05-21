package cn.edu.jssvc.wanhongxu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhongxu on 2017/5/21.
 */

public class HourlyForecast {
    public String date;
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
        @SerializedName("code")
        public String Forecastcode;
    }
}
