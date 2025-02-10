package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ManaController implements Listener {
    private final DreamSpace plugin;

    public ManaController(DreamSpace plugin) {
        this.plugin = plugin;
    }
    public static int getPlayerMana(Player player, DreamSpace plugin){
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "mana");
        if(!container.has(key)){
            container.set(key, PersistentDataType.INTEGER, 0);
        }
        return container.get(key, PersistentDataType.INTEGER);
    }
    public static void setPlayerMana(Player player, DreamSpace plugin, int newmana){
        PersistentDataContainer container = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "mana");
        if(!container.has(key)){
            container.set(key, PersistentDataType.INTEGER, 0);
        }
        container.set(key, PersistentDataType.INTEGER, newmana);

    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event){
        PersistentDataContainer container = event.getPlayer().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "mana");
        if(!container.has(key)){
            container.set(key, PersistentDataType.INTEGER, 0);
        }
        ;

    }
}
