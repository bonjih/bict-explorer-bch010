
package au.edu.usc.bict_explorer.bict_explorer;

import au.edu.usc.bict_explorer.rules.Course;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static au.edu.usc.bict_explorer.bict_explorer.BICT_txt_explorer.downStreamCourse;

/**
 * *
 *
 * @author Ben Hamilton (aka bonjih)
 * <p>
 * BICT GUI / FXML Controller Class
 * Handles the shape additions to the scene and shape/node behaviour
 */

public class BICT_fxmlController implements Initializable { //initialise the controller when created in the call to FXMLLoader.load

    Degree myDegree;
    Option selectedCareer;
    Option currentMinorOption;
    Course currentCourseOption;
    File minorsFile;
    File courseFile;
    File careersFile;
    File file = new File( "bict.txt" );
    PrintWriter bictOut;
    Map<String, Option> courses;
    Map<String, Option> careers;
    Map<String, Option> minors;
    Set<String> careerKeys;
    Set<String> minorKeys;
    Set<String> coursesKeys;
    Set<String> electives = new LinkedHashSet<>();
    Set<Option> minorCourseForthisCareer = new LinkedHashSet<>();
    Set<String> selectedMinorKeys = new LinkedHashSet<>();
    List<String> minorCourse = new ArrayList<>();
    ArrayList<String> extraMinors = new ArrayList<>();
    String selectedElective;
    String careerChoice;
    ObservableList<String> careerListItems = FXCollections.observableArrayList();
    ObservableList<String> choiceBoxItems = FXCollections.observableArrayList();
    ToggleGroup group = new ToggleGroup();

    private Button next;
    private Map<String, Course> myCourses;
    private ObservableList<Node> node;
    private VBox reportBox = new VBox( 2 );
    private VBox detailsBox = new VBox( 1 );
    private Option compulsoryMinors;

    @FXML
    ListView<String> careersListView;

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
    private Pane descriptionBox;

    @FXML
    private ToggleButton elective1;

    @FXML
    private ToggleButton elective2;

    @FXML
    private ToggleButton elective3;

    @FXML
    private ToggleButton elective4;

    @FXML
    private ToggleButton elective5;

    @FXML
    private ToggleButton elective6;

    @FXML
    private Text minorIS;

    @FXML
    private Text minorGP;

    @FXML
    private Text minorTCN;

    @FXML
    private Text minorDM;

    @FXML
    private Text minorSD;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField numOfCourses;

    @FXML
    private TextField numOfMinors;

    @FXML
    private MenuItem guiClose;

    @FXML
    private Text errorStatus;

    /**
     * @throws FileNotFoundException - for print report method file not found
     */
    public BICT_fxmlController() throws FileNotFoundException {

        bictOut = new PrintWriter( file );
    }

    /**
     * Initializes the controller class.
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

        compulsoryMinorsPane.getChildren().add( displayCompulsoryMinors() );

        //report box
        reportBox.setPadding( new Insets( 50, 0, 0, 0 ) );
        reportPane.getChildren().add( reportBox );

        //details box
        detailsBox.setPadding( new Insets( 20, 0, 0, 0 ) );
        descriptionBox.getChildren().add( detailsBox );

        //electives
        coursesKeys.forEach( key -> {
            if (key.startsWith( "EN" ) || key.startsWith( "MT" ) || key.startsWith( "DES" )) {
                electives.add( key );
            }
        } );

        // new elective option. Add into courses.options
        elective1.setText( courses.get( "MTH103" ).getName() );
        elective2.setText( courses.get( "MTH212" ).getName() );
        elective3.setText( courses.get( "DES221" ).getName() );
        elective4.setText( courses.get( "DES222" ).getName() );
        elective5.setText( courses.get( "ENG103" ).getName() );
        elective6.setText( courses.get( "DES223" ).getName() );

        elective1.setToggleGroup( group );
        elective2.setToggleGroup( group );
        elective3.setToggleGroup( group );
        elective4.setToggleGroup( group );
        elective5.setToggleGroup( group );
        elective6.setToggleGroup( group );

        //minors
        minorSD.setOnMouseClicked( e -> {
            makeMinorDetailsBox( e.getSource() );
        } );

        minorIS.setOnMouseClicked( e -> {
            makeMinorDetailsBox( e.getSource() );
        } );

        minorTCN.setOnMouseClicked( e -> {
            makeMinorDetailsBox( e.getSource() );
        } );

        minorGP.setOnMouseClicked( e -> {
            makeMinorDetailsBox( e.getSource() );
        } );

        minorDM.setOnMouseClicked( e -> {
            makeMinorDetailsBox( e.getSource() );
        } );

        confirmMinor.setDisable( true );

    }

    /**
     * Event handler to exit from program
     */
    @FXML
    void setOnCloseRequest() {

//        guiClose.setOnAction( t -> Platform.exit() ); /// ***********TODO Also 2 x clicks to close
        guiClose.setOnAction( t -> System.exit( 0 ) ); /// ***********TODO 2 x clicks to close
    }

