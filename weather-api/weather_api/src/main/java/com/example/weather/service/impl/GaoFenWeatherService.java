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

@Service("gaofen")
public class GaoFenWeatherService implements WeatherService {


    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.gaofen.url}")
    private String url;

    @Value("${weather.gaofen.key}")
    private String key;


    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String HANG_ZHOU = "101210101";
    public static final String XI_HU = "101210113";
    public static final String LANG_FANG = "101090601";
    public static final String GUANG_YANG = "101090611";


    @Override
    public void getWeather() {
        saveWeather(HANG_ZHOU);
        saveWeather(XI_HU);
        saveWeather(LANG_FANG);
        saveWeather(GUANG_YANG);
    }

    private void saveWeather(String city) {
        Map<String, Object> params = new HashMap();
        String requestUrl = url.replace("{city}", city).replace("{key}", key);
        Map map = httpUtil.callWeatherApi(url, HttpMethod.GET, key, city, params, "高分");
        if (map != null) {
            Map resultMap = (Map) map.get("result");
            Map realtime = (Map) resultMap.get("realtime");
            Map location = (Map) resultMap.get("location");
            Weather weather = new Weather();
            weather.setWeather(realtime.get("text").toString());
            weather.setArea(location.get("name").toString());
            weather.setCompany("高分");
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(resultMap.get("last_update").toString());
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
