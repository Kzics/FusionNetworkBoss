package com.fusionboss;

import com.fusionboss.listeners.BossListeners;
import org.bukkit.plugin.java.JavaPlugin;

public class BossMain extends JavaPlugin  {

    private static BossMain instance;

    public static BossMain getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;

        getCommand("fusionboss").setExecutor(new BossListeners(this));
    }


}
