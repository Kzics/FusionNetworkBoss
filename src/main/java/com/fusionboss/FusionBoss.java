package com.fusionboss;

import com.fusionboss.abilities.*;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FusionBoss {

    private final Location location;
    private final Set<Ability> abilities;
    private final Wither wither;
    private boolean isGrasping = false;
    private final BukkitRunnable abilityTask;
    private boolean firstState = false;
    private boolean secondState = false;
    private boolean thirdState = false;
    private boolean circleUsed;

    public FusionBoss(final Location location, BossMain main) {
        Wither wither = location.getWorld().spawn(location, Wither.class);
        wither.setNoDamageTicks(5);
        wither.setFireTicks(0);
        wither.setVisualFire(false);

        abilityTask = new BukkitRunnable(){
            @Override
            public void run() {
                executeAbilities();
            }
        };
        abilityTask.runTaskTimer(main, 0, 20);

        this.abilities = new HashSet<>(List.of(
                new LaserAbility()
        ));

        this.location = location;
        this.wither = wither;
        AttributeInstance attribute = this.wither.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        attribute.setBaseValue(40000);
        wither.setHealth(attribute.getBaseValue());

        AttributeInstance flySpeed = this.wither.getAttribute(Attribute.GENERIC_FLYING_SPEED);
        flySpeed.setBaseValue(flySpeed.getBaseValue() * 1.5);
        AttributeInstance movSpeed = this.wither.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        movSpeed.setBaseValue(flySpeed.getBaseValue() * 1.5);


        wither.customName(Component.text("Fusion Boss"));
    }

    public void executeAbility(String abilityName) {
        for (Ability ability : abilities) {
            if (ability.getClass().getSimpleName().contains(abilityName)) {
                ability.execute(this);
                break;
            }
        }
    }

    public void kill(){
        wither.remove();

        abilityTask.cancel();
    }

    public void setCircleUsed(boolean circleUsed) {
        this.circleUsed = circleUsed;
    }

    public boolean isCircleUsed() {
        return circleUsed;
    }




    public Wither getEntity() {
        return wither;
    }

    public Location getLocation() {
        return wither.getLocation();
    }

    public void setGrasping(boolean grasping) {
        isGrasping = grasping;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public void executeAbilities() {
        int totalFrequency = abilities.stream().mapToInt(Ability::getFrequency).sum();
        int randomValue = new Random().nextInt(totalFrequency);

        for (Ability ability : abilities) {
            randomValue -= ability.getFrequency();
            if (randomValue < 0) {
                if(ability instanceof GraspAbility){
                    if(isGrasping) return;
                }
                ability.execute(this);
                System.out.println("Used ability: " + ability.getClass().getSimpleName());
                break;
            }
        }
    }

    public boolean isFirstState() {
        return firstState;
    }

    public boolean isSecondState() {
        return secondState;
    }

    public boolean isThirdState() {
        return thirdState;
    }

    public void setFirstState(boolean firstState) {
        this.firstState = firstState;
    }

    public void setSecondState(boolean secondState) {
        this.secondState = secondState;
    }

    public void setThirdState(boolean thirdState) {
        this.thirdState = thirdState;
    }
}
