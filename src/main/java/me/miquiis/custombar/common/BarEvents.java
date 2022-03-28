package me.miquiis.custombar.common;

import me.miquiis.custombar.CustomBar;
import me.miquiis.custombar.client.BarOverlay;
import me.miquiis.custombar.server.BarSave;
import me.miquiis.custombar.server.commands.CustomBarCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = CustomBar.MOD_ID)
public class BarEvents {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event)
    {
        new CustomBarCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event)
    {
        BarManager.getCurrentActiveBars().forEach((uuid, barInfo) -> {
            if (barInfo.getTargetEntity() != null && barInfo.getTargetEntity().equals(event.getEntity().getUniqueID()))
            {
                final LivingEntity livingEntity = (LivingEntity) event.getEntity();
                float percent = (livingEntity.getHealth() - event.getAmount()) / livingEntity.getMaxHealth();
                if (percent < 0.0f) percent = 0f;
                if (barInfo.getPercent() != percent){
                    BarManager.updateBar(barInfo.getUniqueID(), percent);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event)
    {
        BarInfo toDelete = null;
        for (BarInfo barInfo : BarManager.getCurrentActiveBars().values())
        {
            if (barInfo.getTargetEntity() != null && barInfo.getTargetEntity().equals(event.getEntity().getUniqueID()))
            {
                if (barInfo.shouldDeleteAfterDeath())
                {
                    toDelete = barInfo;
                    break;
                }
                else
                    BarManager.updateBar(barInfo.getUniqueID(), null, 0f, barInfo.shouldDeleteAfterDeath());
            }
        }
        if (toDelete != null) BarManager.removeBar(toDelete.getUniqueID());
    }

    @SubscribeEvent
    public static void onServerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getPlayer().world.isRemote) return;
        BarManager.syncPlayer((ServerPlayerEntity)event.getPlayer());
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event)
    {
        if (!event.getWorld().isRemote())
        {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            BarSave barSave = BarSave.forWorld(serverWorld);
            barSave.saveBars(BarManager.getCurrentActiveBars().values());
            barSave.markDirty();
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event)
    {
        if (!event.getWorld().isRemote())
        {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            BarSave barSave = BarSave.forWorld(serverWorld);
            barSave.loadBars().forEach(BarManager::addBar);
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event)
    {
        BarManager.clearBars();
    }

}
