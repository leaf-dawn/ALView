package com.leafdawn.pojo;

/**
 * 创建一个边类（邻接顶点）
 *  - 属性
 *   - 顶点1：start
 *   - 顶点2：end
 *   - 权值：weight
 *
 * @author leafdawn
 * @date 2021/06/18/20:21
 **/
public class Edge {

    public String start;
    public String end;
    public int weight;

    public Edge() {

    }
    public Edge(String start, String end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "<" + start + ", " + end + ">=" + weight + "]";
    }
}
