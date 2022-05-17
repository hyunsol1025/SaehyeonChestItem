package saehyeon.chestitem.event;

import saehyeon.chestitem.main.Main;
import saehyeon.chestitem.main.PlayerGlobal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.region.Region;

import java.util.ArrayList;
import java.util.List;

public class onInventory implements Listener {
    @EventHandler
    void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player)e.getPlayer();

        // 아이템을 설정 중인 상태 + 해당 지역이 아이템이 있다면
        if(PlayerGlobal.isItemSetting.containsKey(p.getUniqueId())) {

            Region r = Region.getByName(e.getView().getTitle());

            if(r != null) {

                // 만약 지역에 등록된 아이템이 있다면 아이템 로드
                if (r.hasItem()) {

                    List<ItemStack> items = r.getItems();

                    Bukkit.getServer().getScheduler().runTaskLater(Main.instance, () -> {

                        for (int i = 0; i < 54; i++) {

                            if (items.get(i) == null) {
                                p.getOpenInventory().setItem(i, new ItemStack(Material.AIR));
                            } else {
                                p.getOpenInventory().setItem(i, items.get(i));
                            }

                        }

                    }, 1);

                }
            }
        }
    }

    @EventHandler
    void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();

        if(PlayerGlobal.isItemSetting.containsKey(p.getUniqueId())) {

            Region r = Region.getByName(e.getView().getTitle());

            if(r != null) {

                // 아이템 저장
                ArrayList<ItemStack> items = new ArrayList<>();

                for(int i = 0; i < 54; i++) {
                    items.add(p.getOpenInventory().getItem(i));
                }

                r.setItems(items);
                p.sendMessage("§7"+r.getName()+"§f의 아이템 변경사항을 저장했습니다.");

                PlayerGlobal.isItemSetting.remove(p.getUniqueId());

            } else {

                p.sendMessage("§c아이템을 저장하지 못했습니다. 작업중에 지역이 제거되었을 수 있습니다.");
            }
        }
    }
}
