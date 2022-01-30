package main;

import event.onClick;
import event.onCommand;
import event.onInventory;
import event.onTabComplete;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class Main extends JavaPlugin {

    public static Main instance;
    public static YamlConfiguration regionYAML = new YamlConfiguration();
    public static File regionFile;

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdir();

        getServer().getPluginManager().registerEvents(new onInventory(), this);
        getServer().getPluginManager().registerEvents(new onClick(), this);

        getCommand("상자").setTabCompleter(new onTabComplete());
        getCommand("상자").setExecutor(new onCommand());

        regionFile = new File(getDataFolder(), "regions.yml");

        try {
            regionYAML.load(regionFile);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        try {
            regionYAML.save(regionFile);
        } catch(Exception e) {

        }
    }
}
