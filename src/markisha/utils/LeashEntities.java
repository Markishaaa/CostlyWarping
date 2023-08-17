package markisha.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class LeashEntities {

	private boolean isLeashedToPlayer(Entity entity, Player p) {
		if (!(entity instanceof LivingEntity))
			return false;

		LivingEntity le = (LivingEntity) entity;

		if (!le.isLeashed())
			return false;

		Entity leashHolder = le.getLeashHolder();

		return leashHolder instanceof Player && leashHolder.equals(p);
	}

	public List<Entity> getLeashedEntities(Player p) {
		List<Entity> leashedEntities = new ArrayList<Entity>();

		for (Entity e : p.getNearbyEntities(10, 10, 10)) {
			if (isLeashedToPlayer(e, p)) {
				leashedEntities.add(e);
			}
		}

		return leashedEntities;
	}

}
