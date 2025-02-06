package dev.relaxertime.dreamSpace.CustomEntities;




import dev.relaxertime.dreamSpace.DreamSpace;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


@SuppressWarnings("deprecation")
public class Trader implements Listener {
    private DreamSpace plugin;
    private Location location;
    private Inventory shop;
    private NPC npc;
    private int size = 18;
    private boolean isGun;

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
    public void oninventoryClick(InventoryClickEvent e) throws CloneNotSupportedException {
        if(e.getClickedInventory() != shop) return;
        e.getWhoClicked().getInventory().addItem(e.getCursor());
        e.setCancelled(true);
    }

    public void addItem(ItemStack i, int CustomModelData){
        if (shop.getSize() < 18) return;
        ItemMeta i_meta = i.getItemMeta();
        i_meta.setCustomModelData(CustomModelData);
        i.setItemMeta(i_meta);
        shop.addItem(i);
    }
}
