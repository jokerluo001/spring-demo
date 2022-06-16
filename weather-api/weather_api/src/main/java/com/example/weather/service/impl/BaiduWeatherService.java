package com.example.weather.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
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

@Service("baidu")
public class BaiduWeatherService implements WeatherService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.baidu.url}")
    private String url;

    @Value("${weather.baidu.key}")
    private String key;


    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String HANG_ZHOU = "330100";
    public static final String XI_HU = "330106";
    public static final String LANG_FANG = "131000";
    public static final String GUANG_YANG = "131003";


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
        JSONObject jsonObject = httpUtil.callBaiduWeatherApi(url, HttpMethod.GET, key, city, params, "百度");
        if (jsonObject != null) {
            JSONObject resultJsonObject = (JSONObject) jsonObject.get("result");
            JSONObject nowJsonNode = (JSONObject) resultJsonObject.get("now");
            JSONObject locationJsonNode = (JSONObject) resultJsonObject.get("location");
            Weather weather = new Weather();
            weather.setWeather(nowJsonNode.get("text").toString());
            weather.setArea(locationJsonNode.get("name").toString());
            weather.setCompany("百度");
            weather.setUrl(requestUrl);
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(DateUtils.convertToStr(nowJsonNode.get("uptime").toString()));
            try {
                weather.setMessage(objectMapper.writeValueAsString(jsonObject));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            weatherRepository.save(weather);
        }
    }
}
