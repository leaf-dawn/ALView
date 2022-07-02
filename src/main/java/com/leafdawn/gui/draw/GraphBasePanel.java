package com.leafdawn.gui.draw;

import com.leafdawn.pojo.Edge;
import com.leafdawn.pojo.Graph;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图相关面板
 * @author fzw
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class GraphBasePanel extends JPanel {

    /** 参数Map */
    GraphicalParam graphicalParam = new GraphicalParam();

    private List<String> vertexs;
    private int vexNum;
    private int edgeNum;
    private Edge[] edgeData;

    private Boolean isEnd = false;
    private List<Integer> adj = new ArrayList<>();
    private int v1 = -1,v2 = -1;

    /**
     * 构造方法
     *  - 传入图结构
     * @param graph 图结构
     */
    public GraphBasePanel(Graph graph) {
        super();
        this.vertexs = graph.getVertexs();
        this.vexNum = graph.getVertexsNum();
        this.edgeNum = graph.getEdgeNum();
        int[][] edges = graph.getEdges();
        edgeData = new Edge[edgeNum];
        int index = 0;
        // 获取边信息
        for (int i = 0;i < vexNum;i++) {
            for (int j = i + 1;j < vexNum;j++) {
                if (edges[i][j] != 0 && edges[i][j] != Integer.MAX_VALUE) {
                    edgeData[index++] = new Edge(vertexs.get(i),vertexs.get(j), edges[i][j]);
                }
            }
        }
    }

    public void reset(Graph graph) {
        this.vertexs = graph.getVertexs();
        this.vexNum = graph.getVertexsNum();
        this.edgeNum = graph.getEdgeNum();
        int[][] edges = graph.getEdges();
        edgeData = new Edge[edgeNum];
        int index = 0;
        // 获取边信息
        for (int i = 0;i < vexNum;i++) {
            for (int j = i + 1;j < vexNum;j++) {
                if (edges[i][j] != 0 && edges[i][j] != Integer.MAX_VALUE) {
                    edgeData[index++] = new Edge(vertexs.get(i),vertexs.get(j), edges[i][j]);
                }
            }
        }
        isEnd = false;
        v1 = -1;
        v2 = -1;
        this.adj = new ArrayList<>();
    }

    /**
     * 重写paint方法，画图结构
     * @param g 画笔
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 进入画图
        paintGraph(g);
    }

    /**
     * 封装画图的具体方法
     * @param graphics 画笔
     */
    public void paintGraph(Graphics graphics) {
        // 画顶点
        paintVertex(graphics);
        // 画边
        paintEdges(graphics);
        // 画流程的文字
        showWord(graphics);
    }

    /**
     * 画点
     * @param graphics 画笔
     */
    private void paintVertex(Graphics graphics) {
        boolean is;
        for (int i = 0;i < vexNum;i++) {
            // 获取每个顶点值
            String vertex = vertexs.get(i);
            // 获取每个顶点的位置参数
            int[] vs = graphicalParam.getVMap().get(i).clone();
            //初始为黑色
            graphics.setColor(Color.BLACK);
            Color color = Color.BLACK;
            //检验当前是否需要变为红色
            for (int j = 0;j < adj.size();j += 2) {
                is = ((adj.get(j) == i) || (adj.get(j + 1) == i));
                if (is) {
                    color = Color.RED;
                    break;
                }
            }
            graphics.setColor(color);
            // 画顶点
            graphics.drawString(vertex,vs[0],vs[1]);
            graphics.drawOval(vs[2],vs[3],40,40);
        }
    }

    /**
     * 画边
     * @param graphics graphics
     */
    private void paintEdges(Graphics graphics) {
        boolean is;
        for (int i = 0;i < edgeNum;i++) {
            // v1，v2记录每条边的邻接顶点
            int v1 = 0,v2 = 0;
            // 获取当前边邻接顶点的位置下标
            for (int k = 0;k < vertexs.size();k++) {
                if (vertexs.get(k).equals(edgeData[i].start)) {
                    v1 = k;
                }
                if (vertexs.get(k).equals(edgeData[i].end)) {
                    v2 = k;
                }
            }
            // 获取每条边的对应的位置参数
            String str1 = "(" + v1 + "," + v2 +")";
            String str2 = "(" + v2 + "," + v1 +")";
            int[] es = (graphicalParam.getEMap().get(str1) != null) ? graphicalParam.getEMap().get(str1) : graphicalParam.getEMap().get(str2);
            //默认为黑色
            Color edgeColor = Color.BLACK;
            // 根据每次得到的最小边改变画笔颜色
            for (int k = 0;k < adj.size();k += 2) {
                is = (adj.get(k) == v1 && adj.get(k + 1) == v2) || (adj.get(k) == v2 && adj.get(k + 1) == v1);
                if (is) {
                    edgeColor = Color.RED;
                    break;
                }
            }
            graphics.setColor(edgeColor);
            // 画边
            graphics.drawLine(es[0],es[1],es[2],es[3]);
            graphics.drawString(String.valueOf(edgeData[i].weight),es[4],es[5]);
        }
    }


    /**
     * 展示侧边文字
     * @param graphics graphics
     */
    private void showWord(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        for (int i = 0,k = 1;i < adj.size();i += 2,k++) {
            graphics.drawString("第" + k + "次得到的最小边为：" + "(" + vertexs.get(adj.get(i)) + "," +
                    vertexs.get(adj.get(i + 1)) +")",600,100 + i * 15);
        }
        if (isEnd) {
            graphics.drawString("结束",500,125 + adj.size() * 20);
        }
    }
}
