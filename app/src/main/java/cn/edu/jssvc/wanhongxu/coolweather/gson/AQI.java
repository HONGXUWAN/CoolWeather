package cn.edu.jssvc.wanhongxu.coolweather.gson;

/**
 * Created by wanhongxu on 2017/5/10.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
        public String qlty;
    }
}
