package co.carrd.andwhat5.sts.ui;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.Utilities;
import co.carrd.andwhat5.sts.interfaces.IBooster;
import com.mcsimonflash.sponge.teslalibs.inventory.Action;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.function.Consumer;

public class UISTS {

    Pokemon[] pokemon;
    Player player;

    int noPokemon = 0;

    public UISTS(Player player, Pokemon[] pokemon) {
        this.pokemon = pokemon;
        this.player = player;
    }

    public void displayGUI() {
        Element blueGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.BLUE).build());
        Element redGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.RED).build());
        Element blackGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.BLACK).build());
        Element whiteGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.WHITE).build());

        Layout.Builder layout = Layout.builder()
                .set(blueGlass, 0, 1, 3, 4, 5, 7, 8)
                .set(redGlass, 2, 6)
                .set(blackGlass, 9, 17)
                .set(whiteGlass, 13, 18, 19, 20, 21, 22, 23, 24, 25, 26);

        int slot = 10;
        for (Pokemon poke : pokemon) {
            if (poke == null) {
                layout.set(Element.of(ItemStack.of(ItemTypes.AIR, 1)));
            }
            else {
                noPokemon += 1;
                int price = Utilities.getPrice(poke);
                Consumer<Action.Click> action = a -> {
                    if (noPokemon == 1) {
                        Utilities.sendMsg(player, "&cYou can't sell your last Pokemon!");
                    }
                    else {
                        UIConfirm con = new UIConfirm(player, poke, price);
                        con.displayGUI();
                    }
                };

                ItemStack p = (ItemStack)(Object)ItemPixelmonSprite.getPhoto(poke);
                ArrayList<Text> lore = new ArrayList<>();
                for(IBooster booster : STS.boosters) {
                    if (booster.getMoney(poke) != 0) {
                        lore.add(TextSerializers.FORMATTING_CODE.deserialize("&a" + booster.getItemLore() + booster.getMoney(poke)));
                    }
                }

                p.offer(Keys.ITEM_LORE, lore);
                p.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize("&a\u2605 " + poke.getDisplayName() + " (" + STS.getCurrencySymbol() + price + ") \u2605"));
                layout.set(Element.of(p, action), slot);
            }

            slot += slot == 12 ? 2 : 1;
        }

        Layout lay = layout.build();
        View view = View.builder().archetype(InventoryArchetypes.CHEST)
                .property(InventoryTitle.of(TextSerializers.FORMATTING_CODE.deserialize("&9Sell to Server")))
                .build(STS.getInstance().container);
        view.define(lay);
        view.open(player);
    }

}
