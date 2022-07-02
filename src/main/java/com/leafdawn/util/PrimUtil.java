package com.leafdawn.util;

import com.leafdawn.pojo.Edge;
import com.leafdawn.pojo.Graph;
import com.leafdawn.service.PrimService;

/**
 * @author fzw
 * @date 2022-06-27 11:28
 */
public class PrimUtil {

    /** 获取顶点数目 */
    private int vexNum ;
    /** 择的点到其余边的最短权重 */
    private int[] minWeight;
    /** 该权重对应的连接的已选择的点 */
    private int[] adjVex;
    /** 保存图结构 */
    private Graph g;

    public PrimUtil(Graph g, int firstVex) {
        this.vexNum = g.getVertexsNum();
        this.minWeight = new int[vexNum];
        this.adjVex = new int[vexNum];
        this.g = g;
        //添加firstVex到其余点距离
        for(int i = 0; i < vexNum; i++) {
            minWeight[i] = g.getEdges()[firstVex][i];
            adjVex[i] = firstVex;
        }
    }

    public Edge next() {
        // 下一个要获取的节点的下标
        int minVex = 0;

        //获取最短边的节点
        int minEdg  = Integer.MAX_VALUE;
        for(int j = 0; j < vexNum; j++) {
            if(minWeight[j] != 0 && minWeight[j] < minEdg) {
                minEdg = minWeight[j];
                minVex = j;
            }
        }

        // 设置到达该节点最短距离设置为0
        minWeight[minVex] = 0;
        // 修改已选择的边到其余边的距离
        for(int j = 0; j < vexNum;j++) {
            boolean noSelect = minWeight[j] != 0;
            boolean isLess = g.getEdges()[minVex][j] < minWeight[j];
            if(noSelect && isLess) {
                minWeight[j] = (g.getEdges())[minVex][j];
                adjVex[j] = minVex;
            }
        }

        int pre = adjVex[minVex];
        int end = minVex;

        Edge edge = new Edge();
        edge.start = g.getVertexs().get(pre);
        edge.end = g.getVertexs().get(end);
        // 存入每次得到的最小边顶点值
        return edge;
    }

}
