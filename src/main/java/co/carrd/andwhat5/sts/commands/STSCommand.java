package co.carrd.andwhat5.sts.commands;

import co.carrd.andwhat5.sts.Utilities;
import co.carrd.andwhat5.sts.ui.UISTS;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;

public class STSCommand implements CommandExecutor {

    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextColors.RED, "You need to be in-game to use this command!"));
            return CommandResult.empty();
        }

        Player player = (Player)src;
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)player);

        if (storage == null) {
            Utilities.sendMsg(player, "&cCould not load your party.");
            return CommandResult.success();
        }

        new UISTS(player, storage.getAll()).displayGUI();
        return CommandResult.success();
    }

    public static CommandSpec spec() {
        return CommandSpec.builder()
                .permission("sts.sts.base")
                .child(ReloadCommand.spec(), "reload")
                .executor(new STSCommand())
                .description(Text.of("Opens the Sell to Server GUI."))
                .build();
    }

}
