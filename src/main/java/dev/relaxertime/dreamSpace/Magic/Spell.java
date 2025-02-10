package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Spell implements Listener {
    private final int customModelData;
    private final DreamSpace plugin;

    /**
     * @param customModelData CustomModelData предмета
     * @param plugin Объект Плагина DreamSpace
     */
    protected Spell(int customModelData, DreamSpace plugin) {
        this.customModelData = customModelData;
        this.plugin = plugin;
    }

    public abstract void whatToDo(Player player);
    @EventHandler
    public void inter(PlayerInteractEvent event){
        if(event.getAction().isRightClick()){
            if(event.getItem() != null){
                if(event.getItem().getType().equals(Material.BOOK)){
                    ItemStack item = event.getItem();
                    if(item.hasItemMeta()){
                        if(item.getItemMeta().hasCustomModelData()){
                            if(item.getItemMeta().getCustomModelData() == customModelData){

                            }
                        }
                    }
                }
            }
        }
    }

}
