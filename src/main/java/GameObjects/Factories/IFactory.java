package GameObjects.Factories;

import FileHandling.FileHandler;
import GameObjects.GameObject;

import java.util.List;

public interface IFactory<T extends GameObject> {

    T create(String type);

    List<T> create(int n, String type);

    void setFileHandler(FileHandler newFileHandler);

}
