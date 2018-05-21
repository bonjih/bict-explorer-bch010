package au.edu.usc.bict_explorer.resources;


import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

public class textBasedPrint_TEXT {

    public static void main(String[] args) throws Exception {

        PrintWriter bictOut = new PrintWriter(new File("bict.txt"));

        File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
        File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
        File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

        Degree degree = new Degree( fileCareers, fileMinors, fileCourses );

        for (Map.Entry<String, Option> careersKey : degree.careers().entrySet()) {
            bictOut.println( "Careers: " + careersKey.getValue().getName() + careersKey.getValue().getDownstream() );

            for (Map.Entry<String, Option> minorsKey : degree.minors().entrySet())
                bictOut.println( "Minors: " + minorsKey.getValue().getName() + " " + minorsKey.getValue().getCode() );
            for (Map.Entry<String, Option> courseKey : degree.courses().entrySet()) {
                bictOut.println( "course: " + courseKey.getValue().getName() + " " + courseKey.getValue().getCode() );
            }
        }
        bictOut.close();
    }
}
