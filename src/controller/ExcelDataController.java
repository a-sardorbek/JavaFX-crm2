
package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import databaseConnect.DbConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataController implements Initializable {

   
    @FXML
    private JFXButton hamaExcelBtn;
    @FXML
    private VBox vboxId;
    @FXML
    private JFXDatePicker bugunDatefield;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private final Connection con;
    public ExcelDataController(){
        con = DbConnection.getConnection(true);
    }
    
    PreparedStatement prs;
    ResultSet rs;
    
    private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
        }
    }
    

    @FXML
    private void hammaExcelClick(MouseEvent event) {
         if(event.getSource()==hamaExcelBtn){
             excleData();
                }
         }
    
    private void excleData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = bugunDatefield.getValue().format(formatter);
         Stage stage = (Stage) vboxId.getScene().getWindow();
             DirectoryChooser directoryChooser = new DirectoryChooser();
             directoryChooser.setTitle("Open Resource File");
             
             File selectedFile = directoryChooser.showDialog(stage);
                if (selectedFile != null) {
                     try {
                String query = "select * from patients1 where deleted=false and regDate='"+formattedDate+"'";
                prs = con.prepareStatement(query);
                rs = prs.executeQuery();
                
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Patients info");
                
                CellStyle style = wb.createCellStyle();
                XSSFFont font = wb.createFont();
                font.setFontHeightInPoints((short)11);
                font.setBold(true);
                style.setAlignment(CellStyle.ALIGN_CENTER);
                style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                style.setFont(font);
                
                
                CellStyle style1 = wb.createCellStyle();
                XSSFFont font1 = wb.createFont();
                font1.setFontHeightInPoints((short)11);
                font1.setBold(false);
                style1.setAlignment(CellStyle.ALIGN_CENTER);
                style1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                style1.setFont(font1);
                
                
                XSSFRow header = sheet.createRow(0);
                
                XSSFCell cell0 = header.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue("Ism");
                
                XSSFCell cell1 = header.createCell(1);
                cell1.setCellStyle(style);
                cell1.setCellValue("Familiya");
                
                XSSFCell cell2 = header.createCell(2);
                cell2.setCellStyle(style);
                cell2.setCellValue("Tugulgan Sana");
                
                XSSFCell cell3 = header.createCell(3);
                cell3.setCellStyle(style);
                cell3.setCellValue("Telefon");
                
                XSSFCell cell4 = header.createCell(4);
                cell4.setCellStyle(style);
                cell4.setCellValue("Registratsiya");
                
                XSSFCell cell5 = header.createCell(5);
                cell5.setCellStyle(style);
                cell5.setCellValue("To'lov");
                
                sheet.setColumnWidth(0, 256*20);
                sheet.setColumnWidth(1, 256*20);
                sheet.setColumnWidth(2, 256*20);
                sheet.setColumnWidth(3, 256*20);
                sheet.setColumnWidth(4, 256*20);
                sheet.setColumnWidth(5, 256*20);
                
                sheet.setZoom(120);
                
                int index = 1;
               while(rs.next()){
                   
                XSSFRow row = sheet.createRow(index);
                    
                XSSFCell d0 = row.createCell(0);
                d0.setCellStyle(style1);
                d0.setCellValue(rs.getString("name"));
                
                XSSFCell d1 = row.createCell(1);
                d1.setCellStyle(style1);
                d1.setCellValue(rs.getString("surname"));
                
                XSSFCell d2 = row.createCell(2);
                d2.setCellStyle(style1);
                d2.setCellValue(rs.getString("borthDate"));
                
                XSSFCell d3 = row.createCell(3);
                d3.setCellStyle(style1);
                d3.setCellValue(rs.getString("phone"));
                
                XSSFCell d4 = row.createCell(4);
                d4.setCellStyle(style1);
                d4.setCellValue(rs.getString("regDate"));
                
                XSSFCell d5 = row.createCell(5);
                d5.setCellStyle(style1);
                d5.setCellValue(rs.getString("tolov"));
                    
                index++;
                }
                
                FileOutputStream fileOut = new FileOutputStream(selectedFile+"\\patients.xlsx");
                wb.write(fileOut);
                fileOut.close();
                
                System.out.println("sended");
                System.out.println(formattedDate);
               prs.close();
               rs.close();
                         
            } catch (SQLException | FileNotFoundException ex) {
                Logger.getLogger(ExcelDataController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExcelDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    }
    

