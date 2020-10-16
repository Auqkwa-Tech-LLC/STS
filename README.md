# STS #

### Description ###

This plugin allows players to sell their Pokemon to the server in exchange for the server's default currency.
Originally, this plugin was for Pixelmon 6.1.x, but has since been forked and reworked for 8.1.x compatibility.
The main command brings up a GUI for the player to view the sell prices of each of their Pokemon in their party.
The prices depend on various factors such as level, IVs, if it's a shiny, etc.

**Dependencies:** SpongeForge, Pixelmon 8.1

### Commands (User) ###

#### /sts ####

**Required Permission:** sts.sts.base

**Description:** Brings up a GUI for players to select which Pokemon they would like to sell.
From this menu they may see the sell prices for all their party Pokemon and selling brings up a secondary confirm GUI.

### Commands (Admin) ###

#### /sts reload ####

**Required Permission:** sts.sts.admin

**Description:** Hot-reloads the plugin's configuration

**// NOTE:** This plugin's configuration is formatted in HOCON

### Configuration (sts.conf) ###

```javascript
general
{
	# The amount of money given to the player if the Pokemon has a custom texture.
    customTextureBooster=1000
    # The amount of money given if the Pokemon has a Hidden Ability.
    hiddenAbilityBooster=1000
    # The amount of money given if the Pokemon is a legendary.
    legendaryBooster=4000
    # The amount of money per % of the Pokemons iv. Example: $5 for a 1% IV Pokemon, $10 for a 2% IV Pokemon.
    moneyPerIV=5
    # The amount of money per level of the Pokemon.
    moneyPerLevel=15
    # The amount of money given if the Pokemon has perfect IVs.
    perfectIVBooster=1000
    # The prefix to be used before each message.
    prefix="&6[&4STS&6]"
    # The amount of money given if the Pokemon is shiny.
    shinyBooster=2000
}
```