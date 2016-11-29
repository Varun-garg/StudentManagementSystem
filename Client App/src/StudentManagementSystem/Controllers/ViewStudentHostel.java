package StudentManagementSystem.Controllers;

import StudentManagementSystem.Configuration;
import StudentManagementSystem.Model.Hostel;
import StudentManagementSystem.Model.LoginResponse;
import StudentManagementSystem.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by varun on 17/11/2016.
 */
public class ViewStudentHostel implements Initializable {

    @FXML
    private VBox hostel_info_vbox;

    @FXML
    private JFXButton button;

    public void DisplayHostelInfo() {
        WebTarget clientTarget;
        Client client = ClientBuilder.newClient();
        String rollNo = SessionManager.getInstance().getStudentRollNo();
        clientTarget = client.target(Configuration.API_HOST + "data/student/hostel/" + rollNo + "/?format=json");
        javax.ws.rs.core.Response rawResponse = clientTarget.request("application/json").get();
        String response = rawResponse.readEntity(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            hostel_info_vbox.getChildren().clear();
            Hostel hos = mapper.readValue(response, Hostel.class);
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Roll No:", hos.getRollNum(), 0,300));
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Room No:", hos.getRoomNum(), 1,300));
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Warden Name:", hos.getWardenName(), 2,300));
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Warden Mobile No.:", hos.getWardenMob(), 3,300));
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Caretaker Name:", hos.getCaretakerName(), 4,300));
            hostel_info_vbox.getChildren().add(Utility.GenerateRow("Caretaker Mobile No:", hos.getCaretakerNum(), 5,300));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hostel_info_vbox.setSpacing(10);
        hostel_info_vbox.getChildren().clear();
        this.DisplayHostelInfo();

        button.setText("Add / Update");
        button.setAlignment(Pos.CENTER);
        button.setBlendMode(BlendMode.SRC_ATOP);
        button.getStyleClass().add("windows7-default");
        button.setOnAction(e -> {
            Utility.DisplayForm("Update Hostel Info", "HostelForm.fxml", 600, 600, this);
        });
    }

}
