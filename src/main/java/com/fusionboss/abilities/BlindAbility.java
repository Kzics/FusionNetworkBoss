package com.fusionboss.abilities;

import com.fusionboss.FusionBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlindAbility implements Ability {
    @Override
    public void execute(FusionBoss boss) {
        double radius = 10.0;
        for (Entity entity : boss.getLocation().getWorld().getNearbyEntities(boss.getLocation(), radius, radius, radius)) {
            if (entity instanceof Player) {
                ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 4));
            }
        }
    }

    @Override
    public int getFrequency() {
        return 20;
    }
}
