package explorer;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.ChatController;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Changes implements Serializable {
    private int id;
    private ArrayList<Change> changes;

    public Changes() {
        changes = new ArrayList<>();
        id = hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void acceptChanges(Changes changes, String pathToFolder) throws IOException {
        ArrayList<Change> listOfChanges = changes.getChanges();

        for (Change change : listOfChanges) {
            ChatController.setChange(change);
            switch (change.getChangeType()) {
                case ADD_FOLDER: {
                    File newFolder = new File(pathToFolder + "\\" + change.getNew_path() + "\\" + change.getNew_name());
                    if (!newFolder.mkdir()) {
                        return;
                    }
                    break;
                }
                case COPY_PHOTO: {
                    Files.copy(Paths.get(pathToFolder + "\\" + change.getOld_path()), Paths.get(pathToFolder + "\\" + change.getNew_path()));
                    break;
                }
                case MOVE_PHOTO: {
                    Files.move(Paths.get(pathToFolder + "\\" + change.getOld_path()), Paths.get(pathToFolder + "\\" + change.getNew_path()));
                    break;
                }
                case RENAME_PHOTO:
                case RENAME_FOLDER: {
                    File fileToRename = new File(pathToFolder + "\\" + change.getOld_path() + "\\" + change.getOld_name());
                    File fileRenamed = new File(pathToFolder + "\\" + change.getOld_path() + "\\" + change.getNew_name());
                    if (!fileToRename.renameTo(fileRenamed)) {
                        return;
                    }
                    break;
                }
                case DELETE_FOLDER: {
                    Folder folder = new Folder(null, new File(pathToFolder + "\\" + change.getOld_path() + "\\" + change.getOld_name()));
                    if (!ExplorerCommands.deleteFolder(folder)) {
                        return;
                    }
                    break;
                }
            }
        }
    }

    public void addNewChange(Change change) {
        ChatController.setChange(change);
        changes.add(change);
    }

    private ArrayList<Change> getChanges() {
        return changes;
    }
}
