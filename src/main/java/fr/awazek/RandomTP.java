package fr.awazek;

import fr.awazek.command.RandomTPCommand;
import fr.awazek.listeners.RandomTPListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class RandomTP extends JavaPlugin implements Listener {

    private File file = new File(getDataFolder(), "message.yml");
    private YamlConfiguration textConfig = YamlConfiguration.loadConfiguration(file);


    @Override
    public void onEnable() {
        saveResource("message.yml", false);
        saveDefaultConfig();
        this.getCommand("randomtp").setExecutor(new RandomTPCommand(this));
        this.getServer().getPluginManager().registerEvents(new RandomTPListener(this), this);
    }

    @Override
    public void onDisable() {

    }


    public YamlConfiguration getTextConfig(){
        return textConfig;
    }
}
