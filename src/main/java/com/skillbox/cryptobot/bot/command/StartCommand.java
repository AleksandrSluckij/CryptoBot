package com.skillbox.cryptobot.bot.command;

import static com.skillbox.cryptobot.configuration.MessagesStrings.PROMPT;
import static com.skillbox.cryptobot.utils.AuxiliaryUtil.extractChatIdString;

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
 * Обработка команды начала работы с ботом
 */
@Service
@Slf4j
public class StartCommand implements IBotCommand {

    private final SubscriptionRepository repository;

    @Autowired
    public StartCommand(SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Запускает бота";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        long telegramUserId = message.getChatId();
        createDatabaseRecord(telegramUserId);
        String answerText = "Привет! Данный бот помогает отслеживать стоимость биткоина\n" + PROMPT;
        try {
            absSender.execute(new SendMessage(extractChatIdString(message), answerText));
        } catch (TelegramApiException e) {
            log.error("Error (TelegramApiException) occurred in /start command", e);
        }
    }


    private void createDatabaseRecord(long telegramUserId) {
        if (repository.findByUserIdEquals(telegramUserId) == null) {
            SubscriptionEntity newUser = new SubscriptionEntity(telegramUserId);
            repository.save(newUser);
        }
    }

}