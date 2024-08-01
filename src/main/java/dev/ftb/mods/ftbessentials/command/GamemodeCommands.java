package dev.ftb.mods.ftbessentials.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbessentials.config.FTBEConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

public class GamemodeCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (FTBEConfig.GM.isEnabled()) {
			dispatcher.register(Commands.literal("gmc")
					.requires(FTBEConfig.GM)
					.executes(context -> gamemode(context.getSource().getPlayerOrException(), "creative"))
			);

			dispatcher.register(Commands.literal("gma")
					.requires(FTBEConfig.GM)
					.executes(context -> gamemode(context.getSource().getPlayerOrException(), "adventure"))
			);

			dispatcher.register(Commands.literal("gms")
					.requires(FTBEConfig.GM)
					.executes(context -> gamemode(context.getSource().getPlayerOrException(), "survival"))
			);

			dispatcher.register(Commands.literal("gmsp")
					.requires(FTBEConfig.GM)
					.executes(context -> gamemode(context.getSource().getPlayerOrException(), "spectator"))
			);

		}
	}

	// Function to change the gamemode
	public static int gamemode(ServerPlayer player, String gamemode) {
		GameType targetMode = switch (gamemode.toLowerCase()) {
			case "creative" -> GameType.CREATIVE;
			case "spectator" -> GameType.SPECTATOR;
			case "adventure" -> GameType.ADVENTURE;
			case "survival" -> GameType.SURVIVAL;
			default -> player.gameMode.getGameModeForPlayer();
		};

		if (player.gameMode.getGameModeForPlayer() == targetMode) {
			player.displayClientMessage(new TextComponent("You are already in " + targetMode + " mode."), false);
			return 0;
		}

		player.setGameMode(targetMode);
		player.displayClientMessage(new TextComponent("Your are now in " + targetMode + " mode."), false);

		return 1;
	}
}