package dev.relaxertime.dreamSpace.AntiCheat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.Objects;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length < 2){
            commandSender.sendMessage("Необходмимы ник игрока и причина репорта!");
            return true;
        }
        String playerNAME = strings[0];

        if(Bukkit.getPlayer(playerNAME) != null){
            StringBuilder why = new StringBuilder(" ");
            for(int i = 1; i < strings.length; i++){
                why.append(strings[i]);
                why.append(" ");
            }
            Core.sendTelegramMessage("Поступил репорт на Игрока: " + playerNAME +" От игрока: "+commandSender.getName()+" UUID: " + Objects.requireNonNull(Bukkit.getPlayer(playerNAME)).getUniqueId() + " Время: " + LocalTime.now() + " Причина: " + why);


        }
        else{
            commandSender.sendMessage(ChatColor.RED + "Игрок не в сети!");
        }



        return true;
    }
}
