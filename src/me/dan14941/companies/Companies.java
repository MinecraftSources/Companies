package me.dan14941.companies;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	//MyConfigManager manager;
	//MyConfig playerData;
	//MyConfig cData;
	
	/*
	 * Custom configs
	 */
	
	File cData;
	FileConfiguration newCData;
	
	File pData;
	FileConfiguration newPData;
	
	@Override
	public void onEnable()
	{
		setupEconomy(); //setup economy
		
		String name = "[Companies]";
		
		logger.info(name + " loading...");
		logger.info(name + " Creating configs!"); 
		
		cData = new File(getDataFolder(), "company-data.yml");
		newCData = YamlConfiguration.loadConfiguration(cData);
		newCData.options().copyDefaults(true);
		saveCConfig();
		
		pData = new File(getDataFolder(), "player-data.yml");
		newPData = YamlConfiguration.loadConfiguration(pData);
		newPData.options().copyDefaults(true);
		savePConfig();
		
		logger.info(name + " Loading stats...");
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    logger.info(name + " Could not submit stats :(");
		}
		
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
	
	public void saveCData() 
	{
		try 
		{
			newCData.save(cData);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void savePConfig()
	{
		try
		{
			newPData.save(pData);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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
		
		if(newPData.getBoolean(p.getName() + "company.in-company", true))
		{
			logger.info("[Companies] Config file for " + p.getDisplayName() + " already exists!");
		}
		else if(newPData.getBoolean(p.getName() + "company.in-company", false))
		{
			logger.info("[Companies] Config file for " + p.getDisplayName() + " already exists!");
			newPData.addDefault(p.getName() + "company.in-company", false); 
			savePConfig();
		}
		else
		{
			newPData.addDefault(p.getName() + "company.in-company", false); 
			savePConfig();
		}
	}
}
