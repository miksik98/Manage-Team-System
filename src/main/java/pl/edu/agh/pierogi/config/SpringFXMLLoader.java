package pl.edu.agh.pierogi.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.edu.agh.pierogi.controller.BasicController;

import java.io.IOException;
import java.util.ResourceBundle;

@Component
public class SpringFXMLLoader {
    private final ApplicationContext context;
    private FXMLLoader loader;

    @Autowired
    public SpringFXMLLoader(ApplicationContext context, ResourceBundle resourceBundle) {
        this.context = context;
    }

    public Parent load(String fxmlPath) throws IOException {
        loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }

    public BasicController getController() {
        return loader.getController();
    }
}