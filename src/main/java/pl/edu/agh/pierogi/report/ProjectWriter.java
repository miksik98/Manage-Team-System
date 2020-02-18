package pl.edu.agh.pierogi.report;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.stream.Collectors;

public class ProjectWriter extends ReportWriter {
    private Project project;

    public ProjectWriter(Project project, String fileName) {
        super(fileName);
        this.project = project;
    }

    @Override
    protected String getFilePath() {
        String path = "src/main/resources/reports/project";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        return absolutePath + "/" + fileName;
    }

    @Override
    public boolean exportData() throws FileNotFoundException, DocumentException {
        PdfWriter.getInstance(document, new FileOutputStream(getFilePath()));
        document.open();

        document.add(new Paragraph("PROJECT DETAILS\n\n", title));
        document.add(new Paragraph("BASIC INFORMATION\n", section));

        addBasicProjectInformation(project);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("TEAMS\n", section));

        for (Team team : project.getTeams()) {
            addBasicTeamInformation(team);
            addMembers(team.getPersonTeams()
                    .stream()
                    .map(personTeam -> personTeam.getPerson())
                    .collect(Collectors.toList()));
        }

        document.close();
        return true;
    }

}
