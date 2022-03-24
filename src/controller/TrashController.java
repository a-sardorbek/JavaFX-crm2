
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


public class TrashController implements Initializable{

    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXButton tiklashBtn;
    @FXML
    private TableView<Patients> listTableView;
    @FXML
    private TableColumn<Patients, String> nameField;
    @FXML
    private TableColumn<Patients, String> surnameField;
    @FXML
    private TableColumn<Patients, String> phoneField;
    @FXML
    private TableColumn<Patients, String> borthDateField;
    @FXML
    private TableColumn<Patients, Integer> paymentField;
    @FXML
    private TableColumn<Patients, String> regField;
    @FXML
    private TableColumn<Patients, String> changeColumn;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPatients1();
    }
    
    
    
    @FXML
    private void tiklashClick(MouseEvent event) {
        if(event.getSource() == tiklashBtn){
          String query = "delete from patients1 where deleted=true";
            executeSave(query);
            AlertHelper.showAlert(AnimationType.POPUP, "Korzinka bo'shatildi", "", NotificationType.INFORMATION, 2000);
        }
    }
    
    private final Connection con;
    
    public TrashController(){
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
        String query = "select * from patients1 where deleted=true order by id desc";
        Statement st;
        ResultSet rs;
        Patients patients;
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            patients = new Patients(rs.getInt("id")
                                    ,rs.getString("name")
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
    Patients patients;
    public void showPatients1(){
        
        ObservableList<Patients> list = getStudentList();
        
        
        nameField.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameField.setCellValueFactory(new PropertyValueFactory<>("surname"));
        borthDateField.setCellValueFactory(new PropertyValueFactory<>("borthDate"));
        phoneField.setCellValueFactory(new PropertyValueFactory<>("phone"));
        paymentField.setCellValueFactory(new PropertyValueFactory<>("tolov"));
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

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                         FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                patients = listTableView.getSelectionModel().getSelectedItem();
                                String query = "DELETE FROM patients1 WHERE deleted=true AND id = "+patients.getId();
                                PreparedStatement prs = con.prepareStatement(query);
                                prs.execute();
                                AlertHelper.showAlert(AnimationType.POPUP, "Ma'lumot o'chdi", "", NotificationType.INFORMATION, 2000);
                                showPatients1();
                            } catch (SQLException ex) {
                                Logger.getLogger(DeleteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                             try {
                                patients = listTableView.getSelectionModel().getSelectedItem();
                                String query = "UPDATE patients1 SET deleted=false WHERE id = "+patients.getId();
                                PreparedStatement prs = con.prepareStatement(query);
                                prs.execute();
                                AlertHelper.showAlert(AnimationType.POPUP, "Ma'lumot tiklandi", "", NotificationType.INFORMATION, 2000);
                                showPatients1();
                            } catch (SQLException ex) {
                                Logger.getLogger(DeleteController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                           

                        });

                        
                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         changeColumn.setCellFactory(cellFoctory);
        
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
