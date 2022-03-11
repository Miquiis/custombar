package me.miquiis.custombar.common;

import me.miquiis.custombar.CustomBar;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.UUID;

public class BarInfo {

    private static final ResourceLocation DEFAULT_BAR_TEXTURE = new ResourceLocation(CustomBar.MOD_ID, "textures/gui/default_bar.png");
    private static final int[] DEFAULT_TEXT_COLOR = new int[]{255,255,255};

    private final UUID uniqueID;
    protected UUID playerID;
    protected String stringID;
    protected ITextComponent text;
    protected float percent;
    protected int[] rgbColor;
    protected ResourceLocation texture;
    protected boolean shouldSave;

    @Nullable
    protected UUID targetEntity;
    protected boolean deleteAfterDeath = false;

    public BarInfo(UUID uniqueID, ITextComponent text)
    {
        this(uniqueID, text, 1f);
    }

    public BarInfo(UUID uniqueID, ITextComponent text, float percent)
    {
        this(uniqueID, text, percent, DEFAULT_TEXT_COLOR);
    }

    public BarInfo(UUID uniqueID, ITextComponent text, float percent, int[] rgbColor)
    {
        this.uniqueID = uniqueID;
        this.stringID = null;
        this.text = text;
        this.percent = percent;
        this.rgbColor = rgbColor == null ? DEFAULT_TEXT_COLOR : rgbColor;
        this.texture = DEFAULT_BAR_TEXTURE;
        this.shouldSave = true;
    }

    public BarInfo(UUID uniqueID, ITextComponent text, float percent, ResourceLocation texture, int[] rgbColor)
    {
        this(uniqueID, text, percent, rgbColor);
        if (texture != null) this.texture = texture;
    }

    public BarInfo(UUID uniqueID, ITextComponent text, float percent, ResourceLocation texture, int[] rgbColor, boolean shouldSave)
    {
        this(uniqueID, text, percent, texture, rgbColor);
        this.shouldSave = shouldSave;
    }

    public BarInfo(UUID uniqueID, String stringID, ITextComponent text, float percent, ResourceLocation texture, int[] rgbColor, boolean shouldSave)
    {
        this(uniqueID, text, percent, texture, rgbColor);
        this.shouldSave = shouldSave;
        this.stringID = stringID;
    }

    public void setTargetEntity(@Nullable UUID targetEntity) {
        this.targetEntity = targetEntity;
    }

    public void setDeleteAfterDeath(boolean bool) { this.deleteAfterDeath = bool; }

    public void setPercent(float percent)
    {
        this.percent = percent;
    }

    public void setPlayer(UUID playerID) { this.playerID = playerID; }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public void setText(ITextComponent text)
    {
        this.text = text;
    }

    public void setRgbColor(int[] rgbColor) { this.rgbColor = rgbColor; }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public ITextComponent getText() {
        return text;
    }

    public float getPercent() {
        return percent;
    }

    public String getFormattedPercent()
    {
        return "" + Math.round(percent * 100);
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public int[] getRawColor() {
        return rgbColor;
    }

    public Color getColor() {
        return new Color(rgbColor[0], rgbColor[1], rgbColor[2]);
    }

    public boolean shouldSave() {
        return shouldSave;
    }

    public String getStringID() {
        return stringID;
    }

    @Nullable
    public UUID getTargetEntity() {
        return targetEntity;
    }

    public boolean shouldDeleteAfterDeath() {
        return deleteAfterDeath;
    }

    public ResourceLocation getTexture() {
        return texture;
    }
}
