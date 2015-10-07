package com.markehme.factionsalias.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;

/**
 * Factions 1.x Support
 *     
 * @author MarkehMe<mark@markeh.me>
 *
 */
public class Factions1X implements SupportBase {
	List<Factions1XCommandSkeleton> commands = new ArrayList<Factions1XCommandSkeleton>();
	List<String> helpLines = new ArrayList<String>();
	
	
	public Factions1X(HashMap<String, String> settings) {
		// No settings required for Factions UUID as we've dropped support for 1.7
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
		
		Factions1XCommandSkeleton command = new Factions1XCommandSkeleton(
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
		
		P.p.cmdBase.addSubCommand(
			(FCommand) command
		);
		
		helpLines.add(command.getUseageTemplate(true));
		
	}
	
	@Override
	public void unregister() {
		for (int i=0; i < commands.size(); i++) {
			P.p.cmdBase.subCommands.remove(commands.get(i));
		}
	}
	
	@Override
	public void finishCall() {
		P.p.cmdBase.cmdHelp.updateHelp();
					
		// Ensure there are help lines to be added
		if(helpLines.size() > 0 ) {
				
			ArrayList<String> pageLines = new ArrayList<String>();
				
			int i = 0;
				
			for(String line : helpLines) {
				if(i >= 7) {
					i = 0; // reset the count 
					P.p.cmdBase.cmdHelp.helpPages.add(pageLines); // add our page
					pageLines.clear(); // clear the current lines
				}
					
				i++;
				pageLines.add(line); // add a line 
			}
				
			// Add any leftover lines that haven't made a full page
			if(i > 0) {
				P.p.cmdBase.cmdHelp.helpPages.add(pageLines);
				pageLines.clear();
			}
		}
	}
}
