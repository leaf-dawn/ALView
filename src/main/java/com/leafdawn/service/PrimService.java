package com.leafdawn.service;

import com.leafdawn.pojo.Edge;
import com.leafdawn.pojo.Graph;
import java.util.List;

/**
 * 算法具体实现类
 * @author fzw
 * @date 2022-06-19 14:39
 */
public interface PrimService {


    /**
     * Prim算法的具体实现（适合稠密图）
     * @param g 传入图结构
     * @param firstVex 起始顶点
     * @return
     */
     List<Edge> prim(Graph g, int firstVex) ;


    /**
     * 获取一个随机的graph
     * @param vexNum 节点数
     * @param edgeNum 边数
     * @return :
     */
    Graph getRandomGraph(int vexNum,int edgeNum);

}
