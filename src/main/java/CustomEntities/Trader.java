package CustomEntities;




import dev.relaxertime.dreamSpace.DreamSpace;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@SuppressWarnings("deprecation")
public class Trader implements Listener {
    private DreamSpace plugin;
    private Location location;
    private Inventory shop;
    private NPC npc;
    private int size = 18;
    private boolean isGun;

    public Trader(DreamSpace plugin, Location loc, String label, String name, boolean ig) throws CloneNotSupportedException {
        this.plugin = plugin;
        this.location = loc;
        isGun = ig;
        shop = Bukkit.createInventory(null, size, label);
        setItems(shop, ig);
        npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        npc.spawn(loc);

    }
    private void setItems(Inventory inv, boolean flag) throws CloneNotSupportedException {
        if(flag) {

        }
    }
    @EventHandler
    public void click(NPCRightClickEvent event){
        if(!(event.getNPC() == npc)) return;
        event.getClicker().openInventory(shop);
    }
    @EventHandler
    public void oninventoryClick(InventoryClickEvent e) throws CloneNotSupportedException {
        if(e.getClickedInventory() != shop) return;
        System.out.println(1231236169);
        if(e.getCurrentItem() == null) return;
        if(isGun){
            if(e.getCurrentItem().getItemMeta().getCustomModelData() == 1161){

            }else if(e.getCurrentItem().getItemMeta().getCustomModelData() == 1488){

            }else if(e.getCurrentItem().getItemMeta().getCustomModelData() == 2222){            }
        }
        e.setCancelled(true);
    }
}
