package dev.relaxertime.dreamSpace.Pets;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
@SuppressWarnings("deprecation")
public class PetCommand implements CommandExecutor {
    private final DreamSpace plugin;

    public PetCommand(DreamSpace plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length != 1){
                player.sendMessage("/pet <действие>");
            }
            else{
                if(strings[0].equalsIgnoreCase("menu")){
                    Inventory inventory = Bukkit.createInventory(null, 54, "Меню ваших питомцев");
                    NamespacedKey key = new NamespacedKey(plugin, "pets");
                    PersistentDataContainer container = player.getPersistentDataContainer();
                    if(container.has(key)){
                        int [] l = {0};
                        container.set(key, PersistentDataType.INTEGER_ARRAY, l);
                    }
                    int[] arrayOfIds = container.get(key, PersistentDataType.INTEGER_ARRAY);
                    assert arrayOfIds != null;
                    for(int i =0; i < arrayOfIds.length; i++){
                        Pet pet = Pet.getPetById(i );
                        inventory.setItem(i, pet.getPetStackByID());
                    }

                } else if (strings[0].equalsIgnoreCase("summon") && player.hasPermission("*") && strings.length == 2) {


                }
            }

        }
        else {
            commandSender.sendMessage("Эту команду может использовать только игрок!");
        }
        return true;
    }
}
