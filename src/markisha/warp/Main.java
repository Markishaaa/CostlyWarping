package markisha.warp;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import markisha.commands.WarpCommands;
import markisha.commands.WarpTabCompleter;
import markisha.items.WarpTome;

public class Main extends JavaPlugin {

	private ConfigurationSection warps;
	private WarpTabCompleter tabCompleter;
	
	public Main() {
		if (this.getConfig().getConfigurationSection("warps") == null) {
			this.getConfig().set("warps.ph", "ph");
			this.saveConfig();
			this.getConfig().set("warps.ph", null);
			this.saveConfig();
		}
		
		this.warps = this.getConfig().getConfigurationSection("warps");
		
		this.tabCompleter = new WarpTabCompleter(warps);
	}
	
	@Override
	public void onEnable() {
		WarpTome wt = new WarpTome();
		wt.init();

		loadConfig();
		
		new WarpCommands(this, warps, tabCompleter);
		
		this.getCommand("warp").setTabCompleter(tabCompleter);
		
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CostlyWarping]: Plugin enabled!");
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CostlyWarping]: Plugin disabled!");
	}
	
	private void loadConfig() {
        getConfig().options().copyDefaults(false);
        saveConfig();
    }
	
}
