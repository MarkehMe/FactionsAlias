package com.markehme.factionsalias.support;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.util.TextUtil;

/**
 * Command Skeleton for Factions 1.x 
 * 
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class Factions1XCommandSkeleton extends FCommand {
	public String exec = "";
	public String permissionRequired = null;
	public String permissionDeniedMsg = "";
	
	public Boolean requiresFactionsEnabled = false;
	public Boolean requiresInFaction = false;
	public Boolean requiresSenderPlayer = false;
	public Boolean requiresSenderLeader = false;
	
	public Factions1XCommandSkeleton(
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
		
		if(requiresFactionsEnabled) {
			requiresFactionsEnabled = true;
		}
		
		if(requiresIsPlayer) {
			this.requiresSenderPlayer = true;
		}
		
		if(requiresInFaction) {
			this.requiresInFaction = true;
		}
		
		if(requiresIsLeader) {
			this.requiresSenderLeader = true;
		}
		
		this.permissionRequired = permission;
		this.permissionDeniedMsg = permission;
		
		// TODO: add support for /f help
		
		exec = executingCommand;
		
		this.errorOnToManyArgs = false;
	}
	
	public final void perform() {
		
		if(requiresFactionsEnabled) { 
			if(!P.p.isEnabled()) {
				me.sendMessage(ChatColor.RED + "Factions is not enabled, and you therefore can't use this command!");
				return;
			}
		}
		
		if(requiresInFaction) { 
			if(!FPlayers.getInstance().getByPlayer(me).hasFaction()) {
				me.sendMessage(ChatColor.RED + "You need a Faction to run this command.");
				return;
			}
		}
		
		if(requiresSenderPlayer) {
			if(!(sender instanceof Player)) {
				me.sendMessage(ChatColor.RED + "Only a player can run this command.");
				return;
			}
		}
		
		if(requiresSenderLeader) {
			if(!this.fme.getRole().equals(Role.ADMIN)) {
				me.sendMessage(ChatColor.RED + "Only the leader of the faction can run this command.");
				return;
			}
		}
		
		if(this.permissionRequired != null) {
			if(me instanceof Player) {
				if(!me.hasPermission(permissionRequired)) {
					msg(permissionDeniedMsg);
					return;
				}
			}
		}
		
		Bukkit.getServer().dispatchCommand(sender, exec + " " + TextUtil.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2"));
		
	}

	@Override
	public TL getUsageTranslation() {
		return null;
	}
}
