import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTree {
    private int size;
    private Node root;

    private class Node implements Comparable<Node> {
        private final boolean isVertical;
        private Point2D point;
        private Node left = null;
        private Node right = null;

        Node(Point2D p, boolean isV) {
            point = p;
            isVertical = isV;
        }

        public int compareTo(Node that) {
            if (isVertical()) {
                if (compareX(that) != 0) return compareX(that);
                else if (compareY(that) != 0) return 1;
                else return 0;
            }
            else {
                if (compareY(that) != 0) return compareY(that);
                else if (compareX(that) != 0) return 1;
                else return 0;
            }
        }

        private int compareX(Node that) {
            return Double.compare(this.getX(), that.getX());
        }

        private int compareY(Node that) {
            return Double.compare(this.getY(), that.getY());
        }

        public boolean isVertical() {
            return isVertical;
        }

        public double getX() {
            return point.x();
        }

        public double getY() {
            return point.y();
        }

        public Point2D getPoint() {
            return point;
        }

        public void newCoords(Point2D newP) {
            point = newP;
        }

        public void setLeft(Point2D p) {
            this.left = new Node(p, !isVertical());
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public void setRight(Point2D p) {
            this.right = new Node(p, !isVertical());
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node n = new Node(p, true);
        if (root != null) {
            if (contains(p)) {
                set(p);
            }
            else {
                Node searchNode = root;
                Node parent = null;
                while (searchNode != null && searchNode.compareTo(n) != 0) {
                    parent = searchNode;
                    if (searchNode.compareTo(n) < 0) searchNode = searchNode.getRight();
                    else searchNode = searchNode.getLeft();
                }
                assert parent != null;
                if (parent.compareTo(n) < 0) parent.setRight(p);
                else parent.setLeft(p);
                size++;
            }
        }
        else {
            root = n;
            size++;
        }
    }

    private void set(Point2D p) {
        Node n = new Node(p, true);
        Node searchNode = root;
        while (searchNode != null) {
            if (searchNode.compareTo(n) < 0) searchNode = searchNode.getRight();
            else if (searchNode.compareTo(n) > 0) searchNode = searchNode.getLeft();
            else {
                searchNode.newCoords(p);
                break;
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return false;
        Node n = new Node(p, true);
        Node searchNode = root;
        while (searchNode != null) {
            if (searchNode.compareTo(n) < 0) searchNode = searchNode.getRight();
            else if (searchNode.compareTo(n) > 0) searchNode = searchNode.getLeft();
            else return true;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node n, RectHV rect) {
        StdDraw.setPenRadius(0.005);
        if (n != null) {
            n.getPoint().draw();
            RectHV newRectLeft = null, newRectRight = null;
            if (n.isVertical()) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.getX(), rect.ymin(), n.getX(), rect.ymax());
                newRectRight = new RectHV(n.getX(), rect.ymin(), rect.xmax(), rect.ymax());
                newRectLeft = new RectHV(rect.xmin(), rect.ymin(), n.getX(), rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), n.getY(), rect.xmax(), n.getY());
                newRectRight = new RectHV(rect.xmin(), n.getY(), rect.xmax(), rect.ymax());
                newRectLeft = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.getY());
            }
            draw(n.getLeft(), newRectLeft);
            draw(n.getRight(), newRectRight);
        }
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (size() == 0) return null;
        Node searchNode = root;
        ArrayList<Point2D> inRect = new ArrayList<Point2D>();
        inRect = rangeGetter(rect, searchNode, inRect);
        return inRect;
    }

    private ArrayList<Point2D> rangeGetter(RectHV rect, Node searchNode,
                                           ArrayList<Point2D> inRect) {
        if (searchNode == null) return inRect;
        // Node maxPoint = new Node(new Point2D(rect.xmax(), rect.ymax()), !searchNode.isVertical());
        // Node minPoint = new Node(new Point2D(rect.xmin(), rect.ymin()), !searchNode.isVertical());
        boolean higherThanMaxX = searchNode.isVertical() && searchNode.getX() > rect.xmax();
        boolean higherThanMaxY = !searchNode.isVertical() && searchNode.getY() > rect.ymax();
        boolean lowerThanMinX = searchNode.isVertical() && searchNode.getX() < rect.xmin();
        boolean lowerThanMinY = !searchNode.isVertical() && searchNode.getY() < rect.ymin();
        if (higherThanMaxX || higherThanMaxY)
            inRect = rangeGetter(rect, searchNode.getLeft(), inRect);
        else if (lowerThanMinX || lowerThanMinY)
            inRect = rangeGetter(rect, searchNode.getRight(), inRect);

        if (rect.contains(searchNode.getPoint())) {
            if (!inRect.contains(searchNode.getPoint())) inRect.add(searchNode.getPoint());
            inRect = rangeGetter(rect, searchNode.getLeft(), inRect);
            inRect = rangeGetter(rect, searchNode.getRight(), inRect);
        }
        else if (!higherThanMaxX && !higherThanMaxY && !lowerThanMinX && !lowerThanMinY) {
            inRect = rangeGetter(rect, searchNode.getLeft(), inRect);
            inRect = rangeGetter(rect, searchNode.getRight(), inRect);
        }

        return inRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (size() == 0) return null;
        RectHV acceptable = new RectHV(0, 0, 1, 1);
        return nearestGetter(new Node(p, true), root, acceptable,
                             root.getPoint().distanceSquaredTo(p),
                             root)
                .getPoint();
    }

    private Node nearestGetter(Node query, Node n, RectHV acceptable, double distance,
                               Node minDistanceNode) {
        double nToQuery = n.getPoint().distanceSquaredTo(query.getPoint());
        double minDistance = minDistanceNode.getPoint().distanceSquaredTo(query.getPoint());
        if (nToQuery < minDistance) {
            minDistanceNode = n;
            minDistance = nToQuery;
        }

        RectHV RHS = null, LHS = null;

        if (n.isVertical()) {
            LHS = new RectHV(acceptable.xmin(), acceptable.ymin(), n.getX(),
                             acceptable.ymax());
            RHS = new RectHV(n.getX(), acceptable.ymin(), acceptable.xmax(),
                             acceptable.ymax());
        }
        else {
            LHS = new RectHV(acceptable.xmin(), acceptable.ymin(), acceptable.xmax(),
                             n.getY());
            RHS = new RectHV(acceptable.xmin(), n.getY(), acceptable.xmax(),
                             acceptable.ymax());
        }

        double LHSprox = LHS.distanceSquaredTo(query.getPoint()), RHSprox = RHS
                .distanceSquaredTo(query.getPoint());

        if ((LHSprox < RHSprox) && LHSprox < minDistance && n.getLeft() != null) {
            minDistanceNode = nearestGetter(query, n.getLeft(), LHS, minDistance, minDistanceNode);
            minDistance = minDistanceNode.getPoint().distanceSquaredTo(query.getPoint());
        }
        if (RHSprox < minDistance && n.getRight() != null) {
            minDistanceNode = nearestGetter(query, n.getRight(), RHS, minDistance, minDistanceNode);
            minDistance = minDistanceNode.getPoint().distanceSquaredTo(query.getPoint());
        }
        if (!(LHSprox < RHSprox) && LHSprox < minDistance && n.getLeft() != null) {
            minDistanceNode = nearestGetter(query, n.getLeft(), LHS, minDistance, minDistanceNode);
        }

        return minDistanceNode;

    }

    public static void main(String[] args) {
        StdOut.println("Before");
        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.372, 0.497));
        kd.insert(new Point2D(0.564, 0.413));
        kd.insert(new Point2D(0.226, 0.577));
        kd.insert(new Point2D(0.144, 0.179));
        kd.insert(new Point2D(0.083, 0.51));
        kd.insert(new Point2D(0.32, 0.708));
        kd.insert(new Point2D(0.417, 0.362));
        kd.insert(new Point2D(0.862, 0.825));
        kd.insert(new Point2D(0.785, 0.725));
        kd.insert(new Point2D(0.499, 0.208));
        Iterable<Point2D> a = kd.range(new RectHV(0.341, 0.789, 0.664, 0.968));
    }
}
