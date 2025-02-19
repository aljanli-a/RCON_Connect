package org.example;

import org.example.DiscordBot;
import org.example.TelegramBot;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

public class CommandExecutor extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        String botType = getConfig().getString("bot.type");

        switch (botType.toLowerCase()) {
            case "discord":
                startDiscordBot();
                break;
            case "telegram":
                startTelegramBot();
                break;
            default:
                getLogger().severe("Invalid bot type specified in config.yml. Please use 'discord' or 'telegram'.");
                getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void startDiscordBot() {
        String token = getConfig().getString("discord.token");
        String channelId = getConfig().getString("discord.channel_id");

        new DiscordBot(this, token, channelId).start();
    }

    private void startTelegramBot() {
        String token = getConfig().getString("telegram.token");
        List<String> allowedChatIds = getConfig().getStringList("telegram.allowed_chat_ids");

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot(this, token, allowedChatIds));
        } catch (TelegramApiException e) {
            getLogger().severe("Failed to register Telegram bot: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}