package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.ProjectDAO;
import pl.edu.agh.pierogi.model.Project;

import java.util.LinkedList;
import java.util.List;

public class DeleteProjectCommand implements Command {

    private List<Project> entities;
    private ProjectDAO projectDAO;

    public DeleteProjectCommand(ProjectDAO projectDAO, List<Project> entities) {
        this.projectDAO = projectDAO;
        this.entities = entities;
    }

    @Override
    public void execute() {
        List<Project> tmpEntities = new LinkedList<>(this.entities);
        projectDAO.deleteInBatch(this.entities);
        this.entities = tmpEntities;
    }

    @Override
    public void undo() {
        List<Project> tmpEntities = new LinkedList<>();
        for (Project project : this.entities) {
            tmpEntities.add(projectDAO.save(project));
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
            return "Deleting " + this.entities.size() + " projects";
        } else return "Deleting " + this.entities.size() + " project";
    }
}