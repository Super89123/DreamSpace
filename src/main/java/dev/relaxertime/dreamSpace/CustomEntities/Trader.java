package dev.relaxertime.dreamSpace.CustomEntities;




import dev.relaxertime.dreamSpace.DreamSpace;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.*;


import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;


@SuppressWarnings("deprecation")
public class Trader implements Listener {
    private final DreamSpace plugin;
    private final Location location;
    private final Inventory shop;
    private final HashMap<Integer, Component> descriptoin_map = new HashMap<>();
    private final NPC npc;
    private final int size = 18;


    public Trader(DreamSpace plugin, Location loc, String label, String name) throws CloneNotSupportedException {
        this.plugin = plugin;
        this.location = loc;
        shop = Bukkit.createInventory(null, size, label);
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.spawn(loc);
        System.out.println("Trader spawned at coordinates:" + loc);
    }
    @EventHandler
    public void click(NPCRightClickEvent event){
        if(!(event.getNPC() == npc)) return;
        event.getClicker().openInventory(shop);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws CloneNotSupportedException {
        if(e.getClickedInventory() != shop) return;
        if(e.getCurrentItem() == null) return;
        try{
            ItemStack item = e.getCurrentItem().clone();
            ItemMeta item_meta = item.getItemMeta();
            item_meta.lore(Collections.singletonList(descriptoin_map.get(item_meta.getCustomModelData())));
            item.setItemMeta(item_meta);
            System.out.println(item.lore());
            e.getWhoClicked().getInventory().addItem(item);
            e.setCancelled(true);
        } catch (IndexOutOfBoundsException ignored){
            System.out.println("FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUCK");
        }



    }

    public void addItem(ItemStack i, int CustomModelData, String price, String description_){
        if (shop.getSize() < 18) return;
        ItemMeta i_meta = i.getItemMeta();
        Component description = text("Цена: ").color(color(26214)).append(text(price, color(0x0099CC00)));
        i_meta.lore(Collections.singletonList(description));
        i_meta.setCustomModelData(CustomModelData);
        i.setItemMeta(i_meta);
        shop.addItem(i);
        Component new_description = text("");
        for (String j : description_.split("/")){
            System.out.println(j);
            String[] desc_arr = j.split(";");
            System.out.println(Integer.decode(desc_arr[1]));
            new_description = new_description.append(text(desc_arr[0], color(Integer.decode(desc_arr[1]))));
        }
        System.out.println(new_description);
        descriptoin_map.put(CustomModelData, new_description);
    }

}
