package com.example.weather.service.impl;

import com.example.weather.entity.Weather;
import com.example.weather.repository.WeatherRepository;
import com.example.weather.service.WeatherService;
import com.example.weather.utils.DateUtils;
import com.example.weather.utils.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "gaode")
public class GaoDeWeatherService implements WeatherService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.gaode.url}")
    private String url;

    @Value("${weather.gaode.key}")
    private String key;
    /*
     * BASE:返回实况天气
     * ALL:返回预报天气
     * */
    public static final String BASE = "base";
    public static final String ALL = "all";


    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String HANG_ZHOU = "330100";
    public static final String XI_HU = "330106";
    public static final String LANG_FANG = "131000";
    public static final String GUANG_YANG = "131003";

    @Override
    public void getWeather() {
        saveWeather(HANG_ZHOU,BASE);
        saveWeather(XI_HU,BASE);
        saveWeather(LANG_FANG,BASE);
        saveWeather(GUANG_YANG,BASE);
    }

    private void saveWeather(String city,String type) {
        Map<String, Object> params = new HashMap();
        params.put("extensions", type);
        String requestUrl = url.replace("{city}", city).replace("{key}", key).replace("{extensions}", type);
        Map map = httpUtil.callWeatherApi(url, HttpMethod.GET, key, city, params, "高德");
        if (map != null) {
            List<Map> lives = (List<Map>) map.get("lives");
            Map<String, Object> result = lives.get(0);
            Weather weather = new Weather();
            weather.setWeather(result.get("weather").toString());
            weather.setArea(result.get("province").toString() + result.get("city").toString());
            weather.setCompany("高德");
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(result.get("reporttime").toString());
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
