package com.example.weather.service.impl;

import cn.hutool.core.date.DateUtil;
import com.example.weather.entity.Weather;
import com.example.weather.repository.WeatherRepository;
import com.example.weather.service.WeatherService;
import com.example.weather.utils.DateUtils;
import com.example.weather.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("yikeyun")
public class YiKeYunWeatherService implements WeatherService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.yikeyun.url}")
    private String url;

    @Value("${weather.yikeyun.key}")
    private String key;

    public static final ObjectMapper objectMapper = new ObjectMapper();

    //知心免费版只支持到市
    //支持查询的形式(hangzhou,杭州,ip,等)很多,但是没找到具体city文档....
    public static final String HANG_ZHOU = "101210101";
    public static final String XI_HU = "101210113";
    public static final String GUANG_YANG = "101090611";
    public static final String LANG_FANG = "101090601";


    @Override
    public void getWeather() {
        saveWeather(HANG_ZHOU);
        saveWeather(XI_HU);
        saveWeather(GUANG_YANG);
        saveWeather(LANG_FANG);
    }

    private void saveWeather(String city) {
        Map<String, Object> params = new HashMap();
        String requestUrl = url.replace("{city}", city).replace("{key}", key);
        Map map = httpUtil.callWeatherApi(url, HttpMethod.GET, key, city, params, "易客云");
        if (map != null) {
            Weather weather = new Weather();
            weather.setWeather(map.get("wea").toString());
            weather.setArea(map.get("city").toString());
            weather.setCompany("易客云");
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(map.get("date").toString() + " " + map.get("update_time").toString());
            weather.setUrl(requestUrl);
            try {
                weather.setMessage(objectMapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            weatherRepository.save(weather);
        }
    }
}
