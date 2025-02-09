package dev.relaxertime.dreamSpace;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.relaxertime.dreamSpace.AntiCheat.Core;
import dev.relaxertime.dreamSpace.AntiCheat.ReportCommand;
import dev.relaxertime.dreamSpace.Auction.Auction;


import dev.relaxertime.dreamSpace.CustomEntities.Trader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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


        Objects.requireNonNull(getServer().getPluginCommand("ah")).setExecutor(new Auction());
        Objects.requireNonNull(getServer().getPluginCommand("report")).setExecutor(new ReportCommand());

        /* if (!setupEconomy()) {
            getLogger().severe("Vault не найден! Плагин отключен.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Загрузка данных
        regionDataMap = RegionAuction.DataManager.loadData();
        getLogger().info("Данные загружены: " + regionDataMap.size() + " регионов.");

        // Регистрация событий
        getServer().getPluginManager().registerEvents(new RegionAuction(), this);

        Objects.requireNonNull(getServer().getPluginCommand("unique")).setExecutor(new RegionAuction());
        */ // Plugin startup logic

    }


    @Override
    public void onDisable() {

        // Plugin shutdown logic
    }

}
