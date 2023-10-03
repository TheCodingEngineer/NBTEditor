package me.letscode.minecraft.tools.nbt.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Resources {

    public static ImageIcon getImage(String path) {
        return new ImageIcon(Resources.class.getResource(path));
    }

    public static Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return (resizedImg);
    }

    public static ImageIcon getScaledImage(ImageIcon srcImg, int w, int h){
        return new ImageIcon(getScaledImage(srcImg.getImage(), w, h));
    }

}
