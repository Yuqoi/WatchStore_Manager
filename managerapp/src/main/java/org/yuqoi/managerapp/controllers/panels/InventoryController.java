package org.yuqoi.managerapp.controllers.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.yuqoi.managerapp.models.Watch.Gender;
import org.yuqoi.managerapp.models.Watch.MechanismType;
import org.yuqoi.managerapp.models.Watch.Watch;
import org.yuqoi.managerapp.utils.DatabaseConnector;
import org.yuqoi.managerapp.utils.scenemanager.ScenePaths;
import org.yuqoi.managerapp.utils.scenemanager.SceneSwitcher;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {



    // PANELS FOR Inventory view
    // main table
    public TableView<Watch> watchesTable;

    // all columns
    public TableColumn<Watch, Integer> id_column;
    public TableColumn<Watch, String> watchname_column;
    public TableColumn<Watch, String> brand_column;
    public TableColumn<Watch, Gender> gender_column;
    public TableColumn<Watch, String> mpn_column;
    public TableColumn<Watch, MechanismType> mechanism_column;
    public TableColumn<Watch, Double> price_column;

    // textfields
    public TextField getFindText;

    // buttons
    public Button getFindTextBtn;


    // initialize connection and make prepared statement
    String query = null;
    Connection conn = null;
    PreparedStatement prepStatement = null;
    ResultSet resultSet = null;
    Watch watch = null;

    // watch list of objects
    ObservableList<Watch> WatchList = FXCollections.observableArrayList();


    public void refreshTable(MouseEvent mouseEvent) {
        WatchList.clear();
        try {
            query = "SELECT * FROM watches";
            prepStatement = conn.prepareStatement(query);
            resultSet = prepStatement.executeQuery();

            while (resultSet.next()){
                Gender genderVal =  Gender.valueOf(resultSet.getString("sex"));
                MechanismType mechanismVal =  MechanismType.valueOf(resultSet.getString("mechanism_type"));
                WatchList.add(new Watch(
                        resultSet.getInt("watch_id"),
                        resultSet.getString("watch_name"),
                        resultSet.getString("brand"),
                        Gender.valueOf(genderVal.name()),
                        resultSet.getString("MPN"),
                        MechanismType.valueOf(mechanismVal.name()),
                        resultSet.getDouble("price")
                ));
                watchesTable.setItems(WatchList);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public void updateWatch(MouseEvent mouseEvent) {
        // TODO we should get selected column it cannot be null it lanuches us a panel like add panel and we set data up
        // Selected watch
        if (!watchesTable.getSelectionModel().getSelectedCells().isEmpty()){
            Watch selectedWatch = watchesTable.getSelectionModel().getSelectedItem();

                Watch data = new Watch().getInstance();

                // we sent that data by getting instance
                data.setWatchId(selectedWatch.getWatchId());
                data.setWatchName(selectedWatch.getWatchName());
                data.setBrand(selectedWatch.getBrand());
                data.setGender(selectedWatch.getGender());
                data.setMpn(selectedWatch.getMpn());
                data.setMechanismType(selectedWatch.getMechanismType());
                data.setPrice(selectedWatch.getPrice());


                // probably we have to make another class

                SceneSwitcher.makePopup(ScenePaths.EDITPANEL);



        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDate();
        refreshTable(null);
    }

    private void loadDate(){
        // make a connection to database
        conn = DatabaseConnector.getConnection();

        id_column.setCellValueFactory(new PropertyValueFactory<>("watchId"));
        watchname_column.setCellValueFactory(new PropertyValueFactory<>("watchName"));
        brand_column.setCellValueFactory(new PropertyValueFactory<>("brand"));
        gender_column.setCellValueFactory(new PropertyValueFactory<>("gender"));
        mpn_column.setCellValueFactory(new PropertyValueFactory<>("mpn"));
        mechanism_column.setCellValueFactory(new PropertyValueFactory<>("mechanismType"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void removeWatch(MouseEvent mouseEvent) {
        if (!watchesTable.getSelectionModel().getSelectedCells().isEmpty()){
            Watch selectedItem = watchesTable.getSelectionModel().getSelectedItem();

            int selectedId = selectedItem.getWatchId();

            query = "DELETE FROM watches WHERE watch_id = ?";
            try {
                prepStatement = conn.prepareStatement(query);
                prepStatement.setInt(1, selectedId);
                prepStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getAddWatch(MouseEvent mouseEvent) {
        SceneSwitcher.makePopup(ScenePaths.ADDPANEL);
    }
    public void findWatch(MouseEvent mouseEvent) {
        // query
        query = "SELECT * FROM watches WHERE watch_name LIKE ?";

        try {
            prepStatement= conn.prepareStatement(query);
            prepStatement.setString(1, "%"+ getFindText.getText() + "%");

            // get the results
            resultSet = prepStatement.executeQuery();

            WatchList.clear();

            while (resultSet.next()){
                Gender genderVal =  Gender.valueOf(resultSet.getString("sex"));
                MechanismType mechanismVal =  MechanismType.valueOf(resultSet.getString("mechanism_type"));
                int id = resultSet.getInt("watch_id");
                String name = resultSet.getString("watch_name");
                String brand = resultSet.getString("brand");
                String mpn = resultSet.getString("MPN");
                double price = resultSet.getDouble("price");

                WatchList.add(new Watch(id, name, brand, genderVal, mpn, mechanismVal, price));
                watchesTable.setItems(WatchList);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void createInvoice(MouseEvent mouseEvent) {
        SceneSwitcher.makePopup(ScenePaths.INVOICEPANEL);
    }
}
