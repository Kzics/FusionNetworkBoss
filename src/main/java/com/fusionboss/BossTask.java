package com.fusionboss;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BossTask extends BukkitRunnable {

    private final FusionBoss boss;
    public BossTask(final FusionBoss boss){
        this.boss = boss;
    }


    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()){
            //boss.sendLaser(boss.getLocation(), player.getLocation());
        }
    }
}
