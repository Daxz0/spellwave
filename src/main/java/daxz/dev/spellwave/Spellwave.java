package daxz.dev.spellwave;

import daxz.dev.spellwave.Commands.ItemCommand;
import daxz.dev.spellwave.Registry.EventRegister;
import daxz.dev.spellwave.Registry.ItemRegistry;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import com.jeff_media.customblockdata.CustomBlockData;

public final class Spellwave extends JavaPlugin {

    public static Spellwave instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        EventRegister.registerEvents();
        ItemRegistry.registerItems();
        CustomBlockData.registerListener(this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(ItemCommand.itemHelper());
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Spellwave getInstance() {return instance;}

}
