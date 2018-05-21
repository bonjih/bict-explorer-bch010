package au.edu.usc.bict_explorer.bict_explorer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import au.edu.usc.bict_explorer.rules.Degree;
import au.edu.usc.bict_explorer.rules.Option;

/**
 * FXML Controller class
 *
 * @author Ben Hamilton
 */
public class BICT_fxmlController implements Initializable {
    Degree myDegree;
    Option selectedCareer;
    File fileCareers;
    File fileMinors;
    File fileCourses;
    Map<String,Option> courses;
    Map<String,Option> careers ;
    Map<String,Option> minors;
    Set<String> careerKeys;
    Set<String> minorKeys;
    Set<String> coursesKeys;
    String careerChoice;
    Set<Option> minorCourseForthisCareer ;


    ObservableList<String> careerListItems = FXCollections.observableArrayList();

    @FXML
    ListView<String> careersList;

    @FXML
    CheckBox soft_dev_cb;

    @FXML
    CheckBox game_prog_cb;

    @FXML
    CheckBox database_cb;

    @FXML
    CheckBox telcoms_cb;

    @FXML
    CheckBox info_systems;

    //Map<String,CheckBox> checkBoxMap = new HashMap<>();
    //ObservableList<CheckBox> minorsCheckBox  = FXCollections.observableArrayList(soft_dev_cb,game_prog_cb,database_cb,telcom_net_cb);
    Set<String> selectedMinorKeys = new LinkedHashSet<>();
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // TODOFile careersFile = new File("E:\\javaprojects\\BICT_explorer\\src\\resources\\careers.options");

            fileCareers = new File( "src/au/edu/usc/bict_explorer/resources/careers.options" );
            fileMinors = new File( "src/au/edu/usc/bict_explorer/resources/minors.options" );
            fileCourses = new File( "src/au/edu/usc/bict_explorer/resources/courses.options" );

