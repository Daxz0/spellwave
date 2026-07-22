package daxz.dev.spellwave.Registry;

import daxz.dev.spellwave.Spellwave;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EventRegister {

    public static void registerEvents() {
        PluginManager pm = Spellwave.getInstance().getServer().getPluginManager();
        Plugin instance = Spellwave.getInstance();
//        pm.registerEvents(new OverrideFarmland(), instance);


    }
}
