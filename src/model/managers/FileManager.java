package model.managers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileManager {
    public static final String root = "res";
    public static final String userRoot = root+ File.separator+"users";
    private FileManager(){};
    //FIND METHODS:
    public static Set<Path> findFolders(String folderName) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(root))) {
            return walk
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().equals(folderName))
                    .collect(Collectors.toSet());
        }
    }
    public static Set<String> findFilesNameInDirectory(String directory) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
            return walk
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }
    public static Path findDirectoryFromPath(String directoryName, String searchIn) throws IOException {
        return Files.walk(Paths.get(searchIn))
                .filter(Files::isDirectory)
                .filter(path -> path.getFileName().toString().equals(directoryName))
                .findFirst().orElse(null);

    }

    public static Path findFileAndDirectory(String fileName) throws IOException {
            return Files.walk(Paths.get(root))
                    .filter(path -> path.getFileName().toString().equals(fileName))
                    .findFirst().orElse(null);
    }
    public static Path findFileInADirectory(String fileName,String directory) throws IOException {
        return Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().equals(fileName))
                .findFirst().orElse(null);
    }
    public static String[] findPNGFromPath(Path path) throws IOException {
        try (Stream<Path> filesStream = Files.walk(path)) {
            return filesStream
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".png"))
                    .toArray(String[]::new);
        }
    }
    public static String[] findPNGFromNameInALocation(String name,Path root) throws IOException {
        try (Stream<Path> filesStream = Files.walk(root)) {
            return filesStream
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(str->str.contains(name))
                    .filter(s -> s.endsWith(".png"))
                    .toArray(String[]::new);
        }
    }
    //REMOVE METHODS
    public static void removeLevelFile(String fileName) {
        try {
            Files.delete(Files.walk(Paths.get(UserManager.getInstance().getUserLoaded().getUserPath()+"myLevels"))
                    .filter(path -> path.getFileName().toString().equals(fileName + ".lvl"))
                    .findFirst().orElse(null));
            System.out.println("File " + fileName + " removed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while removing the file " + fileName + ": " + e.getMessage());
        }

    }
    public static void removeWorldFile(String fileName) {
        try {
            Files.delete(Files.walk(Paths.get(UserManager.getInstance().getUserLoaded().getUserPath()+"myWorlds"))
                    .filter(path -> path.getFileName().toString().equals(fileName + ".wrl"))
                    .findFirst().orElse(null));
            System.out.println("File " + fileName + " removed successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while removing the file " + fileName + ": " + e.getMessage());
        }

    }
    public static boolean removeUserFile(String fileName) {
        boolean check = true;
        try {
            Path path = findDirectoryFromPath(fileName,userRoot);
            removeDirectoryRecursively(path);
            System.out.println("File " + fileName + " removed successfully.");
        } catch (IOException e) {
            check = false;
            System.err.println("An error occurred while removing the file " + fileName + ": " + e.getMessage());
        }
        return check;

    }
    private static void removeDirectoryRecursively(Path directoryPath) throws IOException {
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            Files.walk(directoryPath)
                    .sorted((p1, p2) -> -p1.compareTo(p2))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } else {
            System.out.println("The specified directory ("+directoryPath+") does not exist.");
        }
    }
    //GAME DATA METHODS:
    public static void saveProgramData(){
        UserManager uM = UserManager.getInstance();
        uM.getUserMap().values().forEach(user->user.save());
        File file = new File(root,"data");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(uM.getUserSelected());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadProgramData(){
        UserManager uM = UserManager.getInstance();
        try (FileInputStream fileIn = new FileInputStream(findFileAndDirectory("data").toString());
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                uM.setUserSelected(((String)objectIn.readObject()));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            System.out.println("DATA FILE NOT FOUND: " +  e.getMessage());
        }
    }



}
