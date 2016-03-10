package com.markehme.factionsalias.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.markehme.factionsalias.FactionsAlias;
import com.markehme.factionsalias.libs.gson.Gson;
import com.markehme.factionsalias.libs.gson.GsonBuilder;

public class Alias {
	
	// ------------------------------------------
	//  Singleton
	// ------------------------------------------
	
	private static HashMap<String, Alias> instances = new HashMap<String, Alias>();
	public static Alias get(String key) {
		key = key.toLowerCase();
		
		if ( ! instances.containsKey(key)) {
			Gson gson = new Gson();

			try {
				File path = new File(FactionsAlias.get().getDataFolder(), "aliases");
				if ( ! path.exists()) path.mkdir();
				
				Alias aliasObject = null;
				
				File aliasFile = new File(path, key + ".json");
				
				if ( ! aliasFile.exists()) {
					aliasObject = new Alias(key);
				} else { 				
					BufferedReader br = new BufferedReader(new FileReader(aliasFile));
	
					aliasObject = gson.fromJson(br, Alias.class);
				}
				
				instances.put(key, aliasObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instances.get(key);
	}
	
	// ------------------------------------------
	//  Constructors
	// ------------------------------------------
	
	private Alias() { }
	
	private Alias(String key) { this.key = key; }
	
	// ------------------------------------------
	//  Values and defaults
	// ------------------------------------------
	
	private String key;
	private List<String> aliases = new ArrayList<String>();
	private String execute = "";
	private HashMap<String, Boolean> requirements = new HashMap<String, Boolean>();
	private String permission = "";
	private String permissionDeniedMessage = "";
	private String description = "";
	
	// ------------------------------------------
	//  Setters and Getters  
	// ------------------------------------------
	
	@Override
	public String toString() {
		return "Alias [key="+this.key+", aliases=" + this.aliases + ", execute=" + this.execute + ", requirements=" + this.requirements + ", "+
					  "permission=" + this.permission + ", permissionDeniedMessage=" + this.permissionDeniedMessage+", description=" + this.description + "]";
		
	}
	
	public String getKey() { return this.key; }
	
	public void setAliases(List<String> aliases) { this.aliases = aliases; }
	public List<String> getAliases() { return this.aliases; }
	
	public void setExecute(String execute) { this.execute = execute; }
	public String getExecute() { return this.execute; }
	
	public void setRequirement(String req, Boolean value) { this.requirements.put(req, value); }
	public HashMap<String, Boolean> getRequirements() { return this.requirements; }
	public Boolean getRequirement(String req) { return this.requirements.get(req); }
	
	public void setPermission(String perm) { this.permission = perm; }
	public String getPermission() { return this.permission; }
	
	public void setPermissionDeniedMessage(String msg) { this.permissionDeniedMessage = msg; }
	public String getPermissionDeniedMessage() { return this.permissionDeniedMessage; }
	
	public void setDescription(String desc) { this.description = desc; }
	public String getDescription() { return this.description; } 
	
	// ------------------------------------------
	//  Misc. Methods 
	// ------------------------------------------
	
	public void save() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String json = gson.toJson(this);
		
		try {
			File path = new File(FactionsAlias.get().getDataFolder(), "aliases");
			if ( ! path.exists()) path.mkdir();
			
			FileWriter writer = new FileWriter(new File(path, this.key + ".json"));
			writer.write(json);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}