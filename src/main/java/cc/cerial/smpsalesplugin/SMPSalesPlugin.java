package cc.cerial.smpsalesplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SMPSalesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }
}