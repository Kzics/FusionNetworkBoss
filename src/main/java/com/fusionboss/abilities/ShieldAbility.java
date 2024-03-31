package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import org.bukkit.*;

import org.bukkit.boss.BarColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ShieldAbility implements Ability{

    @Override
    public void execute(FusionBoss boss) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.BLACK, 1);

        boss.getEntity().setInvulnerable(true);
        boss.getEntity().getBossBar().setColor(BarColor.WHITE);

        new BukkitRunnable() {
            int counter = 0;
            final int radius = 5;
            @Override
            public void run() {
                final Location[] bossLocation = {boss.getLocation()};
                World world = bossLocation[0].getWorld();

                if (counter> 10) {
                    boss.getEntity().setInvulnerable(false);
                    boss.getEntity().getBossBar().setColor(BarColor.PURPLE);
                    this.cancel();
                } else {
                    Location newBossLocation = boss.getLocation();
                    Vector movement = newBossLocation.toVector().subtract(bossLocation[0].toVector());
                    for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI/64) {
                        for (double phi = 0; phi <= 2*Math.PI; phi += Math.PI/32) {
                            double x = radius * Math.cos(theta) * Math.sin(phi);
                            double y = radius * Math.cos(phi);
                            double z = radius * Math.sin(theta) * Math.sin(phi);
                            Location particleLocation = newBossLocation.clone().add(x, y, z);
                            world.spawnParticle(Particle.REDSTONE, particleLocation.add(movement), 1, dustOptions);
                        }
                    }
                    bossLocation[0] = newBossLocation;
                }
                counter ++;
            }
        }.runTaskTimer(BossMain.getInstance(), 0L, 10L); // Remplacez "BossMain.getInstance()" par une instance de votre classe principale de plugin
    }



    @Override
    public int getFrequency() {
        return 15;
    }
}
