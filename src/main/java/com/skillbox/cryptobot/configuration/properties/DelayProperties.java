package com.skillbox.cryptobot.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "time-values")
@Getter
@Setter
public class DelayProperties {
    private int courseUpdateFrequencyInMinutes;
    private String notificationTimeUnit;
    private int notificationFrequencyValue;
}
