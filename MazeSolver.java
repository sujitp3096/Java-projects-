
import java.util.*;

/**
 * Maze Solver - Console based Java application
 * Finds the shortest path through a maze using Breadth-First Search (BFS).
 *
 * Maze legend:
 *   0 = open path
 *   1 = wall
 *   S = start
 *   E = end
 *
 * The program prints the maze, then the solved maze with the shortest
 * path marked using '*'.
 */
public class MazeSolver {

    static int rows, cols;
    static char[][] maze;
    static int[] start = new int[2];
    static int[] end = new int[2];

    // Directions: up, down, left, right
    static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) {
        // Sample maze - feel free to change dimensions/layout
        char[][] sampleMaze = {
                {'S', '0', '1', '0', '0', '0', '0'},
                {'1', '0', '1', '0', '1', '1', '0'},
                {'1', '0', '0', '0', '0', '1', '0'},
                {'1', '1', '1', '1', '0', '1', '0'},
                {'0', '0', '0', '1', '0', '0', '0'},
                {'0', '1', '0', '0', '0', '1', 'E'},
                {'0', '1', '1', '1', '0', '1', '0'}
        };

        maze = sampleMaze;
        rows = maze.length;
        cols = maze[0].length;

        locateStartAndEnd();

        System.out.println("Maze (S = start, E = end, 1 = wall, 0 = open path):\n");
        printMaze(maze);

        List<int[]> path = solveBFS();

        if (path == null) {
            System.out.println("\nNo path exists from start to end.");
        } else {
            char[][] solved = markPath(path);
            System.out.println("\nSolved Maze (path marked with *):\n");
            printMaze(solved);
            System.out.println("\nShortest path length: " + (path.size() - 1) + " steps");
        }
    }

    static void locateStartAndEnd() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    start[0] = i;
                    start[1] = j;
                } else if (maze[i][j] == 'E') {
                    end[0] = i;
                    end[1] = j;
                }
            }
        }
    }

    static List<int[]> solveBFS() {
        boolean[][] visited = new boolean[rows][cols];
        int[][][] parent = new int[rows][cols][]; // stores parent coordinates for path reconstruction

        Queue<int[]> queue = new LinkedList<>();
        queue.add(start);
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            if (current[0] == end[0] && current[1] == end[1]) {
                return reconstructPath(parent);
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current[0] + dir[0];
                int newCol = current[1] + dir[1];

                if (isValidMove(newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }

        return null; // no path found
    }

    static boolean isValidMove(int row, int col, boolean[][] visited) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        if (visited[row][col]) return false;
        return maze[row][col] != '1';
    }

    static List<int[]> reconstructPath(int[][][] parent) {
        LinkedList<int[]> path = new LinkedList<>();
        int[] current = end;

        while (current != null) {
            path.addFirst(current);
            if (current[0] == start[0] && current[1] == start[1]) break;
            current = parent[current[0]][current[1]];
        }

        return path;
    }

    static char[][] markPath(List<int[]> path) {
        char[][] copy = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            copy[i] = maze[i].clone();
        }

        for (int[] cell : path) {
            if (copy[cell[0]][cell[1]] == '0') {
                copy[cell[0]][cell[1]] = '*';
            }
        }

        return copy;
    }

    static void printMaze(char[][] m) {
        for (char[] row : m) {
            StringBuilder sb = new StringBuilder();
            for (char c : row) {
                sb.append(c).append(' ');
            }
            System.out.println(sb.toString());
        }
    }
}
