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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Core implements Listener {

    private final DreamSpace plugin;
    private final Map<UUID, Integer> hitCount = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();
    private final Map<UUID, NPC> playerNPCs = new HashMap<>();
    private final Map<UUID, Integer> zav = new HashMap<>();

    /**
     * @param plugin Объект DreamSpace
     *
     */
    public Core(DreamSpace plugin) {
        this.plugin = plugin;


        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING || event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
                    int entityId = event.getPacket().getIntegers().read(0);
                    for (Map.Entry<UUID, NPC> entry : playerNPCs.entrySet()) {
                        if (entry.getValue().getEntity().getEntityId() == entityId) {
                            Player player = event.getPlayer();
                            if (!player.getUniqueId().equals(entry.getKey())) {
                                event.setCancelled(true);
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


            if (hitCount.get(playerId) > 5) {

                if (!playerNPCs.containsKey(playerId)) {
                    player.sendMessage(ChatColor.RED + "Подозрительное поведение обнаружено!");
                    createNPC(player);
                    hitCount.put(playerId, 0);
                }
            }
            if(playerNPCs.containsKey(playerId)) {
                NPC npf = playerNPCs.get(playerId);
                if (event.getEntity().equals(npf.getEntity())) {
                    if (!zav.containsKey(playerId)) {
                        zav.put(playerId, 0);
                    }
                    zav.put(playerId, zav.get(playerId) + 1);
                    if (zav.get(playerId) > 10) {
                        sendTelegramMessage("Обнаружен Нарушитель! Ник: " + player.getName() + " UUID: " + playerId + "  + Сервер: DreamScape " + "Время: " + LocalTime.now());
                    }
                }
            }
        }
    }

    private void createNPC(Player player) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, player.getName());
        npc.spawn(player.getLocation());
        npc.setFlyable(true);



        playerNPCs.put(player.getUniqueId(), npc);


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

                double randomY = -2 + (random.nextDouble() * 3);
                npc.teleport(new Location(loc.getWorld(), x, loc.getY() + randomY, z, loc.getYaw(), loc.getPitch()), PlayerTeleportEvent.TeleportCause.PLUGIN);

                angle += Math.PI / 16;
                if (angle >= 2 * Math.PI) {
                    angle = 0;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);


        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (npc.isSpawned()) {
                npc.destroy();
                playerNPCs.remove(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "NPC был уничтожен.");
            }
        }, 20L * 30);
    }

    /**
     * @param message Сообщеие которое стоит отправить в телеграм.
     */
    public static void sendTelegramMessage(String message){
        try {

            String botToken = "7975211101:AAEAkLj11OBpU3MME5flsMA9utmhk_A4MpM";
            String method = "sendMessage";
            String chat = "-1002358135016";
            String urlString = "https://api.telegram.org/bot" + botToken + "/" + method+"?chat_id="+chat+"&text="+message;

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
