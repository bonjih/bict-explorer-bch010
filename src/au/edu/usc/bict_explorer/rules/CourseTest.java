package au.edu.usc.bict_explorer.rules;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CourseTest {


    private Map<String, Course> myCourses;


//    Map<String, Course> myCourses = new HashMap<>();


    File empty = new File( "src/au/edu/usc/bict_explorer/rules/empty.options" );
    File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );


    Degree  myDegree = new Degree( empty, empty, fileCourses );



//    Map<String,Course> fileCourses = new HashMap<>();

//    Degree degree = new Degree( empty, empty, fileCourses );

//    Map<String, Option> courses = degree.courses();

//    Map<String,Course> courses = new HashMap<>();

//    Course course110 = new Course("ICT110", "Introduction to Data Science", "This class is an introduction to the practice of data science", "1", new PreReqs( ""));


    CourseTest() throws IOException, ParseException, ClassNotFoundException {

    }


    @BeforeEach
    void setUp() throws Exception {
        myCourses = new HashMap<>();
        myCourses.put( "ICT310", new Course( "ICT310", "Systems Analysis and Design", "The course introduces the student to a range of concepts used in the analysis and design of information systems.", "1", new PreReqs( "" ) ) );

    }

    @Test
        // is a course offered in a particular semester?
    void testIsOffered() throws IOException, ParseException {
        Course ict112 = new Course( "ICT112", "Creative Problem Solving with Programming", "This course teaches you to program with Python etc.", "1", new PreReqs( "" ) );
        assertTrue( ict112.isOffered( 1 ) ); // add '2' the test will not pass as ICT112 is only offered in sem 1
    }

    @Test
    void isSatisfied() throws IOException, ParseException {

        Map<String, String> myPrereqs = new HashMap<>();
        File fileCourses = new File( "src/au/edu/usc/bict_explorer/rules/courses_test.options" );
        Scanner sf = new Scanner(fileCourses);
        String line;

            while(sf.hasNextLine())  {
                line = sf.nextLine();
                String[] key = line.split("\\W+");

                String ID = key[0];
//                String name = key[4];
//
//                myPrereqs.put(ID,name );
                System.out.println( myPrereqs);
                String p = myPrereqs.get("ICT310");

//                myPrereqs.get( p );
//                Course c = myCourses.get( p );
//                assertTrue(c.getPreReqs().isSatisfied(myPrereqs));

            }
            sf.close();
        }

    @Test
    void getSemesters() {
    }


}