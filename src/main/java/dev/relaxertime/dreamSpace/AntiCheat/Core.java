package dev.relaxertime.dreamSpace.AntiCheat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Core extends PacketAdapter implements Listener {

    private final DreamSpace plugin;
    private final Map<UUID, Integer> hitCount = new HashMap<>();
    private final Map<UUID, Integer> criticalCount = new HashMap<>();
    private final Map<UUID, Entity> npcMap = new HashMap<>();

    public Core(DreamSpace plugin) {
        super(plugin, PacketType.Play.Client.USE_ENTITY);
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
            Player player = event.getPlayer();
            EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);

            if (action == EnumWrappers.EntityUseAction.ATTACK) {
                UUID playerId = player.getUniqueId();
                hitCount.put(playerId, hitCount.getOrDefault(playerId, 0) + 1);

                // Проверка на критический удар
                if (player.getVelocity().getY() < -0.1) {
                    criticalCount.put(playerId, criticalCount.getOrDefault(playerId, 0) + 1);
                }

                // Проверка на подозрительное поведение
                if (hitCount.get(playerId) > 10 && criticalCount.getOrDefault(playerId, 0) > 5) {
                    spawnNPC(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        hitCount.remove(playerId);
        criticalCount.remove(playerId);
        removeNPC(playerId);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UUID playerId = player.getUniqueId();

            if (npcMap.containsKey(playerId)) {
                removeNPC(playerId);
            }
        }
    }

    private void spawnNPC(Player player) {
        Location location = player.getLocation();
        Player npc = (Player) player.getWorld().spawnEntity(location, EntityType.PLAYER);
        npc.setCustomName(player.getName());
        npc.setCustomNameVisible(true);
        npc.setInvulnerable(true);

        // Случайная броня
        npc.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
        npc.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        npc.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        npc.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));

        npcMap.put(player.getUniqueId(), npc);

        // Вращение NPC вокруг игрока
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!npcMap.containsKey(player.getUniqueId())) {
                    this.cancel();
                    return;
                }

                Location loc = player.getLocation();
                double angle = System.currentTimeMillis() / 1000.0;
                double x = Math.cos(angle) * 2;
                double z = Math.sin(angle) * 2;
                npc.teleport(loc.add(x, 0, z));
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void removeNPC(UUID playerId) {
        if (npcMap.containsKey(playerId)) {
            npcMap.get(playerId).remove();
            npcMap.remove(playerId);
        }
    }
}