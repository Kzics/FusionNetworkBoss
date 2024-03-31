package com.fusionboss.abilities;

import com.fusionboss.FusionBoss;

public interface Ability {
    void execute(FusionBoss boss);
    int getFrequency();
}

