package ale.exptree.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ale/exptree/fxml/main.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
(x+y)/((A-B)*C)
(a+i)/((b*c)-d)
(((a+b*y)-d)/(e*(f-2/h))*i)+c
a+(b*(c/d)/(e*f))*g

10+2*6 = 22
100*2+12 = 212
100*(2+12) = 1400
100*(2+12)/14 = 100
 */
