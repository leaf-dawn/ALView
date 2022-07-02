package com.leafdawn.gui.draw;

import com.leafdawn.service.impl.PrimServiceImpl;
import com.leafdawn.pojo.Edge;
import com.leafdawn.pojo.Graph;
import lombok.Data;

import java.util.List;

/**
 * 对GraphBasePanel的再封装
 * 可以调用开始和停止来控制线程的进行
 * 使用prim算法不停绘制图像
 * 进行图像展示和绘画
 * @author fzw
 **/
@Data
public class GraphPainting {

    /** 基础画板 */
    public GraphBasePanel graphBasePanel;
    /** 停顿时间 */
    public static long sleepTime;
    /** 图的结构 */
    private  Graph graph;
    /** 调用算法 */
    private  PrimServiceImpl primServiceImpl;

    /**
     * 用于控制线程
     */
    private final Object lock = new Object();
    private volatile boolean pause = false;

    /**
     * 构造器，传入图结构
     * @param graph 图结构
     */
    public GraphPainting(Graph graph, long time) {
        this.graph = graph;
        sleepTime = time;

        // 把图结构传入算法实现类中
        this.primServiceImpl = new PrimServiceImpl();

        this.graphBasePanel = new GraphBasePanel(graph);
    }

    public void reset(Graph graph, long time) {
        this.graph = graph;
        sleepTime = time;
        this.graphBasePanel.reset(graph);
        this.pause = false;
        //刷新
        drawGraph(null);
    }

    /**
     * 执行面板中的paint方法，使用repaint()重复画图
     * @param vs Prim算法每次得到的边
     */
    void drawGraph(String[] vs) {
        if (vs != null) {
            for (int i = 0;i < graph.getVertexs().size();i++) {
                if (vs[0].equals(graph.getVertexs().get(i))) {
                    graphBasePanel.setV1(i);
                    graphBasePanel.getAdj().add(i);
                }
                if (vs[1].equals(graph.getVertexs().get(i))) {
                    graphBasePanel.setV2(i);
                    graphBasePanel.getAdj().add(i);
                }
            }
        }
        graphBasePanel.repaint();
    }

    /**
     * 绘制相应算法的图形
     * @param firstvertexs Prim算法起始顶点
     */
    public void graphPainting(int firstvertexs) {
        String[] content = new String[2];
        // 获取最小边数组
        List<Edge> edgeData;
        // 获取所有边
        edgeData = primServiceImpl.prim(graph, firstvertexs);
        //把该顶点变红色，演示结束
        if (graph.getEdgeNum() == 0) {
            drawGraph(null);
        }
        // 根据得到的最小边数组绘图
        for (Edge edgeDatum : edgeData) {
            content[0] = edgeDatum.start;
            content[1] = edgeDatum.end;

            // 是否暂停线程
            if (pause) {
                onPause();
            }
            //延时
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 具体绘制图形
            drawGraph(content);

        }
        // 最后再画一次图，把最小生成树显示出来
        graphBasePanel.setIsEnd(true);
        graphBasePanel.repaint();
    }

    /**
     * 暂停线程
     */
    public void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用该方法实现线程的暂停
     */
    public void pauseThread() {
        pause = true;
    }

    /**
     * 调用该方法实现恢复线程的运行
     */
    public void resumeThread() {
        pause = false;
        //恢复线程
        synchronized (lock){
            lock.notify();
        }
    }
}
