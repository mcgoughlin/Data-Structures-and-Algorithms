import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.TreeSet;

public class PointSET {
    private int size = 0;
    private TreeSet<Point2D> bST;


    // construct an empty set of points
    public PointSET() {
        bST = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return bST.isEmpty();
    }


    public int size() {
        return bST.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!bST.contains(p)) {
            bST.add(p);
            size++;
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return bST.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        Iterator<Point2D> a = bST.iterator();
        while (a.hasNext()) {
            a.next().draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (size() == 0) return null;
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> correct = new TreeSet<>();
        for (Point2D a : bST) {
            if (a.x() < rect.xmin() || a.x() > rect.xmax() || a.y() < rect.ymin()
                    || a.y() > rect
                    .ymax()) continue;
            correct.add(a);
        }
        return correct;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (size() == 0) return null;
        if (p == null) throw new IllegalArgumentException();
        Point2D nearest = null;
        for (Point2D a : bST) {
            if (nearest == null || a.distanceTo(p) < nearest.distanceTo(p)) nearest = a;
        }
        return nearest;
    }

    public static void main(String[] args) {
        StdOut.println("Before");
        PointSET kd = new PointSET();
        kd.insert(new Point2D(1, 1));
        kd.insert(new Point2D(0, 0));
        kd.insert(new Point2D(0, 1));
        kd.insert(new Point2D(1, 1));
        StdOut.println("Start");
        StdOut.println(kd.size());
        StdOut.println("Contains (0,1): " + kd.contains(new Point2D(0, 1)));
        StdOut.println("Contains (1,0): " + kd.contains(new Point2D(1, 0)));
        StdOut.println("Contains (1,1): " + kd.contains(new Point2D(1, 1)));
        StdOut.println("Contains (0,0): " + kd.contains(new Point2D(0, 0)));
        StdOut.println("End");
    }
}
