package com.fusionboss.abilities;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Optional;

public class VoidShootAbility implements Ability {
    private final Particle.DustOptions blackParticle = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
    private final Particle.DustOptions whiteParticle = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1);

    @Override
    public void execute(FusionBoss boss) {
        Optional<Player> targetOpt = boss.getLocation().getNearbyPlayers(10).stream().findFirst(); // suppose que FusionBoss a une méthode getTarget() qui renvoie le joueur ciblé
        if (!targetOpt.isPresent()) return; // si aucun joueur n'est ciblé, ne fait rien

        Player target = targetOpt.get();

        Vector direction = target.getLocation().subtract(boss.getLocation()).toVector().normalize();
        new BukkitRunnable() {
            Location particleLocation = boss.getLocation().clone();
            double angle = 0;
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // crée un cercle de particules blanches au début du tir
                    if (particleLocation.distance(boss.getLocation()) < 1) {
                        Location circleLocation = particleLocation.clone().add(new Vector(Math.cos(angle), 0, Math.sin(angle)));
                        player.spawnParticle(Particle.REDSTONE, circleLocation, 50, 0.25, 0.25, 0.25, whiteParticle);
                        angle += Math.PI / 10; // augmente l'angle pour la rotation des particules
                    } else {
                        player.spawnParticle(Particle.REDSTONE, particleLocation, 100, 0.5, 0.5, 0.5, blackParticle);
                    }
                    if(particleLocation.distance(player.getLocation()) < 5){
                        player.damage(7);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,80,2));
                    }
                }
                particleLocation.add(direction); // déplace la position de la particule vers le joueur ciblé
                if (particleLocation.distance(boss.getLocation()) > 10) { // arrête le runnable après que la particule ait avancé de 10 blocs
                    this.cancel();
                }
            }
        }.runTaskTimer(BossMain.getInstance(), 0L, 1L); // exécute le runnable toutes les 1 tick (20 ticks = 1 seconde)
    }



    @Override
    public int getFrequency() {
        return 35;
    }
}

