import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import processing.core.PApplet;

public class GUI extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
    public void start (Stage stage) {
        //final SwingNode swingNode = new SwingNode();

        //createSwingContent(swingNode);
        
        StackPane pane = new StackPane();
        //pane.getChildren().add(swingNode);
        PApplet app = new Display();
        String[] args = {"--location=0,0", "Display"};
        PApplet.runSketch(args, app);

        stage.setTitle("Map");
        stage.setScene(new Scene(pane, 600, 600));
        stage.show();
        
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	PApplet app = new Display();
            	JPanel panel = new JPanel();
            	panel.add(app);
                swingNode.setContent(panel);
            	app.init();
            }
        });
    }
	
}
