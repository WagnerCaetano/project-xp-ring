package com.wagner.xpring;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(XpRingMod.MOD_ID)
public final class XpRingMod {
    public static final String MOD_ID = "xp_ring";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MOD_ID);

    public static final DeferredBlock<XpRingBlock> XP_RING_BLOCK = BLOCKS.register(
            "xp_ring",
            () -> new XpRingBlock(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .sound(SoundType.METAL)
                    .noCollission()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY))
    );

    public static final DeferredItem<XpRingItem> XP_RING = ITEMS.register(
            "xp_ring",
            () -> new XpRingItem(XP_RING_BLOCK.get(), new Item.Properties().stacksTo(1))
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<XpRingBlockEntity>> XP_RING_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("xp_ring", () ->
                    BlockEntityType.Builder.of(XpRingBlockEntity::new, XP_RING_BLOCK.get()).build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<XpRingMenu>> XP_RING_MENU =
            MENUS.register("xp_ring", () -> IMenuTypeExtension.create(XpRingMenu::new));

    public XpRingMod(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
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
