package com.markehme.factionsalias.support;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


import me.markeh.factionsframework.command.FactionsCommand;

public class FactionsPlusXCommandSkeleton extends FactionsCommand {
	public String exec = "";
	public String permissionRequired = null;
	public String permissionDeniedMsg = "";
	
	public Boolean requirePlayerIsLeader = false;
	
	public boolean requiresFactionsEnabled = false;
	
	public boolean requiresIsPlayer = false;
	
	public FactionsPlusXCommandSkeleton(
			List<String> aliases,
			boolean requiresFactionsEnabled,
			boolean requiresIsPlayer,
			boolean requiresInFaction,
			boolean requiresIsLeader,
			String permission,
			String permissionDeniedMessage,
			String desc,
			String executingCommand
	) {
		
		// Move through all the aliases and add them for this command
		for(Object alias : aliases) {
			this.aliases.add((String) alias);
		}
		
		if(requiresIsPlayer) {
			this.requiresIsPlayer = true;
		}
		
		if(requiresInFaction) {
			this.mustHaveFaction = true;
		}
		
		if(requiresFactionsEnabled) {
			this.requiresFactionsEnabled = true;
		}
		
		if(requiresIsLeader) {
			this.requirePlayerIsLeader = true;
		}
		
		this.permissionRequired = permission;
		this.permissionDeniedMsg = permission;
		this.description = desc;
		
		exec = executingCommand;
		
		// don't error on too many args 
		this.errorOnTooManyArgs = false;
		
	}

	@Override
	public void run() {
		if (this.requiresIsPlayer && player == null) {
			msg("This is a player-only command.");
			return;
		}
		
		if(permissionRequired != null && player != null) {
			if( ! player.hasPermission(permissionRequired)) {
				msg(permissionDeniedMsg);
				return;
			}
		}
				
		if(requirePlayerIsLeader && !fplayer.isLeader()) {
			msg(ChatColor.RED + "Only the leader of the faction can run this command.");
			return;
		}
		
		String argString = "";
		for(String arg : this.arguments) {
			argString += arg + " ";
		}
		Bukkit.getServer().dispatchCommand((CommandSender) player, exec + " " + argString.replaceAll("(&([a-f0-9]))", "& $2"));
		
	}
}
