import javafx.application.Application;
import javafx.stage.Stage;

public class Run extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }
    public void start(Stage window) throws Exception
    {
        MainScreen mainScreen = new MainScreen(window);
        window.show();
    }
}
