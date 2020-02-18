package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Team;

public class SaveTeamCommand implements Command {

    private Team entity;
    private TeamDAO teamDAO;

    public SaveTeamCommand(TeamDAO projectDAO, Team entity) {
        this.teamDAO = projectDAO;
        this.entity = entity;
    }

    @Override
    public void execute() {
        this.entity = teamDAO.save(this.entity);
    }

    @Override
    public void undo() {
        this.teamDAO.delete(this.entity);
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return "New team saved: " + this.entity;
    }
}
