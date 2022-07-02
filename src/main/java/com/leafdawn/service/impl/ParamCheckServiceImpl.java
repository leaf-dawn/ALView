package com.leafdawn.service.impl;

import com.leafdawn.service.ParamCheckService;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fzw
 */
public class ParamCheckServiceImpl implements ParamCheckService {

    /**
     * 统计深度优先算法访问顶点数
     */
    public int count;
    @Override
    public String checkVex(JTextField[] content) {
        //检验是否重复
        Map<Integer,Boolean> map = new LinkedHashMap<>();
        for (JTextField jTextField : content) {
            if ("".equals(jTextField.getText())) {
                return "顶点不能为空";
            }
            try {
                Integer num = Integer.parseInt(jTextField.getText());
                boolean b = map.containsKey(num);
                if (b) {
                    return "顶点不能重复";
                } else {
                    map.put(num, true);
                }
            } catch (Exception e) {
                return "顶点只能是[1-9]的数字";
            }
        }
        return "legal";
    }





    @Override
    public String checkAdj(JTextField[] adj, List<String> vex) {
        try {
            for (int i = 0;i < adj.length;i += 3) {
                Integer.parseInt(adj[i + 2].getText());
            }
            for (int i = 0;i < adj.length;i += 3) {
                int flag = 0;
                if (adj[i].getText().equals(adj[i + 1].getText())) {
                    return "邻接顶点不可重复！";
                }
                // 顶点值要存在
                for (String s : vex) {
                    if (adj[i].getText().equals(s)) {
                        flag++;
                    }
                    if (adj[i + 1].getText().equals(s)) {
                        flag++;
                    }
                }
                if (flag != 2) {
                    return  "有不存在的顶点值！";
                }
                // 权值范围1-10
                int w = Integer.parseInt(adj[i + 2].getText());
                if (w <= 0 || w > 10) {
                    return  "权值范围为1-10！";
                }
            }
            // 最后检测是否存在重复边
            for (int i = 0;i < adj.length;i += 3) {
                for (int j = i;j < adj.length - 3;j += 3) {
                    boolean is = (adj[i].getText().equals(adj[j + 3].getText()) && adj[i + 1].getText().equals(adj[j + 4].getText())) ||
                            (adj[i].getText().equals(adj[j + 4].getText()) && adj[i + 1].getText().equals(adj[j + 3].getText()));
                    if (is) {
                        return "存在重复边！";
                    }
                }
            }
        } catch(Exception e) {
            return  "输入边权值不合法！";
        }
        return "legal";
    }


    @Override
    public String checkEdge(JTextField[] content, int vexNum) {
        try {
            int size = (int) Math.sqrt(content.length);
            for (int i = 0;i < size;i++) {
                for (int j = i + 1;j < size;j++) {
                    if (!"m".equals(content[size * i + j].getText())) {
                        Integer.parseInt(content[size * i + j].getText());
                    }
                }
            }
            // 检测权值的范围
            int sum = 0;
            for (int i = 0;i < size;i++) {
                for (int j = i + 1;j < size;j++) {
                    if (!"m".equals(content[size * i + j].getText())) {
                        // 记录边数
                        sum++;
                        int w = Integer.parseInt(content[size * i + j].getText());
                        if (w <= 0 || w > 10) {
                            return "边权值应在1-10范围!";
                        }
                    }
                }
            }
            // 检测边数是否超过范围
            if (sum < vexNum - 1 || sum > (vexNum * (vexNum - 1)) / 2) {
                return "输入边数不合法，边数范围为" + "[" + (vexNum-1) + "," + (vexNum * (vexNum - 1)) / 2 + "]";
            }
        } catch(Exception e) {
            return "输入边权值不合法";
        }
        return "legal";
    }



    @Override
    public String checkConnectivity(int[][] edges, List<String> vex) {
        try {
            int[] visited = new int[vex.size()];
            count = 0;
            Arrays.fill(visited,0);
            dfs(visited,0,vex.size(),edges);
            if (count != vex.size()) {
                return "该图不是连通图！";
            }
        } catch(Exception e) {
            return "图结构不合法！";
        }
        return "legal";
    }

    @Override
    public String checkFirstVex(String content, int vexNum) {
        try {
            //检测是否是整数
            int firstVex = Integer.parseInt(content);
            if (firstVex < 0 || firstVex >= vexNum) {
                return "顶点下标范围为：" + "[" + "0," + (vexNum-1) + "]";
            }
        } catch(Exception e) {
            return "输入参数不合法！";
        }

        return  "legal";
    }


    @Override
    public String checkSleepTime(String content) {
        try {
            double time = Double.parseDouble(content);
            // 时间范围为[0,3]
            if (time <= 0 || time > 3) {
                return "时间范围是[0,3]！";
            }
        } catch(Exception e) {
            return "输入参数不合法！";
        }
        return "legal";
    }



    @Override
    public void dfs(int[] visited, int v, int vexNum, int[][] edges) {
        visited[v] = 1;
        count++;
        for (int i = 0;i < vexNum;i++) {
            if (edges[v][i] != 0 && edges[v][i] != Integer.MAX_VALUE && visited[i] == 0) {
                // 递归调用
                dfs(visited,i,vexNum,edges);
            }
        }
    }
    @Override
    public String checkVexAndEdgeNum(String[] content) {
        try {
            for (String s : content) {
                Integer.parseInt(s);
            }
            if (content.length == 2) {
                // 检测顶点数和边数合法性（保证是连通图）->顶点数最多6，边数范围为[n-1,n(n-1)/2]，n为顶点数
                if (Integer.parseInt(content[0]) <= 0 || Integer.parseInt(content[0]) > 6) {
                    return "顶点数最多为6";
                }else {
                    int vNum = Integer.parseInt(content[0]);
                    if (Integer.parseInt(content[1]) < vNum - 1 || Integer.parseInt(content[1]) > (vNum * (vNum - 1)) / 2) {
                        return "边数范围为" + "[" + (vNum-1) + "," + (vNum * (vNum - 1)) / 2 + "]";
                    }
                }
            }
            if (content.length == 1 && (Integer.parseInt(content[0]) <= 0 || Integer.parseInt(content[0]) > 6) ) {
                return "顶点数最多为6";
            }
        } catch(Exception e) {
            return "输入参数不合法";
        }
        return "legal";
    }

}
