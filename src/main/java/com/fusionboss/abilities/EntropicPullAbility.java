package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EntropicPullAbility implements Ability{
    @Override
    public void execute(FusionBoss boss) {
        Player player = getRandPlayer(boss);
        if(player == null) return;
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 1));

        Vector vectorToBoss = boss.getLocation().toVector().subtract(player.getLocation().toVector());
        Vector velocity = vectorToBoss.normalize().multiply(0.1); // Reduce the multiplier for a slower pull

        // Create a new BukkitRunnable to apply the velocity over time
        new BukkitRunnable() {
            int counter = 0; // Counter to keep track of the time
            public void run() {
                if(counter < 100) { // Run for 5 seconds (100 ticks)
                    player.setVelocity(velocity);
                    counter++;
                } else {
                    this.cancel(); // Cancel the task after 5 seconds
                }
            }
        }.runTaskTimer(BossMain.getInstance(), 0L, 1L);

        new GraspAbility().execute(boss, player);
    }


    @Override
    public int getFrequency() {
        return 20;
    }

    private Player getRandPlayer(FusionBoss boss) {
        return boss.getLocation().getWorld().getNearbyPlayers(boss.getLocation(), 50).stream().findAny().orElse(null);
    }
}
