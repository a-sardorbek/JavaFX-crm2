
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import databaseConnect.Patients;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class DeleteController implements Initializable {

 
    private final Connection con;
    @FXML
    private TableView<Patients> listTableView;
    private JFXButton deleteBtn;
    @FXML
    private TableColumn<Patients, String> nameTable;
    @FXML
    private TableColumn<Patients, String> surnameTable;
    @FXML
    private TableColumn<Patients, String> phoneTable;
    @FXML
    private TableColumn<Patients, String> borthDateTable;
    @FXML
    private TableColumn<Patients, Integer> paymentTable;
    @FXML
    private TableColumn<Patients, String> regTabe;
    @FXML
    private JFXTextField searchField;
    
    Patients patients = null;
    @FXML
    private TableColumn<Patients, String> deleteTable;
    
    public DeleteController(){
        con = DbConnection.getConnection(true);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPatients1();
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
            e.printStackTrace();
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
        
        
        nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameTable.setCellValueFactory(new PropertyValueFactory<>("surname"));
        borthDateTable.setCellValueFactory(new PropertyValueFactory<>("borthDate"));
        phoneTable.setCellValueFactory(new PropertyValueFactory<>("phone"));
        paymentTable.setCellValueFactory(new PropertyValueFactory<>("tolov"));
        regTabe.setCellValueFactory(new PropertyValueFactory<>("regDate"));

        
        Callback<TableColumn<Patients, String>, TableCell<Patients, String>> cellFoctory = (TableColumn<Patients, String> param) -> {
            
            final TableCell<Patients, String> cell = new TableCell<Patients, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                patients = listTableView.getSelectionModel().getSelectedItem();
                                String query = "UPDATE patients1 SET deleted=true WHERE id = "+patients.getId();
                                PreparedStatement prs = con.prepareStatement(query);
                                prs.execute();
                                showPatients1();
                                AlertHelper.showAlert(AnimationType.POPUP, "Malumot o'chdi", "", NotificationType.INFORMATION, 2000);
                            } catch (SQLException ex) {
                                Logger.getLogger(DeleteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        
                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);
                        setText(null);

                    }
                }

            };

            return cell;
        };
         deleteTable.setCellFactory(cellFoctory);
        
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
    
}
