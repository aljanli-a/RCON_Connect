package org.example;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.example.CommandExecutor;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

public class DiscordBot {

    private final CommandExecutor plugin;
    private final String token;
    private final String channelId;

    public DiscordBot(CommandExecutor plugin, String token, String channelId) {
        this.plugin = plugin;
        this.token = token;
        this.channelId = channelId;
    }

    public void start() {
        JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new MessageListener(channelId))
                .build();
    }

    private class MessageListener extends ListenerAdapter {
        private final String channelId;

        public MessageListener(String channelId) {
            this.channelId = channelId;
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getAuthor().isBot()) {
                return;
            }

            MessageChannel channel = event.getChannel();
            if (channel.getId().equals(channelId)) {
                String command = event.getMessage().getContentRaw();
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                });
            }
        }
    }
}