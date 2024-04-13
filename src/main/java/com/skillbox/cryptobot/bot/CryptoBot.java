package com.skillbox.cryptobot.bot;

import static com.skillbox.cryptobot.configuration.MessagesStrings.PROMPT;
import static com.skillbox.cryptobot.utils.AuxiliaryUtil.extractChatIdString;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Service
@Slf4j
public class CryptoBot extends TelegramLongPollingCommandBot {

    private final String botUsername;


    public CryptoBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            List<IBotCommand> commandList
    ) {
        super(botToken);
        this.botUsername = botUsername;

        commandList.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        try {
            execute(new SendMessage(extractChatIdString(update.getMessage()), PROMPT));
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception occurred.");
        }
    }

    public void performMailing(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Telegram API Exception occurred during notification");
        }
    }
}
