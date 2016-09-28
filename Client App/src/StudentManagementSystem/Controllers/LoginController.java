package StudentManagementSystem.Controllers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import StudentManagementSystem.Configuration;
import StudentManagementSystem.DisplayMethods;
import StudentManagementSystem.Model.LoginResponse;
import StudentManagementSystem.Model.Student;
import StudentManagementSystem.SessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sun.rmi.runtime.Log;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class LoginController {

	@FXML
	private JFXTextField usernameField;

	@FXML
	private JFXPasswordField passwordField;

    @FXML
    private VBox fxml_root;

    public void event(ActionEvent event) throws IOException {

		String username = usernameField.getText();
		String password = passwordField.getText();
		System.out.println(username);

		try {

			WebTarget clientTarget;
			Client client = ClientBuilder.newClient();
			clientTarget = client.target(Configuration.API_HOST + "user/login?email=" + username + "&password=" + password);
			javax.ws.rs.core.Response rawResponse = clientTarget.request("application/json").get();

			String response = rawResponse.readEntity(String.class);
			ObjectMapper mapper = new ObjectMapper();
			LoginResponse loginResponse = mapper.readValue(response,LoginResponse.class);

			System.out.println("got message " + loginResponse.getMessage());
			if(loginResponse.getMessage().equals("success")) {
				SessionManager sessionManager = SessionManager.getInstance();
				sessionManager.setFullName(username);
				sessionManager.setLoginStatus(sessionManager.LOGGED_IN);
				sessionManager.setPassword(password);
				Stage CurrentStage = (Stage) fxml_root.getScene().getWindow();
				DisplayMethods displayMethods = DisplayMethods.getInstance();
				displayMethods.MenuDisplay(CurrentStage);
			}
			else
			{
				System.out.println("Login Failed, try again");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
