package Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Config {
    public Config(String path) {
        File f = new File(path);

        try {
            Scanner scanner = new Scanner(f);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
