package StudentManagementSystem.Controllers;

import StudentManagementSystem.Configuration;
import StudentManagementSystem.ConfirmationBox;
import StudentManagementSystem.DisplayMethods;
import StudentManagementSystem.Model.LoginResponse;
import StudentManagementSystem.Model.Student;
import StudentManagementSystem.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
public class MemberHome implements Initializable {

    static final String FETCH_SUCCESS = "success";
    static final String FETCH_FAIL = "fail";

    @FXML
    AnchorPane anchor_pane;

    @FXML
    JFXDrawer students_drawer;

    @FXML
    VBox NavigationVBox;
    @FXML
    AnchorPane content;

    TabPane tabPane = null;

    Student students[];
    private VBox StudentsVBox;

    Task fetchStudentsTask = new Task() {
        @Override
        protected String call() throws Exception {
            try {
                WebTarget clientTarget;
                Client client = ClientBuilder.newClient();
                updateMessage("Fetching students list");
                clientTarget = client.target(Configuration.API_HOST + "data/admin" + "/?format=json");
                javax.ws.rs.core.Response rawResponse = clientTarget.request("application/json").get();
                String response = rawResponse.readEntity(String.class);
                ObjectMapper mapper = new ObjectMapper();

                students = mapper.readValue(response, Student[].class);

            } catch (Exception e) {
                System.out.println("got exception in fetchStudentsTask");
                e.printStackTrace();
                return FETCH_FAIL;
            }
            return FETCH_SUCCESS;
        }
    };

    public void AddStudentToNavigationList(Form newStudentForm) {
        JFXButton student_button = new JFXButton();
        student_button.setText(newStudentForm.asMap().getFirst("full_name"));
        student_button.setPrefWidth(190);
        student_button.setPrefHeight(44);

        student_button.setOnAction(f -> {
            try {
                SessionManager.getInstance().setStudentFullName(newStudentForm.asMap().getFirst("full_name"));
                SessionManager.getInstance().setStudentRollNo(newStudentForm.asMap().getFirst("roll_no"));
                content.getChildren().clear();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getClassLoader().getResource("StudentManagementSystem/Layout/Menu.fxml"));
                tabPane = fxmlLoader2.load();
                content.getChildren().setAll(tabPane);
                students_drawer.close();
                students_drawer.toBack();
            } catch (Exception exception) {
                System.out.println(getClass().getSimpleName());
                exception.printStackTrace();
            }
        });

