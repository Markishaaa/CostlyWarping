package markisha.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class WarpTome {

	private ItemStack warpTome;

	public WarpTome() {
		this.warpTome = createItem();
	}
	
	public void init() {
		createRecipe();
	}

	private ItemStack createItem() {
		ItemStack warpTome = new ItemStack(Material.ENCHANTED_BOOK, 1);
		
		ItemMeta meta = warpTome.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		lore.add("Required for creating warps");
		
		meta.setDisplayName("Warp Tome");
		meta.setLore(lore);
		
		warpTome.setItemMeta(meta);
		
		return warpTome;
	}

	private void createRecipe() {
		ShapedRecipe sr = new ShapedRecipe(NamespacedKey.minecraft("warp_tome"), warpTome);
		sr.shape("rer", "rbr", "rdr");
		sr.setIngredient('r', Material.REDSTONE);
		sr.setIngredient('e', Material.ENDER_PEARL);
		sr.setIngredient('d', Material.DIAMOND);
		sr.setIngredient('b', Material.BOOK);

		Bukkit.getServer().addRecipe(sr);
	}

	public ItemStack getWarpTome() {
		return warpTome;
	}

}
