package com.skillbox.cryptobot.bot.command;

import static com.skillbox.cryptobot.configuration.StaticValues.NO_SUBSCR;

import com.skillbox.cryptobot.database.SubscriptionEntity;
import com.skillbox.cryptobot.database.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
public class UnsubscribeCommand implements IBotCommand {

    private final SubscriptionRepository repository;

    @Autowired
    public UnsubscribeCommand(SubscriptionRepository repository) {
        this.repository = repository;
    }


    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String answerText;

        SubscriptionEntity subscription = repository.findByUserIdEquals(message.getChatId());
        if (subscription.getSubscriptionValue() != null) {
            subscription.setSubscriptionValue(null);
            repository.save(subscription);
            answerText = "Подписка отменена";
        } else {
            answerText = NO_SUBSCR;
        }

        try {
            absSender.execute(new SendMessage(String.valueOf(message.getChatId()), answerText));
        } catch (TelegramApiException e) {
            log.error("Error occurred (TelegramApiException) in /unsubscribe command", e);
        }
    }

}