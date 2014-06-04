package com.markehme.factionsalias.support;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;

/**
 * Factions 2.x Support
 *     Has support for /f help
 *     
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class Factions2X implements SupportBase {
	List<Factions2XCommandSkeleton> commands = new ArrayList<Factions2XCommandSkeleton>();
	
	public void add(List<String> aliases,
			Boolean requiresFactionsEnabled,
			Boolean requiresIsPlayer,
			Boolean requiresInFaction,
			String permission,
			String permissionDeniedMessage,
			String desc,
			String executingCommand) {
		
		
		Factions.get().getOuterCmdFactions().addSubCommand(
			(FCommand) new Factions2XCommandSkeleton(
				aliases,
				requiresFactionsEnabled,
				requiresIsPlayer,
				requiresInFaction,
				permission,
				permissionDeniedMessage,
				desc,
				executingCommand
			)
		);			
	}
}
