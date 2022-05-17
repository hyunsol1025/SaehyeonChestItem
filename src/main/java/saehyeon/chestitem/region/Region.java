package saehyeon.chestitem.region;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.lib.Blockf;
import saehyeon.chestitem.lib.Locationf;
import saehyeon.chestitem.main.YML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Region {

    String regionName;
    Location pos1;
    Location pos2;
    List<ItemStack> items;

    public Region(String regionName, Location pos1, Location pos2, List<ItemStack> items) {
        this.regionName = regionName;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.items = items;
    }

    public String getName() {
        return regionName;
    }

    public Location[] getPosition() {
        return new Location[] { YML.regionYML.getLocation(regionName+"pos1"), YML.regionYML.getLocation(regionName+"pos2") };
    }

    public List<ItemStack> getItems() {
        return (List<ItemStack>)YML.regionYML.get(regionName+".items");
    }

    public void create() {
        YML.regionYML.set(regionName+".pos1",pos1);
        YML.regionYML.set(regionName+".pos2",pos2);
        YML.regionYML.set(regionName+".items",items);
    }

    public void setItems(List<ItemStack> items) {
        YML.regionYML.set(regionName+".items",items);
    }

    public boolean hasItem() {
        List<ItemStack> items = getItems();
        return items != null && items.size() != 0;
    }

    public static Region getByName(String regionName) {
        if(contains(regionName)) {

            return new Region(
                    regionName,
                    YML.regionYML.getLocation(regionName+".pos1"),
                    YML.regionYML.getLocation(regionName+".pos2"),
                    (List<ItemStack>) YML.regionYML.get(regionName+".items")
            );

        }

        return null;
    }

    public static boolean contains(String regionName) {
        return YML.regionYML.contains(regionName);
    }

    public static void set(String regionName, Location loc1, Location loc2, boolean ignoreIfRegionSet) {

        // 만약 지역이 포함되어 있고 포함된 지역이 포함되어 있다면 무시한다는 것이 아니라면
        if( !(contains(regionName) && ignoreIfRegionSet) ) {

            // 지역 범위 지점1, 지점2 설정
            YML.regionYML.set(regionName+".pos1", loc1);
            YML.regionYML.set(regionName+".pos2", loc2);

        }

    }

    public static void remove(String regionName) {
        YML.regionYML.set(regionName,null);
    }

    public static ArrayList<Location> getChests(String regionName, boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {
        Location loc1 = (Location)YML.regionYML.get(regionName+".pos1");
        Location loc2 = (Location)YML.regionYML.get(regionName+".pos2");

        ArrayList<Location> locations = new ArrayList<>();

        for(Location loc : Locationf.AllLocationWithin(loc1, loc2)) {

            // 만약 블럭이 상자 또는 덫상자라면
            if( loc.getBlock().getType() == Material.CHEST || loc.getBlock().getType() == Material.TRAPPED_CHEST) {

                // 만약 못여는 상자는 무시하는데 현재 상자를 열지 못하는 것이 아니라면
                if(!(ignoreCantOpenChest && Blockf.isCantOpenChest(loc))) {

                    // 만약 덫상자를 무시하도록 되어 있고 블럭이 덫상자인 것이 아니라면
                    if(!(ignoreTrappedChest && loc.getBlock().getType() == Material.TRAPPED_CHEST) ) {

                        locations.add(loc);
                    }
                }
            }
        }

        return locations;
    }

    public static HashMap<Location, List<ItemStack>> getChestContents(String regionName, boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {

        List<Location> locations = getChests(regionName,ignoreCantOpenChest,ignoreTrappedChest);
        HashMap<Location, List<ItemStack>> map = new HashMap<>();

        for(Location loc : locations) {
            Chest c = (Chest)loc.getBlock().getState();
            map.put(loc, Arrays.asList(c.getBlockInventory().getContents()));
        }

        return map;
    }

    public static ArrayList<Region> getAll() {
        ArrayList<Region> result = new ArrayList<>();

        for(String s : YML.regionYML.getKeys(false)) {
            result.add(getByName(s));
        }

        return result;
    }
}
