package co.carrd.andwhat5.sts;

import co.carrd.andwhat5.sts.config.STSConfig;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Optional;

public class Utilities {

    /**
     * Messages a command source with a formatted message with a prefix attached.
     * @param str
     */
    public static void sendMsg(CommandSource src, String str) {
        String prefix = STSConfig.General.prefix;
        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + "&f " + str));
    }

    /**
     * Loops through all of the registered boosters and determines the price of the Pokemon.
     * @param pokemon The {@link NBTTagCompound} of the Pokemon the player is selling.
     * @return
     */
    public static int getPrice(Pokemon pokemon) {
        int money = 0;
        for (IBooster booster : STS.boosters) {
            money += booster.getMoney(pokemon);
        }
        return money;
    }

    /**
     * Sells the specified Pokemon to the server.
     * @param player The player selling the Pokemon
     * @param pkmn The {@link NBTTagCompound} of the Pokemon the player is selling.
     * @param price How much the Pokemon is worth.
     * @return
     */
    public static boolean sellPokemon(Player player, Pokemon pkmn, int price) {
        PlayerPartyStorage ps = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
        if (ps == null) {
            return false;
        }

        for (int i = 0; i < 6; i++) {
            if (pkmn.equals(ps.get(i))) {
                EconomyService ecoService = STS.getEcoService();
                Optional<UniqueAccount> optAcc = ecoService.getOrCreateAccount(player.getUniqueId());
                if (!optAcc.isPresent()) {
                    player.sendMessage(Text.of(TextColors.RED, "Failed to create economy account!"));
                    return false;
                }

                EventContext eventContext = buildEventContext();
                if (eventContext == null) {
                    player.sendMessage(Text.of(TextColors.RED, "Internal error! Report this to an admin!"));
                    return false;
                }

                TransactionResult result = optAcc.get().deposit(
                        ecoService.getDefaultCurrency(),
                        BigDecimal.valueOf(price),
                        Cause.of(eventContext, STS.getInstance()));

                ResultType resultType = result.getResult();
                if (resultType.equals(ResultType.SUCCESS)) {
                    ps.set(i, null);
                    return true;
                }
                else {
                    player.sendMessage(Text.of(TextColors.RED, "Failed to put money in your account!"));
                }
            }
        }

        return false;
    }

    @Nullable
    private static EventContext buildEventContext() {
        Optional<PluginContainer> opContainer = Sponge.getPluginManager().fromInstance(STS.getInstance().container);
        if (!opContainer.isPresent()) {
            STS.getLogger().error("Plugin container does not exist?");
            return null;
        }

        return EventContext.builder().add(EventContextKeys.PLUGIN, opContainer.get()).build();
    }

}
