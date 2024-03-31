package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GraspAbility implements Ability{
    @Override
    public void execute(FusionBoss boss) {
        boss.setGrasping(true);
        boss.getEntity().setAI(false);
        Player player = chooseRandomPlayer(boss);
        if(player == null) return;
        player.setGlowing(true);
        player.sendActionBar(Component.text("You have been Grasped!").color(TextColor.color(0,255,0)));

        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player p : player.getLocation().getNearbyPlayers(5)){
                    p.setHealth(0);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                player.setHealth(0);
                player.setGlowing(false);
                boss.setGrasping(false);
                boss.getEntity().setAI(true);


            }
        }.runTaskLater(BossMain.getInstance(), 20* 10L);
    }

    public void execute(FusionBoss boss, Player player) {
        if(player == null) return;
        player.setGlowing(true);

        player.sendActionBar(Component.text("You have been Grasped!").color(TextColor.color(0,255,0)));


        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player p : player.getLocation().getNearbyPlayers(5)){
                    p.setHealth(0);
                }
                player.playSound(player.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);
                player.setHealth(0);
                player.setGlowing(false);
            }
        }.runTaskLater(BossMain.getInstance(), 20* 10L);
    }

    @Override
    public int getFrequency() {
        return 23;
    }

    private Player chooseRandomPlayer(FusionBoss boss) {
        return boss.getLocation().getWorld().getNearbyPlayers(boss.getLocation(),50).stream().findAny().orElse(null);
    }
}
