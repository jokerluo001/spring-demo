package com.example.weather.utils;

import antlr.StringUtils;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HttpUtil {

    public static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RestTemplate restTemplate;
    /*
    * url:请求url
    * httpMethod: 请求方法
    * key: 开发者密钥 ->必填参数
    * city: 城市或区域编码 ->必填参数
    * params: api可选参数
    * company: api公司
    * */
    public Map callWeatherApi(String url, HttpMethod httpMethod, String key, String city,Map<String,Object> params,String company) {
        if (url == null || "".equals(url)) {
            log.error("url为null或为空");
            return null;
        }
        if (key == null || "".equals(key)) {
            log.error("key为null或为空");
            return null;
        }
        if (city == null || "".equals(city)) {
            log.error("city为null或为空");
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json"));
        params.put("key", key);
        params.put("city", city);
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<Map> result = restTemplate.exchange(url, httpMethod,entity,Map.class,params);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            log.error("调用" + company + "天气接口失败,error message->" + result);
            return null;
        }
        return result.getBody();
    }

    public Map callApiSpaceWeatherApi(String url, HttpMethod httpMethod,String city,Map<String,Object> params,String company) {
        if (url == null || "".equals(url)) {
            log.error("url为null或为空");
            return null;
        }
        if (city == null || "".equals(city)) {
            log.error("city为null或为空");
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-APISpace-Token","t50fu8jg3cwg8wnf2ajq82zvb4corjuq");
        headers.add("Authorization-Type","apikey");
        headers.setContentType(MediaType.parseMediaType("application/json"));
        params.put("city", city);
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<Map> result = restTemplate.exchange(url, httpMethod,entity,Map.class,params);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            log.error("调用" + company + "天气接口失败,error message->" + result);
            return null;
        }
        return result.getBody();
    }


    public JSONObject callBaiduWeatherApi(String url, HttpMethod httpMethod,String key,String city,Map<String,Object> params,String company) {
        if (url == null || "".equals(url)) {
            log.error("url为null或为空");
            return null;
        }
        if (key == null || "".equals(key)) {
            log.error("key为null或为空");
            return null;
        }
        if (city == null || "".equals(city)) {
            log.error("city为null或为空");
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json"));
        params.put("key", key);
        params.put("city", city);
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> result = restTemplate.exchange(url, httpMethod,entity, String.class,params);
       /* if (!result.getStatusCode().equals(HttpStatus.OK)) {
            log.error("调用" + company + "天气接口失败,error message->" + result);
            return null;
        }*/
        JSONObject jsonObject = JSONUtil.parseObj(result.getBody());
        return jsonObject;
    }
}
