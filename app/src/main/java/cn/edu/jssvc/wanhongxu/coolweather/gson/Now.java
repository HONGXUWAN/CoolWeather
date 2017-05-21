package cn.edu.jssvc.wanhongxu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhongxu on 2017/5/10.
 */

public class Now {
    @SerializedName("hum")
    public String humidity;
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    @SerializedName("wind")
    public Windy windy;

    public class More{
        @SerializedName("txt")
        public String info;
        @SerializedName("code")
        public String nowcode;

    }

    public class Windy {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String windPower;
    }
}
