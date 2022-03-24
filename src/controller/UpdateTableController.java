
package controller;

import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import databaseConnect.Patients;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class UpdateTableController implements Initializable {

    @FXML
    private TableColumn<Patients, String> changeField;
    @FXML
    private JFXTextField searchField;
    @FXML
    private TableView<Patients> listTableView;
    @FXML
    private TableColumn<Patients, String> ismField;
    @FXML
    private TableColumn<Patients, String> surnameFieldd;
    @FXML
    private TableColumn<Patients, String> phoneFieldd;
    @FXML
    private TableColumn<Patients, String> borthField;
    @FXML
    private TableColumn<Patients, Integer> paymentFiel;
    @FXML
    private TableColumn<Patients, String> regField;

    Patients patients =null;
    @FXML
    private Button refreshBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       showPatients1();
    }    
    
    
     private final Connection con;
     public UpdateTableController(){
         con = DbConnection.getConnection(true);
     }
     
      private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
            System.out.println("Deleted");
            showPatients1();
        } catch (SQLException e) {
            System.out.println("Error in deleete");
        }
    }
    
    public ObservableList<Patients> getStudentList(){
        ObservableList<Patients> patientList = FXCollections.observableArrayList();
        patientList.clear();
        String query = "select * from patients1 where deleted=false order by id desc";
        Statement st;
        ResultSet rs;
        Patients patients;
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            patients = new Patients(rs.getInt("id"),rs.getString("name")
                                    , rs.getString("surname")
                                    , rs.getString("borthDate")
                                    , rs.getString("phone")
                                    , rs.getInt("tolov")
                                    , rs.getString("regDate"));
            patientList.add(patients);
        }
        }catch(SQLException e){
            System.out.println("Error in getting data");
        }
        return patientList;
    }
    
    public void showPatients1(){
        
        ObservableList<Patients> list = getStudentList();
        
        
        ismField.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameFieldd.setCellValueFactory(new PropertyValueFactory<>("surname"));
        borthField.setCellValueFactory(new PropertyValueFactory<>("borthDate"));
        phoneFieldd.setCellValueFactory(new PropertyValueFactory<>("phone"));
        paymentFiel.setCellValueFactory(new PropertyValueFactory<>("tolov"));
        regField.setCellValueFactory(new PropertyValueFactory<>("regDate"));

        
        Callback<TableColumn<Patients, String>, TableCell<Patients, String>> cellFoctory = (TableColumn<Patients, String> param) -> {
            
            final TableCell<Patients, String> cell = new TableCell<Patients, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);

                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                             patients = listTableView.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/view/Update.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                            }
                            
                            UpdateController uc = loader.getController();
                             
                             
                             
                            uc.setUpdate(true);
                            uc.setTextField(patients.getId(), patients.getName(), patients.getSurname(), patients.getBorthDate(), patients.getPhone(), patients.getTolov());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                        });
                        
                        HBox managebtn = new HBox(editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);
                        setText(null);

                    }
                }

            };

            return cell;
        };
         changeField.setCellFactory(cellFoctory);
        
        listTableView.setItems(list);
        
        FilteredList<Patients> filteredData = new FilteredList<>(list, p -> true);
		
		
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (person.getSurname().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				}else if(person.getBorthDate().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                }else if(person.getPhone().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                }else if(person.getRegDate().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                }
				return false; 
			});
		});
		
		
		SortedList<Patients> sortedData = new SortedList<>(filteredData);
		
		
		sortedData.comparatorProperty().bind(listTableView.comparatorProperty());
		
		
		listTableView.setItems(sortedData);
        
    }

    @FXML
    private void refreshClicked(MouseEvent event) {
        if(event.getSource()==refreshBtn){
            showPatients1();
        }
    }
    
}
