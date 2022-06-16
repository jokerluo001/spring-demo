package com.example.weather.task;

import com.example.weather.entity.Weather;
import com.example.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduleTask {


    private int time = 0;

    @Autowired
    List<WeatherService> weatherServices;

    @Scheduled(cron = "* */5 * * * ?")
    public void weatherTask() {
        for (WeatherService weatherService : weatherServices) {
            try {
                weatherService.getWeather();
            } catch (Exception e) {
                Class<? extends WeatherService> aClass = weatherService.getClass();
                Qualifier annotation = aClass.getAnnotation(Qualifier.class);
                System.out.println(annotation.value() + "service调用失败");
            }
        }
        log.info("执行" + ++time + "次");
    }
}
