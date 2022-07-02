package com.leafdawn.gui.draw;

import com.leafdawn.gui.draw.PrimShowFrame;
import com.leafdawn.pojo.Graph;
import com.leafdawn.service.impl.ParamCheckServiceImpl;
import com.leafdawn.pojo.ALViewFrame;
import com.leafdawn.service.impl.PrimServiceImpl;
import com.leafdawn.util.GraphUtil;
import com.leafdawn.util.TypeChangeUtil;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 输入图
 * @author fzw
 **/
@Data
public class InputParam {

    private Graph graph;

    private Integer begin;

    private Integer time = 3 * 1000;

    /**
     * 检查输入参数合法性实体类
     */
    private ParamCheckServiceImpl paramCheckServiceImpl = new ParamCheckServiceImpl();

    private PrimServiceImpl primService = new PrimServiceImpl();

    /**
     * 随机输入时的输入
     */
    public void randomInputParam(int vexNum, int edgeNum) {
        //获取随机图
        graph = primService.getRandomGraph(vexNum,edgeNum);
        //获取开始节点
        ALViewFrame alViewFrame = new ALViewFrame("输入开始节点");
        showBeginNode(alViewFrame,alViewFrame,graph.getVertexsNum());

    }

    /**
     *  - 只输入顶点数
     *  矩阵输入
     * @param vexNum 顶点数
     */
    public void matrixInputParam(int vexNum) {
        ALViewFrame aLViewFrame = new ALViewFrame("输入邻接矩阵");
        JDialog dialog = new JDialog(aLViewFrame, "输入参数", true);

        // 控制面板布局：3行1列，行列间距都是10
        JPanel[] panels = {
                new JPanel(),
                new JPanel(new GridLayout(vexNum,vexNum,5,5)),
                new JPanel(),
        };

        // 设置窗口布局
        dialog.setLayout(new GridLayout(3,1));
        dialog.setSize(600,500);

        // 各个组件
        JLabel label1 = new JLabel("请输入各个顶点值(0-9)：");
        JTextField[] vexText  = new JTextField[vexNum];

        JLabel label = new JLabel("权值取值1-10" );
        JTextField[] texts = new JTextField[vexNum * vexNum];

        JButton okBtn = new JButton("确认");
        // 面板加入组件
        panels[0].add(label1);

        for (int i = 0;i < vexText.length;i++) {
            vexText[i] = new JTextField(3);
            panels[0].add(vexText[i]);
        }
        panels[0].add(label);
        // 矩阵文本框初始化
        for (int i = 0;i < texts.length;i++) {
            texts[i] = new JTextField();
        }

        // 将矩阵下三角置为不可填
        for (int i = 0;i < vexNum;i++) {
            for (int j = i;j >= 0;j--) {
                if (j == i) {
                    texts[vexNum * i + i] = new JTextField("0");
                }
                texts[vexNum * i + j].setEditable(false);
            }
        }
        // 将文本框加入面板
        for (JTextField text : texts) {
            panels[1].add(text);
        }
        panels[2].add(okBtn);
        // 窗口中加入面板
        for (JPanel jp : panels) {
            dialog.add(jp);
        }

        // 点击确认，收集输入信息
        okBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 先检查顶点值合法性
                List<String> vertex = new ArrayList<>();
                String vexMessage = paramCheckServiceImpl.checkVex(vexText);
                if ("legal".equals(vexMessage)) {
                    // 用于获取顶点值
                    // 输入参数无误后，获取顶点值
                    for (JTextField v : vexText) {
                        vertex.add(v.getText());
                    }
                }else {
                    JOptionPane.showMessageDialog(dialog,vexMessage,"提示",JOptionPane.INFORMATION_MESSAGE);
                }
                int[][] edges = new int[vertex.size()][vertex.size()];
                String edgeMessage = paramCheckServiceImpl.checkEdge(texts,vertex.size());
                if ("legal".equals(edgeMessage)) {
                    for (int i = 0;i < edges.length;i++) {
                        for (int j = i + 1;j < edges[i].length;j++) {
                            // 元素m表示无穷大，即无边
                            if ("m".equals(texts[vertex.size() * i + j].getText())) {
                                edges[i][j] = Integer.MAX_VALUE;
                                edges[j][i] = Integer.MAX_VALUE;
                            }else {
                                edges[i][j] = Integer.parseInt(texts[vertex.size() * i + j].getText());
                                edges[j][i] = edges[i][j];
                            }
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(dialog,edgeMessage,"提示",JOptionPane.INFORMATION_MESSAGE);
                }
                // 检测并获取图
                String connMessage = paramCheckServiceImpl.checkConnectivity(edges,vertex);
                if ("legal".equals(connMessage)) {
                    // 创建一个图结构
                    graph = new Graph(vertex,edges);
                }else {
                    JOptionPane.showMessageDialog(dialog,connMessage,"提示",JOptionPane.INFORMATION_MESSAGE);
                }

                //获取开始节点
                showBeginNode(aLViewFrame,aLViewFrame,graph.getVertexsNum());


            }
        });

