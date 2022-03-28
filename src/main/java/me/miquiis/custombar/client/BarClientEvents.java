package me.miquiis.custombar.client;

import me.miquiis.custombar.CustomBar;
import me.miquiis.custombar.common.BarInfo;
import me.miquiis.custombar.common.BarManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CustomBar.MOD_ID, value = Dist.CLIENT)
public class BarClientEvents {

    private static Minecraft client;

    private static Minecraft getClient()
    {
        if (client == null) client = Minecraft.getInstance();
        return client;
    }

    @SubscribeEvent
    public static void onGUIRender(RenderGameOverlayEvent.Text event)
    {
        Minecraft client = getClient();
        int currentBar = 1;
        for (BarInfo barInfo : BarManager.getCurrentActiveBars().values())
        {
            new BarOverlay(client, barInfo).draw(event.getMatrixStack(), currentBar++);
        }
    }

}
