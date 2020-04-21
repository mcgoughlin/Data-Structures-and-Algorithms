import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] linesOut;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points_) {
        if (points_ == null) throw new IllegalArgumentException();
        int n = points_.length;
        for (int i = 0; i < n - 1; i++) {
            if (points_[i] == null || points_[i + 1] == null) throw new IllegalArgumentException();
            if (points_[i].compareTo(points_[i + 1]) == 0) throw new IllegalArgumentException();
        }
        Point[] points = Arrays.copyOf(points_, n);
        ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
        Arrays.sort(points);


        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++)
                for (int ii = j + 1; ii < n; ii++)
                    for (int jj = ii + 1; jj < n; jj++) {
                        if (collinear(points[i], points[j], points[ii], points[jj])) {
                            LineSegment tempLineSegment = new LineSegment(points[i], points[jj]);
                            if (!segments.contains(tempLineSegment))
                                segments.add(tempLineSegment);
                        }
                    }
        }
        linesOut = segments.toArray(new LineSegment[segments.size()]);
    }

    public int numberOfSegments() {
        return linesOut.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(linesOut, numberOfSegments());
    }

    private boolean collinear(Point p, Point q, Point r, Point s) {
        double slope1 = p.slopeTo(q);
        double slope2 = p.slopeTo(s);
        double slope3 = p.slopeTo(r);

        boolean slopeBoolean1 = slope1 == slope2;
        boolean slopeBoolean2 = slope1 == slope3;
        return slopeBoolean1 && slopeBoolean2;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        StdOut.println(collinear.numberOfSegments());
    }
}
