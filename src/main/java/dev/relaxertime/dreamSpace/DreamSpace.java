package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamSpace extends JavaPlugin {

    @Override
    public void onEnable() {

    }

    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onDisable() {

    }
}
