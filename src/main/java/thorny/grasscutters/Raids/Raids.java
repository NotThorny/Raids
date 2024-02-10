package thorny.grasscutters.Raids;

import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.entity.EntityDeathEvent;
import emu.grasscutter.server.event.player.PlayerEnterAreaEvent;
import thorny.grasscutters.Raids.utils.Config;
import thorny.grasscutters.Raids.utils.ConfigParser;

public final class Raids extends Plugin {
    private static Raids instance;
    public ConfigParser config;

    public static Raids getInstance() {
        return instance;
    }
    @Override public void onLoad() {
        // Set the plugin instance.
        instance = this;
        this.config = new ConfigParser();
    }
    @Override public void onEnable() {
        new EventHandler<>(EntityDeathEvent.class)
                .priority(HandlerPriority.NORMAL)
                .listener(EventListener::EntityDeathEvent)
                .register(this);
        new EventHandler<>(PlayerEnterAreaEvent.class)
                .priority(HandlerPriority.NORMAL)
                .listener(EventListener::PlayerEnterAreaEvent)
                .register(this);
        // Register commands.
        this.getHandle().registerCommand(new thorny.grasscutters.Raids.commands.RaidsCommand());

        // Log a plugin status message.
        this.getLogger().info("The Raids plugin has been enabled.");
    }

    @Override public void onDisable() {
        // Log a plugin status message.
        this.getLogger().info("How could you do this to me... Raids plugin has been disabled.");
    }

    public void getConfig() {
        config.getConfig();
    }

    public boolean reloadConfig(Config updated) {
        config.setConfig(updated);
        if (config.saveConfig()) {
            config.loadConfig();
            return true;
        } else
            return false;
    }
}