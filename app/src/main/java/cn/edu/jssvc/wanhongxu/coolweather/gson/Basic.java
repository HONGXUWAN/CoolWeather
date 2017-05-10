package cn.edu.jssvc.wanhongxu.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wanhongxu on 2017/5/10.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
