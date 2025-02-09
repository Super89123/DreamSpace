package dev.relaxertime.dreamSpace.Pets;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Skull;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.*;

public abstract class Pet implements Listener {
    private final String name;
    private final DreamSpace plugin;
    private final boolean isActive = false;
    private final Set<Player> players = new HashSet<>();
    private final String headName;
    private ArmorStand enity;
    private final int id;
    private static final Map<Integer, Pet> petsRegistry = new HashMap<>();

    protected Pet(String name, DreamSpace plugin, String headName, int id) {
        this.name = name;
        this.plugin = plugin;
        this.headName = headName;
        this.id = id;
        petsRegistry.put(id, this);


        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : getPlayers()){
                    passive(player);
                }

            }
        }.runTaskTimer(plugin, 0, 10);
        new BukkitRunnable(){
            double angle = 0;
            @Override
            public void run() {
                angle += 10;
                if (angle >= 360) {
                    angle = 0; // Сбрасываем угол после полного круга
                }

                // Применяем вращение к голове ArmorStand
                getRewardEntity().setHeadPose(new EulerAngle(Math.toRadians(angle), 0, 0));
            }
        }.runTaskTimer(plugin, 0, 1);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    protected String getName(){
        return name;
    }
    public ItemStack getPetStackByID(){

        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "itemstack_id");
        container.set(key, PersistentDataType.INTEGER, id);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(headName));
        meta.setLore(List.of(ChatColor.WHITE + "Здоровье: 100"));


        stack.setItemMeta(meta);
        return stack;
    }    protected Set<Player> getPlayers(){
        return players;
    }
    private ArmorStand getRewardEntity(){
        ArmorStand armorStand = Objects.requireNonNull(Bukkit.getWorld("world")).spawn(new Location(Bukkit.getWorld("world"), 0, 0, 0), ArmorStand.class);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName("Питомец");
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(headName));
        skull.setItemMeta(meta);
        armorStand.setItem(EquipmentSlot.HEAD, skull);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(ChatColor.BLUE + "Питомец: " + name + "ПКМ чтобы забрать");
        armorStand.setInvisible(true);

        return armorStand;
    }
    public void spawnEntity(Location location){
        getRewardEntity().teleport(location);
    }

    protected abstract void passive(Player player);
    @EventHandler
    public void interactEvent(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked().equals(getRewardEntity())){
            Player player = event.getPlayer();
            NamespacedKey key = new NamespacedKey(plugin, "pets");
            PersistentDataContainer container = player.getPersistentDataContainer();
            if(!container.has(key, PersistentDataType.INTEGER_ARRAY)){
                int[] ar = {0};
                container.set(key, PersistentDataType.INTEGER_ARRAY, ar);
            }
            int[] originalArray = container.get(key, PersistentDataType.INTEGER_ARRAY);
            assert originalArray != null;
            if(Arrays.stream(originalArray).noneMatch(x -> x == id)) {
                List<Integer> list = new ArrayList<>(Arrays.asList(Arrays.stream(originalArray).boxed().toArray(Integer[]::new)));

                list.add(id);

                int[] newArray = list.stream().mapToInt(Integer::intValue).toArray();

                container.set(key, PersistentDataType.INTEGER_ARRAY, newArray);
            }
            else {
                player.sendMessage(ChatColor.RED + "У вас уже есть этот питомец");
            }
            event.setCancelled(true);
        }

    }
    public static Pet getPetById(int id) {
        return petsRegistry.get(id);
    }
}
