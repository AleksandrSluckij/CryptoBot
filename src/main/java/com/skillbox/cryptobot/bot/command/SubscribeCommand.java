package com.skillbox.cryptobot.bot.command;

import static com.skillbox.cryptobot.configuration.MessagesStrings.CUR_PRICE_FORMAT;
import static com.skillbox.cryptobot.configuration.MessagesStrings.ERROR_MESSAGE;
import static com.skillbox.cryptobot.configuration.MessagesStrings.NEW_SUBSCR_FORMAT;
import static com.skillbox.cryptobot.utils.AuxiliaryUtil.extractChatIdString;

import com.skillbox.cryptobot.database.SubscriptionRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final SubscriptionRepository repository;
    private final CryptoCurrencyService service;

    @Autowired
    public SubscribeCommand(SubscriptionRepository repository, CryptoCurrencyService service) {
        this.repository = repository;
      this.service = service;
    }


    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String answerText;

        Double subscriptionValue = recognizeSubscriptionValue(arguments);
        if (subscriptionValue != null) {
            repository.updateSubscriptionValue(message.getChatId(), subscriptionValue);
            String text = String.format(CUR_PRICE_FORMAT,
                TextUtil.toString(service.getBitcoinPrice()));
            try {
                absSender.execute(new SendMessage(extractChatIdString(message), text));
            } catch (TelegramApiException e) {
                log.error("Error occurred (TelegramApiException) in /subscribe command", e);
            }

            answerText = String.format(NEW_SUBSCR_FORMAT, TextUtil.toString(subscriptionValue));

        } else {
            answerText = ERROR_MESSAGE;
        }

        try {
            absSender.execute(new SendMessage(extractChatIdString(message), answerText));
        } catch (TelegramApiException e) {
            log.error("Error occurred (TelegramApiException) in /subscribe command", e);
        }
    }

    private Double recognizeSubscriptionValue(String[] arguments) {
        if (arguments.length != 1) {
            return null;
        }
        Optional<String> subscriptionArgument = Optional.ofNullable(arguments[0]);
        if (subscriptionArgument.isPresent()) {
            try {
                return Double.parseDouble(arguments[0]);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }
}