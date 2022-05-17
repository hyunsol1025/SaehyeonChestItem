package saehyeon.chestitem.event;

import saehyeon.chestitem.lib.Otherf;
import saehyeon.chestitem.main.PlayerGlobal;
import saehyeon.chestitem.ErrorType;
import saehyeon.chestitem.item.Item;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import saehyeon.chestitem.main.YML;
import saehyeon.chestitem.region.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class onCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        if(label.equals("상자")) {
            switch(args[0]) {
                case "생성":
                case "지역설정":
                    if(Otherf.isPosValid(p)) {
                        if (args.length >= 3) {

                            Location pos1 = PlayerGlobal.POS1.get(p);
                            Location pos2 = PlayerGlobal.POS2.get(p);

                            Region r = new Region(args[1], pos1,pos2, null);
                            r.create();

                            p.sendMessage("§7"+args[1]+"§f 지역을 생성했습니다.");

                        } else {
                            p.sendMessage("§c구문이 올바르지 않습니다. \n(사용법: /상자 지역설정 [지역이름] [true/false (이미 등록된 지역이라면 무시할 것인지에 대한 여부)])");
                        }
                    } else {
                        p.sendMessage(ErrorType.INVALID_POSITION.toMessage());
                    }

                    break;

                case "지역삭제":
                    if(args.length >= 2) {

                        Region r = Region.getByName(args[1]);

                        if(r != null) {

                            r.remove();
                            p.sendMessage("§7"+args[1]+"§f(을)를 제거했습니다.");

                        } else {

                            p.sendMessage("§c해당 지역은 등록되어있지 않습니다.");

                        }

                    } else {
                        p.sendMessage("§c구문이 올바르지 않습니다. \n(사용법: /상자 지역삭제 [지역이름]");
                    }

                    break;

                case "아이템":
                    if(args.length >= 2) {

                        PlayerGlobal.isItemSetting.put(p.getUniqueId(),true);
                        PlayerGlobal.inv.put(p.getUniqueId(), Bukkit.getServer().createInventory(null, 54,args[1]));
                        p.openInventory(PlayerGlobal.inv.get(p.getUniqueId()));
                    } else {
                        p.sendMessage("§c구문이 올바르지 않습니다.\n(사용법: /상자 아이템 [지역이름])");
                    }

                    break;


                case "숨기기":

                    // 상자 숨기기 [지역이름] t/f t/f
                    if(args.length >= 4) {

                        Region r = Region.getByName(args[1]);

                        if(r != null) {
                            ErrorType err = r.spreadItem(Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]), Boolean.parseBoolean(args[5]));

                            if (err == null) {

                                // 오류가 없음
                                p.sendMessage("§7" + args[1] + "§f지역의 상자에 아이템을 숨겼습니다.");

                            } else {

                                // 오류가 있음
                                p.sendMessage(err.toMessage());

                            }
                        } else {
                            sender.sendMessage(ErrorType.NOT_EXIST_REGION.toMessage());
                        }
                    } else {
                        p.sendMessage("§c사용법: /상자 숨기기 [지역이름] [지역의 모든 상자의 내용물을 비우고 진행할지 여부] [열 수 없는 상자 무시 여부] [덫상자는 무시 여부] [아이템을 숨겼다는 공지 출력 여부]");
                    }

                    break;

                case "내용물보기":
                    if(args.length >= 4) {

                        Region r = Region.getByName(args[1]);

                        if(r != null) {

                            // 내용물 없는 상자는 무시함?
                            boolean ignoreNoItemChest = Boolean.parseBoolean(args[2]);

                            // 열 수 없는 상자는 무시함?
                            boolean ignoreCantOpenChest = Boolean.parseBoolean(args[3]);

                            HashMap<Location, List<ItemStack>> map = r.getChestContents(ignoreNoItemChest, ignoreCantOpenChest);

                            p.sendMessage("\n§7" + args[1] + "§f 지역의 상자 내용물: ");

                            map.forEach((key, value) -> {

                                value.removeAll(Collections.singleton(null));

                                // 상자에 아이템이 있음
                                if(!value.isEmpty()) {
                                    StringBuilder sb = new StringBuilder("");

                                    for (ItemStack i : value) {

                                        sb.append(i.getType())
                                                .append(" x")
                                                .append(i.getAmount())
                                                .append(", ");

                                    }

                                    p.sendMessage(key.getX() + ", " + key.getY() + ", " + key.getZ() + ": " + sb.substring(0, sb.length()));
                                }

                                // 아이템이 없음 + 아이템이 없는 상자는 무시
                                else if(!ignoreNoItemChest) {
                                    p.sendMessage(key.getX() + ", " + key.getY() + ", " + key.getZ() + ": 아이템이 없습니다.");
                                }

                            });

                        } else {

                            p.sendMessage(ErrorType.NOT_EXIST_REGION.toMessage());

                        }
                    } else {
                        p.sendMessage("§c사용법: /상자 내용물보기 [지역이름] [내용물이 없는 상자도 출력할지 여부] [덫상자 무시 여부]");
                    }

                    break;

                case "목록":
                    for(String s : YML.regionYML.getKeys(false)) {
                        String itemContext = "";

                        if(YML.regionYML.contains(s+".items")) {
                            ArrayList<ItemStack> items = (ArrayList<ItemStack>) YML.regionYML.get(s+".items");

                            if(items != null) {
                                itemContext = "총 "+items.size()+"개의 아이템";

                            }
                        } else {
                            itemContext = "아이템이 등록되어 있지 않음.";
                        }

                        Location pos1 = (Location) YML.regionYML.get(s+".pos1");
                        Location pos2 = (Location) YML.regionYML.get(s+".pos2");

                        p.sendMessage(s+" | "+itemContext+" | "+Math.round(pos1.getX())+", "+Math.round(pos1.getY())+", "+Math.round(pos1.getZ())+" ~ "+Math.round(pos2.getX())+", "+Math.round(pos2.getY())+", "+Math.round(pos2.getZ()));
                    }
                    break;
            }
        }

        return false;
    }
}
