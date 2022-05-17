package saehyeon.chestitem.api;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.ErrorType;
import saehyeon.chestitem.region.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChestItem {
    public static void set(String regionName, Location pos1, Location pos2, List<ItemStack> items) {
        Region r = new Region(regionName,pos1, pos2,items);
        r.create();
    }


    /**
     * <b>아이템을 숨깁니다.</b><br>items 안에 있는 아이템들을 숨깁니다.
     * @param items 숨길 아이템들입니다.
     * @param clearAllChest true라면 아이템을 숨기기 전에 모든 상자를 비울 것 입니다.
     * @param ignoreCantOpenChest true라면 열 수 없는 상자에는 숨기지 않습니다.
     * @param ignoreTrappedChest true라면 덫상자에는 숨기지 않습니다.
     * @param enableNotify true라면 상자에 아이템을 모두 숨긴 후 모두에게 메세지를 전송합니다.
     */
    public ErrorType spreadItem(String regionName, ArrayList<ItemStack> items, boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {

        Region r = Region.getByName(regionName);

        if (r != null) {

            r.spreadItem(clearAllChest,ignoreCantOpenChest,ignoreTrappedChest,enableNotify);
            return null;

        }

        return ErrorType.NOT_EXIST_REGION;
    }
}
