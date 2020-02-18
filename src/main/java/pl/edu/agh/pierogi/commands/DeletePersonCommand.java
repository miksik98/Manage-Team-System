package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.PersonDAO;
import pl.edu.agh.pierogi.model.Person;

import java.util.LinkedList;
import java.util.List;

public class DeletePersonCommand implements Command {

    private List<Person> entities;
    private PersonDAO personDAO;

    public DeletePersonCommand(PersonDAO personDAO, List<Person> entities) {
        this.personDAO = personDAO;
        this.entities = entities;
    }

    @Override
    public void execute() {
        List<Person> tmpEntities = new LinkedList<>(this.entities);
        personDAO.deleteInBatch(this.entities);
        this.entities = tmpEntities;

    }

    @Override
    public void undo() {
        List<Person> tmpEntities = new LinkedList<>();
        for (Person person : this.entities) {
            tmpEntities.add(personDAO.save(person));
        }
        this.entities = tmpEntities;

    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        if (this.entities.size() != 1) {
            return "Deleting " + this.entities.size() + " people";
        } else return "Deleting " + this.entities.size() + " person";
    }
}
