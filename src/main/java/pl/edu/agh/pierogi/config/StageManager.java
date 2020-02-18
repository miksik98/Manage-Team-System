package pl.edu.agh.pierogi.config;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import pl.edu.agh.pierogi.controller.BasicController;
import pl.edu.agh.pierogi.view.View;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

import static org.slf4j.LoggerFactory.getLogger;

public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    private Stack<Stage> modals = new Stack<>();

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final View view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }

    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit("Unable to show scene for title" + title, exception);
        }
    }

    private Scene prepareScene(Parent rootnode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }

    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

    public <T> boolean openModal(final View view, T object) {
        try {
            Stage modal = new Stage();
            Pane page = (Pane) springFXMLLoader.load(view.getFxmlFile());

            modal.setTitle(view.getTitle());
            modal.initModality(Modality.APPLICATION_MODAL);

            if (modals.isEmpty())
                modal.initOwner(primaryStage);
            else
                modal.initOwner(modals.lastElement());

            Scene scene = new Scene(page);
            modal.setScene(scene);

            modals.push(modal);

            BasicController controller = springFXMLLoader.getController();
            controller.setData(object);

            modal.showAndWait();
            return controller.isApproved();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeModal() {
        Stage modal = modals.pop();
        modal.close();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}