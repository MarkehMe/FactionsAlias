package com.markehme.factionsalias;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import me.markeh.factionsframework.command.FactionsCommandManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.markehme.factionsalias.entities.Alias;
import com.markehme.factionsalias.entities.FactionsCommandSkeleton;
import com.markehme.factionsalias.libs.gson.Gson;
import com.markehme.factionsalias.libs.gson.GsonBuilder;

public class FactionsAlias extends JavaPlugin {
	
	// Singleton
	private static FactionsAlias instance = null;
	public static FactionsAlias get() { return instance; }
		
	// Alias list
	private List<Alias> aliasList = new ArrayList<Alias>();
	
	private FileConfiguration config;

	public static Gson getGson() {
		return new GsonBuilder().create();
	}
	
	
	@Override
	public void onEnable() {
		instance = this;
				
		// set command executor 
		getCommand("factionsalias").setExecutor(new FactionsAliasCommand());
		
		if ( ! Bukkit.getPluginManager().isPluginEnabled("FactionsPlus")) {
			log("This plugin requires FactionsPlus!");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
				
		if ( ! this.getAliasFolder().exists()) {
			this.getAliasFolder().mkdirs();
			this.createExample();
			
			// only run this check if it exists otherwise we'll throw NPE 
			if (new File("FactionsAlias/config.yml").exists()) this.migrateConfigurationData();
			
		}
				
		saveDefaultConfig();
		
		config = getConfig();
		
		this.loadFromDisk();
	}
	
	private void createExample() {
		this.log("Creating example (powerexample)");
		Alias example = Alias.get("powerexample");
		
		example.setAliases(new ArrayList<String>() {	
			private static final long serialVersionUID = -5066784642852485982L; 
			{
				this.add("pow");
				this.add("power");
				this.add("powerexample");
				this.add("example");
			}
		});
		
		example.setDescription("An example of rewriting to the p sub command");
		example.setExecute("f p");
		example.setPermission("factions.power");
		example.setDescription("This is an example");
		example.setPermissionDeniedMessage(ChatColor.RED + "You dont have permission to do this!");
		example.setRequirement("FactionsEnabledInWorld", true);
		example.setRequirement("ExecuterIsPlayer", false);
		example.setRequirement("ExecuterIsInFaction", false);
		example.setRequirement("ExecuterIsLeader", false);
		
		example.save();
	}


	@Override
	public void onDisable() {
		// Unregister our sub commands
		unregisterSubCommands();
	}
	
	public void reloadSubCommands() {
		reloadConfig();
		
		config = getConfig();
		
		unregisterSubCommands();
	}
	
	public void unregisterSubCommands() {				
		aliasList.clear();
	}
	
	public void migrateConfigurationData() {
		
		int i = 0;
		
		config = getConfig();
		
		if (config.getConfigurationSection("aliases") == null) return;
		
		for(String aliasSection : config.getConfigurationSection("aliases").getKeys(false)) {
			i++;
			
			// Ensure any previous features are added from the older revisions 
			if( ! config.contains("aliases."+aliasSection+".requires.executerIsLeader")) config.set("aliases."+aliasSection+".requires.executerIsLeader", false);
			
			try {
				config.save(new File("FactionsAlias/config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			List<String> aliases = new ArrayList<String>();
			
			for(Object alias : config.getList("aliases."+aliasSection+".aliases").toArray()) {
				aliases.add(alias.toString());
			}
			
			Alias currentAlias = Alias.get(aliasSection);
			
			currentAlias.setAliases(aliases);
			currentAlias.setPermission(config.getString("aliases." + aliasSection + ".permission"));
			currentAlias.setDescription(config.getString("aliases." + aliasSection + ".description"));
			currentAlias.setExecute(config.getString("aliases." + aliasSection + ".execute"));
			currentAlias.setPermissionDeniedMessage(config.getString("aliases." + aliasSection + ".permissionDeniedMessage"));

			currentAlias.setRequirement("FactionsEnabledInWorld", config.getBoolean("aliases."+aliasSection+".requires.factionsEnabledInWorld"));
			currentAlias.setRequirement("ExecuterIsPlayer", config.getBoolean("aliases."+aliasSection+".requires.executerIsPlayer"));
			currentAlias.setRequirement("ExecuterIsInFaction", config.getBoolean("aliases."+aliasSection+".requires.executerIsInFaction"));
			currentAlias.setRequirement("ExecuterIsLeader", config.getBoolean("aliases."+aliasSection+".requires.executerIsLeader"));
			
		}
		
		if (i == 0) {
			if ( ! config.contains("enabled") || ! config.contains("metrics")) {
				config.set("enabled", true);
				config.set("metrics", true);
			}
		}
		
	}
	
	public List<Alias> getAliases() {
		return aliasList;
	}
		
	private File aliasFolder = new File(this.getDataFolder(), "aliases");
	public File getAliasFolder() { return this.aliasFolder; }
	
	public void log(String msg) {
		this.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "[FactionsAlias]" + ChatColor.RESET + " " + ChatColor.WHITE + msg);
	}
	
	private List<FactionsCommandSkeleton> commands = new ArrayList<FactionsCommandSkeleton>();
	
	public final void add(Alias alias) {
	
		log(alias.getKey() + ": " + alias.getDescription());
		
		FactionsCommandSkeleton command = new FactionsCommandSkeleton(
				alias.getAliases(),
				alias.getRequirement("FactionsEnabledInWorld"),
				alias.getRequirement("ExecuterIsPlayer"),
				alias.getRequirement("ExecuterIsInFaction"),
				alias.getRequirement("ExecuterIsLeader"),
				alias.getPermission(),
				alias.getPermissionDeniedMessage(),
				alias.getDescription(),
				alias.getExecute()
			);
			
			commands.add(command);
			
			FactionsCommandManager.get().addCommand(command);

	}

	public void unregister() {
		for (int i=0; i < commands.size(); i++) {
			FactionsCommandManager.get().removeCommand(commands.get(i));
		}
	}
	
	public void loadFromDisk() {
		this.unregister();
		
		try {
			Files.walk(Paths.get(FactionsAlias.get().getAliasFolder().getCanonicalPath())).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					if (filePath.getFileName().toString().contains(".json")) {
						String name = filePath.getFileName().toString().split(".json")[0];
						
						this.add(Alias.get(name));
					}
					System.out.println(filePath);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
