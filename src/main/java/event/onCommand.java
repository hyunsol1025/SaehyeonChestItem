package event;

import main.Main;
import main.PlayerGlobal;
import manager.ErrorType;
import manager.Manager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class onCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        if(label.equals("상자")) {
            if(args[0].equals("지역설정") && Manager.isPosVaild(p)) {
                if(args.length >= 3) {
                    Manager.setRegion(p, args[1], Boolean.parseBoolean(args[2]));
                } else {
                    p.sendMessage(ChatColor.RED+"구문이 올바르지 않습니다. \n(사용법: /상자 지역설정 [지역이름] [true/false (이미 등록된 지역이라면 무시할 것인지에 대한 여부)])");
                }
            }

            else if(args[0].equals("지역삭제")) {
                if(args.length >= 2) {
                    if(Main.regionYAML.contains(args[1])) {
                        Main.regionYAML.set(args[1], null);
                        p.sendMessage(ChatColor.GRAY+args[1]+ChatColor.WHITE+"(을)를 제거했습니다.");
                    } else {
                        p.sendMessage(ChatColor.RED+"해당 지역은 등록되어있지 않습니다.");
                    }
                } else {
                    p.sendMessage(ChatColor.RED+"구문이 올바르지 않습니다. \n(사용법: /상자 지역삭제 [지역이름]");
                }
            }

            else if(args[0].equals("아이템")) {
                if(args.length >= 2) {
                    PlayerGlobal.isItemSetting.put(p.getUniqueId(),true);
                    PlayerGlobal.inv.put(p.getUniqueId(), Bukkit.getServer().createInventory(null, 54,args[1]));
                    p.openInventory(PlayerGlobal.inv.get(p.getUniqueId()));
                } else {
                    p.sendMessage(ChatColor.RED+"구문이 올바르지 않습니다.\n(사용법: /상자 아이템 [지역이름])");
                }

            }

            else if(args[0].equals("숨기기")) {

                // /상자 숨기기 지역이름 t/f t/f
                if(args.length >= 4) {
                    ErrorType err = Manager.spreadItem(args[1], Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]),true);
                    if(err != null) {
                        p.sendMessage(Manager.getErrorMessage(err));
                    } else {
                        p.sendMessage(ChatColor.GRAY+args[1]+ChatColor.WHITE+" 지역의 상자에 아이템을 숨겼습니다.");
                    }
                }
            }

            else if(args[0].equals("목록")) {

                for(String s : Main.regionYAML.getKeys(false)) {
                    String itemContext = "";

                    if(Main.regionYAML.contains(s+".items")) {
                        ArrayList<ItemStack> items = (ArrayList<ItemStack>) Main.regionYAML.get(s+".items");

                        if(items != null) {
                            itemContext = "총 "+items.size()+"개의 아이템";

                        }
                    } else {
                        itemContext = "아이템이 등록되어 있지 않음.";
                    }

                    Location pos1 = (Location) Main.regionYAML.get(s+".pos1");
                    Location pos2 = (Location) Main.regionYAML.get(s+".pos2");

                    p.sendMessage(s+" | "+itemContext+" | "+Math.round(pos1.getX())+", "+Math.round(pos1.getY())+", "+Math.round(pos1.getZ())+" ~ "+Math.round(pos2.getX())+", "+Math.round(pos2.getY())+", "+Math.round(pos2.getZ()));
                }
            }
        }

        return false;
    }
}
