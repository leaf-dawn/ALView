package com.leafdawn.gui.draw;

import com.leafdawn.pojo.Graph;
import com.leafdawn.pojo.ALViewFrame;
import com.leafdawn.service.ParamCheckService;
import com.leafdawn.service.PrimService;
import com.leafdawn.service.impl.ParamCheckServiceImpl;
import com.leafdawn.service.impl.PrimServiceImpl;
import com.leafdawn.util.TypeChangeUtil;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * draw包向外部提供的最主要功能
 * 这里最主要的功能是监听不同的按钮，并调用graphPainting来进行绘画
 * prim算法演示面板的窗口
 * @author fzw
 **/
public class PrimShowFrame {
    ParamCheckService paramCheckService = new ParamCheckServiceImpl();
    /** prim的绘画类 */
    private GraphPainting paintG;
    private boolean isPause = false;
    public int firstVex;
    private int time = 2*1000;
    private Thread runThread;
    private PrimService primService = new PrimServiceImpl();


    public PrimShowFrame() {
        final ALViewFrame primShowFrame = new ALViewFrame("prim");

        // 设置窗口的大小和布局
        primShowFrame.setSize(1000, 600);
        primShowFrame.setLayout(new BorderLayout());
        // 新建绘画类
        this.paintG = new GraphPainting(new Graph(),time);
        // 放置于下面的按钮
        JPanel panel = new JPanel();
        // 控制演示的按键
        JButton startBtn = new JButton("开始演示");
        JButton pauseBtn = new JButton("暂停/继续演示");
        JButton nextBtn = new JButton("下一步");
        panel.add(startBtn);
        panel.add(pauseBtn);
        panel.add(nextBtn);
        //添加控制时间的按钮
        JTextField timeText = new JTextField();
        timeText.setText("2");
        TypeChangeUtil.bestStyle(new JTextField[]{timeText},100,30);
        JButton timeOk = new JButton("确认");
        panel.add(timeText);
        panel.add(timeOk);
        //添加图形输入按钮
        JButton randomBtn = new JButton("随机生成图");
        JMenuBar selfBar = new JMenuBar();
        JMenu selfMenu = new JMenu("手动输入图");
        JMenuItem matrixItem = new JMenuItem("输入邻接矩阵");
        JMenuItem edgesItem = new JMenuItem("输入边");
        selfMenu.add(matrixItem);
        selfMenu.add(edgesItem);
        selfBar.add(selfMenu);
        Panel panel1 = new Panel();
        panel1.add(randomBtn);
        panel1.add(selfBar);
        //开始时全部设置为不可点击
        startBtn.setEnabled(false);
        pauseBtn.setEnabled(false);
        nextBtn.setEnabled(false);

        primShowFrame.add(panel1,BorderLayout.NORTH);
        primShowFrame.add(paintG.graphBasePanel,BorderLayout.CENTER);
        primShowFrame.add(panel,BorderLayout.SOUTH);

        // 点击随机生成
        randomBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] strings = showDialog(primShowFrame, primShowFrame, 2);
                // 检测输入参数合法性
                String message = paramCheckService.checkVexAndEdgeNum(strings);
                if ("legal".equals(message)) {
                    InputParam inputParam = new InputParam();
                    inputParam.randomInputParam(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
                    //重新开启画板
                    paintG.reset(inputParam.getGraph(),time);
                    //刷新
                    paintG.getGraphBasePanel().repaint();
                    //设置开启为可点击
                    startBtn.setEnabled(true);
                    pauseBtn.setEnabled(true);
                    nextBtn.setEnabled(true);
                }else {
                    JOptionPane.showMessageDialog(primShowFrame,message,"提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 点击输入矩阵
        matrixItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[] strings = showDialog(primShowFrame, primShowFrame, 1);
                // 输入值为空时不进行任何操作
                if (strings[0] != null) {
                    // 检测输入参数合法性
                    String message = paramCheckService.checkVexAndEdgeNum(strings);
                    if ("legal".equals(message)) {
                        // 传入顶点数
                        InputParam inputParam = new InputParam();
                        inputParam.matrixInputParam(Integer.parseInt(strings[0]));;
                        //重新开启画板
                        paintG.reset(inputParam.getGraph(),time);
                        //刷新
                        paintG.getGraphBasePanel().repaint();
                        //设置开启为可点击
                        startBtn.setEnabled(true);
                        pauseBtn.setEnabled(true);
                        nextBtn.setEnabled(true);
                    }else {
                        JOptionPane.showMessageDialog(primShowFrame,message,"提示",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // 点击输入边
        edgesItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] strings = showDialog(primShowFrame, primShowFrame, 2);
                // 检测输入参数合法性
                String message = paramCheckService.checkVexAndEdgeNum(strings);
                if ("legal".equals(message)) {
                    // 获取顶点和边
                    InputParam inputParam = new InputParam();
                    inputParam.pointEdgeInputParam(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
                    //重新开启画板
                    paintG.reset(inputParam.getGraph(),time);
                    //刷新
                    paintG.getGraphBasePanel().repaint();
                    //设置开启为可点击
                    startBtn.setEnabled(true);
                    pauseBtn.setEnabled(true);
                    nextBtn.setEnabled(true);
                }else {
                    JOptionPane.showMessageDialog(primShowFrame,message,"提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        // 监听演示按键
        startBtn.addActionListener(new AbstractAction() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                if (runThread != null) {
                    runThread.stop();
                }
                //开启新线程
                runThread = new Thread(() -> {
                    // 算法执行前先把原图画出来
                    paintG.drawGraph(null);
                    paintG.graphPainting(firstVex);
                });
                runThread.start();
               // 执行完开始按键后把按键置为不可点击
               startBtn.setEnabled(false);

            }
        });

        // 暂定/继续按键
        pauseBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPause) {
                    // 暂停线程
                    paintG.pauseThread();
                    isPause = true;
                }else {
                    // 恢复线程
                    paintG.resumeThread();
                    isPause = false;
                }
                // 结束，隐藏按钮
                if (paintG.graphBasePanel.getIsEnd()) {
                    pauseBtn.setEnabled(false);
                }


            }
        });

        /* 监听下一步 */
        nextBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 如果程序是停止的
                    if (isPause) {
                        // 先设置速度
                        GraphPainting.sleepTime = 10;
                        //下一步
                        Thread.sleep(10);
                        paintG.resumeThread();
                        Thread.sleep(10);
                        paintG.pauseThread();
                        Thread.sleep(10);
                        //设置回原有速度
                        GraphPainting.sleepTime = time;
                    }

                    // 结束则关闭按钮
                    if (paintG.getGraphBasePanel().getIsEnd()) {
                        nextBtn.setEnabled(false);
                    }
                } catch(InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        });

