import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Iterator;

public class Solver {
    private final ArrayList<Board> solved;
    private Node searchTwinNode;
    private final int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();
        Node Initial = new Node(initial, null, 0);
        Node InitialTwin = new Node(initial.twin(), null, 0);
        Node searchNode = Initial;
        searchTwinNode = InitialTwin;
        MinPQ<Node> PQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        boolean bl = true;

        while (!searchNode.board.isGoal() && isSolvable()) {
            Iterator<Board> neighbours = searchNode.board.neighbors().iterator();
            Iterator<Board> neighboursTwin = searchTwinNode.board.neighbors().iterator();
            while (neighbours.hasNext()) {
                Node n = new Node(neighbours.next(), searchNode, searchNode.moves);
                if (bl) PQ.insert(n);
                else if (!searchNode.parent.board.equals(n.board)) PQ.insert(n);
            }
            //Twin
            while (neighboursTwin.hasNext()) {
                Node n = new Node(neighboursTwin.next(), searchTwinNode, searchTwinNode.moves);
                if (bl) twinPQ.insert(n);
                else if (!searchTwinNode.parent.board.equals(n.board)) twinPQ.insert(n);
            }
            bl = false;
            searchNode = PQ.delMin();
            searchTwinNode = twinPQ.delMin();
        }

        ArrayList<Board> bSolved = new ArrayList<Board>();

        Node goal = searchNode;
        assert goal.board.isGoal();
        while (goal.parent != null) {
            bSolved.add(goal.board);
            goal = goal.parent;
        }
        if (!bl) bSolved.add(initial);
        ArrayList<Board> finalSolved = new ArrayList<Board>();
        for (int i = bSolved.size() - 1; i >= 0; i--) {
            finalSolved.add(bSolved.get(i));
        }

        if (finalSolved.size() == 0) finalSolved.add(initial);
        if (isSolvable()) {
            solved = finalSolved;
            moves = finalSolved.size() - 1;
        }
        else {
            solved = null;
            moves = -1;
        }
    }

    private final class Node implements Comparable<Node> {
        int priority;
        int moves;
        Node parent;
        Board board;

        Node(Board b, Node parentio, int prevMoves) {
            parent = parentio;
            moves = prevMoves + 1;
            board = b;
            priority = b.manhattan() + moves;
        }

        public int compareTo(Node that) {
            return Integer.compare(this.priority, that.priority);
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return !(searchTwinNode.board.manhattan() == 0);
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solved;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        }
        else {
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }

}
