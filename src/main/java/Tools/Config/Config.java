//package Tools.Config;
//
//import java.io.*;
//import java.util.Properties;
//import java.util.Scanner;
//
//public class Config {
//
//    private int height;
//    private int width;
//    private String mapName;
//
//    public Config() {
//        File configFile = new File("config2.properties");
//        try {
//            Properties props = new Properties();
//            props.setProperty("level", "1");
//            props.setProperty("map", "SWAMP");
//            props.setProperty("height", "480");
//            props.setProperty("width", "720");
//            FileWriter writer = new FileWriter(configFile);
//            props.store(writer, "map level settings");
//            writer.close();
//        } catch(FileNotFoundException e) {
//            System.out.println("file not found");
//        } catch(IOException e) {
//            System.out.println("I/O Error");
//        }
//    }
//    public Config(String path) {
//        File configFile = new File(path);
//
//        try {
//            Scanner scanner = new Scanner(configFile);
//
//            FileReader reader = new FileReader(configFile);
//            Properties props = new Properties();
//            props.load(reader);
//
//            this.height = Integer.parseInt(String.valueOf(props.get("height")));
//            this.width = Integer.parseInt(String.valueOf(props.get("width")));
//            this.mapName = String.valueOf(props.get("map"));
//
//
//            reader.close();
//
//            //Properties props = new Properties();
//            //props.setProperty("level", "2");
//            //props.setProperty("zone", "DESERT");
//            //FileWriter writer = new FileWriter(configFile);
//            //props.store(writer, "host settings");
//            //writer.close();
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            System.out.println(e.toString());
//        }
//    }
//
//    public String getMapName() {
//        return this.mapName;
//    }
//
//    public int getHeight() {
//        return this.height;
//    }
//
//    public int getWidth() {
//        return this.width;
//    }
//
//    public String getFilePath() {
//        String path = String.valueOf(this.getClass().getResource(""));
//        return path + "config.properties";
//    }
//}
