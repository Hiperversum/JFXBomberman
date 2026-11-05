package model.utility;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import model.managers.FileManager;
import model.managers.MapManager;
import model.user.User;
import model.world.Level;
import model.world.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;


public abstract class LoaderUtilityTools {

    private static final String userDirectory = "users";

    //IMAGES METHODS:
    public static Image createImage(String imagePath){
        try {
            Image image = new Image(new FileInputStream(imagePath));
            int width = 48;
            int height = width*(int)image.getHeight()/(int)image.getWidth();
            return scaleImage(image,width,height);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Image createImageFromPath(Path imagePath){
        try {
            Image image = new Image(new FileInputStream(imagePath.toString()));
            int width = 48;
            int height = width*(int)image.getHeight()/(int)image.getWidth();
            return scaleImage(image,width,height);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static Image scaleImage(Image img, int width, int height){
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
        BufferedImage scaledImage = new BufferedImage(width, height, bufferedImage.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(bufferedImage,0,0,width,height,null);
        Image scaledFxImage = SwingFXUtils.toFXImage(scaledImage, null);
        return scaledFxImage;
    }
    public static byte[] imageToImageByte(Image image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return imageBytes;
    }
    public static Image imageBytesToImage(byte[] imageBytes){
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes)) {
            Image javafxImage = new Image (byteArrayInputStream);
            return javafxImage;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //USERS LOADER METHODS:
    public static Map<String, User> loadUsers() {
        Map<String, User> userMap = new TreeMap<>();
        try (Stream<Path> stream = Files.walk(FileManager.findFileAndDirectory(userDirectory), 1)) {
                Path[] subFolders = stream
                        .filter(path -> !path.getFileName().toString().equalsIgnoreCase(userDirectory))
                        .filter(Files::isDirectory)
                        .toArray(Path[]::new);
                for (Path subfolder : subFolders) {
                    userMap.put(subfolder.getFileName().toString(),User.load(Files.walk(subfolder)
                            .filter(Files::isRegularFile)
                            .filter(path -> path.getFileName().toString().equals("userData.bin"))
                            .findFirst().orElse(null).toString()));
                }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while loading Users: ", e);
        }
        return userMap;
    }


    //FONTS METHODS:
    public static void loadFonts(){
        try {
            String root = FileManager.findFileAndDirectory("fonts").toString() + File.separator;
            Font.loadFont(new FileInputStream(new File(root + "PressStart2P.ttf")), 12);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            e.printStackTrace();
        }
    }



}






















