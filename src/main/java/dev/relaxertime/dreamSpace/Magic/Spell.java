package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


@SuppressWarnings("deprecation")
public abstract class Spell implements Listener {
    private final int customModelData;
    private final DreamSpace plugin;
    private final int manaCost;
    private final int cooldownSec;

    /**
     * @param customModelData CustomModelData предмета
     * @param plugin Объект Плагина DreamSpace
     * @param manaCost Стоимость в мане
     * @param cooldownSec Задержка на использование
     */
    protected Spell(int customModelData, DreamSpace plugin, int manaCost, int cooldownSec) {
        this.customModelData = customModelData;
        this.plugin = plugin;
        this.manaCost = manaCost;
        this.cooldownSec = cooldownSec;
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

}
