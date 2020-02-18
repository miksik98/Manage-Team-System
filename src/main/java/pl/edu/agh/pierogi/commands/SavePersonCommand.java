package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.model.Person;

public class SavePersonCommand implements Command {

    private Person entity;
    private PersonDAO personDAO;

    public SavePersonCommand(PersonDAO personDAO, Person entity) {
        this.personDAO = personDAO;
        this.entity = entity;
    }

    @Override
    public void execute() {
        this.entity = personDAO.save(this.entity);
    }

    @Override
    public void undo() {
        personDAO.delete(this.entity);

    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "New person saved: " + this.entity;
    }
}
