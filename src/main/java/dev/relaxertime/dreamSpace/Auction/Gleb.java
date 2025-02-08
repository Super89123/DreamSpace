package dev.relaxertime.dreamSpace.Auction;

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

import java.util.HashMap;
import java.util.Map;// пись

public class Gleb implements CommandExecutor, Listener {

    private final Map<String, RegionData> regionDataMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2 || !command.getName().equalsIgnoreCase("arend")) {
            sender.sendMessage(ChatColor.RED + "Используйте: /arend [название региона]");
            return true;
        }

        if (sender instanceof Player player) {
            String regionName = args[1];
            // Проверка существования региона (должен быть реализован)
            if (!regionExists(regionName)) {
                player.sendMessage(ChatColor.RED + "Регион с таким именем не найден.");
                return true;
            }

            // Открываем меню аренды
            player.openInventory(getRentMenu(regionName));
            return true;
        }

        sender.sendMessage("Эта команда доступна только игрокам.");
        return true;
    }

    private Inventory getRentMenu(String regionName) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Настройки аренды региона: " + regionName);

        // Кнопка для установки времени аренды
        ItemStack timeItem = new ItemStack(Material.CLOCK);
        ItemMeta timeMeta = timeItem.getItemMeta();
        timeMeta.setDisplayName(ChatColor.YELLOW + "Установить время аренды");
        timeItem.setItemMeta(timeMeta);
        inventory.setItem(11, timeItem);

        // Кнопка для установки цены
        ItemStack priceItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta priceMeta = priceItem.getItemMeta();
        priceMeta.setDisplayName(ChatColor.GREEN + "Установить цену аренды");
        priceItem.setItemMeta(priceMeta);
        inventory.setItem(13, priceItem);

        // Кнопка для установки прав
        ItemStack rightsItem = new ItemStack(Material.BOOK);
        ItemMeta rightsMeta = rightsItem.getItemMeta();
        rightsMeta.setDisplayName(ChatColor.BLUE + "Настроить права");
        rightsItem.setItemMeta(rightsMeta);
        inventory.setItem(15, rightsItem);

        // Кнопка для выставления на продажу
        ItemStack sellItem = new ItemStack(Material.DIAMOND);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName(ChatColor.GOLD + "Выставить на продажу");
        sellItem.setItemMeta(sellMeta);
        inventory.setItem(26, sellItem);

        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.GREEN + "Настройки аренды региона: ")) {
            event.setCancelled(true); // Отменяем событие, чтобы не закрыть инвентарь

            Player player = (Player) event.getWhoClicked();
            String regionName = event.getView().getTitle().substring(ChatColor.GREEN.toString().length() + "Настройки аренды региона: ".length());

            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            switch (event.getCurrentItem().getType()) {
                case CLOCK:
                    // Логика для установки времени аренды
                    player.sendMessage(ChatColor.YELLOW + "Введите время аренды в чате.");
                    // Здесь можно добавить логику для получения времени от игрока
                    break;
                case GOLD_INGOT:
                    // Логика для установки цены аренды
                    player.sendMessage(ChatColor.GREEN + "Введите цену аренды в чате.");
                    // Здесь можно добавить логику для получения цены от игрока
                    break;
                case BOOK:
                    // Логика для настройки прав
                    player.sendMessage(ChatColor.BLUE + "Выберите права для арендатора.");
                    // Здесь можно добавить логику для выбора прав
                    break;
                case DIAMOND:
                    // Логика для выставления на продажу
                    player.sendMessage(ChatColor.GOLD + "Регион выставлен на продажу.");
                    // Здесь можно добавить логику для выставления региона на продажу
                    break;
            }
        }
    }

    private boolean regionExists(String regionName) {
        // Здесь должна быть логика проверки существования региона
        return true; // Замените на реальную проверку
    }

    // Класс для хранения данных региона
    private static class RegionData {
        private String owner;
        private double price;
        private int rentalTime;
        private boolean canOpenChests;
        private boolean canBreakBlocks;
        private boolean canOpenDoors;
        private boolean canUseMechanisms;

        // Конструктор и геттеры/сеттеры
    }
}