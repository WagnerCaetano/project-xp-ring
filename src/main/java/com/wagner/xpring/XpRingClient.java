package com.wagner.xpring;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = XpRingMod.MOD_ID, value = Dist.CLIENT)
public final class XpRingClient {
    private XpRingClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(XpRingMod.XP_RING_MENU.get(), XpRingScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(XpRingMod.XP_RING_BLOCK_ENTITY.get(), XpRingRenderer::new);
    }
}