        timeOk.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = timeText.getText();
                GraphPainting.sleepTime = Long.valueOf(content)*1000;

            }
        });

        primShowFrame.setLocationRelativeTo(null);
        primShowFrame.setVisible(true);
    }

    /**
     * 输入顶点数和边数的对话框
     * @param owner 对话框拥有者
     * @param parentComponent 对话框父级组件
     * @param rows 是否需要输入边数
     * @return 返回输入的信息
     */
    private String[] showDialog(ALViewFrame owner, Component parentComponent, int rows) {
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(owner, "输入参数", true);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // 获取输入的内容
        String[] contents = new String[rows];

        // 设置对话框的宽高
        dialog.setSize(300, 160);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);

        JLabel vLabel = new JLabel("请输入顶点数：");
        JLabel eLabel = new JLabel("请输入边数：");
        JTextField vText = new JTextField();
        JTextField eText = new JTextField();
        JButton okBtn = new JButton("确认");
        TypeChangeUtil.bestStyle(new JTextField[]{vText, eText},80,30);
        // 添加组件到面板
        panel.add(vLabel);
        panel.add(vText);
        //选择输入边
        if (rows == 2) {
            panel.add(eLabel);
            panel.add(eText);
        }
        panel.add(okBtn);

        okBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取输入的参数
                contents[0] = vText.getText();
                if (rows == 2) {
                    contents[1] = eText.getText();
                }

                dialog.dispose();
            }
        });
        // 设置对话框的内容面板
        dialog.add(panel);
        // 显示对话框
        dialog.setVisible(true);
        return contents;
    }


}
