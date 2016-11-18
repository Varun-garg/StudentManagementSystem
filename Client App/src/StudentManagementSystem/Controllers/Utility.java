package StudentManagementSystem.Controllers;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by varun on 17/11/2016.
 */
public class Utility {

    public static HBox GenerateRow(String field1, String field2, int color_id) {
        Text text1 = new Text(field1.trim());
        text1.setFont(Font.font("Verdana", FontWeight.BOLD,12));
        Text text2 = new Text(field2.trim());
        text2.minWidth(100);
        String color;

        if(color_id % 2 == 0)
            color = "#cfd8dc";
        else
            color = "#e6ee9c";

        HBox hBox = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        hBox.setMinWidth(300);
        hBox.setMaxWidth(300);
        hBox.setPadding(new Insets(5, 5, 5, 5));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: "+ color + ";-fx-border-color: #263238;-fx-border-radius: 3");
        hBox.getChildren().add(text1);
        HBox InnerRightHbox = new HBox();
        InnerRightHbox.setMinWidth(170);
        //  InnerRightHbox.getChildren().add(text2);
        hBox.getChildren().add(spacer);
        hBox.getChildren().add(text2);

        return hBox;
    }
}
