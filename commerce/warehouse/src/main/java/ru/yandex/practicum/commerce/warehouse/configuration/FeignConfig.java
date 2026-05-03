package ru.yandex.practicum.commerce.warehouse.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "ru.yandex.practicum.commerce.feign")
public class FeignConfig {
}