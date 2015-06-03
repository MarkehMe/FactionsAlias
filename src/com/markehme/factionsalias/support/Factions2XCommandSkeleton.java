package com.markehme.factionsalias.support;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.FactionsCommand;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

/**
 * Command Skeleton for Factions 2.x 
 * 
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class Factions2XCommandSkeleton extends FactionsCommand {
	public String exec = "";
	public String permissionRequired = null;
	public String permissionDeniedMsg = "";
	
	public Boolean requirePlayerIsLeader = false;
	
	public boolean requiresFactionsEnabled = false;
	
	public Factions2XCommandSkeleton(
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
			this.addRequirements(ReqIsPlayer.get());
		}
		
		if(requiresInFaction) {
			this.addRequirements(ReqHasFaction.get());
		}
		
		if(requiresFactionsEnabled) {
			this.requiresFactionsEnabled = true;
		}
		
		if(requiresIsLeader) {
			this.requirePlayerIsLeader = true;
		}
		
		this.permissionRequired = permission;
		this.permissionDeniedMsg = permission;
		
		this.setHelp(desc);
		this.setDesc(desc);
		
		exec = executingCommand;
		
		this.errorOnToManyArgs = false;
	}
	
	@Override
	public void perform() {
		if(permissionRequired != null) {
			if(me instanceof Player) {
				if(!me.hasPermission(permissionRequired)) {
					msg(permissionDeniedMsg);
					return;
				}
			}
		}
		
		if(requiresFactionsEnabled) {
			if(Factions.get().isEnabled()) {
				me.sendMessage(ChatColor.RED + "Factions is not enabled, and you therefore can't use this command!");
				return;
			}
		}
		
		if(requirePlayerIsLeader) {
			if(!msender.getRole().equals(Rel.LEADER)) {
				msg(ChatColor.RED + "Only the leader of the faction can run this command.");
				return;
			}
		}
				
		Bukkit.getServer().dispatchCommand(sender, exec + " " + Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2"));

	}

}