            myDegree = new Degree(fileCareers,fileMinors,fileCourses);

        } catch (IOException | ParseException ex) {
            Logger.getLogger(BICT_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }

        courses = myDegree.courses();
        careers = myDegree.careers();
        minors = myDegree.minors();

        careerKeys = careers.keySet();
        minorKeys = minors.keySet();
        coursesKeys = courses.keySet();

        careers.entrySet().forEach(career->{
            careerListItems.add(career.getValue().getName());
        });
        careersList.setItems(careerListItems);

        //compulsory minors
        Set<Option> compulsoryMinors = minors.get("BICT").getDownstream();

        soft_dev_cb.setDisable(true);
        game_prog_cb.setDisable(true);
        database_cb.setDisable(true);
        telcoms_cb.setDisable(true);
        info_systems.setDisable(true);

        //soft_dev_cb.setE
        Iterator minorKeysIterator = minorKeys.iterator();

    }
    @FXML
    public void onCareerSelected(MouseEvent e){
        careerChoice = careersList.getSelectionModel().getSelectedItem();
        String careerKey=null;
        //write to file
        int choice = careersList.getSelectionModel().getSelectedIndex();
        // System.out.println(choice);
        switch(choice){
            //Analyst programmer
            case 0:careerKey = "AP";
                break;
            //Network analyst
            case 1: careerKey = "NA";
                break;
            //Database administrator
            case 2:careerKey = "DA";
                break;
            //Business Analyst
            case 3:careerKey = "BA";
                break;

            default:
        }

        for(String key:careerKeys){
            if(key.equals(careerKey)){
                selectedCareer = (Option)careers.get(key);
                selectedCareer.setChosen(true);
                myDegree.processChanges();
                myDegree.careers().get(key).setChosen(true);
                updateMinors(selectedCareer);
            }
        }

    }

    private void updateMinors(Option selectedCareer){

        soft_dev_cb.setDisable(false);
        game_prog_cb.setDisable(false);
        database_cb.setDisable(false);
        telcoms_cb.setDisable(false);
        info_systems.setDisable(false);

        soft_dev_cb.setSelected(false);
        game_prog_cb.setSelected(false);
        database_cb.setSelected(false);
        info_systems.setSelected(false);
        telcoms_cb.setSelected(false);

        //System.out.println(soft_dev_cb.getText());
        minorCourseForthisCareer = selectedCareer.getDownstream();

        Object[] keys=selectedMinorKeys.toArray();
        ArrayList<Object> keysList= new ArrayList<>(Arrays.asList(keys));
        //   Object[] toArray = keysList.toArray(keys);

        if(selectedMinorKeys.size()>2){

            while(selectedMinorKeys.size()>1){
                String removeKey =(String)keysList.get(keysList.size()-1);
                selectedMinorKeys.remove(removeKey);
                keysList.remove(removeKey);

                if(selectedMinorKeys.size()==1){break;}

            }

        }

        minorCourseForthisCareer.forEach(action->{

            selectedMinorKeys.add(action.getCode());

            action.setChosen(true);

            if(action.getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(true);
                soft_dev_cb.setDisable(true);

            }
            else if(action.getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(true);
                game_prog_cb.setDisable(true);
            }
            else if(action.getName().equals(database_cb.getText())){
                database_cb.setSelected(true);
                database_cb.setDisable(true);
            }
            else if(action.getName().equals(info_systems.getText())){
                info_systems.setSelected(true);
                info_systems.setDisable(true);
            }
            else if(action.getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(true);
                telcoms_cb.setDisable(true);
            }
        });
        System.out.println(selectedMinorKeys.size());
    }

    @FXML
    public void onSoftDevChecked(Event e){
        if(selectedMinorKeys.size()==3){
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys=selectedMinorKeys.toArray();
            String removeKey =(String)keys[keys.length-1];
            if(minors.get(removeKey).getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(database_cb.getText())){
                database_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(info_systems.getText())){
                info_systems.setSelected(false);
            }
            selectedMinorKeys.remove(removeKey);
            System.out.println(selectedMinorKeys.size());


        }

        if(selectedMinorKeys.size()==2 && soft_dev_cb.isSelected()){
            // String key=null;
            selectedMinorKeys.add("SD");

            if(game_prog_cb.isSelected()){game_prog_cb.setDisable(true);}
            else game_prog_cb.setDisable(false);

            if(database_cb.isSelected()){ database_cb.setDisable(true);}
            else  database_cb.setDisable(false);

            if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
            else telcoms_cb.setDisable(false);

            if(info_systems.isSelected()){info_systems.setDisable(true);}
            else info_systems.setDisable(false);

            System.out.println(selectedMinorKeys.size());
        }
        else {}
    }

    @FXML
    public void onGameProgChecked(ActionEvent e){
        //System.out.println(" HELLO");
        if(selectedMinorKeys.size()==3){
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys=selectedMinorKeys.toArray();
            String removeKey =(String)keys[keys.length-1];

            if(minors.get(removeKey).getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(database_cb.getText())){
                database_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(info_systems.getText())){
                info_systems.setSelected(false);
            }
            selectedMinorKeys.remove(removeKey);
            System.out.println(selectedMinorKeys.size());
        }
        if(selectedMinorKeys.size()==2 && game_prog_cb.isSelected()){
            // String key=null;
            selectedMinorKeys.add("GP");

            if(soft_dev_cb.isSelected()){soft_dev_cb.setDisable(true);}
            else soft_dev_cb.setDisable(false);

            if(database_cb.isSelected()){ database_cb.setDisable(true);}
            else  database_cb.setDisable(false);

            if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
            else telcoms_cb.setDisable(false);

            if(info_systems.isSelected()){info_systems.setDisable(true);}
            else info_systems.setDisable(false);

            System.out.println(selectedMinorKeys.size());
        }
    }

    @FXML
    public void onTelcomChecked(Event e){
        if(selectedMinorKeys.size()==3){
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys=selectedMinorKeys.toArray();
            String removeKey =(String)keys[keys.length-1];

            if(minors.get(removeKey).getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(database_cb.getText())){
                database_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(info_systems.getText())){
                info_systems.setSelected(false);
            }
            selectedMinorKeys.remove(removeKey);
            System.out.println(selectedMinorKeys.size());
        }
        if(selectedMinorKeys.size()==2 && telcoms_cb.isSelected()){
            // String key=null;
            selectedMinorKeys.add("TCN");

            if(soft_dev_cb.isSelected()){soft_dev_cb.setDisable(true);}
            else soft_dev_cb.setDisable(false);

            if(database_cb.isSelected()){ database_cb.setDisable(true);}
            else  database_cb.setDisable(false);

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if(game_prog_cb.isSelected()){game_prog_cb.setDisable(true);}
            else game_prog_cb.setDisable(false);

            if(info_systems.isSelected()){info_systems.setDisable(true);}
            else info_systems.setDisable(false);

            System.out.println(selectedMinorKeys.size());
        }
    }

    @FXML
    public void onDataMgmentChecked(Event e){
        if(selectedMinorKeys.size()==3){
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys=selectedMinorKeys.toArray();
            String removeKey =(String)keys[keys.length-1];

            if(minors.get(removeKey).getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(database_cb.getText())){
                database_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(info_systems.getText())){
                info_systems.setSelected(false);
            }
            selectedMinorKeys.remove(removeKey);
            System.out.println(selectedMinorKeys.size());
        }
        if(selectedMinorKeys.size()==2 && database_cb.isSelected()){
            // String key=null;
            selectedMinorKeys.add("DM");

            if(soft_dev_cb.isSelected()){soft_dev_cb.setDisable(true);}
            else soft_dev_cb.setDisable(false);

            if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
            else  telcoms_cb.setDisable(false);

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if(game_prog_cb.isSelected()){game_prog_cb.setDisable(true);}
            else game_prog_cb.setDisable(false);

            if(info_systems.isSelected()){info_systems.setDisable(true);}
            else info_systems.setDisable(false);

            System.out.println(selectedMinorKeys.size());
        }
    }

    @FXML
    public void onInfoSystemsChecked(Event e){

        if(selectedMinorKeys.size()==3){
            //selectedMinorKeys.remove(selectedMinorKeys.)
            Object[] keys=selectedMinorKeys.toArray();
            String removeKey =(String)keys[keys.length-1];

            if(minors.get(removeKey).getName().equals(soft_dev_cb.getText())){
                soft_dev_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(game_prog_cb.getText())){
                game_prog_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(database_cb.getText())){
                database_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(telcoms_cb.getText())){
                telcoms_cb.setSelected(false);
            }
            else if(minors.get(removeKey).getName().equals(info_systems.getText())){
                info_systems.setSelected(false);
            }
            selectedMinorKeys.remove(removeKey);
            System.out.println(selectedMinorKeys.size());
        }

        if(selectedMinorKeys.size()==2 && info_systems.isSelected()){
            // String key=null;
            selectedMinorKeys.add("IS");

            if(soft_dev_cb.isSelected()){soft_dev_cb.setDisable(true);}
            else soft_dev_cb.setDisable(false);

            if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
            else  telcoms_cb.setDisable(false);

//                    if(telcoms_cb.isSelected()){ telcoms_cb.setDisable(true);}
//                    else telcoms_cb.setDisable(false);
            if(game_prog_cb.isSelected()){game_prog_cb.setDisable(true);}
            else game_prog_cb.setDisable(false);

            if(database_cb.isSelected()){database_cb.setDisable(true);}
            else database_cb.setDisable(false);

            System.out.println(selectedMinorKeys.size());
        }

    }


}
