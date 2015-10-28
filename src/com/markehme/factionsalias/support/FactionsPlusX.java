package com.markehme.factionsalias.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.command.FactionsCommandManager;

public class FactionsPlusX implements SupportBase {
	List<FactionsPlusXCommandSkeleton> commands = new ArrayList<FactionsPlusXCommandSkeleton>();
	
	public FactionsPlusX(HashMap<String, String> hashMap) {
		// Currently no settings are used. 
	}

	public void add(List<String> aliases,
			Boolean requiresFactionsEnabled,
			Boolean requiresIsPlayer,
			Boolean requiresInFaction,
			Boolean requiresIsLeader,
			String permission,
			String permissionDeniedMessage,
			String desc,
			String executingCommand) {
		
		FactionsPlusXCommandSkeleton command = new FactionsPlusXCommandSkeleton(
			aliases,
			requiresFactionsEnabled,
			requiresIsPlayer,
			requiresInFaction,
			requiresIsLeader,
			permission,
			permissionDeniedMessage,
			desc,
			executingCommand
		);
		
		commands.add(command);
		
		FactionsCommandManager.get().addCommand(command);
		
	}

	@Override
	public void unregister() {
		for (int i=0; i < commands.size(); i++) {
			FactionsCommandManager.get().removeCommand(commands.get(i));
		}
	}
	
	@Override
	public void finishCall() {
		// Nothing to do 
	}
}
