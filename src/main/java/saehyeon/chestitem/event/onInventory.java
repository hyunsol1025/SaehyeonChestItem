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

import java.util.ArrayList;

public class onInventory implements Listener {
    @EventHandler
    void onInventoryOpen(InventoryOpenEvent e) {
        Player p = (Player)e.getPlayer();

        String inventoryName = e.getView().getTitle();

        if(PlayerGlobal.isItemSetting.containsKey(p.getUniqueId()) && Main.regionYAML.contains(inventoryName+".items")) {

            ArrayList<ItemStack> items = (ArrayList<ItemStack>) Main.regionYAML.get(inventoryName+".items");


            Bukkit.getServer().getScheduler().runTaskLater(Main.instance, () -> {

                for(int i = 0; i < 54; i++) {
                    if(items.get(i) == null) {
                        p.getOpenInventory().setItem(i,new ItemStack(Material.AIR));
                    } else {
                        p.getOpenInventory().setItem(i,items.get(i));
                    }
                }

            }, 1);

        }
    }

    @EventHandler
    void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();

        // 아이템 저장
        String inventoryName = e.getView().getTitle();

        if(PlayerGlobal.isItemSetting.containsKey(p.getUniqueId())) {
            ArrayList<ItemStack> items = new ArrayList<>();

            for(int i = 0; i < 54; i++) {
                items.add(p.getOpenInventory().getItem(i));
            }

            Main.regionYAML.set(inventoryName+".items", items);
            PlayerGlobal.isItemSetting.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GRAY+inventoryName+ChatColor.WHITE+"의 아이템 변경사항을 저장했습니다.");
        }
    }
}
