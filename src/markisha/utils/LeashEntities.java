package markisha.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LeashEntities {

	private boolean isLeashed(Entity entity) {
		return entity instanceof LivingEntity ? ((LivingEntity) entity).isLeashed() : false;
	}

	public List<Entity> getLeashedEntities(Player p) {
		List<Entity> leashedEntities = new ArrayList<Entity>();

		for (Entity e : p.getNearbyEntities(10, 10, 10)) {
			if (isLeashed(e)) {
				leashedEntities.add(e);
			}
		}

		return leashedEntities;
	}
	
}
