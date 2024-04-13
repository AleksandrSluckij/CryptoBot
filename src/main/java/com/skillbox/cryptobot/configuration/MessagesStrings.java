package com.skillbox.cryptobot.configuration;
import org.springframework.stereotype.Component;

@Component
public final class MessagesStrings {

  public static final String PROMPT = """
      Поддерживаемые команды:
                 /subscribe [число] - подписаться на стоимость биткоина в USD
                 /get_price - получить стоимость биткоина
                 /get_subscription - получить текущую подписку
                 /unsubscribe - отменить подписку на стоимость
      """;

  public static final String CUR_PRICE_FORMAT = "Текущая цена биткоина %s USD";

  public static final String ERROR_MESSAGE = """
                Ошибка формата команды
                Правильный формат:
                /subscribe _стоимость_биткоина, например:
                /subscribe 123.456
                """;

  public static final String NEW_SUBSCR_FORMAT = "Новая подписка создана на стоимость %s USD";

  public static final String CUR_SUBSCR_FORMAT = "Вы подписаны на стоимость биткоина %f USD";

  public static final String TIME_TO_BUY_FORMAT = "Пора покупать, стоимость биткоина %s USD";

  public static final String NO_SUBSCR = "Активные подписки отсутствуют";


  private MessagesStrings() {}
}
