package pl.edu.agh.pierogi.commands.command;

public interface Command {

    void execute();

    void undo();

    void redo();

    String getName();
}
