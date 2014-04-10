package me.dan14941.companies;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Companies extends JavaPlugin implements Listener
{	
	public static Economy economy = null;
	Logger logger = Bukkit.getLogger();
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }
	
	MyConfigManager manager;
	MyConfig playerData;
	
	@Override
	public void onEnable()
	{
		setupEconomy(); //setup economy
		
		String name = "[Companies]";
		
		logger.info(name + " loading...");
		logger.info(name + " Creating configs!"); 
		manager = new MyConfigManager(this);
		playerData = manager.getNewConfig("player-data.yml", new String[] {"Do Not Edit This File!"});
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		this.getCommand("company").setExecutor(new CompanyCommand(this));
		if (!setupEconomy()) 
		{
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found! (You may not have an economy plugin instlled!)", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		else
		{
			Bukkit.getServer().getPluginManager().registerEvents(this, this);
			logger.info("[Companies] plugin loaded!");
		}
		//Updater updater = new Updater(this, id, this.getFile(), Updater.UpdateType.DEFAULT, false);
	}
	
	@Override
	public void onDisable()
	{ 
		logger.info("Companies is stopping!");
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		
		if(getConfig().getString("join-message.enabled").equalsIgnoreCase("true"))
		{
			p.sendMessage(ChatColor.AQUA + getConfig().getString("join-message.message") + " " + p.getName());
		}
		
		if(playerData.get(p.getName() + ".in-town") == null)
		{
			playerData.set(p.getName() + ".in-town", false);
			playerData.saveConfig();
		}
		
	}
}
