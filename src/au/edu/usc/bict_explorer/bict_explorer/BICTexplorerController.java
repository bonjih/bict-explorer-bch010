package au.edu.usc.bict_explorer.bict_explorer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;


/**
 *
 * @author Ben Hamilton
 */

public class BICTexplorerController {
    static Degree myDegree;
    static Option selectedCareer ;//= null;
    static Option extraSelectedMinor;
    static Set<Option> downStreamCourse ;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */

    public static void main(String[] args) throws FileNotFoundException,NoSuchElementException{
        Scanner input = new Scanner(System.in);

        File file =new File("bict.txt");

        PrintWriter bictOut = new PrintWriter(file);
        try {
            // TODO code application logic here\
            File fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
            File fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
            File fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

            myDegree =new Degree(fileCareers,fileMinors,fileCourses);

        } catch (IOException | ParseException ex) {
            Logger.getLogger(BICTexplorerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String,Option> courses = myDegree.courses();
        Map<String,Option> careers = myDegree.careers();
        Map<String,Option> minors = myDegree.minors();

        System.out.println("Choose careers:");
        careers.entrySet().forEach((career) -> {
            System.out.println(career.getKey()+" "+career.getValue().getName());
        });

        String careerChoice = input.nextLine();

        Set<String> careerKeys = careers.keySet();
        Set<String> minorKeys = minors.keySet();
        Set<String>  coursesKeys = courses.keySet();

        for(String key : careerKeys){
            if( key.equals(careerChoice.toUpperCase())){
                //  System.out.println(key);
                selectedCareer = (Option) careers.get(key);
                System.out.println(selectedCareer.getCode());
                selectedCareer.setChosen(true);
            }
        }

        //compulsory minors plus one optional
        Set<Option> compulsoryMinors = minors.get("BICT").getDownstream(); // 'BICT' refer to new line in minors.options
        Set<Option> minorCourseForthisCareer = selectedCareer.getDownstream();
        Set<String> selectedMinorKeys = new HashSet<>();
        Set<Option> upstream;// = compulsoryMinors

        System.out.println("You must do the following courses:");
        System.out.println("Minor couses for "+selectedCareer.getName());
        minorCourseForthisCareer.forEach((Option action)->{
                    System.out.println(action +" "+ action.getName());
                    bictOut.println(action +" "+ action.getName());
                    selectedMinorKeys.add(action.getCode());
                    action.setChosen(true);
                }
        );

        //choose another minor
        System.out.println("You must do three minors : Pick one-(SD,GP,TCN,DM)");
        String selectedMinor = input.nextLine().toUpperCase();
        System.out.println("Compulsory minors");
        bictOut.print("Compulsory minors");

        compulsoryMinors.forEach((Option member) -> {
            System.out.println(member);
            bictOut.println(member);
            member.setChosen(true);
        });


        minorKeys.stream().filter((key) -> (selectedMinor.equals(key))).forEachOrdered((key) -> {
            if( selectedMinorKeys.add(key))
                System.out.println("Success");
            else
                System.out.println(key+"\t Already exists");
        }); //extraSelectedMinor = (Option)minors.get(key);


        //this is a set for storing the minors options
        Set<Option> minorsOptionsSet = new HashSet<>();
        Option minorOption;// = new Option();
        //this is where the keys are read and assigned to options
        Iterator minorKeyIterator = selectedMinorKeys.iterator();
        while(minorKeyIterator.hasNext()){
            minorOption = (Option)minors.get((String)minorKeyIterator.next());
            boolean add;
            add = minorsOptionsSet.add(minorOption);
        }

        Set<String> selectedCoursesKeys = new HashSet<>();
        Map<String,Course> myCourses = new HashMap<>();

        //a little bit of spacing
        System.out.println("");
        minorsOptionsSet.forEach((Option action) -> {
            System.out.println(action.getCode()+ " "+action.getName().toUpperCase());
            downStreamCourse = action.getDownstream();

            downStreamCourse.forEach(member->{
                System.out.println(member.getCode()+" "+ member.getName());
                bictOut.println(member.getCode()+" "+ member.getName());
                selectedCoursesKeys.add(member.getCode());
                myCourses.put(member.getCode(),(Course)courses.get(member.getCode()));
                member.setChosen(true);
            });
            //a little bit of spacing
            // selectedCoursesKeys.add(action.getCode());
            System.out.println("");
            action.setChosen(true);
        });

        //a little bit of spacing
        System.out.println("");
        // Map<String,Course> myCourses = new HashMap<>();
       /* Iterator courseKeyIterator = selectedCoursesKeys.iterator();
        while(courseKeyIterator.hasNext()){
         //   Course put = myCourses.put((String)courseKeyIterator.next(), (Course) courses.get((String)courseKeyIterator.next()));
            //put.setChosen(true);
        }*/

        Map<String,Course> pre = new HashMap<>();

        myCourses.entrySet().forEach((Map.Entry<String, Course> course) -> {
            //code
            //name
            String preRequisites = course.getValue().getPreReqs().toString();
            String[] preRequisitesArray = null;
            if(!preRequisites.isEmpty()){
                preRequisitesArray =   preRequisites.split(",");
                // pre.put(, value)
                for(String preArray : preRequisitesArray){
                    pre.put(preArray,(Course)courses.get(preArray));
                }
            }

            if(course.getValue().isSatisfied(myCourses)){
                System.out.println(course.getValue().getCode()+"\t"+course.getValue().getName()+"\t Semester:"+course.getValue().getSemesters()+"\t Prerequisites \t"+"\t"+preRequisites+
                        "\t SATISFIED");
            }
            else{
                System.out.println(course.getValue().getCode()+"\t"+course.getValue().getName()+"\t Semester:"+course.getValue().getSemesters()+"\t Prerequisites \t"+preRequisites+
                        "\t NOT SATISFIED");
            }
            bictOut.println(course.getValue().getCode()+"\t"+course.getValue().getName()+"\t Semester:"+course.getValue().getSemesters()+"\t Prerequisites \t"+preRequisites);
            //pre.clear();
        });
//                 file.setWritable(false);
        bictOut.close();
    }
}

