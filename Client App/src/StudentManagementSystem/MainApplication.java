package StudentManagementSystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class MainApplication extends Application {

    public static void closeProgram() {
        boolean value = ConfirmationBox.display("Student Management System", "Are you sure you want to exit?");
        if (value) {
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        DisplayMethods displayMethods = DisplayMethods.getInstance();
        displayMethods.LoginDisplay(primaryStage);

        /*SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.setUserType("admin");
        sessionManager.setFullName("Varun Garg");
        sessionManager.setLoginStatus(1);
        sessionManager.setUsername("varun");
        sessionManager.setRollNumber("13ICS057");

        displayMethods.MemberHome(primaryStage);*/
    }
}
