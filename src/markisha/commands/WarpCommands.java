package markisha.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import markisha.items.WarpTome;
import markisha.warp.Main;

public class WarpCommands implements CommandExecutor {

	private Main plugin;
	private ConfigurationSection warps;

	public WarpCommands(Main plugin) {
		this.plugin = plugin;
		this.warps = plugin.getConfig().getConfigurationSection("warps");
		plugin.getCommand("warp").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("warp")) {
			WarpTome wt = new WarpTome();
			ItemStack warpTome = wt.getWarpTome();

			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("create")) {
					return createWarp(p, warpTome, args);
				} else if (args[0].equalsIgnoreCase("delete")) {
					return deleteWarp(p, args);
				} else if (args[0].equalsIgnoreCase("list")) {
					return list(p, args);
				} else {
					return warp(p, args);
				}
			}
		}

		return true;
	}

	private boolean createWarp(Player p, ItemStack warpTome, String[] args) {
		if (args.length < 2) {
			p.sendMessage(ChatColor.YELLOW + "Provide a warp name.");
			return false;
		}

		if (p.getInventory().containsAtLeast(warpTome, 1)) {
			String name = args[1];

			if (warps.get(name) != null) {
				p.sendMessage(ChatColor.YELLOW + "A warp with that name already exists.");
				return false;
			}

			Location l = p.getLocation();

			warps.set(name + ".world", l.getWorld().getName());
			warps.set(name + ".x", l.getX());
			warps.set(name + ".y", l.getY());
			warps.set(name + ".z", l.getZ());
			warps.set(name + ".pitch", l.getPitch());
			warps.set(name + ".yaw", l.getYaw());
			warps.set(name + ".player", p.getDisplayName());
			plugin.saveConfig();

			p.getInventory().removeItem(warpTome);

			p.sendMessage(ChatColor.GREEN + "Warp set!");
			return true;
		}

		p.sendMessage(ChatColor.YELLOW + "You need a warp tome to create a new warp.");
		return false;
	}

	private boolean warpExists(String name, Player p) {
		if (warps.get(name) == null) {
			p.sendMessage(ChatColor.YELLOW + "A warp with that name doesn't exist.");
			return false;
		}

		return true;
	}

	private boolean deleteWarp(Player p, String[] args) {
		String name = args[1];

		if (args.length < 2) {
			p.sendMessage(ChatColor.YELLOW + "Provide a warp name.");
			return false;
		}

		if (!warpExists(name, p))
			return false;

		if (!p.getDisplayName().equalsIgnoreCase(warps.getString(name + ".player"))) {
			p.sendMessage(ChatColor.RED + "You do not have permission to delete this warp. "
					+ "You can delete only the warps you've created");
			return false;
		}

		warps.set(name, null);
		plugin.saveConfig();

		p.sendMessage(ChatColor.GREEN + "Warp " + name + " successfully deleted.");
		return true;
	}

	private boolean warp(Player p, String[] args) {
		String name = args[0];

		if (!warpExists(name, p))
			return false;

		double x = warps.getDouble(name + ".x");
		double y = warps.getDouble(name + ".y");
		double z = warps.getDouble(name + ".z");
		float yaw = (float) warps.getDouble(name + ".yaw");
		float pitch = (float) warps.getDouble(name + ".pitch");
		String world = warps.getString(name + ".world");
		Location loc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

		p.teleport(loc);
		p.sendMessage(ChatColor.GREEN + "Teleported to " + name + ".");
		return true;
	}

	private boolean list(Player p, String[] args) {
		if (warps.getKeys(false).isEmpty()) {
			p.sendMessage(ChatColor.YELLOW + "No warps to show.");
			return false;
		}

		if (args.length >= 2) {
			String nick = args[1];

			p.sendMessage(ChatColor.DARK_GREEN + "List of warps created by " + nick + ":");

			int counter = 0;
			for (String name : warps.getKeys(false)) {
				if (warps.getString(name + ".player").equalsIgnoreCase(nick)) {
					p.sendMessage(ChatColor.GREEN + name);
					counter++;
				}
			}

			if (counter == 0) {
				p.sendMessage(ChatColor.YELLOW + "No warps were created by " + nick + ".");
			}
		} else {
			p.sendMessage(ChatColor.DARK_GREEN + "List of all warps:");
			for (String name : warps.getKeys(false)) {
				p.sendMessage(ChatColor.GREEN + name);
			}
		}

		return true;
	}

}
