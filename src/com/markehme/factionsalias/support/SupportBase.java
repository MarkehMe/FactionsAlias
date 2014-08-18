package com.markehme.factionsalias.support;

import java.util.List;

/**
 * Just our Support Base. Nothing fancy. 
 *  
 * @author MarkehMe<mark@markeh.me>
 *
 */
public interface SupportBase {
		
	public void add(List<String> aliases,
			Boolean requiresFactionsEnabled,
			Boolean requiresIsPlayer,
			Boolean requiresInFaction,
			Boolean requiresIsLeader,
			String permission,
			String permissionDeniedMessage,
			String desc,
			String executingCommand);
	
	public void unregister();
	
	public void finishCall();
}
