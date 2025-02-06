package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.relaxertime.dreamSpace.AntiCheat.Core;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        Core antiCheatListener = new Core(this);

        // Регистрация слушателя пакетов
        protocolManager.addPacketListener(antiCheatListener);

        // Регистрация слушателя событий
        getServer().getPluginManager().registerEvents(antiCheatListener, this);
        // Plugin startup logic

    }

    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
