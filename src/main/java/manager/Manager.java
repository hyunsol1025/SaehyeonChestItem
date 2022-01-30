package manager;

import main.Main;
import main.PlayerGlobal;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

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
}
