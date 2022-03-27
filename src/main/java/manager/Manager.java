package manager;

import main.Func;
import main.Main;
import main.PlayerGlobal;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Manager {
    public static boolean isPosVaild(Player p) {
        if(!PlayerGlobal.POS1.containsKey(p)) {
            p.sendMessage(ChatColor.RED+"첫번째 좌표가 설정되지 않았습니다.");
            return false;
        }

        if(!PlayerGlobal.POS2.containsKey(p)) {
            p.sendMessage(ChatColor.RED+"두번째 좌표가 설정되지 않았습니다.");
            return false;
        }

        return true;
    }

    public static void setRegion(Player p, String regionName, boolean ignoreIfRegionSet) {

        if( !(Main.regionYAML.contains(regionName) && ignoreIfRegionSet) ) {
            Main.regionYAML.set(regionName+".pos1", PlayerGlobal.POS1.get(p));
            Main.regionYAML.set(regionName+".pos2", PlayerGlobal.POS2.get(p));

            if(Main.regionYAML.contains(regionName)) {
                p.sendMessage("포함되어 있는 위치 "+ChatColor.GRAY+regionName+ChatColor.WHITE+"(을)를 재설정했습니다.");
            } else {
                p.sendMessage(ChatColor.GRAY+regionName+ChatColor.WHITE+"(을)를 등록했습니다.");
            }
        }
    }

    /**
     * <b>상자 좌표 반환 메소드</b><br>특정 지역의 모든 상자 좌표를 반환합니다.
     * @return
     */
    public static List<Location> getChestLocations(String regionName, boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {
        Location loc1 = (Location)Main.regionYAML.get(regionName+".pos1");
        Location loc2 = (Location)Main.regionYAML.get(regionName+".pos2");

        List<Location> locations = new ArrayList<>();

        for(Location loc : Func.AllLocationWithin(loc1, loc2)) {
            if( (loc.getBlock().getType() == Material.CHEST || loc.getBlock().getType() == Material.TRAPPED_CHEST) &&
                    !(ignoreCantOpenChest && loc.clone().add(0,1,0).getBlock().getType() != Material.AIR) &&
                    !(ignoreTrappedChest && loc.getBlock().getType() == Material.TRAPPED_CHEST) ) {
                locations.add(loc);
                //Bukkit.broadcastMessage("loc: "+loc+" / loc's type: "+loc.getBlock().getType());

            }
        }

        return locations;
    }

    public static ErrorType spreadItem(String regionName, boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {
        if(Main.regionYAML.contains(regionName+".items")) {

            ArrayList<ItemStack> items = (ArrayList<ItemStack>) ((ArrayList<ItemStack>) Main.regionYAML.get(regionName+".items")).clone();

            spreadItem(items,regionName,clearAllChest,ignoreCantOpenChest,ignoreTrappedChest,enableNotify);

            return null;

        } else {
            if(!Main.regionYAML.contains(regionName)) {
                return ErrorType.NOT_REG_CHEST;
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
    public static ErrorType spreadItem(List<ItemStack> items, String regionName, boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {
        //Bukkit.broadcastMessage("spreadItem 메소드가 호출되었어요!");

        if(!Main.regionYAML.contains(regionName)) {
            return ErrorType.NOT_REG_CHEST;
        }

        List<Location> locations = getChestLocations(regionName,ignoreCantOpenChest,ignoreTrappedChest);

        items.removeAll(Collections.singleton(null));

        if(items.size() > locations.size()) {
            return ErrorType.CHEST_NOT_ENOUGH;
        }

        if(clearAllChest) {
            for(Location loc : locations) {
                Chest chest = (Chest)loc.getBlock().getState();
                chest.getBlockInventory().clear();
            }
        }

        Collections.shuffle(items);
        Collections.shuffle(locations);

        //Bukkit.broadcastMessage("숨겨야 하는 아이템 갯수: "+ items.size()+"개");

        for(int i = 0; i < items.size(); i++) {
            Chest chest = (Chest) locations.get(i).getBlock().getState();
            chest.getBlockInventory().addItem(items.get(i));

            //Bukkit.broadcastMessage("여기에 숨김! : "+locations.get(i).getX()+", "+locations.get(i).getY()+", "+locations.get(i).getZ());
        }

        if(enableNotify)
            Bukkit.broadcastMessage(ChatColor.GRAY+regionName+ChatColor.WHITE+" 지역에 아이템을 모두 숨겼습니다.");
        return null;

    }

    /**
     * <b>상자 내용물 반환 메소드</b><br>특정 지역안의 상자에 대한 상자의 좌표, 상자에 있는 내용물을 한 쌍으로하는 Hashmap을 반환합니다.
     * @return
     */
    public static HashMap<Location, List<ItemStack>> getChestContents(String regionName, boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {

        List<Location> locations = getChestLocations(regionName,ignoreCantOpenChest,ignoreTrappedChest);
        HashMap<Location, List<ItemStack>> map = new HashMap<>();

        for(Location loc : locations) {
            Chest c = (Chest)loc.getBlock().getState();
            map.put(loc,Arrays.asList(c.getBlockInventory().getContents()));
        }

        return map;
    }

    public static String getErrorMessage(ErrorType err) {
        switch(err) {
            case NOT_ITEMS:
                return ChatColor.RED+"해당 지역에 등록된 아이템이 없습니다.";

            case NOT_REG_CHEST:
                return ChatColor.RED+"해당 지역은 등록되어있지 않습니다.";

            case CHEST_NOT_ENOUGH:
                return ChatColor.RED+"아이템을 배치할 수 있는 상자가 부족홥니다.";
        }

        return null;
    }
}
