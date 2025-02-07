package dev.relaxertime.dreamSpace.AntiCheat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import dev.relaxertime.dreamSpace.DreamSpace;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Core implements Listener, CommandExecutor {

    private final DreamSpace plugin;
    private final Map<UUID, Integer> hitCount = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();



    public Core(DreamSpace plugin) {

        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            UUID playerId = player.getUniqueId();

            // Проверка на критический удар

                hitCount.put(playerId, hitCount.getOrDefault(playerId, 0) + 1);

                lastHitTime.put(playerId, System.currentTimeMillis());


                // Если игрок совершил слишком много критических ударов за короткое время
                if (hitCount.get(playerId) > 5) {
                    player.sendMessage(ChatColor.RED + "Подозрительное поведение обнаружено!");
                    createNPC(player);
                    hitCount.put(playerId, 0);

            }
        }
    }

    private void createNPC(Player player) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, player.getName());
        npc.spawn(player.getLocation());
        npc.setFlyable(true);

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


                double randomy = -2 + (random.nextDouble() * 4); //
                npc.teleport(new Location(loc.getWorld(), x, loc.getY() + randomy, z, loc.getYaw(), loc.getPitch()), PlayerTeleportEvent.TeleportCause.PLUGIN);

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

                player.sendMessage(ChatColor.GREEN + "NPC был уничтожен.");
            }
        }, 20L * 30); // 30 секунд (20 тиков = 1 секунда)
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("anticheat")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.GREEN + "Античит активирован!");
                return true;
            }
        }
        return false;
    }


}