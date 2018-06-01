package au.edu.usc.bict_explorer.rules;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseTest {

    private Map<String, Option> opts;

    CourseTest() throws IOException, ParseException, ClassNotFoundException {

    }

    @BeforeEach
    void setUp() throws Exception {
        opts = new HashMap<>();
        opts.put( "ICT112", new Course( "ICT112", "Creative Problem Solving with Programming", "This course teaches you to program with Python etc.", "1", new PreReqs( "" ) ) );

    }

    @Test
        // is a course offered in a particular semester?
    void testIsOffered() throws IOException, ParseException {
        Course ict112 = new Course( "ICT112", "Creative Problem Solving with Programming", "This course teaches you to program with Python etc.", "1", new PreReqs( "" ) );
        assertTrue( ict112.isOffered( 1 ) ); // add '2' the test will not pass as ICT112 is only offered in sem 1
    }

    @Test
        // are all prereqs satisfied for the chosen courses.
    void isSatisfied() throws IOException, ParseException {

        Map<String, Option> isSat = Degree.readOptions(
                new File( "src/au/edu/usc/bict_explorer/resources/courses.options" ),
                opts );
        Course c = (Course) isSat.get( "ICT310" );
        assertTrue( c.preReqs.isSatisfied2( isSat ) );
    }

    @Test
        // does the course code make the correct semester?
    void getSemesters() throws IOException, ParseException {

        Map<String, Option> sems = Degree.readOptions(
                new File( "src/au/edu/usc/bict_explorer/resources/courses.options" ),
                opts );
        Course c;
        c = (Course) sems.get( "ICT221" );
        assertEquals( "1", c.getSemesters() );

    }
}