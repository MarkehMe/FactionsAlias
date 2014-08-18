package com.markehme.factionsalias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.markehme.factionsalias.support.Factions1X;
import com.markehme.factionsalias.support.Factions2X;
import com.markehme.factionsalias.support.SupportBase;

/**
 * Troubles compiling?
 * Ensure when you reference Factions jars you reference MCore
 * and Factions 2.x FIRST. Refresh project. Then reference the
 * latest Factions 1.6. You will probably still get errors so
 * it is sometimes better to work on either Factions 1X or 2X
 * separately. 
 * 
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class FactionsAlias extends JavaPlugin {
	
	// Our Factions 1X and 2X support base
	private SupportBase supportBase = null;
	
	// Alias list
	private List<String> aliasList = new ArrayList<String>();
	
	private FileConfiguration config;

	@Override
	public void onEnable() {
		aliasList.clear();
		
		getCommand("factionsalias").setExecutor(new FactionsAliasCommand(this));
		
		Boolean isFactions2X = true;
		String isFactions16 = "N"; // only because 1.6.x does help different to 1.7 and 1.8
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			isFactions2X = false;
		}
		
		if(isFactions2X) { 
			supportBase = new Factions2X(new HashMap<String, String>(null));
			log("Detected Factions 2.x");
		} else {
			Plugin plugin = Bukkit.getPluginManager().getPlugin("Factions");
			
			if(plugin.getDescription().getVersion().startsWith("1.6")) {
				isFactions16 = "Y";
			}
			
			supportBase = new Factions1X(Util.settings("1.6", isFactions16));
			log("Detected Factions 1.x");
		}
		
		saveDefaultConfig();
		
		config = getConfig();
		
		registerSubCommands();
	}
	
	@Override
	public void onDisable() {
		// Unregister our sub commands
		unregisterSubCommands();
		
		// Remove the support base 
		supportBase = null;
	}
	
	public void reloadSubCommands() {
		reloadConfig();
		
		config = getConfig();
		
		unregisterSubCommands();
		registerSubCommands();
	}
	
	public void unregisterSubCommands() {		
		supportBase.unregister();
		
		aliasList.clear();
	}
	
	public void registerSubCommands() {
		
		int i = 0;
		
		for(String aliasSection : config.getConfigurationSection("aliases").getKeys(false)) {
			i++;
			
			// ensure any new features are added
			ensureSectionIsUpToDate(aliasSection);
			
			List<String> a = new ArrayList<String>();
			a.clear();
			
			for(Object badfb : config.getList("aliases."+aliasSection+".aliases").toArray()) {
				a.add(badfb.toString());
				aliasList.add(badfb.toString());
			}
			
			supportBase.add(
				a,
				config.getBoolean("aliases."+aliasSection+".requires.factionsEnabledInWorld"),
				config.getBoolean("aliases."+aliasSection+".requires.executerIsPlayer"),
				config.getBoolean("aliases."+aliasSection+".requires.executerIsInFaction"),
				config.getBoolean("aliases."+aliasSection+".requires.executerIsLeader"),
				config.getString("aliases."+aliasSection+".permission"), 
				config.getString("aliases."+aliasSection+".permissionDeniedMessage"),
				config.getString("aliases."+aliasSection+".description"),
				config.getString("aliases."+aliasSection+".execute")
			);
		}
		
		supportBase.finishCall();
		
		log("Loaded "+i+" subcommands!");
	}
	
	public List<String> getAliases() {
		return aliasList;
	}
	
	public void ensureSectionIsUpToDate(String section) {
		if(!config.contains("aliases."+section+".requires.executerIsLeader")) config.set("aliases."+section+".requires.executerIsLeader", false);
	}
	
	public void log(String str) {
		getLogger().log(Level.INFO, str);
	}
}
