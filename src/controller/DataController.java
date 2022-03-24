
package controller;

import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import databaseConnect.Patients;
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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class DataController implements Initializable {

    @FXML
    private TableColumn<Patients, String> nameTable;
    @FXML
    private TableColumn<Patients, String> surnameTable;
    @FXML
    private TableColumn<Patients, String> phoneTable;
    @FXML
    private TableColumn<Patients, String> borthDateTable;
    @FXML
    private TableColumn<Patients, Integer> paymeentTable;
    @FXML
    private TableColumn<Patients, String> regTable;
    @FXML
    private TableView<Patients> listTableView;
    @FXML
    private JFXTextField searchField;
    
    
    private final Connection con;
    public DataController(){
        con = DbConnection.getConnection(true);
    }
    
   
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPatients1();
    }  
    
    public ObservableList<Patients> getStudentList(){
        ObservableList<Patients> patientList = FXCollections.observableArrayList();
        String query = "select * from patients1 where deleted=false order by id desc";
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
    
    public void showPatients1(){
        
        
        ObservableList<Patients> list = getStudentList();
        
        nameTable.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameTable.setCellValueFactory(new PropertyValueFactory<>("surname"));
        borthDateTable.setCellValueFactory(new PropertyValueFactory<>("borthDate"));
        phoneTable.setCellValueFactory(new PropertyValueFactory<>("phone"));
        paymeentTable.setCellValueFactory(new PropertyValueFactory<>("tolov"));
        regTable.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        
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
    private void handleDateClick(MouseEvent event) {
       Patients p =  listTableView.getSelectionModel().getSelectedItem();
        System.out.println(p.getId());
        System.out.println(p.getName());
        System.out.println(p.getRegDate());
    }
    
}
