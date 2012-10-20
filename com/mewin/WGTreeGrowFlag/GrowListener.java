/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mewin.WGTreeGrowFlag;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 *
 * @author mewin
 */
public class GrowListener implements Listener {
    private WGCustomFlagsPlugin custPlugin;
    private WorldGuardPlugin wgPlugin;
    private WGTreeGrowFlagPlugin plugin;
    
    public GrowListener(WGTreeGrowFlagPlugin plugin, WorldGuardPlugin wgPlugin, WGCustomFlagsPlugin custPlugin)
    {
        this.custPlugin = custPlugin;
        this.wgPlugin = wgPlugin;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onStructureGrow(StructureGrowEvent e)
    {
        RegionManager rm = wgPlugin.getRegionManager(e.getWorld());
        
        if (rm == null)
        {
            return;
        }
        
        ApplicableRegionSet regions = rm.getApplicableRegions(e.getLocation());
        Collection<ProtectedRegion> regCol = (Collection<ProtectedRegion>) getPrivateValue(regions, "applicable");
        
        if (!regions.allows(WGTreeGrowFlagPlugin.TREE_GROW_FLAG))
        {
            if (!e.isFromBonemeal() || !e.getPlayer().isOp())
            {
                e.setCancelled(true);
                if (e.getPlayer() != null)
                {
                    e.getPlayer().sendMessage(ChatColor.RED + "You may not grow trees here.");
                }
            }
        }
        
        Iterator<BlockState> itr = e.getBlocks().iterator();
        if (e.getPlayer() == null || !e.getPlayer().isOp())
        {
            while(itr.hasNext())
            {
                BlockState state = itr.next();

                ApplicableRegionSet blockRegions = rm.getApplicableRegions(state.getLocation());

                if (state.getType() == Material.LEAVES)
                {
                    Iterator<ProtectedRegion> itr2 = blockRegions.iterator();

                    while (itr2.hasNext())
                    {
                        ProtectedRegion region = itr2.next();
                        State flagState = region.getFlag(WGTreeGrowFlagPlugin.LEAVE_GROW_FLAG);

                        if (flagState == State.DENY && !regCol.contains(region))
                        {
                            itr.remove();
                        }
                    }
                }
            }
        }
    }
    
    private Object getPrivateValue(Object obj, String name)
    {
        try
        {
            Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(obj);
        }
        catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
        {
            return null;
        }
    }
}
