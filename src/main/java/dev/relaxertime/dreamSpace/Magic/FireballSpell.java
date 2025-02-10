package dev.relaxertime.dreamSpace.Magic;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FireballSpell extends Spell{
    /**

     * @param plugin          Объект Плагина DreamSpace

     */
    public FireballSpell(DreamSpace plugin) {
        super(10300, plugin, 10, 2, "Файрболл");
    }

    /**
     * @param player  игрок
     * @param clickedLocation локация
      */
    @Override
    public void whatToDo(Player player, Location clickedLocation) {

        Vector direction = player.getLocation().getDirection();


        Fireball fireball = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);


        fireball.setDirection(direction);


        fireball.setIsIncendiary(false);
        fireball.setYield(0);


        fireball.setFireTicks(0);
        fireball.setShooter(player);
        fireball.setVelocity(direction.multiply(2.0));


    }
}
