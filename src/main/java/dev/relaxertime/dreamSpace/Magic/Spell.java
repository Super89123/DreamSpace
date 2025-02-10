package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.DreamSpace;
import dev.relaxertime.dreamSpace.Pets.Pet;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("deprecation")
public abstract class Spell implements Listener {
    private final int customModelData;
    private final DreamSpace plugin;
    private final int manaCost;
    private final int cooldownSec;
    private static final Map<Integer, Spell> spells = new HashMap<>();
    private final String name;

    /**
     * @param customModelData CustomModelData предмета
     * @param plugin Объект Плагина DreamSpace
     * @param manaCost Стоимость в мане
     * @param cooldownSec Задержка на использование
     */
    protected Spell(int customModelData, DreamSpace plugin, int manaCost, int cooldownSec, String name) {
        this.customModelData = customModelData;
        this.plugin = plugin;
        this.manaCost = manaCost;
        this.cooldownSec = cooldownSec;
        this.name = name;
        spells.put(customModelData, this);
    }

    public abstract void whatToDo(Player player, Location clickedLocation);
    @EventHandler
    public void inter(PlayerInteractEvent event){
        if(event.getAction().isRightClick()){
            if(event.getItem() != null){
                if(event.getItem().getType().equals(Material.BOOK)){
                    ItemStack item = event.getItem();
                    if(item.hasItemMeta()){
                        if(item.getItemMeta().hasCustomModelData()){
                            if(item.getItemMeta().getCustomModelData() == customModelData){
                                Player player = event.getPlayer();
                                if(ManaController.getPlayerMana(player, plugin) >= manaCost){
                                    if(player.getCooldown(Material.BOOK) == 0) {
                                        whatToDo(player, event.getInteractionPoint());
                                        ManaController.setPlayerMana(player, plugin, ManaController.getPlayerMana(player, plugin) - manaCost);
                                        player.setCooldown(Material.BOOK, cooldownSec*20 );
                                    }
                                    else {
                                        player.sendMessage(ChatColor.RED + "Пожалуйста, подождите немного!");
                                        player.playSound(player, Sound.ENTITY_SHULKER_BULLET_HIT, 100, 100);
                                    }
                                }
                                else{
                                    player.sendMessage(ChatColor.RED + "У вас недостаточно маны");
                                    player.playSound(player, Sound.ENTITY_SHULKER_BULLET_HIT, 100, 100);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static Spell getPetByCustomModelData(int cms) {
        return spells.get(cms);
    }
    public ItemStack getSpellStack(){
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(customModelData);
        meta.setDisplayName(name);
        meta.setLore(List.of(ChatColor.WHITE + "Стоимость в мане: " + manaCost, ChatColor.WHITE + "Задержка: " + cooldownSec * 20 + " секунд" ));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    public String getName(){
        return name;
    }

}
