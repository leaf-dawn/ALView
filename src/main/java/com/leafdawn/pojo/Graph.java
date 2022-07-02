package com.leafdawn.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图的结构
 *  - vertexs存储顶点
 *  - edges存储边
 *
 * @author leafdawn
 * @date 2021/06/16/21:48
 **/
@Data
public class Graph {

    private  List<String> vertexs;
    private  int[][] edges;

    public Graph() {
        this.vertexs = new ArrayList<>();
        this.edges = new int[][]{};
    }


    /**
     * 图的构造器
     * @param vertexs 存放顶点的礼盒
     * @param edges 存放边信息的二维数组
     */
    public Graph(List<String> vertexs, int[][] edges) {
        this.vertexs = vertexs;
        this.edges = edges;
    }


    /**
     * 获取顶点个数
     * @return 返回顶点个数
     */
    public int getVertexsNum() {
        return vertexs.size();
    }

    /**
     * 获取边个数
     * @return 返回边个数
     */
    public int getEdgeNum() {

        int sum = 0;

        for (int i = 0;i < edges.length;i++) {
            for (int j = i + 1;j < edges[i].length;j++) {
                if (edges[i][j] != 0 && edges[i][j] != Integer.MAX_VALUE) {
                    sum++;
                }
            }
        }

        return sum;
    }
}
