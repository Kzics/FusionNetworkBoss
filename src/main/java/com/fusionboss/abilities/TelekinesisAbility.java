package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TelekinesisAbility implements Ability{

    @Override
    public void execute(FusionBoss boss) {
        Location bossLocation = boss.getLocation();
        int radius = 20;
        List<Entity> affectedEntities = new ArrayList<>();

        for (Entity entity : bossLocation.getWorld().getNearbyEntities(bossLocation, radius, radius, radius)) {
            if (entity instanceof Player) {
                entity.setVelocity(new Vector(0, 1, 0));
                entity.setGravity(false);
                affectedEntities.add(entity);
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : affectedEntities) {
                    if (entity instanceof Player) {
                        entity.setVelocity(new Vector(0, 1, 0));
                        entity.setGravity(true);
                        ((Player) entity).damage(2);
                        ((Player)entity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER,40,2));

                    }
                }
            }
        }.runTaskLater(BossMain.getInstance(), 20);
    }

    @Override
    public int getFrequency() {
        return 30;
    }

}