    /**
     * @throws IOException    to throw error if txt file does not exist.
     * @throws ParseException if the given format (txt) is not supplied. Event
     *                        handler for 'about/help' menu'
     */
    @FXML
    void setOnAboutRequest() throws IOException, ParseException {
        String helpFile = new File( "src/au/edu/usc/bict_explorer/bict_explorer/menuItemHelp.txt" ).toURI().toURL().toExternalForm();

        WebView webView = new WebView();
        webView.setMaxHeight( 300 );
        webView.setMaxWidth( 550 );
        webView.setStyle( "-fx-border-color: black;" );

        errorStatus = new Text();

        if (helpFile != null) {
            try {

                webView.getEngine().load( helpFile );
                Scene scene = new Scene( webView );
                Stage stage = new Stage();
                stage.setTitle( "Help Menu" );
                stage.setResizable( false );
                stage.setScene( scene );
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                errorStatus.setText( "An ERROR occurred while loading the file!" );
            }
        }
    }

    /**
     * @param object creates source
     *               Provides the description of each minor
     */
    private void makeMinorDetailsBox(Object object) {
        Text sourceText = (Text) object;
        String minorText = sourceText.getText();

        minors.entrySet().forEach( minor -> {
            if (minor.getValue().getName().equals( minorText )) {
                currentMinorOption = minor.getValue();
            }
        } );
        if (!detailsBox.getChildren().isEmpty()) {
            detailsBox.getChildren().clear();
        }

        if (currentMinorOption instanceof Option) {
            String name = currentMinorOption.getName();
            String description = currentMinorOption.getDescription();
            Set<Option> downStream = currentMinorOption.getDownstream();

            Label nameLabel = new Label( name );
            nameLabel.setStyle( "-fx-font-size:16; -fx-font-weight:bold" );

            Label descriptionLabel = new Label( "DESCRIPTION:" + description );
            descriptionLabel.setStyle( "-fx-font-size:15; -fx-font-weight:bold " );

            detailsBox.getChildren().addAll( nameLabel, descriptionLabel );

            Label bictMinor = new Label( "BICT MINOR" );

            detailsBox.getChildren().add( bictMinor );
            downStream.forEach( option -> {
                Label label = new Label( option.getName() );

                detailsBox.getChildren().add( label );
            } );

        }
    }

    /**
     * the following functions are for the elective selections 1-6.
     * No the best implementation as there is duplicate code
     * Intellij shows repeating code, however not all elective methods have 1,2,3,4,5,6, combination of.
     */
    @FXML
    public void onElective1selected() {
        if (elective1.isSelected()) {
            elective1.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective1.getText();
            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );
        }

