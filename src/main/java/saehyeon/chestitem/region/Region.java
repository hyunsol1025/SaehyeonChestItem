package saehyeon.chestitem.region;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.ErrorType;
import saehyeon.chestitem.lib.Blockf;
import saehyeon.chestitem.lib.Locationf;
import saehyeon.chestitem.main.YML;

import java.util.*;

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

    /**
     * <b>이름을 반환합니다.</b>
     * @return
     */
    public String getName() {
        return regionName;
    }

    /**
     * <b>범위의 첫번째 좌표, 두번째 좌표를 반환합니다.</b>
     */
    public Location[] getPosition() {
        return new Location[] { YML.regionYML.getLocation(regionName+"pos1"), YML.regionYML.getLocation(regionName+"pos2") };
    }

    /**
     * <b>지역에 등록된 아이템을 반환합니다.</b>
     */
    public List<ItemStack> getItems() {
        return (List<ItemStack>)YML.regionYML.get(regionName+".items");
    }

    /**
     * <b>지역을 생성합니다.</b><br>
     * new Region()을 통해 만든 지역 인스턴스를 yml에 작성하여 실질적으로 지역을 생성합니다.<br>
     * 이 메소드를 호출하지 않는다면 서버 종료 시 지역이 사라질 것 입니다.
     */
    public void create() {
        YML.regionYML.set(regionName+".pos1",pos1);
        YML.regionYML.set(regionName+".pos2",pos2);
        YML.regionYML.set(regionName+".items",items);
    }

    /**
     * <b>지역을 삭제합니다.</b>
     */
    public void remove() {
        YML.regionYML.set(regionName,null);
        regionName = null;
        pos1 = null;
        pos2 = null;
        items = null;
    }

    /**
     * <b>아이템을 설정합니다.</b>
     */
    public void setItems(List<ItemStack> items) {
        this.items = items;
        YML.regionYML.set(regionName+".items",items);
    }

    /**
     * <b>아이템이 하나라도 등록되어 있는지 여부를 반환합니다.</b>
     */
    public boolean hasItem() {
        List<ItemStack> items = getItems();
        return items != null && items.size() != 0;
    }

    /**
     * <b>지역이름을 통해 지역을 얻습니다.</b>
     * @param regionName 검색할 이름입니다.
     */
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

    /**
     * <b>특정 이름을 가진 지역이 등록되어 있는지 여부를 반환합니다.</b>
     * @param regionName 확인할 지역이름입니다.
     */
    public static boolean contains(String regionName) {
        return YML.regionYML.contains(regionName);
    }

    /**
     * <b>지역 내 모든 상자의 좌표를 반환합니다.</b>
     * @param ignoreCantOpenChest true라면 열 수 없는 상자는 무시합니다.
     * @param ignoreTrappedChest true라면 덫상자는 무시합니다.
     */
    public ArrayList<Location> getChests(boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {
        Location loc1 = pos1;
        Location loc2 = pos2;

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

    /**
     * <b>지역 내의 모든 상자의 내용물을 반환합니다.</b><br>
     * key는 상자 위치(Location), value는 내용물(List)
     * @param ignoreCantOpenChest true라면 열 수 없는 상자를 무시할 것 입니다.
     * @param ignoreTrappedChest true라면 덫상자를 무시할 것 입니다.
     */
    public HashMap<Location, List<ItemStack>> getChestContents(boolean ignoreCantOpenChest, boolean ignoreTrappedChest) {

        List<Location> locations = getChests(ignoreCantOpenChest,ignoreTrappedChest);
        HashMap<Location, List<ItemStack>> map = new HashMap<>();

        for(Location loc : locations) {
            Chest c = (Chest)loc.getBlock().getState();
            map.put(loc, Arrays.asList(c.getBlockInventory().getContents()));
        }

        return map;
    }

    /**
     * <b>모든 지역을 반환합니다.</b>
     */
    public static ArrayList<Region> getAll() {
        ArrayList<Region> result = new ArrayList<>();

        for(String s : YML.regionYML.getKeys(false)) {
            result.add(getByName(s));
        }

        return result;
    }

    /**
     * <b>아이템을 숨깁니다.</b><br>items 안에 있는 아이템들을 숨깁니다.
     * @param clearAllChest true라면 아이템을 숨기기 전에 모든 상자를 비울 것 입니다.
     * @param ignoreCantOpenChest true라면 열 수 없는 상자에는 숨기지 않습니다.
     * @param ignoreTrappedChest true라면 덫상자에는 숨기지 않습니다.
     * @param enableNotify true라면 상자에 아이템을 모두 숨긴 후 모두에게 메세지를 전송합니다.
     */
    public ErrorType spreadItem(boolean clearAllChest, boolean ignoreCantOpenChest, boolean ignoreTrappedChest, boolean enableNotify) {

        List<Location> locations = getChests(ignoreCantOpenChest, ignoreTrappedChest);

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

        // 상자에 아이템 숨기기
        for (int i = 0; i < items.size(); i++) {

            Chest chest = (Chest) locations.get(i).getBlock().getState();
            chest.getBlockInventory().addItem(items.get(i));

        }

        // 만약 상자를 숨긴 후 공지하게 설정 되었다면: 공지 메세지 띄우기
        if (enableNotify)
            Bukkit.broadcastMessage("§7" + regionName + "§f 지역에 아이템을 모두 숨겼습니다.");

        return null;
    }
}
