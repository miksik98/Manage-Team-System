package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.model.Project;

public class SaveProjectCommand implements Command {

    private Project entity;
    private ProjectDAO projectDAO;

    public SaveProjectCommand(ProjectDAO projectDAO, Project entity) {
        this.projectDAO = projectDAO;
        this.entity = entity;
    }

    @Override
    public void execute() {
        this.entity = projectDAO.save(this.entity);
    }

    @Override
    public void undo() {
        this.projectDAO.delete(this.entity);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "New project saved: " + this.entity;
    }
}
