package net.syobonoon.plugin.crackshotscope;

import org.bukkit.plugin.java.JavaPlugin;

import com.shampaggon.crackshot.events.WeaponScopeEvent;

public class CrackShotScope extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable");
        new ScopeEventsListener(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable");
        WeaponScopeEvent.getHandlerList().unregister(this);
    }
}
