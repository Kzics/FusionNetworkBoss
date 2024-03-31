package com.fusionboss.listeners;

import com.fusionboss.BossMain;
import com.fusionboss.FusionBoss;
import com.fusionboss.abilities.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BossListeners implements Listener, CommandExecutor {


    private final BossMain main;
    private FusionBoss boss;
    public BossListeners(BossMain main){
        main.getServer().getPluginManager().registerEvents(this,main);
        this.main = main;

    }





    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        final Entity damager = event.getDamager();
        if(damager instanceof Wither){
            return;
        }
        if(event.getEntity() instanceof Wither){
            final AttributeInstance attributeInstance = ((Wither) event.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH);
            final double maxHealth = attributeInstance.getBaseValue();
            final double currentHealth = ((Wither) event.getEntity()).getHealth();
            final double healthPercentage = currentHealth / maxHealth * 100;

            if (healthPercentage <= 75 && !boss.isFirstState()) {
                boss.addAbility(new BlindAbility());
                boss.addAbility(new EntropicPullAbility());

                boss.setFirstState(true);

                boss.executeAbility("blind");

            } else if (healthPercentage <= 50  && !boss.isSecondState()) {
                boss.addAbility(new VoidShootAbility());
                boss.addAbility(new TNTAbility());
                boss.addAbility(new GraspAbility());

                boss.setSecondState(true);

            } else if (healthPercentage <= 30 && !boss.isThirdState()) {
                boss.addAbility(new ShieldAbility());
                boss.addAbility(new TelekinesisAbility());

                boss.setThirdState(true);
            }else if(healthPercentage<= 50 && boss.isCircleUsed()) {
                boss.executeAbility("circle");

                boss.setCircleUsed(true);
            }

            if(event.getDamager() instanceof ProjectileSource){
                ProjectileSource shooter = (ProjectileSource) event.getDamager();
                Entity entity = (Entity) shooter;
                Location originalLocation = entity.getLocation();
                World world = originalLocation.getWorld();
                Random random = new Random();
                event.setCancelled(true);

                double damage = (int) event.getDamage() * 0.6;
                ((Wither) event.getEntity()).damage(damage);

                if(random.nextInt(3) == 0) {
                    double x = originalLocation.getX() + (random.nextDouble() * 40 - 20);
                    double z = originalLocation.getZ() + (random.nextDouble() * 40 - 20);
                    double y = world.getHighestBlockYAt((int)x, (int)z);
                    Location teleportLocation = new Location(world, x, y, z);
                    entity.teleport(teleportLocation);
                }
            }

            if(event.getEntity().isInvulnerable()){
                event.setCancelled(true);
            }
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0){
            if(boss != null) boss.kill();
            boss = new FusionBoss(((Player)sender).getLocation(),main);
            Bukkit.broadcast(Component.text("Boss has been spawned!"));
            return false;
        }
        switch (args[0]) {
            case "blind" -> boss.executeAbility("Blind");
            case "laser" -> boss.executeAbility("Laser");
            case "shield" -> boss.executeAbility("Shield");
            case "telekinesis" -> boss.executeAbility("Telekinesis");
            case "thunder" -> boss.executeAbility("Thunder");
        }
        return false;
    }
}
