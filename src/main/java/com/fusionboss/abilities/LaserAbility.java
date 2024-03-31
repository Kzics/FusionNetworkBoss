package com.fusionboss.abilities;

import com.fusionboss.FusionBoss;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class LaserAbility implements Ability {
    @Override
    public void execute(FusionBoss boss) {
        Location start = boss.getLocation();

        World world = start.getWorld();

        for (Player player : start.getNearbyPlayers(10)) {
            Location end = player.getLocation();

            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            for (double i = 0; i < start.distance(end); i += 0.5) {
                Location point = start.clone().add(direction.clone().multiply(i));
                world.spawnParticle(Particle.SONIC_BOOM, point, 1);

                double radius = 1.0;
                double damage = 10.0;
                for (Entity entity : point.getWorld().getNearbyEntities(point, radius, radius, radius)) {
                    if (entity instanceof Player) {
                        ((Player) entity).damage(damage);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,40,2));
                    }
                }
            }
        }
    }

    @Override
    public int getFrequency() {
        return 100;
    }
}
