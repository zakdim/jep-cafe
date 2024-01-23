package org.github.zakdim.jepcafe14;

import org.junit.jupiter.api.Test;

/**
 * Create by dmitri on 2024-01-21.
 */
public class PatternMatchingTest {

    @Test
    void instanceOfTest_old() {
        Object o = new String("String object");

        if (o instanceof String) {
            String s = (String) o;
            System.out.println(s);
        } else {
            System.out.println("Object is not a String");
        }
    }

    @Test
    void instanceOfTest_new() {
        Object o = new String("String object");

        if (o instanceof String s) {
            System.out.println(s);
        } else {
            System.out.println("Object is not a String");
        }

        o = new String("");
        if (o instanceof String s && s.length() > 0) {
            System.out.println(s);
        } else {
            System.out.println("Object is not a String or an empty String");
        }
    }

    private void process(Object o) {
        // OLD
        if (!(o instanceof String)) {
            return;
        }
        String s = (String) o;
        // do something with s

        // NEW
        if (!(o instanceof String s2)) {
            return;
        }
        // do something with s2

    }

    interface Shape {}
    record Circle(double radius) implements Shape {}
    record Square(double edge) implements Shape {}

    @Test
    void switchExpression_patterMatching() {
        Shape shape = new Circle(10d);
//        Shape shape = null;

        double surface =
                switch (shape) {
                    case null           -> 0d;
                    case Circle circle when circle.radius() < 10 ->
                            Math.PI * circle.radius() * circle.radius();
                    case Square square  -> square.edge() * square.edge();
                    default             -> throw new IllegalStateException("Unexpected value: " + shape);
                };

        System.out.printf("surface=%.3f\n", surface);

        double surface2 =
                switch (shape) {
                    case null   -> 0d;
                    case Circle(double radius) when radius < 10 -> Math.PI * radius * radius;
                    case Square(double edge)                    -> edge * edge;
                    default     -> 0d;
                };

        System.out.printf("surface=%.3f\n", surface);

    }

    record Point(int x, int y) {}

    @Test
    void record_patterMatching() {
        Object o = new Point(2, 3);

        if (o instanceof Point p) {
            int x = p.x();
            int y = p.y();
            System.out.println("p.x=" + x + ", p.y=" + y + ": " + p);
        } else {
            System.out.println("Object is not a Point");
        }

        // Record pattern matching
//        if (o instanceof Point(int x, int y) p) {
        if (o instanceof Point(int x, int y)) {
            System.out.println("p.x=" + x + ", p.y=" + y);
        } else {
            System.out.println("Object is not a Point");
        }

    }
}
