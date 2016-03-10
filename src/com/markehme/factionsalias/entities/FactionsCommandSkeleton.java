package com.markehme.factionsalias.entities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;

public class FactionsCommandSkeleton extends FactionsCommand {
	public String exec = "";
	public String permissionRequired = null;
	public String permissionDeniedMsg = "";
	
	public Boolean requirePlayerIsLeader = false;
	
	public boolean requiresFactionsEnabled = false;
	
	public boolean requiresIsPlayer = false;
	
	public FactionsCommandSkeleton(
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
		
		if(requiresIsPlayer) this.addRequirement(ReqIsPlayer.get());
		
		if(requiresInFaction) this.addRequirement(ReqHasFaction.get());
		
		
		if(requiresFactionsEnabled) {
			this.requiresFactionsEnabled = true;
		}
		
		if(requiresIsLeader) {
			this.requirePlayerIsLeader = true;
		}
		
		this.permissionRequired = permission;
		this.permissionDeniedMsg = permission;
		this.description = desc;
		this.helpLine = desc;
		
		exec = executingCommand;
		
		// don't error on too many args 
		this.errorOnTooManyArgs = false;
		
	}

	@Override
	public void run() {
		if (this.requiresIsPlayer && ! (sender instanceof Player)) {
			msg("This is a player-only command.");
			return;
		}
		
		if (permissionRequired != null) {
			if ( ! sender.hasPermission(permissionRequired)) {
				msg(permissionDeniedMsg);
				return;
			}
		}
				
		if (requirePlayerIsLeader && ! fplayer.isLeader()) {
			msg(ChatColor.RED + "Only the leader of the faction can run this command.");
			return;
		}
		
		String args = "";
		if (this.arguments.size() > 0) args = this.getArgsConcated(0);
		
		Bukkit.getServer().dispatchCommand(sender, exec + " " + args.replaceAll("(&([a-f0-9]))", "& $2"));
		
	}
}
