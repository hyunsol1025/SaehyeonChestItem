package saehyeon.chestitem.lib;

import org.bukkit.Location;
import org.bukkit.Material;

public class Blockf {
    public static boolean isCantOpenChest(Location chestLocation) {
        Material mat = chestLocation.clone().add(0,1,0).getBlock().getType();
        return !(mat.toString().contains("TORCH") || mat.toString().contains("AIR") || mat.toString().contains("GLASS") || mat.toString().contains("STAIR"));
    }
}

