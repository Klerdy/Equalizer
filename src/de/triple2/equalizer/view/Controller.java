package de.triple2.equalizer.view;

import java.io.File;

import de.triple2.equalizer.controller.EqualizerService;
import de.triple2.equalizer.controller.SoundProcessor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

/**
 * Interface Logik.
 */
public class Controller {

    public BorderPane borderPane;
    public HBox hBoxTop, hBoxCenter, hBoxBottom;
    public TextField textFieldOpen;
    public Button buttonOpen;
	public Button buttonPlay;
    public Slider slider1, slider2, slider3, slider4, slider5, slider6, slider7, slider8, slider9, slider10;
    public Label labelName, labelTime;

    private File file;
    private final FileChooser fileChooser = new FileChooser();
	private SoundProcessor soundProcessor = new SoundProcessor();
	
	/** Der Service zum Abspielen der Musik in einem Hintergrund Thread. */
	private EqualizerService musicService;

    /**
     * Wird bei Klick auf den Open Button aufgerufen.
     */
    public void handleButtonOpen() {
    	// �ffne Datei Auswahl Dialog
        configureFileChooser(fileChooser, "Lade Musik Datei");
        file = fileChooser.showOpenDialog(Main.getStage());

        // falls Datei gew�hlt wurde
        if (file != null) {
            textFieldOpen.setText(file.getAbsolutePath());
        }
    }

    /**
     * Wird bei Klick auf den Play Button aufgerufen.
     */
    public void handleButtonPlay() {
    	if(buttonPlay.getText().equals("Abspielen")) {
        	// hole den Pfad aus dem Textfeld
            file = new File(textFieldOpen.getText());

        	if(file.exists()) {
        		buttonPlay.setText("Stop");
    			musicService = new EqualizerService(file);
    			musicService.setSoundProcessor(soundProcessor);
    			musicService.setOnSucceeded(e -> {
    				System.out.println("Done: ");
    				buttonPlay.setText("Abspielen");
    			});
    			musicService.setOnCancelled(e -> {
    				buttonPlay.setText("Abspielen");
    			});
    			musicService.start();
    	
        	}
        	else {
        		// Fehlermeldungen
        		if(textFieldOpen.getText().equals("")) {
        			showError("Abspielen nicht m�glich!", "Keine Datei angegeben.", "");
        		}
        		else {
            		showError("Abspielen nicht m�glich!", "Datei konnte nicht gefunden werden:", file.getAbsolutePath());
        		}
        	}

            // TODO labelName.setText(fileName);
            // labelTime.setText(time);
    	}
    	else {
    		// bei Klick auf Stop
    		musicService.cancel();
    	}

    }

    /**
     * Konfiguriert den �ffnen Dialog.
     *
     * @param chooser Der �ffnen Dialog.
     * @param title Der Titel des Dialog Fenster.
     */
    private void configureFileChooser(final FileChooser chooser, final String title) {
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("WAV", "*.wav"));
    }

    /**
     * Zeigt einen Fehler Dialog an.
     * @param headerText �berschrift
     * @param contentText Text der Fehlermeldung
     * @param filePath Pfad zur Datei
     */
    private void showError(String headerText, String contentText, String filePath) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Fehler");
		alert.setHeaderText(headerText);
		alert.setContentText(contentText + "\n" + filePath);

		alert.showAndWait();
    }
}
