package daxz.dev.spellwave.Commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import daxz.dev.spellwave.Registry.ItemRegistry;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class ItemCommand {

    public static LiteralCommandNode<CommandSourceStack> itemHelper() {

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("itemh");

        root.then(
                Commands.literal("give")
                        .requires(sender -> sender.getSender().hasPermission("itemHelper.give"))
                        .then(
                                Commands.argument("give", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            ItemRegistry.getRegisteredItems().keySet().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> {
                                            if (ctx.getSource().getSender() instanceof Player player) {
                                                ItemRegistry.giveItem(player, ctx.getArgument("give", String.class));
                                            }
                                            return 1;
                                        })
                        )
        );

        return root.build();
    }




}
