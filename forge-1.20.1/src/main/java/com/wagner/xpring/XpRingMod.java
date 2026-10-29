package com.wagner.xpring;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(XpRingMod.MOD_ID)
public final class XpRingMod {
    public static final String MOD_ID = "xp_ring";

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public static final RegistryObject<XpRingBlock> XP_RING_BLOCK = BLOCKS.register(
            "xp_ring",
            () -> new XpRingBlock(BlockBehaviour.Properties.of()
                    .strength(0.4F)
                    .sound(SoundType.METAL)
                    .noCollission()
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY))
    );

    public static final RegistryObject<XpRingItem> XP_RING = ITEMS.register(
            "xp_ring",
            () -> new XpRingItem(XP_RING_BLOCK.get(), new Item.Properties().stacksTo(1))
    );

    public static final RegistryObject<BlockEntityType<XpRingBlockEntity>> XP_RING_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("xp_ring", () ->
                    BlockEntityType.Builder.of(XpRingBlockEntity::new, XP_RING_BLOCK.get()).build(null));

    public static final RegistryObject<MenuType<XpRingMenu>> XP_RING_MENU =
            MENUS.register("xp_ring", () -> IForgeMenuType.create(XpRingMenu::new));

    public XpRingMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
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
