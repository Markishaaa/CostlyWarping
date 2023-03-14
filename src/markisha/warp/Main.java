package markisha.warp;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import markisha.commands.WarpCommands;
import markisha.items.WarpTome;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		WarpTome wt = new WarpTome();
		wt.init();

		loadConfig();
		
		new WarpCommands(this);
		
//		getServer().getPluginManager().registerEvents(null, this);
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
