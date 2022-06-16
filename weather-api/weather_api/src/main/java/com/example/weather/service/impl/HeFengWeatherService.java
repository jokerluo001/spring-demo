/*
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

@Service("hefeng")
public class HeFengWeatherService implements WeatherService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.hefeng.url}")
    private String url;

    @Value("${weather.hefeng.key}")
    private String key;


    public static final String HANG_ZHOU = "101210101";
    public static final String XI_HU = "101210113";
    public static final String LANG_FANG = "101090601";
    public static final String GUANG_YANG = "101090611";
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void getWeather() {
        //saveWeather(HANG_ZHOU);
        //saveWeather(XI_HU);
        //saveWeather(LANG_FANG);
        saveWeather(GUANG_YANG);
    }


    private void saveWeather(String city) {
        Map<String, Object> params = new HashMap();
        String requestUrl = url.replace("{city}", city).replace("{key}", key);
        String test = httpUtil.callHeFengWeatherApi(url, HttpMethod.GET, key, city, params, "和风");
        System.out.println(test);
        if (map != null) {
            Map now = (Map) map.get("now");
            Weather weather = new Weather();
            weather.setWeather(now.get("text").toString());
            //返回报文没有城市
            if (HANG_ZHOU.equals(city)) {
                weather.setArea("杭州");
            } else if (XI_HU.equals(city)) {
                weather.setArea("西湖");
            } else if (LANG_FANG.equals(city)) {
                weather.setArea("廊坊");
            } else {
                weather.setArea("广阳");
            }
            weather.setCompany("和风");
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(DateUtil.parse(map.get("updateTime").toString()).toString());
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
*/
