package markisha.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

		if (plugin.getConfig().getConfigurationSection("warps") == null) {
			this.plugin.getConfig().set("warps.ph", "ph");
			this.plugin.saveConfig();
			this.plugin.getConfig().set("warps.ph", null);
			this.plugin.saveConfig();
		}
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
				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")) {
					return createWarp(p, warpTome, args);
				} else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
					return deleteWarp(p, args);
				} else if (args[0].equalsIgnoreCase("list")) {
					return list(p, args);
				} else if (args[0].equalsIgnoreCase("listPlayer") || args[0].equalsIgnoreCase("playerList")) {
					return warpsOfPlayer(p, args);
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
			String name = args[1].toLowerCase();

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
		String name = args[1].toLowerCase();

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
		String name = args[0].toLowerCase();

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

	private boolean warpsOfPlayer(Player p, String[] args) {
		if (args.length >= 2) {
			String nick = args[1];
			int page;

			try {

				if (args.length == 2)
					page = 1;
				else
					page = Integer.parseInt(args[2]);

			} catch (NumberFormatException nfe) {
				p.sendMessage(ChatColor.YELLOW + "You must write a page NUMBER.");
				return false;
			}

			if (page < 0) {
				p.sendMessage(ChatColor.YELLOW + "Maybe write a positive page number?");
				return false;
			}

			Set<String> warpSet = warps.getKeys(false);
			List<String> warpList = new ArrayList<>();
			
			for (String warpName : warpSet) {
				if (warps.getString(warpName + ".player").equalsIgnoreCase(nick)) {
					warpList.add(warpName);
				}
			}

			if (!warpList.isEmpty()) {
				Collections.sort(warpList);

				int startPos = 0;
				int endPos = 9;
				for (int i = 1; i < page; i++) {
					startPos += 10;
					endPos += 10;

					if (startPos > warpList.size())
						break;
				}

				int i = startPos;
				if (warpList.size() - 1 >= i) {
					p.sendMessage(ChatColor.DARK_GREEN + "List of warps by " + nick + " (page " + page + "):");

					while (warpList.size() - 1 >= i && i <= endPos) {
						p.sendMessage(ChatColor.GREEN + warpList.get(i));

						i++;
					}
				} else {
					p.sendMessage(ChatColor.YELLOW + "No warps by player " + nick + " to show on page " + page + ".");
					return false;
				}
			} else {
				p.sendMessage(ChatColor.YELLOW + "No warps by " + nick + " to show.");
				return false;
			}
		} else {
			p.sendMessage(ChatColor.YELLOW + "You must write a nickname.");
			return false;
		}

		return true;
	}

	private boolean list(Player p, String[] args) {
		if (warps.getKeys(false).isEmpty()) {
			p.sendMessage(ChatColor.YELLOW + "No warps to show.");
			return false;
		}

		if (args.length <= 2) {
			try {
				int page;

				if (args.length == 1)
					page = 1;
				else
					page = Integer.parseInt(args[1]);

				if (page < 0) {
					p.sendMessage(ChatColor.YELLOW + "Maybe write a positive page number?");
					return false;
				}

				Set<String> warpSet = warps.getKeys(false);

				if (!warpSet.isEmpty()) {
					List<String> warpList = new ArrayList<>(warpSet);
					Collections.sort(warpList);

					int startPos = 0;
					int endPos = 9;
					for (int i = 1; i < page; i++) {
						startPos += 10;
						endPos += 10;

						if (startPos > warpList.size())
							break;
					}

					int i = startPos;
					if (warpList.size() - 1 >= i) {
						p.sendMessage(ChatColor.DARK_GREEN + "List of warps (page " + page + "):");

						while (warpList.size() - 1 >= i && i <= endPos) {
							p.sendMessage(ChatColor.GREEN + warpList.get(i));

							i++;
						}
					} else {
						p.sendMessage(ChatColor.YELLOW + "No warps to show on page " + page + ".");
						return false;
					}
				} else {
					p.sendMessage(ChatColor.YELLOW + "No warps to show.");
					return false;
				}

			} catch (NumberFormatException nfe) {
				p.sendMessage(ChatColor.YELLOW + "You must write a page NUMBER.");
				return false;
			}
		}

		return true;
	}

}
