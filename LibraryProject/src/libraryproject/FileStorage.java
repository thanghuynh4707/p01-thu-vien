                                                /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileStorage {

    private String filePath;

    public FileStorage(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be empty");
        }
        this.filePath = filePath.trim();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path must not be empty");
        }
        this.filePath = filePath.trim();
    }

    public boolean exists() {
        return new File(this.filePath).isFile();
    }

    public void save(LibraryService service) {
        if (service == null) {
            throw new IllegalArgumentException("There is no data to save");
        }
        File target = new File(this.filePath);
        File parent = target.getAbsoluteFile().getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalArgumentException("Couldn not create directory: " + parent.getPath());
        }
        File temp = new File(this.filePath + ".tmp");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(temp))) {
            out.writeObject(service);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn not save data: " + e.getMessage());
        }
        try {
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn not save data: " + e.getMessage());
        }
    }

    public LibraryService load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filePath))) {
            Object obj = in.readObject();
            if (!(obj instanceof LibraryService)) {
                throw new IllegalArgumentException("Data file has an invalid format: " + this.filePath);
            }
            return (LibraryService) obj;
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn not read file " + this.filePath + ":" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Data file is not compatible with this program: " + e.getMessage());
        }
    }
}
