import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    // Constructor
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // Draws the point
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // Draw line between invoking point and argument point
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Computes slope between invoking point and argument point
    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            else {
                return Double.POSITIVE_INFINITY;
            }
        }
        double x0 = (double) this.x;
        double y0 = (double) this.y;
        double x1 = (double) that.x;
        double y1 = (double) that.y;
        double ans = ((y1 - y0) / (x1 - x0));
        if (ans == -0) return 0;
        else return ans;

        /* YOUR CODE HERE */
    }

    // Compares the height of points. Seperates like-height points by their x.
    public int compareTo(Point that) {
        if (this.y == that.y) {
            return Integer.compare(this.x, that.x);
        }
        else if (this.y > that.y) {
            return 1;
        }
        return -1;
    }

    // Original method had no arguments?? Figure out if this is even possible.
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            public int compare(Point pP, Point qQ) {
                double p = slopeTo(pP);
                double q = slopeTo(qQ);
                // StdOut.println("a[j]: " + pP.toString() + " a[i]: " + qQ.toString());
                // StdOut.println("slope a[j]: " + p + ", slope a[i]: " + q);
                return Double.compare(p, q);
            }
        };
    }


    // Debugging method
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // client test
    public static void main(String[] args) {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        Point p = new Point(3000, 4000);
        Comparator<Point> comparator = p.slopeOrder();
        p.draw();
        Point q = new Point(5000, 5000);
        q.draw();
        p.drawTo(q);
        StdDraw.show();


        /* YOUR CODE HERE */
    }
}

