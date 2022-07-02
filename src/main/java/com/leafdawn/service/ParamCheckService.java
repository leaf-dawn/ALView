package com.leafdawn.service;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * 参数检查工具类
 *  - 检测输入的参数是否合法
 * @author fzw
 * @date 2022-06-19 14:36
 */
public interface ParamCheckService {

    /**
     * 检测输入顶点数和边数的合法性
     * @param content
     * @return
     */
    String checkVexAndEdgeNum(String[] content);

    /**
     * 检测输入顶点值的合法性
     * @param content
     * @return
     */
    String checkVex(JTextField[] content);

    /**
     * 检测输入矩阵权值的合法性
     * @param content
     * @param vexNum 顶点数
     * @return
     */
    String checkEdge(JTextField[] content, int vexNum);

    /**
     * 检测输入的边信息合法性
     * @param adj 边信息内容
     * @param vex 顶点信息集合
     * @return
     */
    String checkAdj(JTextField[] adj, List<String> vex);

    /**
     * 检测执行Prim算法是输入的起始顶点是否合法
     * @param content
     * @param vexNum 顶点数
     * @return
     */
    String checkFirstVex(String content, int vexNum);

    /**
     * 检测输入休眠时间合法性
     * @param content
     * @return
     */
     String checkSleepTime(String content);

    /**
     * 检测图是否是连通图
     * @param edges 矩阵数组
     * @param vex 顶点集合
     * @return
     */
    String checkConnectivity(int[][] edges,List<String> vex);

    /**
     * 深度优先算法函数，递归调用
     * @param visited 标记访问数组
     * @param v 每次访问的顶点下标
     * @param vexNum 顶点数
     * @param edges 邻接矩阵数组
     */
    void dfs(int[] visited, int v, int vexNum, int[][] edges);
}