        elective2.setStyle( "-fx-background-color:#94baf7" );
        elective3.setStyle( "-fx-background-color:#94baf7" );
        elective4.setStyle( "-fx-background-color:#94baf7" );
        elective5.setStyle( "-fx-background-color:#94baf7" );
        elective6.setStyle( "-fx-background-color:#94baf7" );

    }

    @FXML
    public void onElective2selected() {
        if (elective2.isSelected()) {
            elective2.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective2.getText();

            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );
        }

        elective1.setStyle( "-fx-background-color:#94baf7" );
        elective3.setStyle( "-fx-background-color:#94baf7" );
        elective4.setStyle( "-fx-background-color:#94baf7" );
        elective5.setStyle( "-fx-background-color:#94baf7" );
        elective6.setStyle( "-fx-background-color:#94baf7" );
    }

    @FXML
    public void onElective3selected() {
        if (elective3.isSelected()) {
            elective3.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective3.getText();

            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );
        }

        elective1.setStyle( "-fx-background-color:#94baf7" );
        elective2.setStyle( "-fx-background-color:#94baf7" );
        elective4.setStyle( "-fx-background-color:#94baf7" );
        elective5.setStyle( "-fx-background-color:#94baf7" );
        elective6.setStyle( "-fx-background-color:#94baf7" );
    }

    @FXML
    public void onElective4selected() {
        if (elective4.isSelected()) {
            elective4.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective4.getText();

            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );
        }

        elective1.setStyle( "-fx-background-color:#94baf7" );
        elective2.setStyle( "-fx-background-color:#94baf7" );
        elective3.setStyle( "-fx-background-color:#94baf7" );
        elective5.setStyle( "-fx-background-color:#94baf7" );
        elective6.setStyle( "-fx-background-color:#94baf7" );

    }

    @FXML
    public void onElective5selected() {
        if (elective5.isSelected()) {
            elective5.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective5.getText();

            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );
        }

        elective1.setStyle( "-fx-background-color:#94baf7" );
        elective2.setStyle( "-fx-background-color:#94baf7" );
        elective3.setStyle( "-fx-background-color:#94baf7" );
        elective4.setStyle( "-fx-background-color:#94baf7" );
        elective6.setStyle( "-fx-background-color:#94baf7" );

    }

    @FXML
    public void onElective6selected() {
        if (elective6.isSelected()) {
            elective6.setStyle( "-fx-background-color:#c6e519" );
            selectedElective = elective6.getText();

            courses.entrySet().forEach( course -> {
                if (course.getValue().getName().equals( selectedElective )) {
                    makeCourseDetailsBox( course.getValue().getName() );
                }
            } );

        }
        elective1.setStyle( "-fx-background-color:#94baf7" );
        elective2.setStyle( "-fx-background-color:#94baf7" );
        elective3.setStyle( "-fx-background-color:#94baf7" );
        elective4.setStyle( "-fx-background-color:#94baf7" );
        elective5.setStyle( "-fx-background-color:#94baf7" );
    }

    /**
     * Method for selecting a career and then stores into careerchoice
     */
    @FXML
    public void onCareerSelected() {
        if (!reportBox.getChildren().isEmpty()) {
            reportBox.getChildren().clear();
        }

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
        makeDetailsBox( selectedCareer );
    }

    /**
     * @param currentCareerOption loads course details for Options and stores in param
     * setups  Options detailsbox and publishes course selected details
     */
    private void makeDetailsBox(Option currentCareerOption) {
        //   if(currentCareerOption.getClass()==Option){}
        if (!detailsBox.getChildren().isEmpty()) {
            detailsBox.getChildren().clear();
        }

        if (currentCareerOption instanceof Option) {
            String name = currentCareerOption.getName();
            String description = currentCareerOption.getDescription();
            Set<Option> downStream = currentCareerOption.getDownstream();

            Label nameLabel = new Label( name );
            nameLabel.setStyle( "-fx-font-size:16; -fx-font-weight:bold" );

            Label descriptionLabel = new Label( "DESCRIPTION:" + description );
            descriptionLabel.setStyle( "-fx-font-size:15; -fx-font-weight:bold " );

            detailsBox.getChildren().addAll( nameLabel, descriptionLabel );

            Label bictMinor = new Label( "BICT MINOR" );

            detailsBox.getChildren().add( bictMinor );
            downStream.forEach( option -> {
                Label label = new Label( option.getName() );

                detailsBox.getChildren().add( label );
            } );
        }
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
     * Confirm minor selection before viewing courses
     */
    @FXML
    public void onConfirmMinor() {

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
     * @return vb - VBox content
     * Displays compulsory subjects for any minor.
     */

    // in my effort to reduce redundant code prefWidth/height is common across all.
    // this has effected overall layout
    private VBox displayCompulsoryMinors() {
        VBox vb = new VBox( 1 );
        vb.getStyleClass().add( "pane" );

        HBox title = new HBox();
        title.setPadding( new Insets( 0, 40, 0, 0 ) );
        Label courseCode = new Label( "Course Code" );
        courseCode.setPrefSize( 100, 5 );

        Label courseTitle = new Label( "Course Title" );
        courseTitle.setPrefSize( 250, 5 );

        title.getChildren().addAll( courseCode, courseTitle, new Label( "Semesters" ) );
        vb.getChildren().add( title );
        compulsoryMinors = minors.get( "BICT" );

        // compulsory course (1st years) box setup
        compulsoryMinors.getDownstream().forEach( minor -> {
            HBox detailsHB = new HBox( 1.5 );
            detailsHB.setStyle( "-fx-background-color:#94baf7" );
            Course c = (Course) minor;
            c.setChosen( true );
            Label codeLabel = new Label( c.getCode() );
            codeLabel.setPrefSize( 75, 5 );

            Label nameLabel = new Label( c.getName() );
            nameLabel.setPrefSize( 275, 5 );

            detailsHB.getChildren().addAll( codeLabel, nameLabel, new Label( c.getSemesters() ) );
            detailsHB.setOnMouseClicked( event -> {
                //  String code = ()
                HBox source = (HBox) event.getSource();
                Label label = (Label) source.getChildren().get( 1 );
                makeCourseDetailsBox( label.getText() );
            } );
            vb.getChildren().add( detailsHB );

        } );
        vb.getChildren().add( new Label( "   " ) ); //for space
        vb.getChildren().add( new Label( "Choose an elective for compulsory courses" ) );
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
            makeReport(); // TODO bug???
        } );
        vb.getChildren().add( next );

        return vb;
    }

    /**
     * @param code used to hold course code
     *             Creates Course details view
     */
    private void makeCourseDetailsBox(String code) {

        courses.entrySet().forEach( course -> {
            if (course.getValue().getName().equals( code )) {
                currentCourseOption = (Course) course.getValue();
            }
        } );
        if (!detailsBox.getChildren().isEmpty()) {
            detailsBox.getChildren().clear();
        }

        if (currentCourseOption instanceof Course) {
            String name = currentCourseOption.getName();
            String description = currentCourseOption.getDescription();
            Set<Option> downStream = currentCourseOption.getDownstream();
            String semester = currentCourseOption.getSemesters();
            String preQ = currentCourseOption.getPreReqs().toString();

            Label nameLabel = new Label( name );
            nameLabel.setStyle( "-fx-font-size:16; -fx-font-weight:bold" );

            Label descriptionLabel = new Label( "DESCRIPTION:" );
            TextArea descriptionText = new TextArea( description );
            descriptionText.setPrefSize( 650, 100 );
            descriptionText.setPrefColumnCount( 500 );
            descriptionText.setPrefRowCount( 100 );
            descriptionText.setEditable( false );
            descriptionText.setWrapText( true );
            descriptionLabel.setStyle( "-fx-font-size:15; -fx-font-weight:bold " );


            Label semLabel = new Label( "Semester :     " + semester );
            nameLabel.setStyle( "-fx-font-size:15; -fx-font-weight:bold" );
            Label preqLabel = new Label( "Prerequisites :   " + preQ );
            nameLabel.setStyle( "-fx-font-size:15; -fx-font-weight:bold" );

            detailsBox.getChildren().addAll( nameLabel, descriptionLabel, descriptionText, semLabel, preqLabel );
            downStream.forEach( option -> {
                Label label = new Label( option.getName() );

                detailsBox.getChildren().add( label );
            } );

        }
    }

    /**
     * Creates report based user selection
     * Shows errors if user information is missing.
     */
    private void makeReport() {
        if (!reportBox.getChildren().isEmpty()) {
            reportBox.getChildren().clear();
        }

        if (selectedMinorKeys.size() == 2) {
            //Alert dialog
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "BICT Minor" );
            alert.setContentText( "You must pick an an extra BICT minor" );
            alert.show();
        } else if (selectedElective == null) {
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "BICT Elective" );
            alert.setContentText( "You must pick at least one Elective" );
            alert.show();
        } else {
            next.setDisable( true );

            Set<Option> minorsOptionsSet = new HashSet<>();

            Option minorOption;// = new Option();
            //this is where the keys are read and assigned to options
            Iterator minorKeyIterator = selectedMinorKeys.iterator();
            while (minorKeyIterator.hasNext()) {
                minorOption = minors.get( minorKeyIterator.next() );
                boolean add;
                add = minorsOptionsSet.add( minorOption );
            }

            Set<String> selectedCoursesKeys = new HashSet<>();
            myCourses = new HashMap<>();

            minorsOptionsSet.forEach( (Option action) -> {

                downStreamCourse = action.getDownstream();

                downStreamCourse.forEach( member -> {

                    selectedCoursesKeys.add( member.getCode() );
                    myCourses.put( member.getCode(), (Course) courses.get( member.getCode() ) );
                    member.setChosen( true );
                } );

                action.setChosen( true );
            } );

            myCourses.entrySet().forEach( (Map.Entry<String, Course> course) -> {

                HBox hb = new HBox( 20 );
                hb.setStyle( "-fx-background-color:#94baf7" );
                String preRequisites = course.getValue().getPreReqs().toString();

                Label code = new Label( course.getValue().getCode() );
                code.setPrefSize( 55, 5 );

                Label name = new Label( course.getValue().getName() );
                name.setPrefSize( 295, 5 );

                Label sem = new Label( course.getValue().getSemesters() );
                sem.setPrefSize( 25, 5 );

                Label preQ = new Label( preRequisites );
                preQ.setPrefSize( 200, 5 );

                ImageView satisfiedIcon = new ImageView();
                satisfiedIcon.setFitHeight( 15 );
                satisfiedIcon.setFitWidth( 15 );

                if (course.getValue().isSatisfied( myCourses )) {

                    Image satisfiedImage = new Image( "https://upload.wikimedia.org/wikipedia/en/e/e4/Green_tick.png" ); // cant retrieve from local folder?
                    Tooltip tip = new Tooltip( "Prerequisites satisfied" );
                    Tooltip.install( satisfiedIcon, tip );
                    satisfiedIcon.setImage( satisfiedImage );

                    hb.getChildren().addAll( code, name, sem, preQ, satisfiedIcon );

                } else {
                    Image dissatisfiedImage = new Image( "https://upload.wikimedia.org/wikipedia/commons/7/7e/Red_x.png" ); // cant retrieve from local folder??
                    Tooltip tip = new Tooltip( "Prerequisites not satisfied" );
                    Tooltip.install( satisfiedIcon, tip );
                    satisfiedIcon.setImage( dissatisfiedImage );

                    hb.getChildren().addAll( code, name, sem, preQ, satisfiedIcon );
                }

                hb.setOnMouseClicked( event -> {
                    //  String code = ()
                    HBox source = (HBox) event.getSource();
                    Label label = (Label) source.getChildren().get( 1 );
                    makeCourseDetailsBox( label.getText() );
                } );

                reportBox.getChildren().add( hb );

            } );
        }

        //to generate course etc stats
