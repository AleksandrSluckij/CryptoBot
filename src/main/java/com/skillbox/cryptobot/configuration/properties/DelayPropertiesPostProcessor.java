package com.skillbox.cryptobot.configuration.properties;

import com.skillbox.cryptobot.exception.DelayPropertyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Objects;
import org.springframework.util.StringUtils;

public class DelayPropertiesPostProcessor implements EnvironmentPostProcessor {
  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    String notificationTimeUnit = environment.getProperty("time-values.notification-time-unit");
    String courseUpdateFrequencyString = environment.getProperty("time-values.course-update-frequency-in-minutes");
    String notificationFrequencyString = environment.getProperty("time-values.notification-frequency-value");

    if (!Objects.equals(notificationTimeUnit, "MINUTES") && !Objects.equals(notificationTimeUnit, "HOURS")) {
        throw new DelayPropertyException("Units of delay must be 'MINUTES' or 'HOURS' only!!!" );
    }
    if (!StringUtils.hasText(courseUpdateFrequencyString) ||
        !courseUpdateFrequencyString.matches("\\d+") ||
        Integer.parseInt(courseUpdateFrequencyString) < 1 || Integer.parseInt(courseUpdateFrequencyString) > 40) {
          throw new DelayPropertyException("Value of course update delay must be integer positive value in range 1 - 40 (minutes!)");
    }
    check(notificationFrequencyString, notificationTimeUnit);
  }

  private void check(String notificationFrequencyString, String notificationTimeUnit) {
    if (!StringUtils.hasText(notificationFrequencyString) ||
        !notificationFrequencyString.matches("\\d+")) {
      throw new DelayPropertyException("Value of notification frequency must be integer positive value!");
    }
    int value = Integer.parseInt(notificationFrequencyString);
    if (notificationTimeUnit.equals("MINUTES") && (value < 5 || value > 720)) {
        throw new DelayPropertyException("Value of notification frequency in minutes must be in range 5 - 720");
    }
    if (value < 1 || value > 12) {
      throw new DelayPropertyException("Value of notification frequency in hours must be in range 1 - 12");
    }
  }

}
