package saehyeon.chestitem.lib;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import saehyeon.chestitem.main.PlayerGlobal;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class Otherf {

    public static boolean isPosValid(Player p) {
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

    public static boolean isBoolean(String str) {
        try {
            boolean test = Boolean.parseBoolean(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int rand(int min, int max) {
        return new Random().nextInt(max)+min;
    }
}
