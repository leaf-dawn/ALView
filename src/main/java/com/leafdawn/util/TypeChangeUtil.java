package com.leafdawn.util;

import javax.swing.*;
import java.awt.*;

/**
 * 改变文本样式工具
 * @author leafdawn
 **/
public class TypeChangeUtil {
    /**
     * 改变文本样式
     */
    public static void bestStyle(JTextField[] texts,
                          int width, int height) {
        // 用于改变按键或文本框大小
        Dimension dimension = new Dimension(width,height);
        if (texts != null) {
            // 文本框大小
            for (JTextField jt : texts) {
                jt.setPreferredSize(dimension);
            }
        }
    }
}
