package pl.edu.agh.pierogi;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.agh.pierogi.commands.command.CommandRegistry;
import pl.edu.agh.pierogi.config.StageManager;
import pl.edu.agh.pierogi.view.View;

@SpringBootApplication
public class PierogiApplication extends Application {

    public static CommandRegistry peopleCommandRegistry = new CommandRegistry();
    public static CommandRegistry projectsCommandRegistry = new CommandRegistry();
    public static CommandRegistry teamsCommandRegistry = new CommandRegistry();

    private ConfigurableApplicationContext springContext;
    private StageManager stageManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = springBootApplicationContext();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stageManager = springContext.getBean(StageManager.class, primaryStage);
        primaryStage.setTitle("Team projects manager 2k19");

        initRootLayout();
    }

    private void initRootLayout() {
        stageManager.switchScene(View.MAIN);
    }

    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PierogiApplication.class);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }

}

