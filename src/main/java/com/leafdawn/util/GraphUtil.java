package com.leafdawn.util;

/**
 * 图相关工具
 * @author fzw
 * @date 2022-06-19 14:53
 */
public class GraphUtil {

    /**
     * 设置0-》无限大
     */
    public static void setZeroToUnlimited(int[][] edges) {
        for (int i = 0;i < edges.length;i++) {
            for (int j = 0;j < edges[i].length;j++) {
                if ((i != j) && (edges[i][j] == 0)) {
                    edges[i][j] = Integer.MAX_VALUE;
                    edges[j][i] = Integer.MAX_VALUE;
                }
            }
        }
    }
}
