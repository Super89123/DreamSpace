package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.Pets.PetCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagicCommand implements CommandExecutor {
    /**
     * @param commandSender
     * @param command
     * @param s
     * @param strings
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){
            if(strings.length == 2){
                if(strings[0].equalsIgnoreCase("give")){
                    if(PetCommand.isInteger(strings[1])){
                        Spell spell = Spell.getPetByCustomModelData(Integer.parseInt(strings[1]));
                        if(spell != null){
                            player.getInventory().addItem(spell.getSpellStack());
                            player.sendMessage(ChatColor.GREEN + "Вы получили: " + spell.getName());
                        }
                        else {
                            player.sendMessage(ChatColor.RED+ "Вы ввели не правильную customModelData");
                        }
                    }
                }
            }
        }
        return true;
    }
}
