package com.skillbox.cryptobot.configuration.properties;

import com.skillbox.cryptobot.exception.DelayPropertyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import org.springframework.util.StringUtils;

public class DelayPropertiesPostProcessor implements EnvironmentPostProcessor {

  // Maybe remove these constants to properties ?
  private static final int COURSE_UPDATE_FREQUENCY_MIN_VALUE = 1;
  private static final int COURSE_UPDATE_FREQUENCY_MAX_VALUE = 40;
  private static final int NOTIFICATION_FREQUENCY_IN_MINUTES_MIN_VALUE = 5;
  private static final int NOTIFICATION_FREQUENCY_IN_MINUTES_MAX_VALUE = 720;
  private static final int NOTIFICATION_FREQUENCY_IN_HOURS_MIN_VALUE = 1;
  private static final int NOTIFICATION_FREQUENCY_IN_HOURS_MAX_VALUE = 12;

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    String notificationTimeUnit = environment.getProperty("time-values.notification-time-unit");
    String courseUpdateFrequencyString = environment.getProperty("time-values.course-update-frequency-in-minutes");
    String notificationFrequencyString = environment.getProperty("time-values.notification-frequency-value");

    if (!"MINUTES".equals(notificationTimeUnit) && !"HOURS".equals(notificationTimeUnit)) {
        throw new DelayPropertyException("Units of notification delay must be 'MINUTES' or 'HOURS' only!!!" );
    }
    if (notContainsInteger(courseUpdateFrequencyString) ||
        valueOutOfLimits(courseUpdateFrequencyString, COURSE_UPDATE_FREQUENCY_MIN_VALUE,
            COURSE_UPDATE_FREQUENCY_MAX_VALUE)) {
      throw new DelayPropertyException(String.format("Value of course update delay must be integer positive value in range %s - %s (minutes!)",
          COURSE_UPDATE_FREQUENCY_MIN_VALUE, COURSE_UPDATE_FREQUENCY_MAX_VALUE));
    }
    check(notificationFrequencyString, notificationTimeUnit);
  }

  private boolean valueOutOfLimits(String valueString, int minValue, int maxValue) {
    int value = Integer.parseInt(valueString);
    return value < minValue || value > maxValue;
  }

  private boolean notContainsInteger(String valueString) {
    return !StringUtils.hasText(valueString) || !valueString.matches("\\d+");
  }

  private void check(String notificationFrequencyString, String notificationTimeUnit) {
    if (notContainsInteger(notificationFrequencyString)) {
      throw new DelayPropertyException("Value of notification frequency must be integer positive value!");
    }
    if (notificationTimeUnit.equals("MINUTES") &&
        valueOutOfLimits(notificationFrequencyString, NOTIFICATION_FREQUENCY_IN_MINUTES_MIN_VALUE,
            NOTIFICATION_FREQUENCY_IN_MINUTES_MAX_VALUE)) {
        throw new DelayPropertyException(String.format("Value of notification frequency in minutes must be in range %s - %s",
            NOTIFICATION_FREQUENCY_IN_MINUTES_MIN_VALUE,
            NOTIFICATION_FREQUENCY_IN_MINUTES_MAX_VALUE));
    }
    if (valueOutOfLimits(notificationFrequencyString, NOTIFICATION_FREQUENCY_IN_HOURS_MIN_VALUE,
        NOTIFICATION_FREQUENCY_IN_HOURS_MAX_VALUE)) {
      throw new DelayPropertyException(String.format("Value of notification frequency in hours must be in range %s - %s",
          NOTIFICATION_FREQUENCY_IN_HOURS_MIN_VALUE, NOTIFICATION_FREQUENCY_IN_HOURS_MAX_VALUE));
    }
  }

}
