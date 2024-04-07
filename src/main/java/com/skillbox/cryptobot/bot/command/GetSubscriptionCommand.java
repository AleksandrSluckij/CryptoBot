package com.skillbox.cryptobot.bot.command;

import static com.skillbox.cryptobot.configuration.StaticValues.CUR_SUBSCR_FORMAT;
import static com.skillbox.cryptobot.configuration.StaticValues.NO_SUBSCR;

import com.skillbox.cryptobot.database.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscriptionRepository repository;

    @Autowired
    public GetSubscriptionCommand(SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        Double subscriptionValue = repository.findSubscriptionValue(message.getChatId());
        String answerText = subscriptionValue == null ? NO_SUBSCR
                : String.format(CUR_SUBSCR_FORMAT, subscriptionValue);

        try {
            absSender.execute(new SendMessage(String.valueOf(message.getChatId()), answerText));
        } catch (TelegramApiException e) {
            log.error("Error occurred (TelegramApiException) in /get_subscription command", e);
        }


    }
}