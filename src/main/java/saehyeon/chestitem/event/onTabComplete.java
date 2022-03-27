package saehyeon.chestitem.event;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class onTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(alias.equals("상자")) {
            return Arrays.asList("지역설정","지역삭제","목록","아이템","숨기기");
        }
        return null;
    }
}
