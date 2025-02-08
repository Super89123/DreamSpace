package dev.relaxertime.dreamSpace.Auction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;//пись
import org.bukkit.World;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionAuction  implements Listener, CommandExecutor {

    private Map<String, RegionData> regionDataMap = new HashMap<>();
    private Map<UUID, String> pendingPurchases = new HashMap<>();
    private Economy economy;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("arend")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 2) {
                    String regionName = args[1];
                    openRegionMenu(player, regionName);
                    return true;
                }
            }
        } else if (args[0].equalsIgnoreCase("aharend")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                openAuctionMenu(player);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("buyregion")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                handleBuyRegion(player);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("rentregion")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                handleRentRegion(player);
                return true;
            }
        }
        return false;
    }

    private void openRegionMenu(Player player, String regionName) {
        Inventory menu = Bukkit.createInventory(player, 9, "Настройки региона: " + regionName);

        ItemStack timeSetting = new ItemStack(Material.CLOCK);
        ItemMeta timeMeta = timeSetting.getItemMeta();
        timeMeta.setDisplayName("Установить время аренды");
        timeSetting.setItemMeta(timeMeta);
        menu.setItem(0, timeSetting);

        ItemStack priceSetting = new ItemStack(Material.GOLD_INGOT);
        ItemMeta priceMeta = priceSetting.getItemMeta();
        priceMeta.setDisplayName("Установить цену");
        priceSetting.setItemMeta(priceMeta);
        menu.setItem(1, priceSetting);

        ItemStack permissionsSetting = new ItemStack(Material.BOOK);
        ItemMeta permissionsMeta = permissionsSetting.getItemMeta();
        permissionsMeta.setDisplayName("Настроить права");
        permissionsSetting.setItemMeta(permissionsMeta);
        menu.setItem(2, permissionsSetting);

        ItemStack sellRegion = new ItemStack(Material.EMERALD);
        ItemMeta sellMeta = sellRegion.getItemMeta();
        sellMeta.setDisplayName("Выставить на продажу");
        sellRegion.setItemMeta(sellMeta);
        menu.setItem(8, sellRegion);

        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Настройки региона:")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            String regionName = event.getView().getTitle().replace("Настройки региона: ", "");

            switch (clickedItem.getType()) {
                case CLOCK:
                    player.sendMessage(ChatColor.GREEN + "Установите время аренды.");
                    // Логика для установки времени аренды
                    break;
                case GOLD_INGOT:
                    player.sendMessage(ChatColor.GREEN + "Установите цену.");
                    // Логика для установки цены
                    break;
                case BOOK:
                    player.sendMessage(ChatColor.GREEN + "Настройте права.");
                    // Логика для настройки прав
                    break;
                case EMERALD:
                    regionDataMap.put(regionName, new RegionData(regionName, player.getUniqueId()));
                    player.sendMessage(ChatColor.GREEN + "Регион " + regionName + " выставлен на продажу.");
                    break;
            }
        } else if (event.getView().getTitle().equals("Аукцион регионов")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

            String regionName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            RegionData regionData = regionDataMap.get(regionName);

            if (regionData != null) {
                pendingPurchases.put(player.getUniqueId(), regionName);
                player.sendMessage(ChatColor.GREEN + "Вы хотите купить или арендовать регион " + regionName + "?");
                player.sendMessage(ChatColor.GREEN + "Используйте команду /buyregion для покупки или /rentregion для аренды.");
            }
        }
    }

    private void openAuctionMenu(Player player) {
        Inventory auctionMenu = Bukkit.createInventory(player, 27, "Аукцион регионов");

        for (Map.Entry<String, RegionData> entry : regionDataMap.entrySet()) {
            ItemStack regionItem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = regionItem.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + entry.getKey());
            regionItem.setItemMeta(meta);
            auctionMenu.addItem(regionItem);
        }

        player.openInventory(auctionMenu);
    }

    private void handleBuyRegion(Player player) {
        UUID playerId = player.getUniqueId();
        if (pendingPurchases.containsKey(playerId)) {
            String regionName = pendingPurchases.get(playerId);
            RegionData regionData = regionDataMap.get(regionName);
            if (regionData != null) {
                double price = regionData.getPrice();
                if (economy.has(player, price)) {
                    economy.withdrawPlayer(player, price);
                    economy.depositPlayer(Bukkit.getOfflinePlayer(regionData.getOwnerId()), price);
                    player.sendMessage(ChatColor.GREEN + "Вы купили регион " + regionName + "!");
                    regionDataMap.remove(regionName);
                    pendingPurchases.remove(playerId);
                } else {
                    player.sendMessage(ChatColor.RED + "У вас недостаточно денег.");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "У вас нет активной покупки региона.");
        }
    }

    private void handleRentRegion(Player player) {
        UUID playerId = player.getUniqueId();
        if (pendingPurchases.containsKey(playerId)) {
            String regionName = pendingPurchases.get(playerId);
            RegionData regionData = regionDataMap.get(regionName);
            if (regionData != null) {
                double price = regionData.getRentPrice();
                if (economy.has(player, price)) {
                    economy.withdrawPlayer(player, price);
                    economy.depositPlayer(Bukkit.getOfflinePlayer(regionData.getOwnerId()), price);
                    player.sendMessage(ChatColor.GREEN + "Вы арендовали регион " + regionName + "!");
                    pendingPurchases.remove(playerId);
                } else {
                    player.sendMessage(ChatColor.RED + "У вас недостаточно денег.");
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + "У вас нет активной аренды региона.");
        }
    }

    public static class RegionData {
        private final String regionName;
        private final UUID ownerId;
        private double price;
        private double rentPrice;
        private long rentTime;
        private boolean canBreakBlocks;
        private boolean canOpenChests;
        private boolean canOpenDoors;
        private boolean canUseMechanisms;

        public RegionData(String regionName, UUID ownerId) {
            this.regionName = regionName;
            this.ownerId = ownerId;
        }

        public double getPrice() {
            return price;
        }

        public double getRentPrice() {
            return rentPrice;
        }

        public UUID getOwnerId() {
            return ownerId;
        }
    }

    public static class DataManager {
        private static final Gson gson = new Gson();
        private static final File dataFile = new File("plugins/DreamSpace/data.json");

        public static void saveData(Map<String, RegionData> data) {
            try (FileWriter writer = new FileWriter(dataFile)) {
                gson.toJson(data, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static Map<String, RegionData> loadData() {
            if (!dataFile.exists()) return new HashMap<>();

            try (FileReader reader = new FileReader(dataFile)) {
                Type type = new TypeToken<Map<String, RegionData>>() {}.getType();
                return gson.fromJson(reader, type);
            } catch (IOException e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        }
    }
}