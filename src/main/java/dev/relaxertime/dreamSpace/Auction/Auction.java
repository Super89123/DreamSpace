package dev.relaxertime.dreamSpace.Auction;

import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


@SuppressWarnings("deprecation")
public class Auction implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length < 1){
            commandSender.sendMessage("/ah sell");
            return true;
        }
        if(commandSender instanceof Player player){
            if(strings[0].equalsIgnoreCase("sell")){
                if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                    player.sendMessage(ChatColor.RED+ "Предмет в руке не должен быть пустым!");
                }
                else {
                    player.openInventory(getFirstSellInventory());
                }
                return true;
            }
        }
        else {
            commandSender.sendMessage("Администраторам не следует это делать!");
        }



        return true;
    }
    public static Inventory getFirstSellInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.RED + "Какой тип аукциона вы выберете?");
        ItemStack stack = OraxenItems.getItemById("null_icon").build();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Выставить на настоящий аукцион");
        stack.setItemMeta(meta);
        inventory.setItem(19, stack);
        inventory.setItem(20, stack);
        inventory.setItem(21, stack);
        inventory.setItem(28, stack);
        inventory.setItem(29, stack);
        inventory.setItem(30, stack);

        inventory.setItem(23, stack);
        inventory.setItem(24, stack);
        inventory.setItem(25, stack);
        inventory.setItem(32, stack);
        inventory.setItem(33, stack);
        inventory.setItem(34, stack);
        return inventory;
    }
    @EventHandler
    public void clickEventonVibor(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player)) return;
        if(Objects.requireNonNull(event.getCurrentItem()).getItemMeta().getCustomModelData() == 10029){
            switch (event.getSlot()){
                case 19,20,21,28,29,30:
                    event.getWhoClicked().openInventory(getTrueSellInventory());
                    event.setCancelled(true);
                case 23,24,25,32,33,34:
                    event.getWhoClicked().openInventory(getSellInventory());
                    event.setCancelled(true);

            }
            event.setCancelled(true);
            //TODO Никит перепиши здесь проверку меню пж)))
        }

    }
    public static Inventory getTrueSellInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Сколько будет стоить?");
        for(int i = 0; i < 54; i++){
            inventory.setItem(i,OraxenItems.getItemById("null_icon").build() );
        }
        return inventory;
    }
    public static Inventory getSellInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Сколько будет стоить?");
        for(int i = 0; i < 54; i++){
            inventory.setItem(i,OraxenItems.getItemById("null_icon").build() );
        }
        return inventory;
    }
    public static ItemStack buildEmptyStack(String name){
        ItemStack stack = OraxenItems.getItemById("null_icon").build();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        stack.setItemMeta(meta);
        return stack;
    }
}
