import javafx.collections.FXCollections;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    TextField multiLabel = new TextField("1.0");
    TextField offset = new TextField("0.0");
    TextField saveFileName = new TextField();
    ComboBox<String> mode = new ComboBox<>(FXCollections.observableArrayList("reset", "bounce", "stretch", "stretch x2"));
    CheckBox showOffset = new CheckBox();
    Button load = new Button("Load");
    Button save = new Button("Save");
    Button play = new Button("Play");
    Button newSound = new Button("New");
    Label label = new Label("file:");
    Label scale = new Label();
    Label showOffsetLabel = new Label("Show offset");
    Label bounceLabel = new Label("Ping-pong");
    ImageView waveFormView = new ImageView();
    ImageView logoView = new ImageView();
    ImageView logoGradient = new ImageView(CreateGraphics.createGradient(360, 210));
    ImageView controlGradient = new ImageView();
    Slider multiplier = new Slider(0, 1, 1);
    Slider offsetSlider = new Slider(0, 1, 0);
    ListView<String> suggest = new ListView<>();
    public MainScreen(Stage window)
    {
        this.window = window;
        logo = CreateGraphics.createLogo();
        logoView.setImage(logo);
        window.setTitle("WaveShaper: load sound");
        window.setResizable(false);
        showOffset.setSelected(true);
        showOffset.selectedProperty().addListener(e-> changeValue());
        mode.setValue("reset");
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
        mode.setOnAction(e-> {
            waveForm = Processor.processWave(multiplier.getValue(), offsetSlider.getValue(), mode.getValue(), showOffset.isSelected());
            waveFormView.setImage(waveForm);
        });
        waveFormView.setImage(waveForm);
        suggest.setLayoutX(50);
        suggest.setLayoutY(170);
        suggest.setPrefWidth(200);
        suggest.setPrefHeight(0);
        suggest.setOnMouseClicked(e->
        {
            String name = "";
            for(int j = 4; j < suggest.getSelectionModel().getSelectedItem().length(); j++)
            {
                name = name + suggest.getSelectionModel().getSelectedItem().charAt(j);
            }
            fileName.setText(name);
            try
            {
                showSound();
            }
            catch (NullPointerException ex)
            {
                window.setTitle("WaveShaper: load sound (loading failed!)");
            }
        });
        pane.getChildren().addAll(logoGradient, logoView, suggest, fileName, load, label);

        load.setOnAction(e ->
        {
            try
            {
                showSound();
            }
            catch (NullPointerException ex)
            {
                window.setTitle("WaveShaper: load sound (loading failed!)");
            }
        });
        fileName.setOnKeyPressed(e ->
        {
            scanForFile(e);
            if(e.getCode().equals(KeyCode.ENTER))
            {
                try
                {
                    showSound();
                }
                catch (NullPointerException ex)
                {
                    window.setTitle("WaveShaper: load sound (loading failed!)");
                }
            }
        });

        scene = new Scene(pane, 350, 200);
        loadPane.getChildren().addAll(controlGradient, waveFormView, multiplier, multiLabel, offset, offsetSlider, saveFileName, save, bounceLabel, play, mode, showOffset, showOffsetLabel, scale, newSound);
        audioScene = new Scene(loadPane, waveForm.getWidth() + 20, 366);
        window.setScene(scene);
        fileName.requestFocus();
    }

    public void showSound()
    {
        offsetSlider.setValue(0);
        multiplier.setValue(1);
        waveForm = Processor.loadFile(fileName.getText(), offsetSlider.getValue(), showOffset.isSelected());
        waveFormView.setImage(waveForm);
        window.setTitle("WaveShaper: "+fileName.getText());
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
            window.setTitle("WaveShaper: load sound");
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
                window.setTitle("WaveShaper: "+fileName.getText()+ "(playback failed!)");
                ex.printStackTrace();
            }
        });
        saveFileName.setLayoutX(waveForm.getWidth() - 320);
        saveFileName.setLayoutY(220);
        saveFileName.setPrefWidth(150);
        saveFileName.setOnKeyPressed(e->
        {
            if(e.getCode() == KeyCode.ENTER)
            {
                try
                {
                    Calendar now = Calendar.getInstance();
                    AudioLoader.saveAudioFile(Processor.sound, saveFileName.getText());
                    window.setTitle("WaveShaper: "+fileName.getText()+ " (\""+saveFileName.getText()+".wav\" save successful "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+")");
                }
                catch (IOException ioe)
                {
                    window.setTitle("WaveShaper: "+fileName.getText()+ " (saving failed!)");
                }
            }
        });
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
                window.setTitle("WaveShaper: "+fileName.getText()+ " (\""+saveFileName.getText()+".wav\" save successful "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+")");
            }
            catch (IOException ioe)
            {
                window.setTitle("WaveShaper: "+fileName.getText()+ " (saving failed!)");
            }
        });
        bounceLabel.setLayoutX(35);
        bounceLabel.setLayoutY(224);
        mode.setLayoutX(10);
        mode.setLayoutY(220);
        showOffset.setLayoutX(130);
        showOffset.setLayoutY(223);
        showOffsetLabel.setLayoutX(155);
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
        audioScene.setOnKeyPressed(e->
        {
            if(e.isControlDown())
            {
                if(e.getCode() == KeyCode.N)
                {
                    window.setScene(scene);
                    window.setWidth(356);
                    window.setTitle("WaveShaper: load sound");
                }
                if(e.getCode() == KeyCode.S)
                {
                    try
                    {
                        Calendar now = Calendar.getInstance();
                        AudioLoader.saveAudioFile(Processor.sound, saveFileName.getText());
                        window.setTitle("WaveShaper: "+fileName.getText()+ " (\""+saveFileName.getText()+".wav\" save successful "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+")");
                    }
                    catch (IOException ioe)
                    {
                        window.setTitle("WaveShaper: "+fileName.getText()+ " (saving failed!)");
                    }
                }
            }
        });
    }

    public void changeValue()
    {
        multiLabel.setText(""+multiplier.getValue());
        offset.setText(""+offsetSlider.getValue());
        waveForm = Processor.processWave(multiplier.getValue(), offsetSlider.getValue(), mode.getValue(), showOffset.isSelected());
        waveFormView.setImage(waveForm);
    }

    public void changeSlider(KeyEvent ke)
    {
        if(ke.getCode().equals(KeyCode.ENTER))
        {
            try
            {
                multiplier.setValue(Double.parseDouble(multiLabel.getText()));
            }
            catch(NumberFormatException e)
            {
                multiLabel.setText("Not a number");
            }
            try
            {
                offsetSlider.setValue(Double.parseDouble((offset.getText())));
            }
            catch(NumberFormatException e)
            {
                offset.setText("Not a number");
            }
            waveForm = Processor.processWave(multiplier.getValue(), offsetSlider.getValue(), mode.getValue(), showOffset.isSelected());
            waveFormView.setImage(waveForm);
        }
    }

    public void scanForFile(KeyEvent e)
    {
        String s = fileName.getText();


        if((e.getCode().isDigitKey() || e.getCode().isLetterKey()) && !e.isControlDown())
        {
            s = s + e.getText();
        }
        if(e.getCode() == KeyCode.BACK_SPACE)
        {
            String s2 = "";
            for(int i = 0; i < s.length() - 1; i++)
            {
                s2 = s2 + s.charAt(i);
            }
            s = s2;
        }
        System.out.println(s);
        if((s.length() > 0))
        {
            File f = new File(System.getProperty("user.dir"));
            suggest.setItems(FXCollections.observableArrayList(Processor.sortSuggestions(new ArrayList<>(Arrays.asList(f.list())), s)));
            suggest.setPrefHeight(150);
            suggest.setLayoutY(20);
        }
        else
        {
            suggest.setLayoutY(300);
            suggest.setPrefHeight(0);
        }
        window.setTitle("WaveShaper: load sound");
        if(e.isControlDown() && e.getCode().isDigitKey())
        {
            int i = Integer.parseInt(e.getText());
            if(i != 0)
            {
                i = i-1;
                String name = "";
                for(int j = 4; j < suggest.getItems().get(i).length(); j++)
                {
                    name = name + suggest.getItems().get(i).charAt(j);
                }
                fileName.setText(name);
                try
                {
                    showSound();
                } catch (NullPointerException ex)
                {
                    window.setTitle("WaveShaper: load sound (loading failed!)");
                }
            }
        }
    }
}
