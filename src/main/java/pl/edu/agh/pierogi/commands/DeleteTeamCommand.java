package pl.edu.agh.pierogi.commands;

import pl.edu.agh.pierogi.commands.command.Command;
import pl.edu.agh.pierogi.dao.TeamDAO;
import pl.edu.agh.pierogi.model.Team;

import java.util.LinkedList;
import java.util.List;

public class DeleteTeamCommand implements Command {
    private List<Team> entities;
    private TeamDAO teamDAO;

    public DeleteTeamCommand(TeamDAO teamDAO, List<Team> entities) {
        this.teamDAO = teamDAO;
        this.entities = entities;
    }

    @Override
    public void execute() {
        List<Team> tmpEntities = new LinkedList<>(this.entities);
        teamDAO.deleteInBatch(this.entities);
        this.entities = tmpEntities;
    }

    @Override
    public void undo() {
        List<Team> tmpEntities = new LinkedList<>();
        for (Team team : this.entities) {
            tmpEntities.add(teamDAO.save(team));
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
            return "Deleting " + this.entities.size() + " teams";
        } else return "Deleting " + this.entities.size() + " team";
    }
}
