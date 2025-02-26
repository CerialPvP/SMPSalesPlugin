package cc.cerial.cerialplugintemplate;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CerialPluginTemplate extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().severe("========================================================================");
        getLogger().severe("This is a template plugin, made by Cerial.");
        getLogger().severe("To get started, simply modify the build.gradle.kts to your liking.");
        getLogger().severe("The server will shut down because of this.");
        getLogger().severe("========================================================================");
        Bukkit.getPluginManager().disablePlugin(this);
    }
}