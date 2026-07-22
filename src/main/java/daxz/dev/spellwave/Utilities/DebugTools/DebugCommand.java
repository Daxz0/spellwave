package daxz.dev.spellwave.Utilities.DebugTools;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DebugCommand {

    public static LiteralCommandNode<CommandSourceStack> debug() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("debug");


        root
                .then(Commands.literal("data_components")
                        .executes(ctx -> {

                          if (ctx.getSource().getSender() instanceof Player player){

                              ItemStack item = player.getInventory().getItemInMainHand();

                          }
                          return 0;

                        })
                );

        return root.build();
    }
}
