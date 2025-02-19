package org.example;

import org.example.CommandExecutor;
import org.bukkit.Bukkit;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    private final CommandExecutor plugin;
    private final String token;
    private final List<String> allowedChatIds;

    public TelegramBot(CommandExecutor plugin, String token, List<String> allowedChatIds) {
        this.plugin = plugin;
        this.token = token;
        this.allowedChatIds = allowedChatIds;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return "TelegramCommandExecutorBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String messageChatId = update.getMessage().getChatId().toString();

            if (allowedChatIds.contains(messageChatId)) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), messageText);
                });
            }
        }
    }
}