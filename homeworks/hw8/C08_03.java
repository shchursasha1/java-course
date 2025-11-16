import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class C08_03 {
    
    public static List<String> listFilesRecursively(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(directoryPath);
        
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Directory does not exist or is not a directory");
            return fileList;
        }
        
        listFilesHelper(directory, fileList, "");
        return fileList;
    }
    
    private static void listFilesHelper(File directory, List<String> fileList, String indent) {
        File[] files = directory.listFiles();
        
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            fileList.add(indent + file.getName());
            if (file.isDirectory()) {
                listFilesHelper(file, fileList, indent + "  ");
            }
        }
    }
    
    public static void main(String[] args) {
        String directoryPath = ".";
        
        List<String> files = listFilesRecursively(directoryPath);
        
        System.out.println("Files and directories:");
        for (String file : files) {
            System.out.println(file);
        }
        
        System.out.println("\nTotal items: " + files.size());
    }
}

