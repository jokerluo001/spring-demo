package com.example.weather.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "weather",schema = "ahdp")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "company")
    private String company;
    @Column(name = "url")
    private String url;
    @Column(name = "weather")
    private String weather;
    @Column(name = "area")
    private String area;
    @Column(name = "date")
    private String date;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "message")
    private String message;
    @Column(name = "time")
    private String time;
}
