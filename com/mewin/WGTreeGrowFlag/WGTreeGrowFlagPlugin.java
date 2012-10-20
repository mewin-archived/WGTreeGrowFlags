/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mewin.WGTreeGrowFlag;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author mewin
 */
public class WGTreeGrowFlagPlugin extends JavaPlugin {
    public static final StateFlag LEAVE_GROW_FLAG = new StateFlag("leave-grow", false);
    public static final StateFlag TREE_GROW_FLAG = new StateFlag("tree-grow", true);
    
    private WGCustomFlagsPlugin custPlugin;
    private WorldGuardPlugin wgPlugin;
    private GrowListener listener;
    
    @Override
    public void onEnable()
    {
        custPlugin = getCustPlugin();
        wgPlugin = getWGPlugin();
        
        if (custPlugin == null)
        {
            getLogger().warning("No WorldGuard Custom Flags plugin found, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        if (wgPlugin == null)
        {
            getLogger().warning("No WorldGuard plugin found, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        custPlugin.addCustomFlag(TREE_GROW_FLAG);
        custPlugin.addCustomFlag(LEAVE_GROW_FLAG);
        
        listener = new GrowListener(this, wgPlugin, custPlugin);
        
        getServer().getPluginManager().registerEvents(listener, this);
    }
    
    private WorldGuardPlugin getWGPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        
        if (plugin == null || ! (plugin instanceof WorldGuardPlugin))
        {
            return null;
        }
        
        return (WorldGuardPlugin) plugin;
    }
    
    private WGCustomFlagsPlugin getCustPlugin()
    {
        Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");
        
        if (plugin == null || ! (plugin instanceof WGCustomFlagsPlugin))
        {
            return null;
        }
        
        return (WGCustomFlagsPlugin) plugin;
    }
}
