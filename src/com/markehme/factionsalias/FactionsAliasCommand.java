package com.markehme.factionsalias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionsAliasCommand   implements CommandExecutor {
	private FactionsAlias instance;
	
	public FactionsAliasCommand(FactionsAlias i) {
		this.instance = i;
	}
	 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("reload")) {
				if(sender instanceof Player) {
					if(!sender.isOp() && !sender.hasPermission("factionsalias.reload")) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
						return true;
					}
				}
				
				instance.reloadSubCommands();
				
				sender.sendMessage(ChatColor.GREEN + "FactionsAlias has been reloaded!");
				
				return true;
			} else if(args[0].equalsIgnoreCase("list")) {
				if(sender instanceof Player) {
					if(!sender.isOp() && !sender.hasPermission("factionsalias.list")) {
						sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.AQUA + " --- Registered Aliases --- ");
				
				for(String a : instance.getAliases()) {
					sender.sendMessage(ChatColor.DARK_AQUA + "- " + ChatColor.RED + a);
				}
				
				sender.sendMessage(ChatColor.AQUA + " -------------------------- ");
				
				return true;
			} else if(args[0].equalsIgnoreCase("help")) {
				showHelp(sender);
			} else {
				sender.sendMessage(ChatColor.RED + "Unknown subcommand");
			}
		} else {
			showHelp(sender);
		}
		
		return true;
	}
	
	private void showHelp(CommandSender sender) {
		if(sender instanceof Player) {
			if(!sender.isOp() && !sender.hasPermission("factionsalias.help")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
				return;
			}
		}
		
		sender.sendMessage(ChatColor.AQUA + 		" ----- FactionsAlias ----- ");
		sender.sendMessage(ChatColor.DARK_AQUA + 	"  /factionsalias help");
		sender.sendMessage(ChatColor.DARK_AQUA + 	"  /factionsalias list");
		sender.sendMessage(ChatColor.DARK_AQUA + 	"  /factionsalias reload");
		sender.sendMessage(ChatColor.AQUA + 		" ------------------------- ");
	}
}
