package com.leafdawn.pojo;

import javax.swing.*;

/**
 * 统一的窗口
 * @author fzw
 **/
public class ALViewFrame extends JFrame {

    public ALViewFrame(String title) {
        this.setTitle(title);
        // 设置图标
        this.setIconImage(new ImageIcon("brand.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

