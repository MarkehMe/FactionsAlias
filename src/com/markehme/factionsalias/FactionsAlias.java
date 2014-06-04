package com.markehme.factionsalias;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.markehme.factionsalias.support.Factions1X;
import com.markehme.factionsalias.support.Factions2X;
import com.markehme.factionsalias.support.SupportBase;

/**
 * Troubles compiling?
 * Ensure when you reference Factions jars you reference MCore
 * and Factions 2.x FIRST. Refresh proejct. Then reference the
 * latest Factions 1.6. 
 * 
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class FactionsAlias extends JavaPlugin {
	@Override
	public void onEnable() {
		Boolean isFactions2X = true;
		
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			isFactions2X = false;
		}
		
		SupportBase commander = null;
		
		if(isFactions2X) { 
			commander = new Factions2X();
		} else {
			commander = new Factions1X();
		}
		
		saveDefaultConfig();
		
		FileConfiguration config = getConfig();
		
		for(String aliasSection : config.getConfigurationSection("aliases").getKeys(false)) {
			List<String> a = new ArrayList<String>();
			
			for(Object badfb : config.getList("aliases."+aliasSection+".aliases").toArray()) {
				a.add((String) badfb);
			}
			commander.add(
					a,
					config.getBoolean("aliases."+aliasSection+".requires.factionsEnabledInWorld"),
					config.getBoolean("aliases."+aliasSection+".requires.executerIsPlayer"),
					config.getBoolean("aliases."+aliasSection+".requires.executerIsInFaction"),
					config.getString("aliases."+aliasSection+".permission"), 
					config.getString("aliases."+aliasSection+".permissionDeniedMessage"),
					config.getString("aliases."+aliasSection+".description"),
					config.getString("aliases."+aliasSection+".execute")
				);
			
		}
		
		
	}
}
