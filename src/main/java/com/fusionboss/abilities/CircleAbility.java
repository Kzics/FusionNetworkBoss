package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CircleAbility implements Ability{

    private final int recursionDepth;
    public CircleAbility(int recursionDepth){
        this.recursionDepth = recursionDepth;
    }

    @Override
    public void execute(FusionBoss boss) {
        if(recursionDepth > 3) return;

        new BukkitRunnable() {
            int timer = 0;
            final Location location = boss.getLocation();
            @Override
            public void run() {
                createCircle(location, 5);

                for (Player player : location.getNearbyPlayers(50)) {
                    double distanceToCenter = player.getLocation().distance(location);

                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Distance : " + Math.round(distanceToCenter)));

                    if(timer == 5 *4) {
                        if (!isOnCircle(player.getLocation(), distanceToCenter, 5)) {
                            player.setHealth(0);
                            player.sendMessage("You are not on the circle");
                            new CircleAbility(recursionDepth + 1).execute(boss);
                        }
                        this.cancel();
                    }
                }
                timer++;
            }
        }.runTaskTimer(BossMain.getInstance(), 0, 5);
    }
    private void createCircle(Location center, int radius) {
        for (int i = 0; i < 360; i += 5) {
            double angle = i * Math.PI / 180;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            Location particleLocation = new Location(center.getWorld(), x, center.getY(), z);
            center.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 5, new Particle.DustOptions(Color.BLACK, 1));
        }
    }

    private boolean isOnCircle(Location playerLocation, double distanceToCenter, int radius) {

        if (playerLocation.getBlock().isPassable() && playerLocation.add(0, -1, 0).getBlock().isPassable()) {
            return distanceToCenter <= radius;
        } else {
            return false;
        }
    }

    @Override
    public int getFrequency() {
        return 0;
    }
}