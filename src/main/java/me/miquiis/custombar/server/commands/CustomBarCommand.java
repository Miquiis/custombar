package me.miquiis.custombar.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.miquiis.custombar.common.BarManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ColorArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;
import java.util.UUID;

public class CustomBarCommand {

    public CustomBarCommand(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(
                Commands.literal("custombar")
                        .then(Commands.literal("add").then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
                            final ITextComponent name = new StringTextComponent(StringArgumentType.getString(context, "name"));
                            return addCustomBar(name);
                        }).then(Commands.argument("percent", FloatArgumentType.floatArg()).executes(context -> {
                            final ITextComponent name = new StringTextComponent(StringArgumentType.getString(context, "name"));
                            final float percent = FloatArgumentType.getFloat(context, "percent");
                            return addCustomBar(name, percent);
                        }).then(Commands.argument("shouldSave", BoolArgumentType.bool()).executes(context -> {
                            final ITextComponent name = new StringTextComponent(StringArgumentType.getString(context, "name"));
                            final float percent = FloatArgumentType.getFloat(context, "percent");
                            final boolean shouldSave = BoolArgumentType.getBool(context, "shouldSave");
                            return addCustomBar(name, percent, shouldSave);
                        }).then(Commands.argument("color", StringArgumentType.string()).executes(context -> {
                            final ITextComponent name = new StringTextComponent(StringArgumentType.getString(context, "name"));
                            final float percent = FloatArgumentType.getFloat(context, "percent");
                            final boolean shouldSave = BoolArgumentType.getBool(context, "shouldSave");
                            final int[] rgbColor = Arrays.stream(StringArgumentType.getString(context, "color").split(",")).mapToInt(Integer::parseInt).toArray();
                            return addCustomBar(name, percent, shouldSave, rgbColor);
                        }).then(Commands.argument("texture", ResourceLocationArgument.resourceLocation()).executes(context -> {
                            final ITextComponent name = new StringTextComponent(StringArgumentType.getString(context, "name"));
                            final float percent = FloatArgumentType.getFloat(context, "percent");
                            final boolean shouldSave = BoolArgumentType.getBool(context, "shouldSave");
                            final int[] rgbColor = Arrays.stream(StringArgumentType.getString(context, "color").split(",")).mapToInt(Integer::parseInt).toArray();
                            final ResourceLocation texture = ResourceLocationArgument.getResourceLocation(context, "texture");
                            return addCustomBar(name, percent, shouldSave, rgbColor, texture);
                        })))))))
                        .then(Commands.literal("remove").then(Commands.argument("name", StringArgumentType.string()).executes(context -> {
                            final String name = StringArgumentType.getString(context, "name");
                            return removeCustomBar(name);
                        })))
                        .then(Commands.literal("clear").executes(context -> {
                            return clearBars();
                        }))
                        .then(Commands.literal("update").then(Commands.argument("name", StringArgumentType.string()).then(Commands.argument("percent", FloatArgumentType.floatArg()).executes(context -> {
                            final String name = StringArgumentType.getString(context, "name");
                            final float percent = FloatArgumentType.getFloat(context, "percent");
                            return updateBar(name, percent);
                        }))))
                        .then(Commands.literal("assign").then(Commands.argument("name", StringArgumentType.string()).then(Commands.argument("entity", EntityArgument.entity()).executes(context -> {
                            final String name = StringArgumentType.getString(context, "name");
                            final Entity entity = EntityArgument.getEntity(context, "entity");

                            if (!(entity instanceof LivingEntity)) return -1;

                            final LivingEntity livingEntity = (LivingEntity) entity;
                            final float percent = livingEntity.getHealth() / livingEntity.getMaxHealth();
                            return assignBar(name, entity.getUniqueID(), percent, false);
                        }).then(Commands.argument("removeOnDeath", BoolArgumentType.bool()).executes(context -> {
                            final String name = StringArgumentType.getString(context, "name");
                            final Entity entity = EntityArgument.getEntity(context, "entity");
                            final boolean removeOnDeath = BoolArgumentType.getBool(context, "removeOnDeath");

                            if (!(entity instanceof LivingEntity)) return -1;

                            final LivingEntity livingEntity = (LivingEntity) entity;
                            final float percent = livingEntity.getHealth() / livingEntity.getMaxHealth();
                            return assignBar(name, entity.getUniqueID(), percent, removeOnDeath);
                        })))))
        );
    }

    private int assignBar(String name, UUID entityID, float percent, boolean removeOnDeath) {
        BarManager.updateBar(name, entityID, percent, removeOnDeath);
        return 1;
    }

    private int updateBar(String text, float percent) {
        BarManager.updateBar(text, percent);
        return 1;
    }

    private int addCustomBar(ITextComponent text, float percent, boolean shouldSave, int[] rgbColor, ResourceLocation texture)
    {
        System.out.println(shouldSave);
        System.out.println(rgbColor);
        System.out.println(texture);
        BarManager.addBar(UUID.randomUUID(), text, percent, texture, rgbColor, shouldSave);
        return 1;
    }

    private int addCustomBar(ITextComponent text, float percent, boolean shouldSave, int[] rgbColor)
    {
        addCustomBar(text, percent, shouldSave, rgbColor, null);
        return 1;
    }

    private int addCustomBar(ITextComponent text, float percent, boolean shouldSave)
    {
        addCustomBar(text, percent, shouldSave, null, null);
        return 1;
    }

    private int addCustomBar(ITextComponent text, float percent)
    {
        BarManager.addBar(text, percent);
        return 1;
    }

    private int addCustomBar(ITextComponent text)
    {
        return addCustomBar(text, 0f);
    }

    private int removeCustomBar(String name)
    {
        BarManager.removeBar(name);
        return 1;
    }

    private int clearBars()
    {
        BarManager.clearBars();
        return 1;
    }

}
