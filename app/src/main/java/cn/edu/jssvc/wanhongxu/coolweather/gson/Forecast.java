package cn.edu.jssvc.wanhongxu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhongxu on 2017/5/10.
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;
    public Astro astro;

    public class Temperature{
        public String max;
        public String min;
    }
    public class More{
        @SerializedName("code_d")
        public String Forecastcode_d;
        @SerializedName("code_n")
        public String Forecastcode_n;
        @SerializedName("txt_d")
        public String info_d;
        @SerializedName("txt_n")
        public String info_n;
    }

    public class Astro {
        public String sr;
        public String ss;
    }
}
