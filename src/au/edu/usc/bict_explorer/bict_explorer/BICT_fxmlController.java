
package au.edu.usc.bict_explorer.bict_explorer;

import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static au.edu.usc.bict_explorer.bict_explorer.BICT_txt_explorer.downStreamCourse;

/**
 * @author Ben Hamilton
 */

/**
 * BICT GUI / FXML Controller Class
 * Handles the shape additions to the scene and shape/node behaviour
 */

public class BICT_fxmlController implements Initializable { //initialise the controller when created in the call to FXMLLoader.load

    Degree myDegree;
    Option selectedCareer;
    File minorsFile;
    File courseFile;
    File careersFile;
    Map<String, Option> courses;
    Map<String, Option> careers;
    Map<String, Option> minors;
    Set<String> careerKeys;
    Set<String> minorKeys;
    Set<String> coursesKeys;
    String careerChoice;
    Set<Option> minorCourseForthisCareer = new LinkedHashSet<>();
    private Button next;
    private ObservableList<Node> node;
    private VBox reportBox = new VBox( 1 );
    List<String> minorCourse = new ArrayList<>();

    ObservableList<String> careerListItems = FXCollections.observableArrayList();

    @FXML
    ListView<String> careersListView;

    ObservableList<String> compulsoryMinorsList = FXCollections.observableArrayList();

    @FXML
    private Text minor1;

    @FXML
    private Text minor2;

    @FXML
    private Text minor3;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private Button confirmMinor;

    @FXML
    private Pane compulsoryMinorsPane;

    @FXML
    private Pane reportPane;

    @FXML
    private MenuItem guiClose;

    @FXML
    private MenuItem fileOpen;

    @FXML
    private MenuItem fileSave;

    @FXML
    private Text errorStatus;

    ArrayList<String> extraMinors = new ArrayList<>();

    ObservableList<String> choiceBoxItems = FXCollections.observableArrayList();

    private Option compulsoryMinors;

    //ArrayList<String> selectedMinorCourses =new ArrayL
    Set<String> selectedMinorKeys = new LinkedHashSet<>();

    /**
     * Initializes the controller class.
     * <p>
     * /**
     * Career observable list and used to listen when list changes
     * Observability by wrapping lists with ObservableList.
     *
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            minorsFile = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
            courseFile = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );
            careersFile = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );

            myDegree = new Degree( careersFile, minorsFile, courseFile );
        } catch (IOException | ParseException ex) {
            Logger.getLogger( BICT_fxmlController.class.getName() ).log( Level.SEVERE, null, ex );
        }

        courses = myDegree.courses();
        careers = myDegree.careers();
        minors = myDegree.minors();

        careerKeys = careers.keySet();
        minorKeys = minors.keySet();
        coursesKeys = courses.keySet();

        careers.entrySet().forEach( career -> {
            careerListItems.add( career.getValue().getName() );
        } );
        careersListView.setItems( careerListItems );

        //compulsory minors
        compulsoryMinorsPane.getChildren().add( displayCompulsoryMinors() );
        confirmMinor.setDisable( true );

        reportPane.getChildren().add( makeReportPane() );

    }

    File dataFile = null;
    final FileChooser fileChooser = new FileChooser();
    ToggleButton tbButton = new ToggleButton();

    /**
     * @throws IOException    to throw error if read file is incorrect.
     * @throws ParseException if the given format (txt) is not supplied. Event
     *                        handler for open file request.
     */
    @FXML
    void setOnfileOpenRequest() throws IOException, ParseException {

        fileChooser.setTitle( "Open file" );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );

