package pl.edu.agh.pierogi.commands.command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

    private ObservableList<Command> commandStack = FXCollections
            .observableArrayList();

    private ObservableList<Command> undoCommandStack = FXCollections
            .observableArrayList();


    public void executeCommand(Command command) {
        command.execute();
        System.out.println(command.getName());
//        disable undo/redo until soft delete will be implemented
//        commandStack.add(command);
//        undoCommandStack.clear();
    }

    public void redo() {
        if (!undoCommandStack.isEmpty()) {
            System.out.println(undoCommandStack.size());
            Command lastCommand = undoCommandStack.get(undoCommandStack.size() - 1);
            System.out.println(undoCommandStack.size());
            undoCommandStack.remove(lastCommand);
            System.out.println(undoCommandStack.size());
            lastCommand.redo();
            System.out.println("Redo command: \"" + lastCommand.getName() + "\"");
            commandStack.add(lastCommand);
        }
    }

    public void undo() {
        if (!commandStack.isEmpty()) {
            Command lastCommand = commandStack.get(commandStack.size() - 1);
            commandStack.remove(lastCommand);
            lastCommand.undo();
            System.out.println("Undo command: \"" + lastCommand.getName() + "\"");
            undoCommandStack.add(lastCommand);
        }
    }

    public ObservableList<Command> getCommandStack() {
        return commandStack;
    }

    public ObservableList<Command> getUndoCommandStack() {
        return undoCommandStack;
    }
}
