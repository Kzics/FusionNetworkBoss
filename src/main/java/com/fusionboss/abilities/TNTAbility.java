package com.fusionboss.abilities;

import com.fusionboss.FusionBoss;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

public class TNTAbility implements Ability{
    @Override
    public void execute(FusionBoss boss) {
        for (Player player : boss.getLocation().getNearbyPlayers(10)) {
            Location location = boss.getLocation().clone().add(0, 1, 0);
            TNTPrimed tnt = boss.getLocation().getWorld().spawn(location, TNTPrimed.class);
            tnt.setFuseTicks(80);

            Vector direction = player.getLocation().subtract(boss.getLocation()).toVector().normalize();
            tnt.setVelocity(direction);
        }
    }

    @Override
    public int getFrequency() {
        return 55;
    }
}
