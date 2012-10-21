/*
 * Copyright (C) 2012 mewin <mewin001@hotmail.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mewin.WGTreeGrowFlags;

import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author mewin <mewin001@hotmail.de>
 */
public class WGTreeGrowFlagsPlugin extends JavaPlugin {
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
