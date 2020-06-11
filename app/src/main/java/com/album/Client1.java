package com.album;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.files.Photo;
import java.io.*;
import java.util.ArrayList;

public class Client1 {

    public static int compressionQuality = 100;

    public static ArrayList<Photo> getCompressedFiles(String dir) throws IOException {
        ArrayList<Photo> photos = new ArrayList<>();

        System.out.println(compressionQuality);

        File[] files = new File(dir).listFiles();
        ByteArrayOutputStream bStream;

        for (File file : files) {
            Photo photo = new Photo();
            if (compressionQuality != 100) {
                System.out.println("СЖИМАЕМ!");
                bStream = new ByteArrayOutputStream();
                Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                bmp.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bStream);
                photo.setSize(bStream.size());
                String name = file.getName();
                photo.setName(name);
                photo.setByteArray(bStream.toByteArray());
                photos.add(photo);
            } else {
                System.out.println("НЕ СЖИМАЕМ!");
                bStream = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int theByte = 0;
                while((theByte = bis.read()) != -1) bStream.write(theByte);
                bStream.close();
                photo.setByteArray(bStream.toByteArray());
                photo.setName(file.getName());
                photo.setSize(bStream.size());
                photos.add(photo);
            }
        }
        return photos;
    }
}
