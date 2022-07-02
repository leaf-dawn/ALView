package com.leafdawn.service.impl;

import com.leafdawn.pojo.Edge;
import com.leafdawn.pojo.Graph;
import com.leafdawn.service.PrimService;
import com.leafdawn.util.GraphUtil;
import com.leafdawn.util.PrimUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fzw
 **/
public class PrimServiceImpl implements PrimService {

    private ParamCheckServiceImpl paramCheckServiceImpl;

    public PrimServiceImpl() {
        paramCheckServiceImpl = new ParamCheckServiceImpl();
    }

    @Override
    public List<Edge> prim(Graph g, int firstVex) {
        //获取存储的边数组
        List<Edge> edges = new ArrayList<>();
        PrimUtil primUtil = new PrimUtil(g,firstVex);
        for(int i = 0; i < g.getVertexsNum()-1; i++) {
            edges.add(primUtil.next());
        }
        return edges;
    }



    @Override
    public Graph getRandomGraph(int vexNum, int edgeNum) {
        // 用于获取顶点值
        List<String> vertex = new ArrayList<>();
        int index1,index2;
        // 生成各个顶点
        for (int i = 0;i < vexNum;i++) {
            vertex.add("V" + i);
        }
        // 创建边信息数组(邻接矩阵)
        int[][] edges = new int[vexNum][vexNum];
        int flag = 1;
        while (flag == 1) {
            // 每次重新生成都要初始化，元素全部置为0
            for (int[] edge : edges) {
                Arrays.fill(edge, 0);
            }
            for (int i = edgeNum;i > 0; ) {
                // 随机生成两个不相等的索引值
                index1 = (int)(Math.random() * vexNum);
                index2 = (int)(Math.random() * vexNum);
                if ((index1 != index2) && (edges[index1][index2] == 0)) {
                    // 随机生成边，即生成权值（[1,11)之间的数）
                    edges[index1][index2] = (int)(Math.random() * 10 + 1);
                    edges[index2][index1] = edges[index1][index2];
                    // 成功生成一条边后再i--
                    i--;
                }
            }
            // 生成边权值后，把剩下的0元素都改成无穷大m
            GraphUtil.setZeroToUnlimited(edges);
            int[] visited = new int[vexNum];
            paramCheckServiceImpl.count = 0;
            Arrays.fill(visited,0);
            paramCheckServiceImpl.dfs(visited,0,vexNum,edges);
            if (paramCheckServiceImpl.count != vexNum) {
                flag = 1;
            }else {
                flag = 0;
            }
        }
        // 创建一个图结构
        return new Graph(vertex,edges);
    }

}
