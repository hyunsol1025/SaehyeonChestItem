package saehyeon.chestitem.item;

import saehyeon.chestitem.ErrorType;
import saehyeon.chestitem.main.PlayerGlobal;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.main.YML;
import saehyeon.chestitem.region.Region;

import java.util.*;

public class Chest {

    public static ErrorType spreadItem(String regionName, boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {
        if(YML.regionYML.contains(regionName+".items")) {

            ArrayList<ItemStack> items = (ArrayList<ItemStack>) ((ArrayList<ItemStack>) YML.regionYML.get(regionName+".items")).clone();

            spreadItem(items,regionName,clearAllChest,ignoreCantOpenChest,ignoreTrappedChest,enableNotify);

            return null;

        } else {

            if(!YML.regionYML.contains(regionName)) {
                return ErrorType.NOT_EXIST_REGION;
            }

            return ErrorType.NOT_ITEMS;
        }
    }

    /**
     * <b>spreadItem</b><br>items 안에 있는 아이템들을 숨깁니다.
     * @param items
     * @param regionName
     * @param ignoreCantOpenChest
     * @param ignoreTrappedChest
     * @return
     */
    public static ErrorType spreadItem(ArrayList<ItemStack> items, String regionName, boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {
        //Bukkit.broadcastMessage("spreadItem 메소드가 호출되었어요!");

        if (YML.regionYML.contains(regionName)) {
            List<Location> locations = Region.getChests(regionName, ignoreCantOpenChest, ignoreTrappedChest);

            items.removeAll(Collections.singleton(null));

            // 숨길 수 있는 상자의 좌표들 갯수보다 아이템의 갯수가 더 많음
            if (items.size() > locations.size()) {
                return ErrorType.NOT_CHEST_ENOUGH;
            }

            // 아이템을 숨기기전 모든 상자를 숨기도록 설정함
            if (clearAllChest) {

                for (Location loc : locations) {

                    org.bukkit.block.Chest chest = (org.bukkit.block.Chest) loc.getBlock().getState();
                    chest.getBlockInventory().clear();

                }
            }

            Collections.shuffle(items);
            Collections.shuffle(locations);

            for (int i = 0; i < items.size(); i++) {
                org.bukkit.block.Chest chest = (org.bukkit.block.Chest) locations.get(i).getBlock().getState();
                chest.getBlockInventory().addItem(items.get(i));

                //Bukkit.broadcastMessage("여기에 숨김! : "+locations.get(i).getX()+", "+locations.get(i).getY()+", "+locations.get(i).getZ());
            }

            if (enableNotify)
                Bukkit.broadcastMessage(ChatColor.GRAY + regionName + ChatColor.WHITE + " 지역에 아이템을 모두 숨겼습니다.");

            return null;

        } else {
            return ErrorType.NOT_EXIST_REGION;
        }



    }
}
