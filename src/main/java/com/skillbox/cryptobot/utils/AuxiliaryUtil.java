package com.skillbox.cryptobot.utils;

import org.telegram.telegrambots.meta.api.objects.Message;

public class AuxiliaryUtil {
  public static String extractChatIdString (Message message) {
    return message.getChatId().toString();
  }

}
