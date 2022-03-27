package saehyeon.chestitem.event;

import saehyeon.chestitem.main.PlayerGlobal;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class onClick implements Listener {
    @EventHandler
    void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(p.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE && e.getClickedBlock() != null) {
            e.setCancelled(true);

            if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
                PlayerGlobal.POS1.put(p,e.getClickedBlock().getLocation());
            }

            else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                PlayerGlobal.POS2.put(p,e.getClickedBlock().getLocation());
            }
        }
    }
}
