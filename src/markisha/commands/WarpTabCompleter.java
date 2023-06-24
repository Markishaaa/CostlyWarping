package markisha.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class WarpTabCompleter implements TabCompleter {

	private List<String> commandsList;
	private List<String> warpsList;

	private ConfigurationSection warps;

	public WarpTabCompleter(ConfigurationSection warps) {
		this.commandsList = Arrays.asList("create", "remove", "list", "listPlayer");
		this.warps = warps;

		updateTabComplete();
	}

	private List<String> findPlayerWarps(Player p) {
		List<String> playerWarp = new ArrayList<>();

		for (String w : warps.getKeys(false)) {
			if (p.getDisplayName().equalsIgnoreCase(warps.getString(w + ".player"))) {
				playerWarp.add(w);
			}
		}

		return playerWarp;
	}

	public void updateTabComplete() {
		warpsList = new ArrayList<>();

		for (String w : warps.getKeys(false)) {
			warpsList.add(w);
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return null;
		if (!cmd.getName().equalsIgnoreCase("warp"))
			return null;

		List<String> completions = new ArrayList<>();

		StringUtil.copyPartialMatches(args[0], warpsList, completions);
		
		if (args.length == 1 && args[0].equalsIgnoreCase("cmd")) {
			return commandsList;
		}
		
		if (args.length == 1) {
			return completions;
		} else if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove"))) {
			StringUtil.copyPartialMatches(args[1], findPlayerWarps((Player) sender), completions);
			
			return completions;
		}

		return null;
	}

}
