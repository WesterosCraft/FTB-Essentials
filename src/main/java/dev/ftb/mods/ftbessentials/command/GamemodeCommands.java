package dev.ftb.mods.ftbessentials.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ftb.mods.ftbessentials.config.FTBEConfig;
import dev.ftb.mods.ftbessentials.util.FTBEPlayerData;
import dev.ftb.mods.ftbessentials.util.OtherPlayerInventory;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;



public class GamemodeCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (FTBEConfig.GM.isEnabled()) {
			dispatcher.register(Commands.literal("gm")
					.requires(FTBEConfig.GM.enabledAndOp())
					.executes(context -> gamemode(context.getSource().getPlayerOrException()))
					.then(Commands.argument("gamemode", EntityArgument.gamemode())
							.executes(context -> gamemode(EntityArgument.getPlayer(context, "player")))
					)
			);
		}
    }


    public static int gamemode(ServerPlayer player) {
		var data = FTBEPlayerData.get(player);
		var abilities = player.getAbilities();

		if (data.fly) {
			data.fly = false;
			data.save();
			abilities.mayfly = false;
			abilities.flying = false;
			player.displayClientMessage(new TextComponent("Flight disabled"), true);
		} else {
			data.fly = true;
			data.save();
			abilities.mayfly = true;
			player.displayClientMessage(new TextComponent("Flight enabled"), true);
		}

		player.onUpdateAbilities();
		return 1;
	}
}