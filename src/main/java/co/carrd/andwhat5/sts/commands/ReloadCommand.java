package co.carrd.andwhat5.sts.commands;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.Utilities;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ReloadCommand implements CommandExecutor {

    @Override
    public CommandResult execute(@Nonnull CommandSource src, @Nonnull CommandContext args) {
        try {
            STS.getInstance().loadConfig();
            Utilities.sendMsg(src, "&aReloaded config!");
        }
        catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
        return CommandResult.success();
    }

    public static CommandSpec spec() {
        return CommandSpec.builder()
                .executor(new ReloadCommand())
                .permission("sts.sts.admin")
                .description(Text.of("Reloads the STS plugin"))
                .build();
    }

}
