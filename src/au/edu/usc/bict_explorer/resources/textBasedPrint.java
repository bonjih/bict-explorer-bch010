package au.edu.usc.bict_explorer.resources;


import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;

import java.io.File;
import java.util.Map;

public class textBasedPrint {

    public static void main(String[] args) throws Exception {

        File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
        File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
        File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

        Degree degree = new Degree( fileCareers, fileMinors, fileCourses );

        for (Map.Entry<String, Option> careersKey : degree.careers().entrySet()) {
            System.out.println( "Careers: " + careersKey.getValue().getName() + careersKey.getValue().getDownstream() );
            for (Map.Entry<String, Option> minorsKey : degree.minors().entrySet())
                System.out.println( "Minors: " + minorsKey.getValue().getName() + " " + minorsKey.getValue().getCode() );
            for (Map.Entry<String, Option> courseKey : degree.courses().entrySet()) {
                System.out.println( "course: " + courseKey.getValue().getName() + " " + courseKey.getValue().getCode() );
            }
        }
    }
}


//
//        String fileName = "bict.txt";
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter( fileName );
//        } catch (FileNotFoundException e) {
//                e.printStackTrace();
//        }
//        // loop through *.options
//
//        writer.close();
