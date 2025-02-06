package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.relaxertime.dreamSpace.AntiCheat.Core;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DreamSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        Core antiCheatListener = new Core(this);

        // Регистрация слушателя пакетов


        // Регистрация слушателя событий
        getServer().getPluginManager().registerEvents(antiCheatListener, this);
        Objects.requireNonNull(getServer().getPluginCommand("/ac")).setExecutor(new Core(this));

        // Plugin startup logic

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
