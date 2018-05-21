package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreReqsTest {
    private Map<String, Course> courses;

    @BeforeEach
    void setup() {
        courses = new HashMap<>();
        courses.put("ICT112", new Course("ICT112", "Creative Problem Solving with Programming", "This course teaches you to program with Python etc.", "1", new PreReqs( "")));
        courses.put("ICT115", new Course("ICT115", "Design", "System Design", "1", new PreReqs( "")));
    }

    @Test
    void testEmpty() {
        PreReqs pre = new PreReqs("");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testSingleMissing() {
        PreReqs pre = new PreReqs("ICT113");
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testSingleDone() {
        PreReqs pre = new PreReqs("ICT112");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testTwoDone() {
        PreReqs pre = new PreReqs("ICT112,ICT115");
        assertTrue(pre.isSatisfied(courses));
    }

    @Test
    void testTwoFirst() {
        PreReqs pre = new PreReqs("ICT112,ICT116");
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testTwoSecond() {
        PreReqs pre = new PreReqs("ICT111,ICT112");
        assertFalse(pre.isSatisfied(courses));
    }

    @Test
    void testToString() {
        PreReqs pre = new PreReqs("ICT111,ICT112");
        assertEquals("ICT111,ICT112", pre.toString());
    }
}
