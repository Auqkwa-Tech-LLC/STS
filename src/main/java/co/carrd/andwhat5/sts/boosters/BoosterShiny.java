package co.carrd.andwhat5.sts.boosters;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.config.STSConfig;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class BoosterShiny implements IBooster {

    @Override
    public int getMoney(Pokemon pokemon) {
        return pokemon.isShiny() ? STSConfig.General.shinyBooster : 0;
    }

    @Override
    public String getItemLore() {
        return "Shiny Pokemon Booster: " + STS.getCurrencySymbol();
    }

}
