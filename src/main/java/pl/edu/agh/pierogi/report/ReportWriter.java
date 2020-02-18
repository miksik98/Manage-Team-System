package pl.edu.agh.pierogi.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import pl.edu.agh.pierogi.model.Grade;
import pl.edu.agh.pierogi.model.Person;
import pl.edu.agh.pierogi.model.Project;
import pl.edu.agh.pierogi.model.Team;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ReportWriter {
    protected final Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17, BaseColor.BLACK);
    protected final Font plain = FontFactory.getFont(FontFactory.HELVETICA, 14, BaseColor.BLACK);
    protected final Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
    protected final Font section = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.GRAY);
    protected String fileName;
    protected Document document;

    public ReportWriter(String fileName) {
        this.fileName = fileName;
        this.document = new Document();
    }

    abstract public boolean exportData() throws FileNotFoundException, DocumentException;

    abstract protected String getFilePath();

    public boolean checkFileExistance() {
        File file = new File(getFilePath());
        return file.exists();
    }

    protected void addTableHeader(PdfPTable table, List<String> headers) {
        headers.forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            table.addCell(header);
        });
    }

    protected void addRow(PdfPTable table, List<String> row) {
        row.forEach(col -> {
            table.addCell(col);
        });
    }

    protected void addGrades(List<Grade> grades) throws DocumentException {
        if (grades.size() > 0) {
            document.add(new Paragraph("GRADES\n\n", bold));
            String[] headers = {"Value", "Max Value", "Description"};
            PdfPTable table = new PdfPTable(headers.length);
            addTableHeader(table, Arrays.asList(headers));
            for (Grade grade : grades) {
                String[] row = {String.valueOf(grade.getValue()),
                        String.valueOf(grade.getMaxValue()),
                        String.valueOf(grade.getDescription())};
                addRow(table, Arrays.asList(row));
            }
            document.add(table);
            document.add(new Paragraph("\n\n"));
        }
    }

    protected void addMembers(List<Person> people) throws DocumentException {
        if (people.size() > 0) {
            document.add(new Paragraph("TEAM MEMBERS\n\n", bold));
            String[] headers = {"Firstname", "Lastname", "Email"};
            PdfPTable table = new PdfPTable(headers.length);
            addTableHeader(table, Arrays.asList(headers));
            for (Person person : people) {
                String[] row = {person.getFirstName(), person.getLastName(), person.getEmail()};
                addRow(table, Arrays.asList(row));
            }
            document.add(table);
            document.add(new Paragraph("\n\n"));
        }
    }

    protected void addTeamInformation(Team team) throws DocumentException {
        addBasicTeamInformation(team);
        addBasicProjectInformation(team.getProject());
    }

    protected void addBasicTeamInformation(Team team) throws DocumentException {
        Optional<Person> leader = team.getPersonTeams().stream()
                .map(pt -> pt.getPerson())
                .filter(p -> p.getPersonID() == team.getLeaderID())
                .findFirst();
        document.add(new Chunk("TEAM NAME: ", bold));
        document.add(new Chunk(team.getName(), plain));
        document.add(new Paragraph(""));
        document.add(new Chunk("TEAM LEADER: ", bold));
        if (!leader.isEmpty()) {
            document.add(new Chunk(leader.get().getFirstName() + " " + leader.get().getLastName(), plain));
        } else {
            document.add(new Chunk("None", plain));
        }
        document.add(new Paragraph(""));
    }

    protected void addBasicProjectInformation(Project project) throws DocumentException {
        document.add(new Chunk("PROJECT TOPIC: ", bold));
        if (project != null && project.getTopic() != null)
            document.add(new Chunk(project.getTopic(), plain));
        else
            document.add(new Chunk("None", plain));
        document.add(new Paragraph(""));
        document.add(new Chunk("PROJECT DESCRIPTION: ", bold));
        if (project != null && project.getDescription() != null)
            document.add(new Chunk(project.getDescription(), plain));
        else
            document.add(new Chunk("None", plain));
        document.add(new Paragraph(""));
    }

    protected void addBasicPersonInformation(Person person) throws DocumentException {
        Element[] elements = {new Chunk("FIRSTNAME: ", bold), new Chunk(person.getFirstName(), plain), new Paragraph(""),
                new Chunk("LASTNAME: ", bold), new Chunk(person.getLastName(), plain), new Paragraph(""),
                new Chunk("EMAIL: ", bold), new Chunk(person.getEmail(), plain)};
        for (Element element : elements) {
            document.add(element);
        }
    }
}
