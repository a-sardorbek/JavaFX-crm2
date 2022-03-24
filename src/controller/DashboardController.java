
package controller;

import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnection;
import databaseConnect.Patients;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class DashboardController implements Initializable {

    
     @FXML
    private BorderPane borderpane;
    @FXML
    private TableColumn<Patients, String> nameTable;
    @FXML
    private TableColumn<Patients, String> surnameTable;
    @FXML
    private TableColumn<Patients, String> borthDateTable;
    
    @FXML
    private TableColumn<Patients, Integer> paymentTable;
    @FXML
    private TableColumn<Patients, Date> registerDatetable;
    @FXML
    private TableView<Patients> listTableView;
    @FXML
    private JFXTextField searchField;
    @FXML
    private TableColumn<Patients, String> phone;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPatients1();
    }
    
    private final Connection con;
    
    public DashboardController(){
        con = DbConnection.getConnection(true);
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
            patients = new Patients(rs.getString("name")
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
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        paymentTable.setCellValueFactory(new PropertyValueFactory<>("tolov"));
        registerDatetable.setCellValueFactory(new PropertyValueFactory<>("regDate"));
        
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
    private void malumotPage(MouseEvent event) {
            loadUI("Data");
    }

    @FXML
    private void qoshishPage(MouseEvent event) {
        loadUI("Add");
    }
    
     private void loadUI(String ui){
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/view/"+ui+".fxml"));
           
        }catch(IOException e){
            System.out.println("error in loadPAge");
        }
        
        borderpane.setCenter(root);
    }

    @FXML
    private void fullClicked(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
    }

    @FXML
    private void exitClicked(MouseEvent event) {
        DbConnection.getConnection(false);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    private void iconifiedClicked(MouseEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void uzgartirishClicked(MouseEvent event) {
        loadUI("UpdateTable");
    }

    @FXML
    private void uchirishClicked(MouseEvent event) {
        loadUI("Delete");
    }

    @FXML
    private void excelForm(MouseEvent event) {
        loadUI("ExcelData");
    }

    @FXML
    private void korzinaClicked(MouseEvent event) {
        loadUI("Trash");
    }

    

   
}
