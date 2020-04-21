import java.util.ArrayList;

public class Board {
    private final int n;
    private final int[][] vals;
    private final int zeroI, zeroJ;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        vals = tiles;
        int[] a = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    a[0] = i;
                    a[1] = j;
                    break;

                }
            }
        }
        zeroI = a[0];
        zeroJ = a[1];
    }

    private int[][] cloneGenerator() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) clone[i][j] = vals[i][j];
        return clone;
    }

    private int[][] neighbourGenerator(int jAdjust, int iAdjust, int[] zero) {
        int[][] clone = cloneGenerator();
        clone[zero[0]][zero[1]] = clone[zero[0] + iAdjust][zero[1] + jAdjust];
        clone[zero[0] + iAdjust][zero[1] + jAdjust] = 0;
        return clone;
    }


    // string representation of this board
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n);
        stringBuilder.append(System.lineSeparator());
        int maxChar = String.valueOf(n * n).length();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int numspace = maxChar - String.valueOf(vals[i][j]).length() + 1;
                String space = String.format("%" + numspace + "s", " ");
                String s = String.valueOf(vals[i][j]);
                s = String.format("%s%s", space, s);
                stringBuilder.append(s);
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int h = 0;
        int[][] clone = cloneGenerator();

        for (int i = 0; i < n; i++)
            for (int j = 1; j <= n; j++) {
                if (clone[i][j - 1] == 0) continue;
                if ((i * n) + j != clone[i][j - 1]) h++;
            }
        return h;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int m = 0;
        int[][] clone = cloneGenerator();

        for (int i = 0; i < n; i++)
            for (int j = 1; j <= n; j++) {
                if (clone[i][j - 1] == 0) continue;
                int pos = (i * n) + j;
                int tile = clone[i][j - 1];
                if (pos != tile) {
                    // Vertical Distance
                    int v = Math.abs(vertReturner(pos) - vertReturner(tile));
                    // Horzontal Distance
                    int h = Math.abs(horReturner(pos) - horReturner(tile));
                    m += v + h;
                }
            }

        return m;
    }

    private int vertReturner(int a) {
        int rem = a % n;
        if (rem == 0) return (a / n) - 1;
        else return (a - rem) / n;
    }

    private int horReturner(int a) {
        int rem = a % n;
        if (rem == 0) return n;
        else return rem;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0 && manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board newBoard = (Board) y;
        if (newBoard.dimension() != this.dimension()) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) if (newBoard.vals[i][j] != vals[i][j]) return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> nb = new ArrayList<Board>();
        int[] zeroLoc = new int[] { zeroI, zeroJ };
        if (zeroLoc[0] > 0) nb.add(new Board(neighbourGenerator(0, -1, zeroLoc)));
        if (zeroLoc[1] > 0) nb.add(new Board(neighbourGenerator(-1, 0, zeroLoc)));
        if (zeroLoc[0] < n - 1) nb.add(new Board(neighbourGenerator(0, 1, zeroLoc)));
        if (zeroLoc[1] < n - 1) nb.add(new Board(neighbourGenerator(1, 0, zeroLoc)));
        return nb;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // random numbers
        int rand1 = 1, rand2 = 1, rand3
                = 0, rand4 = 0;
        int[][] clone = cloneGenerator();
        if (clone[rand1][rand2] == 0) {
            rand1 = n - 1;
            rand2 = n - 2;
        }
        else if (clone[rand3][rand4] == 0) {
            rand3 = n - 1;
            rand4 = n - 2;
        }
        // creating a new board for the random swap
        int swap = vals[rand1][rand2];
        clone[rand1][rand2] = clone[rand3][rand4];
        clone[rand3][rand4] = swap;
        return new Board(clone);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board test = new Board(new int[3][3]);
        test.toString();
        test.equals(test);
        test.isGoal();
        test.manhattan();
        test.hamming();
        test.twin();
    }

}
