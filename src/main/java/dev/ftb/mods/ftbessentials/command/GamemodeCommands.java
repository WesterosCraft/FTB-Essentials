package dev.ftb.mods.ftbessentials.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.ftb.mods.ftbessentials.config.FTBEConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import java.util.Arrays;
import java.util.Collection;


public class GamemodeCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (FTBEConfig.GM.isEnabled()) {
			dispatcher.register(Commands.literal("gm")
					.requires(FTBEConfig.GM.enabledAndOp())
					.executes(context -> gamemode(context.getSource().getPlayerOrException(), ""))
					.then(Commands.argument("gamemode", StringArgumentType.greedyString())
							.suggests((context, builder) -> SharedSuggestionProvider.suggest(getGamemodeSuggestions(), builder))
							.executes(context -> gamemode(context.getSource().getPlayerOrException(), StringArgumentType.getString(context, "gamemode"))))
			);

		}
	}
	public static Collection<String> getGamemodeSuggestions() {
		return Arrays.asList(
				GameType.SURVIVAL.getName(),
				GameType.CREATIVE.getName(),
				GameType.ADVENTURE.getName(),
				GameType.SPECTATOR.getName()
		);
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
			player.displayClientMessage(new TextComponent("You are already in that gamemode."), false);
			return 0;
		}

		player.setGameMode(targetMode);
		player.displayClientMessage(new TextComponent("Your gamemode has been updated."), false);

		return 1;
	}
}