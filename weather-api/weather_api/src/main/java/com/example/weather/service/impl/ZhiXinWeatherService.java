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

@Service("zhixin")
public class ZhiXinWeatherService implements WeatherService {

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${weather.zhixin.url}")
    private String url;

    @Value("${weather.zhixin.key}")
    private String key;

    public static final ObjectMapper objectMapper = new ObjectMapper();

    //知心免费版只支持到市
    //支持查询的形式(hangzhou,杭州,ip,等)很多,但是没找到具体city文档....
    public static final String HANG_ZHOU = "杭州";
    public static final String LANG_FANG = "廊坊";


    @Override
    public void getWeather() {
        saveWeather(HANG_ZHOU);
        saveWeather(LANG_FANG);
    }


    private void saveWeather(String city) {
        Map<String, Object> params = new HashMap();
        String requestUrl = url.replace("{city}", city).replace("{key}", key);
        Map map = httpUtil.callWeatherApi(url, HttpMethod.GET, key, city, params, "知心");
        if (map != null) {
            List<Map> result = (List<Map>) map.get("results");
            Map resultMap = result.get(0);
            Map now = (Map) resultMap.get("now");
            Weather weather = new Weather();
            weather.setWeather(now.get("text").toString());
            weather.setArea(city);
            weather.setCompany("知心");
            weather.setDate(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setTime(DateUtils.getDateStr(new Date(), DateUtils.FORMAT_DEFAULT));
            weather.setUpdateTime(DateUtil.parse(resultMap.get("last_update").toString()).toString());
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
