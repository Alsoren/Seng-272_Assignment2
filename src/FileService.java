import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileService {

    private final String fileName;

    public FileService(String fileName) {
        this.fileName = fileName;
    }

    public void appendEntry(String entryText) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(entryText);
        }
    }

    public String buildEntryText(String projectName,
                                 String teamLeader,
                                 String teamSize,
                                 String projectType,
                                 String startDate,
                                 String recordTime) {

        StringBuilder sb = new StringBuilder();
        sb.append("=== Project Entry ===\n");
        sb.append("Project Name : ").append(projectName).append("\n");
        sb.append("Team Leader : ").append(teamLeader).append("\n");
        sb.append("Team Size : ").append(teamSize).append("\n");
        sb.append("Project Type : ").append(projectType).append("\n");
        sb.append("Start Date : ").append(startDate).append("\n");
        sb.append("Record Time : ").append(recordTime).append("\n");
        sb.append("====================\n");
        return sb.toString();
    }
}