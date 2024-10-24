package project.tripMaker.service;

import java.util.*;

public class RouteService {

  // 간선을 표현하는 클래스 (출발지, 도착지, 가중치)
  class Edge implements Comparable<Edge> {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
      this.src = src;
      this.dest = dest;
      this.weight = weight;
    }

    // 간선의 가중치 순으로 정렬하기 위한 비교 함수
    @Override
    public int compareTo(Edge compareEdge) {
      return this.weight - compareEdge.weight;
    }
  }

  // 크루스칼 알고리즘 클래스
  class KruskalAlgorithm {
    int V;  // 노드 수
    List<Edge> edges;  // 간선 리스트

    public KruskalAlgorithm(int v) {
      V = v;
      edges = new ArrayList<>();
    }

    // 유니온-파인드: 부모 찾기
    int find(int[] parent, int i) {
      if (parent[i] == i) {
        return i;
      }
      return find(parent, parent[i]);
    }

    // 유니온-파인드: 두 집합을 합침
    void union(int[] parent, int[] rank, int x, int y) {
      int xroot = find(parent, x);
      int yroot = find(parent, y);

      if (rank[xroot] < rank[yroot]) {
        parent[xroot] = yroot;
      } else if (rank[xroot] > rank[yroot]) {
        parent[yroot] = xroot;
      } else {
        parent[yroot] = xroot;
        rank[xroot]++;
      }
    }

    // 크루스칼 알고리즘으로 최소 비용 신장 트리 찾기
    void kruskalMST() {
      // 간선들을 가중치 기준으로 정렬
      Collections.sort(edges);

      // 결과 간선을 저장할 배열
      Edge[] result = new Edge[V];
      int e = 0;
      int i = 0;

      // 부모 및 랭크 배열 초기화
      int[] parent = new int[V];
      int[] rank = new int[V];

      for (int v = 0; v < V; v++) {
        parent[v] = v;
        rank[v] = 0;
      }

      // 간선을 선택하여 MST 만들기
      while (e < V - 1) {
        Edge nextEdge = edges.get(i++);
        int x = find(parent, nextEdge.src);
        int y = find(parent, nextEdge.dest);

        // 사이클이 생기지 않으면 간선 선택
        if (x != y) {
          result[e++] = nextEdge;
          union(parent, rank, x, y);
        }
      }

      // 결과 출력
      System.out.println("최소 비용 신장 트리의 간선들:");
      for (i = 0; i < e; i++) {
        System.out.println(result[i].src + " -- " + result[i].dest + " == " + result[i].weight);
      }
    }

    // timeMatrix에서 간선을 추가하는 함수
    void addEdgesFromMatrix(int[][] timeMatrix) {
      for (int i = 0; i < timeMatrix.length; i++) {
        for (int j = i + 1; j < timeMatrix[i].length; j++) {
          if (timeMatrix[i][j] != 0) {
            edges.add(new Edge(i, j, timeMatrix[i][j]));
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    int[][] timeMatrix = {
        //   A    B    C    D    E    F    G    H    I    J
        {  0,  10,  15,  20,  30,  25,  60,  90,  80,  70}, // A
        { 10,   0,  35,  25,  40,  30,  50,  70,  60,  55}, // B
        { 15,  35,   0,  30,  20,  40,  45,  60,  85,  75}, // C
        { 20,  25,  30,   0,  10,  15,  50,  65,  55,  45}, // D
        { 30,  40,  20,  10,   0,  35,  55,  75,  90,  85}, // E
        { 25,  30,  40,  15,  35,   0,  60,  70,  50,  40}, // F
        { 60,  50,  45,  50,  55,  60,   0,  30,  20,  10}, // G
        { 90,  70,  60,  65,  75,  70,  30,   0,  25,  15}, // H
        { 80,  60,  85,  55,  90,  50,  20,  25,   0,  35}, // I
        { 70,  55,  75,  45,  85,  40,  10,  15,  35,   0}  // J
    };

    // Route 클래스의 인스턴스 생성
    RouteService route = new RouteService();
    KruskalAlgorithm kruskal = route.new KruskalAlgorithm(10);

    // timeMatrix에서 간선을 추가
    kruskal.addEdgesFromMatrix(timeMatrix);

    // 크루스칼 알고리즘 실행
    kruskal.kruskalMST();
  }
}
