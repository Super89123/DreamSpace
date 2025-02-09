package dev.relaxertime.dreamSpace.Pets;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.*;
@SuppressWarnings("deprecation")
public abstract class Pet implements Listener {
    private final String name;
    private final DreamSpace plugin;
    private final boolean isActive = false;
    private final Set<Player> players = new HashSet<>();
    private final String headName;
    private final ArmorStand entity;
    private final int id;
    private static final Map<Integer, Pet> petsRegistry = new HashMap<>();
    private final Set<ArmorStand> armorStands = new HashSet<>();

    protected Pet(String name, DreamSpace plugin, String headName, int id) {
        this.name = name;
        this.plugin = plugin;
        this.headName = headName;
        this.id = id;
        petsRegistry.put(id, this);
        entity = getRewardEntity();




        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    NamespacedKey activeId = new NamespacedKey(plugin, "activePet");
                    PersistentDataContainer pcs = p.getPersistentDataContainer();
                    if(pcs.has(activeId)){
                        if(Objects.equals(pcs.get(activeId, PersistentDataType.INTEGER), id)){
                            passive(p);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 10);
        new BukkitRunnable(){
            double angle = 0;
            @Override
            public void run() {
                angle += 10;
                if (angle >= 360) {
                    angle = 0;
                }

                for(ArmorStand as : armorStands){
                    as.setHeadPose(new EulerAngle(0, Math.toRadians(angle), 0));
                }

            }
        }.runTaskTimer(plugin, 0, 1);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    protected String getName(){
        return name;
    }
    public ItemStack getPetStack(){

        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "itemstack_id");
        container.set(key, PersistentDataType.INTEGER, id);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(headName));
        meta.setLore(List.of(ChatColor.WHITE + "Здоровье: 100", ChatColor.WHITE + "Уровень : 1"));
        meta.setDisplayName(name);


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
        armorStand.setCustomName(ChatColor.BLUE + "Питомец: " + name + " ПКМ чтобы забрать");
        armorStand.setInvisible(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCanMove(true);

        return armorStand;
    }
    public void spawnEntity(Location location)
    {
        ArmorStand as = location.getWorld().spawn(new Location(location.getWorld(), location.x(), location.y()-1, location.z()), ArmorStand.class);{
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName("Питомец");
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(headName));
        skull.setItemMeta(meta);
        as.setItem(EquipmentSlot.HEAD, skull);
        as.setBasePlate(false);
        as.setCustomName(ChatColor.BLUE + "Питомец: " + name + " ПКМ чтобы забрать");
        as.setInvisible(true);
        as.setCustomNameVisible(true);
        as.setCanMove(true);
        armorStands.add(as);

    }
    }

    protected abstract void passive(Player player);
    @EventHandler
    public void interactEvent(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand){
        if(event.getRightClicked().equals(entity) || armorStands.contains((ArmorStand) event.getRightClicked())){
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
                player.sendMessage(ChatColor.GREEN + "Вы получили " + getName());
                event.getRightClicked().remove();
            }
            else {
                player.sendMessage(ChatColor.RED + "У вас уже есть этот питомец");
            }
            event.setCancelled(true);
        }

     }
    }
    public static Pet getPetById(int id) {
        return petsRegistry.get(id);
    }
    public int getId(){
        return id;
    }
}
