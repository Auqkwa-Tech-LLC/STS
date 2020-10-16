package co.carrd.andwhat5.sts.ui;

import co.carrd.andwhat5.sts.STS;
import co.carrd.andwhat5.sts.Utilities;
import com.mcsimonflash.sponge.teslalibs.inventory.Action;
import com.mcsimonflash.sponge.teslalibs.inventory.Element;
import com.mcsimonflash.sponge.teslalibs.inventory.Layout;
import com.mcsimonflash.sponge.teslalibs.inventory.View;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.function.Consumer;

public class UIConfirm {

    int price;
    Pokemon pokemon;
    Player player;

    public UIConfirm(Player player, Pokemon pokemon, int price) {
        this.price = price;
        this.pokemon = pokemon;
        this.player = player;
    }

    public void displayGUI() {
        Element blueGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.BLUE).build());
        Element redGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.RED).build());
        Element blackGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.BLACK).build());
        Element whiteGlass = Element.of(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.WHITE).build());

        Consumer<Action.Click> close = c -> {
            PlayerPartyStorage s = Pixelmon.storageManager.getParty((EntityPlayerMP)player);
            if (s == null) {
                Task.builder().execute(() -> player.closeInventory()).submit(STS.getInstance());
            }
            else {
                UISTS ui = new UISTS(player, s.getAll());
                ui.displayGUI();
            }
        };

        Consumer<Action.Click> sell = s -> {
            if (Utilities.sellPokemon(player, pokemon, price)) {
                Utilities.sendMsg(player, "&aYou sold your " + pokemon.getDisplayName() + " for " + STS.getCurrencySymbol() + price + "!");
                Task.builder().execute(() -> player.closeInventory()).submit(STS.getInstance());
            }
            else {
                Utilities.sendMsg(player, "&cError selling " + pokemon.getDisplayName() + ". Is it still in your party?");
                Task.builder().execute(() -> player.closeInventory()).submit(STS.getInstance());
            }
        };

        ItemStack rDye = ItemStack.builder().itemType(ItemTypes.DYE).add(Keys.DYE_COLOR, DyeColors.RED).build();
        ItemStack gDye = ItemStack.builder().itemType(ItemTypes.DYE).add(Keys.DYE_COLOR, DyeColors.GREEN).build();
        rDye.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize("&c\u2605 " + "Cancel" + " \u2605"));
        gDye.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.deserialize("&a\u2605 " + "Sell " + pokemon.getDisplayName() + " for " + STS.getCurrencySymbol() + price + " \u2605"));

        Element redDye = Element.of(rDye, close);
        Element greenDye = Element.of(gDye, sell);

        Layout layout = Layout.builder()
                .set(blueGlass, 0, 1, 3, 4, 5, 7, 8)
                .set(redGlass, 2, 6)
                .set(blackGlass, 9, 10, 11, 15, 16, 17)
                .set(whiteGlass, 13, 18, 19, 20, 21, 22, 23, 24, 25, 26)
                .set(redDye, 12)
                .set(greenDye, 14)
                .build();

        View view = View.builder().archetype(InventoryArchetypes.CHEST).property(InventoryTitle.of(TextSerializers.FORMATTING_CODE.deserialize("&9Sell to Server - Confirm"))).build(STS.getInstance().container);
        view.define(layout);
        view.open(player);
    }

}
