package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.relaxertime.dreamSpace.AntiCheat.Core;
import dev.relaxertime.dreamSpace.Auction.Auction;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DreamSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        Core antiCheatListener = new Core(this);

        // Регистрация слушателя пакетов


        // Регистрация слушателя событий
        getServer().getPluginManager().registerEvents(antiCheatListener, this);
        getServer().getPluginManager().registerEvents(new Auction(), this);
        Objects.requireNonNull(getServer().getPluginCommand("ac")).setExecutor(new Core(this));
        Objects.requireNonNull(getServer().getPluginCommand("ah")).setExecutor(new Auction());

        // Plugin startup logic

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
