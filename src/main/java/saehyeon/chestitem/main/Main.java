package saehyeon.chestitem.main;

import saehyeon.chestitem.event.onClick;
import saehyeon.chestitem.event.onCommand;
import saehyeon.chestitem.event.onInventory;
import saehyeon.chestitem.event.onTabComplete;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

        getServer().getPluginManager().registerEvents(new onInventory(), this);
        getServer().getPluginManager().registerEvents(new onClick(), this);

        getCommand("상자").setTabCompleter(new onTabComplete());
        getCommand("상자").setExecutor(new onCommand());

        YML.regionFile = new File(getDataFolder(), "regions.yml");

        try {
            YML.regionYML.load(YML.regionFile);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        try {
            YML.regionYML.save(YML.regionFile);
        } catch(Exception e) {

        }
    }
}
