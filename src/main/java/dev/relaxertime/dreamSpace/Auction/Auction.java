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
        if (strings.length < 1) {
            commandSender.sendMessage("/ah sell");
            return true;
        }
        if (commandSender instanceof Player player) {
            if (strings[0].equalsIgnoreCase("sell")) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    player.sendMessage(ChatColor.RED + "Предмет в руке не должен быть пустым!");
                } else {
                    player.openInventory(getFirstSellInventory());
                }
                return true;
            }
        } else {
            commandSender.sendMessage("Администраторам не следует это делать!");
        }


        return true;
    }

    public static Inventory getFirstSellInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.WHITE + "<shift:-5>猫");
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
        ItemStack stack1 = buildEmptyStack(ChatColor.GREEN + "Выставить на классический аукцион");
        inventory.setItem(23, stack1);
        inventory.setItem(24, stack1);
        inventory.setItem(25, stack1);
        inventory.setItem(32, stack1);
        inventory.setItem(33, stack1);
        inventory.setItem(34, stack1);
        return inventory;
    }

    @EventHandler
    public void clickEventonVibor(InventoryClickEvent event) {
        ItemStack stack = OraxenItems.getItemById("null_icon").build();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Выставить на настоящий аукцион");
        stack.setItemMeta(meta);
        ItemStack pusto = OraxenItems.getItemById("null_icon").build();
        if(event.getInventory().getSize() != 54) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory().getItem(19) != null && Objects.requireNonNull(event.getInventory().getItem(19)).equals(stack)) {
            switch (event.getSlot()) {
                case 19, 20, 21, 28, 29, 30:
                    event.getWhoClicked().openInventory(getTrueSellInventory());
                    event.setCancelled(true);
                case 23, 24, 25, 32, 33, 34:
                    event.getWhoClicked().openInventory(getSellInventory());
                    event.setCancelled(true);

            }
            event.setCancelled(true);

        } else if (event.getInventory().getItem(0) != null && Objects.requireNonNull(event.getInventory().getItem(0)).equals(pusto)) {
            switch (event.getSlot()){
                case 45,46:
                    addToNumber(event.getInventory(), 1);
                    event.setCancelled(true);
                case 47,48:
                    addToNumber(event.getInventory(), 2);
                    event.setCancelled(true);
                case 49,50:
                    addToNumber(event.getInventory(), 3);
                    event.setCancelled(true);
                case 51,52:
                    addToNumber(event.getInventory(), 0);
                    event.setCancelled(true);
                case 36,37:
                    addToNumber(event.getInventory(), 4);
                    event.setCancelled(true);
                case 38,39:
                    addToNumber(event.getInventory(), 5);
                    event.setCancelled(true);
                case 40,41:
                    addToNumber(event.getInventory(), 6);
                    event.setCancelled(true);
                case 27,28:
                    addToNumber(event.getInventory(), 7);
                    event.setCancelled(true);
                case 29,30:
                    addToNumber(event.getInventory(), 8);
                    event.setCancelled(true);
                case 31,23:
                    addToNumber(event.getInventory(), 9);
                    event.setCancelled(true);






            }

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

    /**
     * @param name Имя пустого предмета
     * @return Возвращает пустой предмет с конкретным названием
     */
    public static ItemStack buildEmptyStack(String name){
        ItemStack stack = OraxenItems.getItemById("null_icon").build();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + name);
        stack.setItemMeta(meta);
        return stack;
    }
    private void addToNumber(Inventory inventory, int number){
        ItemStack cifra1 = OraxenItems.getItemById("cifra1").build();
        ItemStack cifra2 = OraxenItems.getItemById("cifra2").build();
        ItemStack cifra3 = OraxenItems.getItemById("cifra3").build();
        ItemStack cifra4 = OraxenItems.getItemById("cifra4").build();
        ItemStack cifra5 = OraxenItems.getItemById("cifra5").build();
        ItemStack cifra6 = OraxenItems.getItemById("cifra6").build();
        ItemStack cifra7 = OraxenItems.getItemById("cifra7").build();
        ItemStack cifra8 = OraxenItems.getItemById("cifra8").build();
        ItemStack cifra9 = OraxenItems.getItemById("cifra9").build();
        ItemStack cifra0 = OraxenItems.getItemById("cifra0").build();
        ItemStack pusto = OraxenItems.getItemById("null_icon").build();
        if(Objects.requireNonNull(inventory.getItem(17)).equals(pusto )) {
            switch (number) {
                case 1:
                    inventory.setItem(17, cifra1);

                case 2:
                    inventory.setItem(17, cifra2);

                case 3:
                    inventory.setItem(17, cifra3);

                case 4:
                    inventory.setItem(17, cifra4);

                case 5:
                    inventory.setItem(17, cifra5);

                case 6:
                    inventory.setItem(17, cifra6);

                case 7:
                    inventory.setItem(17, cifra7);

                case 8:
                    inventory.setItem(17, cifra8);

                case 9:
                    inventory.setItem(17, cifra9);

                case 0:
                    inventory.setItem(17, cifra0);

            }

        } else if (Objects.requireNonNull(inventory.getItem(16)).equals(pusto) && !Objects.equals(inventory.getItem(17), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(16, cifra1);

                case 2:
                    inventory.setItem(16, cifra2);

                case 3:
                    inventory.setItem(16, cifra3);

                case 4:
                    inventory.setItem(16, cifra4);

                case 5:
                    inventory.setItem(16, cifra5);

                case 6:
                    inventory.setItem(16, cifra6);

                case 7:
                    inventory.setItem(16, cifra7);

                case 8:
                    inventory.setItem(16, cifra8);

                case 9:
                    inventory.setItem(16, cifra9);

                case 0:
                    inventory.setItem(16, cifra0);

            }

        }else if (Objects.requireNonNull(inventory.getItem(15)).equals(pusto) && !Objects.equals(inventory.getItem(17), pusto) && !Objects.equals(inventory.getItem(16), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(15, cifra1);

                case 2:
                    inventory.setItem(15, cifra2);
                case 3:
                    inventory.setItem(15, cifra3);

                case 4:
                    inventory.setItem(15, cifra4);

                case 5:
                    inventory.setItem(15, cifra5);

                case 6:
                    inventory.setItem(15, cifra6);

                case 7:
                    inventory.setItem(15, cifra7);

                case 8:
                    inventory.setItem(15, cifra8);

                case 9:
                    inventory.setItem(15, cifra9);

                case 0:
                    inventory.setItem(15, cifra0);

            }


        }
        else if (Objects.requireNonNull(inventory.getItem(14)).equals(pusto) && !Objects.equals(inventory.getItem(17), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(15), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(14, cifra1);

                case 2:
                    inventory.setItem(14, cifra2);

                case 3:
                    inventory.setItem(14, cifra3);

                case 4:
                    inventory.setItem(14, cifra4);

                case 5:
                    inventory.setItem(14, cifra5);

                case 6:
                    inventory.setItem(14, cifra6);

                case 7:
                    inventory.setItem(14, cifra7);

                case 8:
                    inventory.setItem(14, cifra8);

                case 9:
                    inventory.setItem(14, cifra9);

                case 0:
                    inventory.setItem(14, cifra0);

            }

        } else if (Objects.requireNonNull(inventory.getItem(13)).equals(pusto) && !Objects.equals(inventory.getItem(14), pusto) && !Objects.equals(inventory.getItem(15), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(17), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(13, cifra1);

                case 2:
                    inventory.setItem(13, cifra2);

                case 3:
                    inventory.setItem(13, cifra3);
                case 4:
                    inventory.setItem(13, cifra4);
                case 5:
                    inventory.setItem(13, cifra5);
                case 6:
                    inventory.setItem(13, cifra6);
                case 7:
                    inventory.setItem(13, cifra7);
                case 8:
                    inventory.setItem(13, cifra8);
                case 9:
                    inventory.setItem(13, cifra9);
                case 0:
                    inventory.setItem(13, cifra0);
            }

        }
         else if (Objects.requireNonNull(inventory.getItem(12)).equals(pusto) && !Objects.requireNonNull(inventory.getItem(13)).equals(pusto) && !Objects.equals(inventory.getItem(14), pusto) && !Objects.equals(inventory.getItem(15), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(17), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(12, cifra1);
                case 2:
                    inventory.setItem(12, cifra2);
                case 3:
                    inventory.setItem(12, cifra3);
                case 4:
                    inventory.setItem(12, cifra4);
                case 5:
                    inventory.setItem(12, cifra5);
                case 6:
                    inventory.setItem(12, cifra6);
                case 7:
                    inventory.setItem(12, cifra7);
                case 8:
                    inventory.setItem(12, cifra8);
                case 9:
                    inventory.setItem(12, cifra9);
                case 0:
                    inventory.setItem(12, cifra0);
            }
        } else if (Objects.requireNonNull(inventory.getItem(11)).equals(pusto) && !Objects.requireNonNull(inventory.getItem(13)).equals(pusto) && !Objects.equals(inventory.getItem(14), pusto) && !Objects.equals(inventory.getItem(15), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(17), pusto) && !Objects.equals(inventory.getItem(12), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(11, cifra1);
                case 2:
                    inventory.setItem(11, cifra2);
                case 3:
                    inventory.setItem(11, cifra3);
                case 4:
                    inventory.setItem(11, cifra4);
                case 5:
                    inventory.setItem(11, cifra5);
                case 6:
                    inventory.setItem(11, cifra6);
                case 7:
                    inventory.setItem(11, cifra7);
                case 8:
                    inventory.setItem(11, cifra8);
                case 9:
                    inventory.setItem(11, cifra9);
                case 0:
                    inventory.setItem(11, cifra0);
            }

        }else if (Objects.requireNonNull(inventory.getItem(10)).equals(pusto) && !Objects.requireNonNull(inventory.getItem(13)).equals(pusto) && !Objects.equals(inventory.getItem(14), pusto) && !Objects.equals(inventory.getItem(15), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(17), pusto) && !Objects.equals(inventory.getItem(11), pusto) && !Objects.equals(inventory.getItem(12), pusto) ) {
            switch (number) {
                case 1:
                    inventory.setItem(10, cifra1);
                case 2:
                    inventory.setItem(10, cifra2);
                case 3:
                    inventory.setItem(10, cifra3);
                case 4:
                    inventory.setItem(10, cifra4);
                case 5:
                    inventory.setItem(10, cifra5);
                case 6:
                    inventory.setItem(10, cifra6);
                case 7:
                    inventory.setItem(10, cifra7);
                case 8:
                    inventory.setItem(10, cifra8);
                case 9:
                    inventory.setItem(10, cifra9);
                case 0:
                    inventory.setItem(10, cifra0);
            }

        }
        else if (Objects.requireNonNull(inventory.getItem(9)).equals(pusto) && !Objects.requireNonNull(inventory.getItem(13)).equals(pusto) && !Objects.equals(inventory.getItem(14), pusto) && !Objects.equals(inventory.getItem(15), pusto) && !Objects.equals(inventory.getItem(16), pusto) && !Objects.equals(inventory.getItem(17), pusto) && !Objects.equals(inventory.getItem(10), pusto) && !Objects.equals(inventory.getItem(11), pusto) && !Objects.equals(inventory.getItem(12), pusto)) {
            switch (number) {
                case 1:
                    inventory.setItem(9, cifra1);
                case 2:
                    inventory.setItem(9, cifra2);
                case 3:
                    inventory.setItem(9, cifra3);
                case 4:
                    inventory.setItem(9, cifra4);
                case 5:
                    inventory.setItem(9, cifra5);
                case 6:
                    inventory.setItem(9, cifra6);
                case 7:
                    inventory.setItem(9, cifra7);
                case 8:
                    inventory.setItem(9, cifra8);
                case 9:
                    inventory.setItem(9, cifra9);
                case 0:
                    inventory.setItem(9, cifra0);
            }

        }



    }
}
