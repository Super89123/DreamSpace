package dev.relaxertime.dreamSpace.AntiCheat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import dev.relaxertime.dreamSpace.DreamSpace;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Core implements Listener {

    private final DreamSpace plugin;
    private final Map<UUID, Integer> hitCount = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();
    private final Map<UUID, NPC> playerNPCs = new HashMap<>(); // Хранение NPC для каждого игрока

    public Core(DreamSpace plugin) {
        this.plugin = plugin;

        // Регистрация обработчика пакетов для скрытия NPC от других игроков
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING || event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                    int entityId = event.getPacket().getIntegers().read(0);
                    for (Map.Entry<UUID, NPC> entry : playerNPCs.entrySet()) {
                        if (entry.getValue().getEntity().getEntityId() == entityId) {
                            Player player = event.getPlayer();
                            if (!player.getUniqueId().equals(entry.getKey())) {
                                event.setCancelled(true); // Скрываем NPC от других игроков
                            }
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            UUID playerId = player.getUniqueId();

            hitCount.put(playerId, hitCount.getOrDefault(playerId, 0) + 1);
            lastHitTime.put(playerId, System.currentTimeMillis());

            // Если игрок совершил слишком много ударов за короткое время
            if (hitCount.get(playerId) > 5) {
                // Проверяем, есть ли уже NPC для этого игрока
                if (!playerNPCs.containsKey(playerId)) {
                    player.sendMessage(ChatColor.RED + "Подозрительное поведение обнаружено!");
                    createNPC(player);
                    hitCount.put(playerId, 0); // Сбрасываем счетчик ударов
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Вы уже проверяетесь!");
                }
            }
        }
    }

    private void createNPC(Player player) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, player.getName());
        npc.spawn(player.getLocation());
        npc.setFlyable(true);

        // Сохраняем NPC для игрока
        playerNPCs.put(player.getUniqueId(), npc);

        // Запуск задачи для вращения NPC вокруг игрока
        new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                if (!npc.isSpawned()) {
                    this.cancel();
                    return;
                }

                Location loc = player.getLocation();
                double x = loc.getX() + 2 * Math.cos(angle);
                double z = loc.getZ() + 2 * Math.sin(angle);
                Random random = new Random();

                double randomY = -2 + (random.nextDouble() * 4);
                npc.teleport(new Location(loc.getWorld(), x, loc.getY() + randomY, z, loc.getYaw(), loc.getPitch()), PlayerTeleportEvent.TeleportCause.PLUGIN);

                angle += Math.PI / 16;
                if (angle >= 2 * Math.PI) {
                    angle = 0;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        // Уничтожение NPC через 30 секунд
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (npc.isSpawned()) {
                npc.destroy();
                playerNPCs.remove(player.getUniqueId()); // Удаляем NPC из списка
                player.sendMessage(ChatColor.GREEN + "NPC был уничтожен.");
            }
        }, 20L * 30); // 30 секунд (20 тиков = 1 секунда)
    }
}