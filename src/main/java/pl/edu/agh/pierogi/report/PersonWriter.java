package pl.edu.agh.pierogi.report;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.PersonTeam;
import pl.edu.agh.pierogi.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Set;

public class PersonWriter extends ReportWriter {
    private Person person;

    public PersonWriter(Person person, String fileName) {
        super(fileName);
        this.person = person;
    }

    @Override
    protected String getFilePath() {
        String path = "src/main/resources/reports/person";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        return absolutePath + "/" + fileName;
    }

    @Override
    public boolean exportData() throws FileNotFoundException, DocumentException {
        PdfWriter.getInstance(document, new FileOutputStream(getFilePath()));
        document.open();

        document.add(new Paragraph("PERSON DETAILS\n\n", title));
        document.add(new Paragraph("BASIC INFORMATION\n", section));
        addBasicPersonInformation(person);
        document.add(new Paragraph("\n "));

        Set<PersonTeam> personTeamList = person.getPersonTeams();
        if (!personTeamList.isEmpty()) {
            document.add(new Paragraph("TEAMS\n", section));
            for (PersonTeam personTeam : personTeamList) {
                List<Grade> grades = personTeam.getGrade();
                Team team = personTeam.getTeam();
                addBasicTeamInformation(team);
                addGrades(grades);
            }
        }
        document.close();
        return true;
    }
}
