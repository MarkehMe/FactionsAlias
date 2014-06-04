package com.markehme.factionsalias.support;

import java.util.List;

import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;

/**
 * Factions 1.x Support
 *     Does not have support for /f help (yet)
 *     
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class Factions1X implements SupportBase {
	public void add(List<String> aliases,
			Boolean requiresFactionsEnabled,
			Boolean requiresIsPlayer,
			Boolean requiresInFaction,
			String permission,
			String permissionDeniedMessage,
			String desc,
			String executingCommand) {
		
		P.p.cmdBase.addSubCommand(
			(FCommand) new Factions1XCommandSkeleton(
				aliases,
				requiresFactionsEnabled,
				requiresIsPlayer,
				requiresInFaction,
				permission,
				permissionDeniedMessage,
				desc,
			executingCommand)
		);
	}
}
