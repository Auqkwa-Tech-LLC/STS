package co.carrd.andwhat5.sts;

import co.carrd.andwhat5.sts.boosters.*;
import co.carrd.andwhat5.sts.commands.STSCommand;
import co.carrd.andwhat5.sts.config.STSConfig;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Plugin(
        id = "sts",
        name = "STS",
        authors = "AnDwHaT5",
        version = "1.1.0"
)
public class STS {

    private static STS instance;
    @Inject
    public PluginContainer container;

    public static STS getInstance() {
        return instance;
    }

    @Inject
    private Logger logger;

    public static Logger getLogger() {
        return instance.logger;
    }

    private EconomyService economyService;

    public static EconomyService getEcoService() {
        return instance.economyService;
    }

    public static String getCurrencySymbol() {
        return getEcoService().getDefaultCurrency().getSymbol().toPlainSingle();
    }

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    public STSConfig config;

    public static List<IBooster> boosters = new ArrayList<>();

    public void loadConfig() throws IOException, ObjectMappingException {
        //Config
        CommentedConfigurationNode node = this.configLoader.load();
        TypeToken<STSConfig> type = TypeToken.of(STSConfig.class);
        this.config = node.getValue(type, new STSConfig());
        node.setValue(type, this.config);
        this.configLoader.save(node);
        //End config
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;

        Optional<EconomyService> opEcoService = Sponge.getServiceManager().provide(EconomyService.class);
        if (!opEcoService.isPresent()) {
            logger.error("=== Failed to load economy service! STS will be disabled! ===");
            return;
        }

        economyService = opEcoService.get();

        try {
            loadConfig();
        }
        catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }

        //Loads the premade boosters into ram.
        boosters.add(new BoosterMoneyPerLevel());
        boosters.add(new BoosterShiny());
        boosters.add(new CustomTextureBooster());
        boosters.add(new HiddenAbilityBooster());
        boosters.add(new IVBooster());
        boosters.add(new LegendaryBooster());
        boosters.add(new PerfectIVBooster());

        Sponge.getCommandManager().register(this, STSCommand.spec(), "sts");
    }

}
