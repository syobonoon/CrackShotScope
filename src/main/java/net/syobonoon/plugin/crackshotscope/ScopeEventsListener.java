package net.syobonoon.plugin.crackshotscope;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponScopeEvent;

public class ScopeEventsListener implements Listener {
    CSUtility cs = new CSUtility();
    private ItemStack PreSneakHelmet;

    HashMap<Player, ItemStack> hm = new HashMap<Player, ItemStack>();

    public ScopeEventsListener(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void scopeEvent(WeaponScopeEvent e){
        ItemStack ReplaceScope = new ItemStack(Material.PUMPKIN, 1);
        Player target = e.getPlayer();

        if(e.isZoomIn()) {
            this.PreSneakHelmet = target.getInventory().getHelmet();
            ItemMeta ReplaceScopeMeta = ReplaceScope.getItemMeta();
            ReplaceScopeMeta.setDisplayName("Scope");
            ReplaceScope.setItemMeta(ReplaceScopeMeta);
            target.getInventory().setHelmet(ReplaceScope);
            hm.put(target, this.PreSneakHelmet);

        }else if (!e.isZoomIn()){
            ItemStack HelmetFromHM = (ItemStack)hm.get(target);
            target.getInventory().setHelmet(HelmetFromHM);

            if (!target.isDead()) {
                hm.remove(target);
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void RespawnEvent(PlayerRespawnEvent e) {
        if (hm.toString().contains(e.getPlayer().toString())) {
            hm.remove(e.getPlayer());
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onDeathEvent(PlayerDeathEvent e) {
        String playerraw = e.getEntity().getDisplayName();
        Player player = Bukkit.getPlayer(playerraw);

        if (hm.toString().contains(player.toString())) {
            for (ItemStack i : e.getDrops()) {
                if (i.getItemMeta().getDisplayName().equals("Scope")) i.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onInventoryEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if ((e.getSlotType() == InventoryType.SlotType.ARMOR) && (hm.toString().contains(p.toString()))) {
            e.setCancelled(true);
            p.closeInventory();
        }
    }
}