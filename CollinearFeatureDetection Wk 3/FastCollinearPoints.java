import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] linesOut;

    // Constructor - assigns memory of points and # of points
    public FastCollinearPoints(Point[] points_) {
        if (points_ == null) throw new IllegalArgumentException();
        int n = points_.length;
        for (int i = 0; i < n - 1; i++) {
            if (points_[i] == null || points_[i + 1] == null) throw new IllegalArgumentException();
            if (points_[i].compareTo(points_[i + 1]) == 0) throw new IllegalArgumentException();
        }
        ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
        Point[] pointsCopy1 = Arrays.copyOf(points_, n);
        Point[] pointsCopy2 = Arrays.copyOf(points_, n);
        Arrays.sort(pointsCopy1);

        for (int i = 0; i < n; i++) {
            // presorted by y coord, thus the origin is the lowest point that hasn't been inspected
            Point origin = pointsCopy1[i];

            // Arrays.sort is STABLE --> for every different origin, the points are sorted by:
            // First Priority: Gradient made with origin
            // Second Priority: y-coord
            Arrays.sort(pointsCopy2);
            Arrays.sort(pointsCopy2, origin.slopeOrder());

            // counts consecutive gradients (indicates collinearity) (starts at 1 because each line has itself)
            int lineCount = 1;
            // threshold for 4 collinear points
            int threshold = 4;

            Arrays.sort(pointsCopy2);
            Arrays.sort(pointsCopy2, origin.slopeOrder());

            // Stop duplicate lines, by effectively asserting you can only make a new segment when origin is lowest point
            // To do this, we need to save the 2nd lowest point when we first create the line - lineBeginning.
            Point lineBeginning = null;
            for (int j = 0; j < pointsCopy2.length - 1; ++j) {
                if (pointsCopy2[j].slopeTo(origin) == pointsCopy2[j + 1].slopeTo(origin)) {
                    lineCount++;
                    if (lineCount == 2) {
                        lineBeginning = pointsCopy2[j];
                        lineCount++;
                    }
                    else if (lineCount >= threshold && j + 1 == pointsCopy2.length - 1) {
                        assert lineBeginning != null;
                        if (lineBeginning.compareTo(origin) > 0) {
                            segments.add(new LineSegment(origin, pointsCopy2[j + 1]));
                        }
                        lineCount = 1;
                    }
                }
                else if (lineCount >= threshold) {
                    assert lineBeginning != null;
                    if (lineBeginning.compareTo(origin) > 0) {
                        segments.add(new LineSegment(origin, pointsCopy2[j]));
                    }
                    lineCount = 1;
                }
                else {
                    lineCount = 1;
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
