package markisha.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

public class LeashEntities implements Listener {

	public static Map<Player, List<Entity>> leashedEntities;
	
	public LeashEntities() {
		leashedEntities = new HashMap<Player, List<Entity>>();
	}
	
	@EventHandler
	public void whenLeashed(PlayerLeashEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getEntity();
		
		if (leashedEntities.containsKey(p)) {
			if (leashedEntities.get(p) != null) {
				leashedEntities.get(p).add(e);
			} else {
				List<Entity> entities = new ArrayList<Entity>();
				entities.add(e);
				leashedEntities.put(p, entities);
			}
		} else {
			List<Entity> entities = new ArrayList<Entity>();
			entities.add(e);
			leashedEntities.put(p, entities);
		}
	}
	
	@EventHandler
	public void whenUnleashed(PlayerUnleashEntityEvent event) {
		Player p = event.getPlayer();
		Entity e = event.getEntity();
		
		if (leashedEntities.containsKey(p)) {
			if (leashedEntities.get(p) != null) {
				leashedEntities.get(p).remove(e);
			}
		}
	}
	
}
