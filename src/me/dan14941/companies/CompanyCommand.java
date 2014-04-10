package me.dan14941.companies;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompanyCommand implements CommandExecutor
{
	//private final 
	Companies plugin;
	
	public CompanyCommand(Companies passedPlugin)
	{
		this.plugin = passedPlugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		String prefix = ChatColor.translateAlternateColorCodes('&', "&e[Companies] ");
		Player player = (Player) sender;
		//String syntax = prefix + ChatColor.RED + "Incorrect Syntax do /company help for commands!";

		if (args.length > 0) 
		{
			if(args[0].equalsIgnoreCase("help")) 
			{
				// creates help in chat
				player.sendMessage(ChatColor.AQUA + "+== Companies ==+");
				player.sendMessage(ChatColor.YELLOW + "/company list" + ChatColor.GOLD + " Shows companys");
				player.sendMessage(ChatColor.YELLOW + "/company <company name>" + ChatColor.GOLD + " Gets information on a company");
				
				if(player.hasPermission("companies.create")) 
				{
					player.sendMessage(ChatColor.YELLOW + "/company create <company name>" + ChatColor.GOLD + " Creates a company");
				}
				if (player.hasPermission("companies.config")) 
				{
					player.sendMessage(ChatColor.YELLOW + "/company config [edit|add] " + ChatColor.GOLD + "Command for edditing the config");
				}
				player.sendMessage(ChatColor.YELLOW + "/company leave" + ChatColor.GOLD + " Leave your company");
			}
			else if(args[0].equalsIgnoreCase("config")) 
			{
				if (player.hasPermission("companies.config")) 
				{
					if(args.length >= 0 && !(args.length > 1))
					{
						player.sendMessage(ChatColor.AQUA + "+== Companies ==+");
						player.sendMessage(ChatColor.YELLOW + "/company config [edit|add] " + ChatColor.GOLD + "Command for edditing the config");
					}
					else if(args.length > 1) 
					{
						if(args[1].equalsIgnoreCase("edit"))
						{ 
							if(args.length >= 1 && !(args.length > 2))
							{
								player.sendMessage(ChatColor.AQUA + "+== Companies ==+");
								player.sendMessage(ChatColor.RED + "This is only for edditing the config.yml!");
								player.sendMessage(ChatColor.YELLOW + "/company config edit <path> <value>");
							}
							else if(args.length >= 3)
							{
								StringBuilder str = new StringBuilder();
								for (int i = 3; i < args.length; i++)
								{
									str.append(args[i] + " ");
									str.deleteCharAt(str.length()-1);
								}
								String pathV = str.toString();
								
								plugin.getConfig().set(args[2], pathV);
								plugin.saveConfig();
								player.sendMessage(prefix + ChatColor.YELLOW + "You changed: " + ChatColor.AQUA + args[2] + ChatColor.YELLOW + " to: " + ChatColor.AQUA + pathV);
							}
						}
					}
				}
				else 
				{
					player.sendMessage(prefix + ChatColor.RED + "You do not have enough permission for this command!");
				}
			}
		}
		else 
		{
			player.sendMessage(prefix + ChatColor.RED + "Incorrect Syntax do /company help for commands!");
		}
		return true;
	}
}