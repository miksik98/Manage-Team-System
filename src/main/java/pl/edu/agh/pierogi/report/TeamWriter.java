package pl.edu.agh.pierogi.report;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class TeamWriter extends ReportWriter {
    private Team team;

    public TeamWriter(Team team, String fileName) {
        super(fileName);
        this.team = team;
    }

    @Override
    protected String getFilePath() {
        String path = "src/main/resources/reports/team";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        return absolutePath + "/" + fileName;
    }

    @Override
    public boolean exportData() throws FileNotFoundException, DocumentException {
        PdfWriter.getInstance(document, new FileOutputStream(getFilePath()));
        document.open();
        document.add(new Paragraph("TEAM DETAILS\n\n", title));
        document.add(new Paragraph("BASIC INFORMATION\n", section));

        addTeamInformation(team);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("TEAM MEMBERS\n", section));
        for (PersonTeam personTeam : team.getPersonTeams()) {
            addBasicPersonInformation(personTeam.getPerson());
            List<Grade> grades = personTeam.getGrade();
            addGrades(grades);
        }
        document.close();
        return true;
    }

}
