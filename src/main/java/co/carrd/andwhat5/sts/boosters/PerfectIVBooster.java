package co.carrd.andwhat5.sts.boosters;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.config.STSConfig;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

public class PerfectIVBooster implements IBooster {

    @Override
    public int getMoney(Pokemon pokemon) {
        IVStore ivs = pokemon.getIVs();

        float ivHP = ivs.get(StatsType.HP);
        float ivAtk = ivs.get(StatsType.Attack);
        float ivDef = ivs.get(StatsType.Defence);
        float ivSpeed = ivs.get(StatsType.Speed);
        float ivSAtk = ivs.get(StatsType.SpecialAttack);
        float ivSDef = ivs.get(StatsType.SpecialDefence);
        int per = Math.round(((ivHP + ivDef + ivAtk + ivSpeed + ivSAtk + ivSDef) / 186f) * 100);
        return per == 100 ? STSConfig.General.perfectIVBooster : 0;
    }

    @Override
    public String getItemLore() {
        return "Perfect IV Booster: " + STS.getCurrencySymbol();
    }

}
