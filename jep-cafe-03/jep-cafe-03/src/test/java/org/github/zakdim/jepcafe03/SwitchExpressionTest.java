package org.github.zakdim.jepcafe03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by dmitri on 2024-01-21.
 */
public class SwitchExpressionTest {

    @Test
    void switchStatement() {
        int month = 8;
        List<String> futureMonths = new ArrayList<>();

        switch (month) {
            case 1: futureMonths.add("January"); break;
            case 2: futureMonths.add("February"); break;
            case 3: futureMonths.add("March"); break;
            case 4: futureMonths.add("April"); break;
            case 5: futureMonths.add("May"); break;
            case 6: futureMonths.add("June"); break;
            case 7: futureMonths.add("July"); break;
            case 8: futureMonths.add("August"); break;
            case 9: futureMonths.add("September"); break;
            case 10: futureMonths.add("October"); break;
            case 11: futureMonths.add("November"); break;
            case 12: futureMonths.add("December"); break;
            default: break;
        }

        System.out.println(futureMonths);
        assertEquals(1, futureMonths.size());
        assertEquals("August", futureMonths.get(0));
    }

    @Test
    void switchStatement_numDays() {
        int numDays = 0;
        int month = 2;
        int year = Year.now().getValue();

        switch (month) {
            case 1: case 3: case 5:     // January March May
            case 7: case 8: case 10:    // July August October
            case 12:
                numDays = 31;
                break;
            case 4: case 6:     // April June
            case 9: case 11:    // September November
                numDays = 30;
                break;
            case 2: // February
                if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0))
                    numDays = 29;
                else
                    numDays = 28;
                break;
            default:
                System.out.println("Invalid month.");
                break;
        }

        System.out.printf("Days in month %d, %d: %d\n", month, year, numDays);
    }

    @Test
    void switchStatement_dayOfWeek() {
        DayOfWeek day = DayOfWeek.MONDAY;

        switch (day) {
            case MONDAY:
            case THURSDAY:
                System.out.println("school");
                break;
            case WEDNESDAY:
                System.out.println("music and sport");
            case TUESDAY:
            case FRIDAY:
                System.out.println("more school");
                break;
            default:
                System.out.println("take a break");
                break;
        }
    }

    @Test
    void switchExpression_dayOfWeek() {
        DayOfWeek day = DayOfWeek.SATURDAY;

        String activity =
                switch (day) {
                    case MONDAY, TUESDAY    -> "school";
                    case WEDNESDAY          -> "music and sport";
                    case THURSDAY, FRIDAY   -> "more school";
                    case SATURDAY           -> "friends and family";
                    default                 -> "take a break";
                };

        System.out.println(activity);
        System.out.println(convertToActivity(day));
    }

    private String convertToActivity(DayOfWeek day) {
        String activity =
                switch (day) {
                    case MONDAY, TUESDAY -> {
                        System.out.println("School");
//                        return "School";
                        yield "School";
                    }
                    default -> "other";
                };

        return activity;
    }
}