//        myCourses.entrySet().forEach( (Map.Entry<String, Course> course) -> {
//            String preReqs = course.getValue().getPreReqs().toString();
//            List<String> preReqsList = new ArrayList<>( Arrays.asList( preReqs.split( "," ) ) );
//
//            for (int i = 0; i < preReqsList.size(); i++) {
//                numOfPreReqs.setText( String.valueOf( i ) );
//
//            }
//        } );

        int courseCount = myCourses.size(); // TODO bug when dont pick an elective
        int minorCount = minorCourse.size();
        numOfCourses.setText( String.valueOf( courseCount ) );
        numOfMinors.setText( String.valueOf( minorCount ) );
    }

    /**
     * Prints the report based on user selection.
     */
    @FXML
    private void printReportClicked() {

        if (myCourses.isEmpty()) {
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "MISSING DATA" );
            alert.setContentText( "You have not picked any course" );

            alert.show();

        } else if (nameTextField.getText().isEmpty()) {
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "NAME" );
            alert.setContentText( "You must enter your name to print the report" );
            alert.show();

        } else {
            bictOut.println( "NAME:" + nameTextField.getText().toUpperCase() );
            bictOut.println( "CAREER :" + selectedCareer );
            bictOut.println( "MINOR : " );
            selectedMinorKeys.forEach( minor -> bictOut.println( minors.get( minor ).getName() ) );
            bictOut.println( "ELECTIVE " + selectedElective );
            bictOut.println( "COURSES" );
            myCourses.entrySet().forEach( (Map.Entry<String, Course> course) -> {
                String preRequisites = course.getValue().getPreReqs().toString();
                bictOut.println( course.getValue().getCode() + "\t" + course.getValue().getName() + "\t Semester:" + course.getValue().getSemesters() + "\t Prerequisites \t" + preRequisites );
            } );
            Alert alert = new Alert( AlertType.INFORMATION );
            alert.setTitle( "COMPLETE" );
            alert.setContentText( "Report generated successfully\n" + "File location:" + file.getPath() );
            alert.show();
            bictOut.close();
        }
    }
}
