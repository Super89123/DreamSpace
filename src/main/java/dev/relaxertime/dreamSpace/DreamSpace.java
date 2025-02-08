package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.relaxertime.dreamSpace.AntiCheat.Core;
import dev.relaxertime.dreamSpace.AntiCheat.ReportCommand;
import dev.relaxertime.dreamSpace.Auction.Auction;
import dev.relaxertime.dreamSpace.Auction.Gleb;
import dev.relaxertime.dreamSpace.CustomEntities.Trader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class DreamSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        Core antiCheatListener = new Core(this);
        try {
            Trader trader = new Trader(this, new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0), "shop", "trader");
            trader.addItem(new ItemStack(Material.ARROW), 1111111, "2222222", "hello;0x006666/");
            getServer().getPluginManager().registerEvents(trader, this);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // Регистрация слушателя пакетов


        // Регистрация слушателя событий
        getServer().getPluginManager().registerEvents(antiCheatListener, this);
        getServer().getPluginManager().registerEvents(new Auction(), this);
        getServer().getPluginManager().registerEvents(new Gleb(), this);

        Objects.requireNonNull(getServer().getPluginCommand("ah")).setExecutor(new Auction());
        Objects.requireNonNull(getServer().getPluginCommand("report")).setExecutor(new ReportCommand());
        Objects.requireNonNull(getServer().getPluginCommand("arend")).setExecutor(new Gleb());

        // Plugin startup logic

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
