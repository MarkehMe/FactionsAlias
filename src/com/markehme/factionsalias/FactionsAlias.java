package com.markehme.factionsalias;

import java.io.File;
import java.io.IOException;
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
	private HashMap<String, String> aliasMap = new HashMap<String, String>();
	
	private FileConfiguration config;

	@Override
	public void onEnable() {
		aliasList.clear();
		
		getCommand("factionsalias").setExecutor(new FactionsAliasCommand(this));
		
		Boolean isFactions2X = true;
		
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			isFactions2X = false;
		}
		
		if(isFactions2X) { 
			supportBase = new Factions2X(null);
			log("Detected Factions 2.x");
		} else {
			supportBase = new Factions1X(null);
			log("Detected Factions UUID 1.6");
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
		aliasMap.clear();
	}
	
	public void registerSubCommands() {
		
		int i = 0;
		
		for(String aliasSection : config.getConfigurationSection("aliases").getKeys(false)) {
			i++;
			
			// ensure any new features are added
			ensureSectionIsUpToDate(aliasSection);
			
			try {
				config.save(new File("FactionsAlias/config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<String> a = new ArrayList<String>();
			
			for(Object badfb : config.getList("aliases."+aliasSection+".aliases").toArray()) {
				a.add(badfb.toString());
				aliasList.add(badfb.toString());
				aliasMap.put(badfb.toString(), config.getString("aliases."+aliasSection+".execute"));
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
	
	public String getExecFor(String alias) {
		return this.aliasMap.get(alias);
	}
	
	public void ensureSectionIsUpToDate(String section) {
		if(!config.contains("aliases."+section+".requires.executerIsLeader")) config.set("aliases."+section+".requires.executerIsLeader", false);
	}
	
	public void log(String str) {
		getLogger().log(Level.INFO, str);
	}
}
