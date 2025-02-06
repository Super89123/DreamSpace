package dev.relaxertime.dreamSpace.AntiCheat;

import com.google.common.util.concurrent.Service;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Core implements Listener {
    @EventHandler
    public void attackEvent(EntityDamageByEntityEvent event){

    }

}
