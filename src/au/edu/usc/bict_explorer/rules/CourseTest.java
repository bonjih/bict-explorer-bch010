package au.edu.usc.bict_explorer.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import au.edu.usc.bict_explorer.rules.Course;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CourseTest  {

    private Map<String, Option> sem1;

    @BeforeEach
    void setUp() throws Exception {

        sem1 = new HashMap<>();
        sem1.put("OPT1", new Option("dd ", " d", ""));
    }

    @Test
    void testIsOffered()  throws IOException, ParseException {
        Course ict112 = new Course("ICT112", "Creative Problem Solving with Programming", "This course teaches you to program with Python etc.", "1", new PreReqs( ""));
        assertTrue(ict112.isOffered( 1 ));
    }

    @Test
    void isSatisfied() {
    }



    @Test
    void getSemesters() {
    }


}