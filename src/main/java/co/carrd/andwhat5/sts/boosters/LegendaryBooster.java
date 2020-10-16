package co.carrd.andwhat5.sts.boosters;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.config.STSConfig;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

public class LegendaryBooster implements IBooster {

    @Override
    public int getMoney(Pokemon pokemon) {
        return pokemon.isLegendary() ? STSConfig.General.legendaryBooster : 0;
    }

    @Override
    public String getItemLore() {
        return "Legendary Pokemon Booster: " + STS.getCurrencySymbol();
    }

}
