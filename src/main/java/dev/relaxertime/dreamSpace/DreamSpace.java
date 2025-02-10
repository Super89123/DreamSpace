package dev.relaxertime.dreamSpace;


import dev.relaxertime.dreamSpace.AntiCheat.Core;
import dev.relaxertime.dreamSpace.AntiCheat.ReportCommand;
import dev.relaxertime.dreamSpace.Auction.Auction;


import dev.relaxertime.dreamSpace.CustomEntities.Trader;

import dev.relaxertime.dreamSpace.Magic.FireballSpell;
import dev.relaxertime.dreamSpace.Magic.ManaController;
import dev.relaxertime.dreamSpace.Pets.FirstPet;
import dev.relaxertime.dreamSpace.Pets.PetCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


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



        // Регистрация слушателя событий
        getServer().getPluginManager().registerEvents(antiCheatListener, this);
        getServer().getPluginManager().registerEvents(new Auction(), this);

        getServer().getPluginManager().registerEvents(new FireballSpell(this), this);
        Objects.requireNonNull(getServer().getPluginCommand("magic")).setExecutor(new PetCommand(this));


        Objects.requireNonNull(getServer().getPluginCommand("ah")).setExecutor(new Auction());
        Objects.requireNonNull(getServer().getPluginCommand("report")).setExecutor(new ReportCommand());
        Objects.requireNonNull(getServer().getPluginCommand("pet")).setExecutor(new PetCommand(this));
        FirstPet pet = new FirstPet(this);
        getServer().getPluginManager().registerEvents(pet, this);
        new BukkitRunnable(){

            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    Component cp = Component.text("Мана: " + ManaController.getPlayerMana(player , DreamSpace.this), TextColor.color(124, 150, 104));
                    player.sendActionBar(cp);
                }

            }
        }.runTaskTimer(this, 0, 1);


    }


    @Override
    public void onDisable() {

        // Plugin shutdown logic
    }

}
