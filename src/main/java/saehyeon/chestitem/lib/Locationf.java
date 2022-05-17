package saehyeon.chestitem.lib;

import org.bukkit.Location;

import java.util.ArrayList;

public class Locationf {
    public static ArrayList<Location> AllLocationWithin(Location loc1, Location loc2) {
        ArrayList<Location> loc = new ArrayList<>();

        for(double y = Math.min(loc1.getY(), loc2.getY()); y <= Math.max(loc1.getY(), loc2.getY()); y++) {
            for(double x = Math.min(loc1.getX(), loc2.getX()); x <= Math.max(loc1.getX(), loc2.getX()); x++) {
                for(double z = Math.min(loc1.getZ(), loc2.getZ()); z <= Math.max(loc1.getZ(), loc2.getZ()); z++) {
                    loc.add(new Location(loc1.getWorld(), x,y,z));
                }
            }
        }

        return loc;
    }

    public static Boolean isBetweenLocation(Location old, Location loc1, Location loc2) {

        //Bukkit.broadcastMessage("old: "+old.getX()+", "+old.getY()+", "+old.getZ());
        //Bukkit.broadcastMessage("loc1: "+loc1.getX()+", "+loc1.getY()+", "+loc1.getZ());
        //Bukkit.broadcastMessage("loc2: "+loc2.getX()+", "+loc2.getY()+", "+loc2.getZ());

        if(Math.min(loc1.getX(), loc2.getX()) <= old.getX() && old.getX() <= Math.max(loc1.getX(), loc2.getX()) &&
                Math.min(loc1.getY(), loc2.getY()) <= old.getY() && old.getY() <= Math.max(loc1.getY(), loc2.getY()) &&
                Math.min(loc1.getZ(), loc2.getZ()) <= old.getZ() && old.getZ() <= Math.max(loc1.getZ(), loc2.getZ()) ) {
            return true;
        }

        return false;
    }
}