        StudentsVBox.getChildren().add(student_button);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            StudentsVBox = FXMLLoader.load(getClass().getResource("../Layout/StudentsDrawer.fxml"));
            students_drawer.setSidePane(StudentsVBox);
        } catch (Exception e) {
            System.out.println("caught exception in " + this.getClass().getSimpleName());
            e.printStackTrace();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("StudentManagementSystem/Layout/Menu.fxml"));

        if (SessionManager.getInstance().getStudentRollNo() != null) {
            try {
                content.getChildren().clear();
                tabPane = fxmlLoader.load();
                content.getChildren().setAll(tabPane);
            } catch (Exception e) {
                System.out.println(getClass().getSimpleName());
                e.printStackTrace();
            }
        } else {
            content.getChildren().clear();
            VBox NoStudentMessage = Utility.WarningLabel("Select a student first", 0,300);
            NoStudentMessage.setLayoutX((content.getMinWidth() - 300) / 2);
            NoStudentMessage.setLayoutY((content.getMinHeight()) / 2);
            content.getChildren().add(NoStudentMessage);
        }

        NavigationVBox.setStyle("-fx-background-color: rgba(0, 100, 100, 0.5);");
        students_drawer.toBack();
        StudentsVBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        if (tabPane != null)
            tabPane.toBack();

        JFXButton studentsButton = new JFXButton();
        studentsButton.setText("Students");
        studentsButton.setPrefWidth(190);
        studentsButton.setPrefHeight(44);
        studentsButton.setOnAction(e -> {
            if (students_drawer.isShown()) {
                students_drawer.close();
                if (tabPane != null)
                    tabPane.toFront();
            } else {
                students_drawer.open();
                students_drawer.toFront();
                if (tabPane != null)
                    tabPane.toBack();
            }
        });
        NavigationVBox.getChildren().add(studentsButton);

        JFXButton newStudentButton = new JFXButton();
        newStudentButton.setText("New Student");
        newStudentButton.setPrefWidth(190);
        newStudentButton.setPrefHeight(44);
        newStudentButton.setOnAction(e ->
        {
            Utility.DisplayForm("New Student", "StudentForm.fxml", 600, 600, this);
        });
        NavigationVBox.getChildren().add(newStudentButton);

        JFXButton ReviewButton = new JFXButton();
        ReviewButton.setText("Submit Review");
        ReviewButton.setPrefWidth(190);
        ReviewButton.setPrefHeight(44);
        ReviewButton.setOnAction(e ->
        {
            Utility.DisplayForm("Reviews", "SubmitReview.fxml", 700, 650, this);
        });
        NavigationVBox.getChildren().add(ReviewButton);

        JFXButton Logs = new JFXButton();
        Logs.setText("Logs");
        Logs.setPrefWidth(190);
        Logs.setPrefHeight(44);
        Logs.setOnAction(e ->
        {
            Utility.DisplayForm("Logs", "Logs.fxml",700,650,this);
        });
        NavigationVBox.getChildren().add(Logs);


        JFXButton logoutButton = new JFXButton();
        logoutButton.setText("Log Out");
        logoutButton.setPrefWidth(190);
        logoutButton.setPrefHeight(44);
        logoutButton.setOnAction(e -> {

            boolean userChoice = ConfirmationBox.display("Logout", "Are you sure");
            if (userChoice == false)
                return;

            Form form = new Form();
            WebTarget clientTarget;
            Client client = ClientBuilder.newClient();
            clientTarget = client.target(Configuration.API_HOST + "user/logout/");

            javax.ws.rs.core.Response rawResponse = clientTarget.request("application/json").header("Cookie", SessionManager.getCookie()).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

            String response = rawResponse.readEntity(String.class);
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse addResponse = null;
            try {
                addResponse = mapper.readValue(response, LoginResponse.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.out.println("got message " + addResponse.getMessage());
            if (addResponse.getMessage().equals("success")) {
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.setFullName(null);
                sessionManager.setLoginStatus(SessionManager.LOGGED_OUT);
                sessionManager.setPassword(null);
                sessionManager.setRollNumber(null);
                sessionManager.setStudentRollNo(null);
                sessionManager.setUserType(null);
                sessionManager.setUsername(null);
                Stage CurrentStage = (Stage) anchor_pane.getScene().getWindow();
                DisplayMethods displayMethods = DisplayMethods.getInstance();
                try {
                    displayMethods.LoginDisplay(CurrentStage);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } else {
                System.out.println("Cannot Logout at this stage");
                System.out.println(response);
            }
        });
        NavigationVBox.getChildren().add(logoutButton);

        fetchStudentsTask.setOnSucceeded(e -> {

            for (int i = 0; i < students.length; i++) {
                JFXButton student_button = new JFXButton();
                student_button.setText(students[i].getFullName());
                student_button.setPrefWidth(190);
                student_button.setPrefHeight(44);
                final int CurrentIndex = i;
                student_button.setOnAction(f -> {
                    try {
                        SessionManager.getInstance().setStudentFullName(students[CurrentIndex].getFullName());
                        SessionManager.getInstance().setStudentRollNo(students[CurrentIndex].getRollNo());
                        content.getChildren().clear();
                        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getClassLoader().getResource("StudentManagementSystem/Layout/Menu.fxml"));
                        tabPane = fxmlLoader2.load();
                        content.getChildren().setAll(tabPane);
                        students_drawer.close();
                        students_drawer.toBack();
                    } catch (Exception exception) {
                        System.out.println(getClass().getSimpleName());
                        exception.printStackTrace();
                    }
                });

                StudentsVBox.getChildren().add(student_button);
            }
        });

        Thread thread = new Thread(fetchStudentsTask);
        thread.setDaemon(true);
        thread.start();
    }
}