        File savedFile = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        if (savedFile != null) {

            try (Scanner scan = new Scanner( savedFile )) {

                // grabbing the file data String
                String content = scan.useDelimiter( "/n" ).next(); //TODO
//            myTextArea.setText( content );

                dataFile = savedFile;

                // enabling fileSave
                fileSave.setDisable( false );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @throws IOException    to throw error if write to file error has occurred.
     * @throws ParseException if the given format (txt) is not supplied. Event
     *                        handler for save file request
     */
    @FXML
    private void setOnfileSaveAsRequest() throws IOException, ParseException {

        fileChooser.setTitle( "Save file" );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "Text Files", "*.txt" ),
                new FileChooser.ExtensionFilter( "All Files", "*.*" ) );

        File saveAsFile = fileChooser.showOpenDialog( fileOpen.getParentPopup().getScene().getWindow() );

        if (saveAsFile != null) {
            try (PrintStream ps = new PrintStream( saveAsFile )) {

//                ps.print( careersBox.getText() );
                // saving the file for use by the fileSave
                dataFile = saveAsFile;

                // enabling fileSave
                fileSave.setDisable( false );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Event handler to exit from program
     */
    @FXML
    void setOnCloseRequest() {

//        guiClose.setOnAction( t -> Platform.exit() );
        guiClose.setOnAction( t -> System.exit( 0 ) ); /// ***********TODO fix
    }

    /**
     * @throws IOException    to throw error if txt file does not exist.
     * @throws ParseException if the given format (txt) is not supplied. Event
     *                        handler for 'about/help' menu'
     */
    @FXML
    void setOnAboutRequest(ActionEvent event) throws IOException, ParseException {
        String helpFile = new File( "src/au/edu/usc/bict_explorer/bict_explorer/menuItemHelp.txt" ).toURI().toURL().toExternalForm();

        WebView webView = new WebView();
        webView.setMaxHeight( 300 );
        webView.setMaxWidth( 550 );

        errorStatus = new Text();

        if (helpFile != null) {
            try {

                webView.getEngine().load( helpFile );
                Scene scene = new Scene( webView );

//                stage.setTitle("Help Menu");
//                stage.setResizable(false);
//                stage.setScene(scene);
//                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorStatus.setText( "An ERROR occurred while loading the file!" );
            }
        }
    }

    /**
     * @param e not used
     *          Mouse even handler for career selection
     */
    @FXML
    public void onCareerSelected(MouseEvent e) {
        if (!reportBox.getChildren().isEmpty()) {
            reportBox.getChildren().clear();
            reportBox.getChildren().add( makeReportPane() );
        }

        // reportBox.getChildren().removeAll(reportBox.getChildren());
        careers.entrySet().forEach( career -> {
            career.getValue().setChosen( false );
        } );

        careerChoice = careersListView.getSelectionModel().getSelectedItem();

        careers.entrySet().forEach( career -> {
            if (career.getValue().getName().equals( careerChoice )) {
                selectedCareer = career.getValue();

                career.getValue().setChosen( true );
                myDegree.processChanges();
                updateMinors( selectedCareer );
            }

        } );

        //write to file
        next.setDisable( false );

    }

    /**
     * @param selectedCareer Unique code for a career
     *                       Defines the properties when the minor checkbox is selected or not.
     */

    private void updateMinors(Option selectedCareer) {
        // minorCourseForthisCareer.clear();
        confirmMinor.setDisable( false );
        extraMinors.clear();
        choiceBoxItems.clear();
        selectedMinorKeys.clear();
        minor3.setText( "" );

        //clearing all chosen fields
        minors.entrySet().forEach( minor -> {
            minor.getValue().setChosen( false );
        } );

        compulsoryMinors.setChosen( true );

//        List<String> minorCourse = new ArrayList<>();
        minorCourse.clear();
        minorCourseForthisCareer = selectedCareer.getDownstream();

        minorCourseForthisCareer.forEach( minor -> {
            minor.setChosen( true );
            minorCourse.add( minor.getName() );
            selectedMinorKeys.add( minor.getCode() );

        } );

        minor1.setText( minorCourse.get( 0 ) );
        minor2.setText( minorCourse.get( 1 ) );

        extraMinors = new ArrayList<>();

        minors.entrySet().forEach( (Map.Entry<String, Option> minor) -> {

            if (!minor.getValue().isChosen()) {
                extraMinors.add( minor.getValue().getName() );
            }
        } );

        choiceBoxItems.addAll( extraMinors );
        choiceBox.setItems( choiceBoxItems );

    }

    /**
     * @param e Confirm minor selection before viewing courses
     */

    @FXML
    public void onConfirmMinor(MouseEvent e) {

        selectedMinorKeys.clear();
        minorCourseForthisCareer = selectedCareer.getDownstream();

        minorCourseForthisCareer.forEach( minor -> {
            minor.setChosen( true );
            minorCourse.add( minor.getName() );
            selectedMinorKeys.add( minor.getCode() );

        } );

        if (choiceBox.getValue() != null) {

            String value = (String) choiceBox.getValue();
            minor3.setText( (String) choiceBox.getValue() );
            minors.entrySet().forEach( minor -> {
                if (value.equals( minor.getValue().getName() )) {
                    selectedMinorKeys.add( minor.getValue().getCode() );
                    minor.getValue().setChosen( true );
                    myDegree.processChanges();
                }
            } );

        }
        next.setDisable( false );

    }

    /**
     * Always displays compulsory subjects for any minor.
     */
    private VBox displayCompulsoryMinors() {
        VBox vb = new VBox( 1 );
        vb.getStyleClass().add( "pane" ); //returns an ObservableList.. to css path

        HBox title = new HBox();
        title.setPadding( new Insets( 0, 40, 0, 0 ) );
        Label courseCode = new Label( "Code" );
        courseCode.setPrefSize( 100, 5 );
        courseCode.setTextFill( Color.rgb( 27, 43, 218 ) );
        courseCode.setFont( Font.font( null, FontWeight.BOLD, 14) );

        Label courseTitle = new Label( "Course Title" );
        courseTitle.setPrefSize( 250, 5 );
        courseTitle.setTextFill( Color.rgb( 27, 43, 218 ) );
        courseTitle.setFont( Font.font( null, FontWeight.BOLD, 14 ) );

        Label semesters = new Label( "Semester" );
        title.getChildren().addAll( courseCode, courseTitle, semesters );
        semesters.setTextFill( Color.rgb( 27, 43, 218 ) );
        semesters.setFont( Font.font( null, FontWeight.BOLD, 14 ) );

        vb.getChildren().add( title );
        compulsoryMinors = minors.get( "BICT" );

        compulsoryMinors.getDownstream().forEach( minor -> {
            HBox detailsHB = new HBox();
            Course c = (Course) minor;
            c.setChosen( true );
            Label codeButton = new Label( c.getCode() );
            codeButton.setPrefSize( 75, 5 );

            Label nameLabel = new Label( c.getName() );
            nameLabel.setPrefSize( 275, 5 );

            detailsHB.getChildren().addAll( codeButton, nameLabel, new Label( "        " ), new Label( c.getSemesters() ) );
            vb.getChildren().add( detailsHB );

        } );

        vb.getChildren().add( new Label( "   " ) ); //for space
        vb.getChildren().add( new Label( "Choose one elective:" ) );
        vb.getChildren().add( new Label( "   " ) ); //for space

        HBox hb1 = new HBox();
        CheckBox cb1 = new CheckBox( "ICT341" );
        Course choice1 = (Course) courses.get( cb1.getText() );
        hb1.getChildren().addAll( cb1, new Label( choice1.getName() ), new Label( choice1.getSemesters() ) );

        HBox hb2 = new HBox();
        CheckBox cb2 = new CheckBox( "ICT342" );
        Course choice2 = (Course) courses.get( cb2.getText() );
        hb2.getChildren().addAll( cb2, new Label( choice2.getName() ), new Label( choice2.getSemesters() ) );
        vb.getChildren().addAll( hb1, hb2 );

        vb.getChildren().add( new Label( "   " ) ); //for space
        next = new Button( "Next ->" );
        next.setAlignment( Pos.BOTTOM_RIGHT );
        next.setOnAction( e -> {
            makeReport();
        } );
        vb.getChildren().add( next );

        return vb;

    }

    /**
     * Makes report based pane
     */
    private VBox makeReportPane() {
        if (!reportBox.getChildren().isEmpty()) {
            reportBox.getChildren().clear();
            reportBox.getChildren().add( makeReportPane() );
        }

        VBox content = new VBox();

        Label minor = new Label( "BICT Minor" );
        minor.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );
        HBox title = new HBox();
        Label code = new Label( "Code" );
        code.setPrefSize( 50, 5 );
        code.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );

        Label courseTitle = new Label( "Title" );
        courseTitle.setPrefSize( 300, 5 );
        courseTitle.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );
        Label sem = new Label( "Semesters" );
        sem.setPrefSize( 75, 5 );
        sem.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );
        Label preq = new Label( "Prerequisites" );
        preq.setPrefSize( 230, 5 );
        preq.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );
        Label satisfied = new Label( "Satisfied" );
        satisfied.setTextFill( Color.rgb( 27, 43, 218 ) );
        minor.setFont( Font.font( null, FontWeight.BOLD, 13 ) );

        title.getChildren().addAll( code, courseTitle, sem, preq, satisfied );

        content.getChildren().addAll( minor, title );

        return content;
    }

    /**
     * Makes the report based on user selection.
     */

    private void makeReport() {
        if (!reportBox.getChildren().isEmpty()) {
            reportBox.getChildren().clear();
            reportBox.getChildren().add( makeReportPane() );
        }

        if (selectedMinorKeys.size() == 2) {
            //Alert dialog
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "BICT Minor" );
            alert.setContentText( "You must pick an an extra BICT minor" );
            alert.show();

        } else {

            next.setDisable( true );
            node = reportPane.getChildren();
            reportBox = (VBox) node.get( node.size() - 1 );

            Set<Option> minorsOptionsSet = new HashSet<>();

            Option minorOption;// = new Option();
            //this is where the keys are read and assigned to options
            Iterator minorKeyIterator = selectedMinorKeys.iterator();
            while (minorKeyIterator.hasNext()) {
                minorOption = (Option) minors.get( (String) minorKeyIterator.next() );
                boolean add;
                add = minorsOptionsSet.add( minorOption );
            }

            Set<String> selectedCoursesKeys = new HashSet<>();
            Map<String, Course> myCourses = new HashMap<>();

            minorsOptionsSet.forEach( (Option action) -> {

                downStreamCourse = action.getDownstream();

                downStreamCourse.forEach( member -> {

                    // bictOut.println(member.getCode()+" "+ member.getName());
                    selectedCoursesKeys.add( member.getCode() );
                    myCourses.put( member.getCode(), (Course) courses.get( member.getCode() ) );
                    member.setChosen( true );
                } );

                action.setChosen( true );
            } );

            Map<String, Course> pre = new HashMap<>();

            myCourses.entrySet().forEach( (Map.Entry<String, Course> course) -> {

                HBox hb = new HBox( 20 );
                String preRequisites = course.getValue().getPreReqs().toString();

                Label code = new Label( course.getValue().getCode() );
                code.setPrefSize( 50, 5 );

                Label name = new Label( course.getValue().getName() );
                name.setPrefSize( 300, 5 );

                Label sem = new Label( course.getValue().getSemesters() );
                sem.setPrefSize( 50, 5 );

                Label preQ = new Label( preRequisites );
                preQ.setPrefSize( 200, 5 );


                ImageView satisfiedIcon = new ImageView();
                satisfiedIcon.setFitHeight(15);
                satisfiedIcon.setFitWidth(15);

                if (course.getValue().isSatisfied(myCourses)) {

                    Image satisfiedImage = new Image("src/au/edu/usc/bict_explorer/resources/tick.png");

                    Tooltip tip = new Tooltip("Prerequisites satisfied");
                    Tooltip.install(satisfiedIcon, tip);
                    satisfiedIcon.setImage(satisfiedImage);

                    hb.getChildren().addAll(code, name, sem, preQ, satisfiedIcon);

                } else {
                    Image dissatisfiedImage = new Image("src/au/edu/usc/bict_explorer/resources/wrong.png");
                    Tooltip tip = new Tooltip("Prerequisites not satisfied");
                    Tooltip.install(satisfiedIcon, tip);
                    satisfiedIcon.setImage(dissatisfiedImage);

                    hb.getChildren().addAll(code, name, sem, preQ, satisfiedIcon);
                    // hb.getChildren().addAll(new Label(course.getValue().getCode()),new Label(course.getValue().getName()),new Label(course.getValue().getSemesters()),new Label(preRequisites),new Label("NOT OK"));
                }

                reportBox.getChildren().add( hb );
            } );
        }

    }
}
