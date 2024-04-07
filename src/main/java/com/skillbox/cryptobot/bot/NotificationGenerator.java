package com.skillbox.cryptobot.bot;

import static com.skillbox.cryptobot.configuration.StaticValues.TIME_TO_BUY_FORMAT;

import com.skillbox.cryptobot.configuration.properties.DelayProperties;
import com.skillbox.cryptobot.database.SubscriptionEntity;
import com.skillbox.cryptobot.database.SubscriptionRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class NotificationGenerator {
  private final CryptoBot bot;

  private final CryptoCurrencyService currencyService;
  private final SubscriptionRepository subscriptionRepository;
  private final DelayProperties delayProperties;

  public NotificationGenerator(CryptoBot bot, CryptoCurrencyService currencyService,
      SubscriptionRepository subscriptionRepository, DelayProperties delayProperties) {
    this.bot = bot;
    this.currencyService = currencyService;
    this.subscriptionRepository = subscriptionRepository;
    this.delayProperties = delayProperties;
  }

  @Scheduled(fixedDelayString = "${time-values.course-update-frequency-in-minutes}", timeUnit = TimeUnit.MINUTES)
  public void generateNotifications () {

    double price;
    price = currencyService.updateBitcoinPrice();
    log.info("Bitcoin price received: {}", price);
    String messageText = String.format(TIME_TO_BUY_FORMAT, TextUtil.toString(price));
    long currentTime = System.currentTimeMillis();
    List<SubscriptionEntity> usersToNotify = subscriptionRepository.findUsersToNotify(price,
        currentTime - getNotificationDelay());
    usersToNotify.forEach(u -> {
          bot.performMailing(new SendMessage(String.valueOf(u.getUserId()), messageText));
          u.setLastNotified(currentTime);
        });
    subscriptionRepository.saveAll(usersToNotify);

  }

  private long getNotificationDelay () {
    return TimeUnit.valueOf(delayProperties.getNotificationTimeUnit()).toMillis(delayProperties.getNotificationFrequencyValue());
  }

}
