package me.miquiis.custombar.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import me.miquiis.custombar.common.BarInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class BarOverlay extends AbstractGui {

    private final Minecraft client;
    private final BarInfo barInfo;

    public BarOverlay(Minecraft client, BarInfo barInfo)
    {
        this.client = client;
        this.barInfo = barInfo;
    }

    public void draw(MatrixStack matrixStack, int currentBar) {
        int i = this.client.getMainWindow().getScaledWidth();
        int j = 15 + ((10 + this.client.fontRenderer.FONT_HEIGHT) * (currentBar - 1));

        int k = i / 2 - 91;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(barInfo.getTexture());
        this.drawBars(matrixStack, k, j, barInfo);
        ITextComponent itextcomponent = new StringTextComponent(barInfo.getText().getString().replace("%PERCENT%", barInfo.getFormattedPercent()));
        int l = this.client.fontRenderer.getStringPropertyWidth(itextcomponent);
        int i1 = i / 2 - l / 2;
        int j1 = j - 11;
        this.client.fontRenderer.drawTextWithShadow(matrixStack, itextcomponent, (float)i1, (float)j1, barInfo.getColor().getRGB());
    }

    private void drawBars(MatrixStack matrixStack, int x, int y, BarInfo p_238485_4_) {
        blit(matrixStack, x, y, 0, 0, 182, 5, 182, 10);

        int i = Math.min((int)(p_238485_4_.getPercent() * 183.0F), 183);

        if (i > 0) {
            blit(matrixStack, x, y, 0, 5, i, 5,182, 10);
        }

    }

}
