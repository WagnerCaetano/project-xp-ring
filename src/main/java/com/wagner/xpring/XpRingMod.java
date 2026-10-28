package com.wagner.xpring;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

@Mod(XpRingMod.MOD_ID)
public final class XpRingMod {
    public static final String MOD_ID = "xp_ring";

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DeferredItem<XpRingItem> XP_RING = ITEMS.register(
            "xp_ring",
            () -> new XpRingItem(new net.minecraft.world.item.Item.Properties().stacksTo(1))
    );

    public static final DeferredHolder<MenuType<?>, MenuType<XpRingMenu>> XP_RING_MENU =
            MENUS.register("xp_ring", () -> IMenuTypeExtension.create(XpRingMenu::new));

    public XpRingMod(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        MENUS.register(modEventBus);
        modEventBus.addListener(this::addToCreativeTab);
    }

    private void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(XP_RING);
        }
    }
}