        dialog.setVisible(true);
    }

    /**
     *  - 输入顶点数和边数
     *  顶点和边输入
     * @param vexNum 顶点数
     * @param edgeNum 边数
     */
    public void pointEdgeInputParam(int vexNum, int edgeNum) {
        ALViewFrame aLViewFrame = new ALViewFrame("输入边");
        JDialog dialog = new JDialog(aLViewFrame, "输入参数", true);

        JPanel[] panels = {
                new JPanel(),
                new JPanel(),
                new JPanel(),
        };
        JPanel[] panels1;

        // 根据边数变化窗口的高度以及面板panels1的数量
        int size;
        if (edgeNum % 3 == 0) {
            size = edgeNum/3;
        }else {
            size = edgeNum/3 + 1;
        }
        panels1 = new JPanel[size];

        // 设置窗口布局
        dialog.setLayout(new GridLayout(3 + size,1));
        dialog.setSize(860,175 + size * 100);
        JLabel label = new JLabel("请按格式输入边的邻接顶点(1-9)和权值(1-10)，如(1 9 5)");
        JLabel label1 = new JLabel( "请输入各个顶点值(1-9)：");
        JTextField[] vexText  = new JTextField[vexNum];
        JButton okBtn = new JButton("确认");
        // 面板加入组件
        panels[0].add(label);
        panels[1].add(label1);
        for (int i = 0;i < vexText.length;i++) {
            vexText[i] = new JTextField(3);
            panels[1].add(vexText[i]);
        }
        // 创建标签数组
        String[] inputManage = new String[edgeNum];
        for (int i = 0;i < inputManage.length;i++) {
            inputManage[i] = "第"+ (i+1) +"条边信息：";
        }

        JLabel[] jls = new JLabel[edgeNum];
        for (int i = 0;i < jls.length;i++) {
            jls[i] = new JLabel(inputManage[i]);
        }

        // 创建文本数组
        JTextField[] texts = new JTextField[edgeNum * 3];
        for (int i = 0;i < texts.length;i++) {
            texts[i] = new JTextField(3);
        }

        // 根据边数加入对应的标签和文本组件到面板中
        for (int j = 0,i = 0,k = 0;j < panels1.length;j++,k += 3,i += 9) {
            panels1[j] = new JPanel();

            panels1[j].add(jls[k]);
            panels1[j].add(texts[i]);
            panels1[j].add(texts[i + 1]);
            panels1[j].add(texts[i + 2]);

            if (k + 1 < jls.length) {
                panels1[j].add(jls[k + 1]);
                panels1[j].add(texts[i + 3]);
                panels1[j].add(texts[i + 4]);
                panels1[j].add(texts[i + 5]);
            }

            if (k + 2 < jls.length) {
                panels1[j].add(jls[k + 2]);
                panels1[j].add(texts[i + 6]);
                panels1[j].add(texts[i + 7]);
                panels1[j].add(texts[i + 8]);
            }
        }
        panels[2].add(okBtn);
        // 窗口中加入面板
        dialog.add(panels[0]);
        dialog.add(panels[1]);
        for (JPanel jp : panels1) {
            dialog.add(jp);
        }
        dialog.add(panels[2]);

        // 点击确认收集输入信息
        okBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // 检验并获取顶点
                List<String> vertex = new ArrayList<>();
                String vexMessage = paramCheckServiceImpl.checkVex(vexText);
                if ("legal".equals(vexMessage)) {
                    // 顶点值合法，获取各个顶点
                    for (int i = 0; i < vexNum; i++) {
                        vertex.add(vexText[i].getText());
                    }
                } else {
                    JOptionPane.showMessageDialog(aLViewFrame, vexMessage, "提示", JOptionPane.INFORMATION_MESSAGE);
                }

                // 检验并获取边
                int[][] edges = new int[][]{};
                String adjMessage = paramCheckServiceImpl.checkAdj(texts, vertex);
                if ("legal".equals(adjMessage)) {
                    // 边信息合法，创建边信息数组
                    edges = transformTextToEdges(vexNum, edgeNum, texts, vertex);
                    // 生成边权值后，把剩下的0元素都改成无穷大m
                    GraphUtil.setZeroToUnlimited(edges);
                } else {
                    JOptionPane.showMessageDialog(aLViewFrame, adjMessage, "提示", JOptionPane.INFORMATION_MESSAGE);
                }


                // 判断生成的图是否是连通图,并获取图
                String connMessage = paramCheckServiceImpl.checkConnectivity(edges, vertex);
                if ("legal".equals(connMessage)) {
                    graph = new Graph(vertex, edges);
                } else {
                    JOptionPane.showMessageDialog(aLViewFrame, connMessage, "提示", JOptionPane.INFORMATION_MESSAGE);
                }

                //获取开始节点
                showBeginNode(aLViewFrame, aLViewFrame, graph.getVertexsNum());

            }
        });

        dialog.setVisible(true);
    }


    /**
     * 输入Prim算法执行起始顶点
     * @param owner 对话框拥有者
     * @param parentComponent 对话框父级组件
     * @param vexNum 顶点数
     */
    private void showBeginNode(ALViewFrame owner, Component parentComponent, int vexNum) {
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(owner, "输入参数", true);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // 设置对话框的宽高
        dialog.setSize(350, 160);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(parentComponent);
        JLabel label1 = new JLabel("请输入算法执行起始顶点下标");
        JLabel label2 = new JLabel("范围：" + "[" + "0-" + (vexNum-1) + "]");
        JTextField text = new JTextField();

        JButton okBtn = new JButton("确认");

        JTextField[] texts = {text};

        TypeChangeUtil.bestStyle(texts,100,30);

        panel.add(label1);
        panel.add(text);
        panel.add(label2);
        panel.add(okBtn);

        // 点击确认
        okBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = text.getText();
                // 检测输入内容合法性
                String message = paramCheckServiceImpl.checkFirstVex(content, vexNum);
                if ("legal".equals(message)) {

                    // 如果合法，就将输入值直接记住（改变全局变量fVex）
                    begin = Integer.parseInt(content);
                }
                owner.dispose();
                dialog.dispose();
            }
        });

        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);
    }


    /**
     * 从text中获取边
     */
    private int[][] transformTextToEdges(int vexNum, int edgeNum, JTextField[] texts,List<String> vertex) {
        int index1 = 0,index2 = 0;
        // 边信息合法，创建边信息数组
        int[][] edges = new int[vexNum][vexNum];
        // 初始化，元素全部置为0
        for (int[] edge : edges) {
            // 新操作
            Arrays.fill(edge, 0);
        }
        // 先把邻接顶点和权值存起来
        String[] adj = new String[edgeNum * 2];
        String[] weights = new String[edgeNum];
        for (int i = 0,k = 0,a = 0;i < texts.length;i += 3) {
            adj[k] = texts[i].getText();
            adj[k + 1] = texts[i + 1].getText();
            weights[a] = texts[i + 2].getText();
            k += 2;
            a++;
        }
        // 遍历顶点值集合，找到邻接顶点对应位置
        for (int j = 0,k = 0;j < adj.length;j += 2) {
            // 找到邻接顶点索引值
            for (int i = 0;i < vertex.size();i++) {
                if (adj[j].equals(vertex.get(i))) {
                    index1 = i;
                }
                if (adj[j + 1].equals(vertex.get(i))) {
                    index2 = i;
                }
            }
            // 再对应边位置存入权值
            edges[index1][index2] = Integer.parseInt(weights[k]);
            edges[index2][index1] = edges[index1][index2];
            k++;
        }
        return edges;
    }


}
