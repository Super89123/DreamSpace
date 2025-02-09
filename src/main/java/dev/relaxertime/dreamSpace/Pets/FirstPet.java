package dev.relaxertime.dreamSpace.Pets;

import dev.relaxertime.dreamSpace.DreamSpace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FirstPet extends Pet{
    public FirstPet(DreamSpace plugin) {
        super("Крыса", plugin, "Phytik", 1);
    }

    @Override
    protected void passive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20, 5, false, false, false));

    }
}
