import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;

public class MainScreen
{
    WritableImage waveForm = new WritableImage(10, 10);
    WritableImage logo = new WritableImage(150 , 150);
    Stage window = null;
    Scene scene = null;
    Scene audioScene = null;
    Pane pane = new Pane();
    Pane loadPane = new Pane();
    TextField fileName = new TextField();
    TextField multiLabel = new TextField("1");
    TextField offset = new TextField("0");
    TextField saveFileName = new TextField();
    CheckBox bounce = new CheckBox();
    CheckBox showOffset = new CheckBox();
    Button load = new Button("Load");
    Button save = new Button("Save");
    Button play = new Button("Play");
    Button newSound = new Button("New");
    Label label = new Label("file:");
    Label scale = new Label();
    Label showOffsetLabel = new Label("Show offset");
    Label bounceLabel = new Label("Bounce when exceeding limits");
    ImageView waveFormView = new ImageView();
    ImageView logoView = new ImageView();
    ImageView logoGradient = new ImageView(CreateGraphics.createGradient(360, 210));
    ImageView controlGradient = new ImageView();
    Slider multiplier = new Slider(1, 100, 1);
    Slider offsetSlider = new Slider(0, 2, 0);
    public MainScreen(Stage window)
    {
        this.window = window;
        logo = CreateGraphics.createLogo();
        logoView.setImage(logo);
        window.setTitle("Multiplimiter: load sound");
        window.setResizable(false);
        showOffset.setSelected(true);
        bounce.selectedProperty().addListener(e-> changeValue());
        showOffset.selectedProperty().addListener(e-> changeValue());
        offsetSlider.valueProperty().addListener(e-> changeValue());
        multiplier.valueProperty().addListener(e-> changeValue());
        label.setLayoutX(10);
        label.setLayoutY(173);
        label.setFont(Font.font(Font.getDefault().toString(), FontWeight.BOLD, 14D));
        fileName.setPromptText("filename...");
        fileName.setPrefWidth(200);
        fileName.setLayoutX(50);
        fileName.setLayoutY(170);
        load.setPrefWidth(80);
        load.setLayoutX(260);
        load.setLayoutY(170);
        waveFormView.setImage(waveForm);
        pane.getChildren().addAll(logoGradient, logoView, fileName, load, label);

        load.setOnAction(e ->
        {
            try
            {
                showSound();
            }
            catch (NullPointerException ex)
            {
                window.setTitle("Multiplimiter: load sound (loading failed!)");
            }
        });
        fileName.setOnKeyPressed(e ->
        {
            if(e.getCode().equals(KeyCode.ENTER))
            {
                try
                {
                    showSound();
                }
                catch (NullPointerException ex)
                {
                    window.setTitle("Multiplimiter: load sound (loading failed!)");
                }
            }
        });

        scene = new Scene(pane, 350, 200);
        loadPane.getChildren().addAll(controlGradient, waveFormView, multiplier, multiLabel, offset, offsetSlider, saveFileName, save, bounceLabel, play, bounce, showOffset, showOffsetLabel, scale, newSound);
        audioScene = new Scene(loadPane, waveForm.getWidth() + 20, 326);
        window.setScene(scene);
    }

    public void showSound()
    {
        bounce.setSelected(false);
        offsetSlider.setValue(0);
        multiplier.setValue(1);
        waveForm = Processor.loadFile(fileName.getText(), offsetSlider.getValue(), showOffset.isSelected());
        waveFormView.setImage(waveForm);
        window.setTitle("Multiplimiter: "+fileName.getText());
        window.setWidth(waveForm.getWidth() + 26);
        controlGradient.setLayoutY(120);
        controlGradient.setImage(CreateGraphics.createGradient((int) waveForm.getWidth() + 26, 326));
        newSound.setLayoutX(waveForm.getWidth() - 40);
        newSound.setLayoutY(220);
        newSound.setPrefWidth(50);
        newSound.setOnAction(e ->
        {
            window.setScene(scene);
            window.setWidth(356);
            window.setTitle("Multiplimiter: load sound");
        });
        play.setLayoutX(waveForm.getWidth() - 100);
        play.setLayoutY(220);
        play.setPrefWidth(50);
        play.setOnAction(e->
        {
            try
            {
                AudioLoader.playAudioFile(Processor.sound);
            }
            catch(Exception ex)
            {
                window.setTitle("Multiplimiter: "+fileName.getText()+ "(playback failed!)");
                ex.printStackTrace();
            }
        });
        saveFileName.setLayoutX(waveForm.getWidth() - 320);
        saveFileName.setLayoutY(220);
        saveFileName.setPrefWidth(150);
        scale.setLayoutX(waveForm.getWidth() - 405);
        scale.setLayoutY(224);
        scale.setText("Scale: 1:"+CreateGraphics.getScale());
        save.setPrefWidth(50);
        save.setLayoutX(waveForm.getWidth() - 160);
        save.setLayoutY(220);
        save.setOnAction(e ->
        {
            try
            {
                Calendar now = Calendar.getInstance();
                AudioLoader.saveAudioFile(Processor.sound, saveFileName.getText());
                window.setTitle("Multiplimiter: "+fileName.getText()+ " (\""+saveFileName.getText()+".wav\" save successful "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+")");
            }
            catch (IOException ioe)
            {
                window.setTitle("Multiplimiter: "+fileName.getText()+ " (saving failed!)");
            }
        });
        bounceLabel.setLayoutX(35);
        bounceLabel.setLayoutY(224);
        bounce.setLayoutX(10);
        bounce.setLayoutY(223);
        showOffset.setLayoutX(300);
        showOffset.setLayoutY(223);
        showOffsetLabel.setLayoutX(325);
        showOffsetLabel.setLayoutY(224);
        waveFormView.setLayoutX(10);
        waveFormView.setLayoutY(10);
        multiLabel.setLayoutX(10);
        multiLabel.setLayoutY(250);
        multiLabel.setPrefWidth(100);
        multiLabel.setOnKeyPressed(this::changeSlider);
        offset.setLayoutX(10);
        offset.setLayoutY(290);
        offset.setPrefWidth(100);
        offset.setOnKeyPressed(this::changeSlider);
        offsetSlider.setLayoutX(120);
        offsetSlider.setLayoutY(295);
        offsetSlider.setPrefWidth(waveForm.getWidth() - 110);
        multiplier.setLayoutX(120);
        multiplier.setLayoutY(255);
        multiplier.setPrefWidth(waveForm.getWidth() - 110);
        window.setScene(audioScene);
    }

    public void changeValue()
    {
        multiLabel.setText(""+multiplier.getValue());
        offset.setText(""+offsetSlider.getValue());
        waveForm = Processor.processWave(multiplier.getValue(), offsetSlider.getValue(), bounce.isSelected(), showOffset.isSelected());
        waveFormView.setImage(waveForm);
    }
    public void changeSlider(KeyEvent ke)
    {
        if(ke.getCode().equals(KeyCode.ENTER))
        {
            multiplier.setValue(Double.parseDouble(multiLabel.getText()));
            offsetSlider.setValue(Double.parseDouble((offset.getText())));
            waveForm = Processor.processWave(multiplier.getValue(), offsetSlider.getValue(), bounce.isSelected(), showOffset.isSelected());
            waveFormView.setImage(waveForm);
        }
    }
}
