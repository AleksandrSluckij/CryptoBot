package com.skillbox.cryptobot.bot.command;

import static com.skillbox.cryptobot.configuration.MessagesStrings.CUR_PRICE_FORMAT;
import static com.skillbox.cryptobot.utils.AuxiliaryUtil.extractChatIdString;

import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды получения текущей стоимости валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class GetPriceCommand implements IBotCommand {

    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "get_price";
    }

    @Override
    public String getDescription() {
        return "Возвращает цену биткоина в USD";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String answerText = String.format(CUR_PRICE_FORMAT, TextUtil.toString(service.getBitcoinPrice()));
      try {
            absSender.execute(new SendMessage(extractChatIdString(message), answerText));
        } catch (TelegramApiException e) {
            log.error("Error (TelegramApiException) occurred in /get_price command", e);
        }
    }
}