package com.album;

import com.files.Photo;

import java.io.*;
import java.util.ArrayList;

public class Client1 {

    public static float compressionQuality = 1f;

    public static ArrayList<Photo> getCompressedFiles(String dir) throws IOException {
        ArrayList<Photo> photos = new ArrayList<>();

        System.out.println(compressionQuality);

        File[] files = new File(dir).listFiles();
        ByteArrayOutputStream bStream;

        for (File file : files) {
            Photo photo = new Photo();
            if (compressionQuality != 1f) {
                System.out.println("СЖИМАЕМ!");
                /*bStream = new ByteArrayOutputStream();
                ImageOutputStream sos = new MemoryCacheImageOutputStream(bStream);
                ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
                ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
                jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpgWriteParam.setCompressionQuality(compressionQuality);
                jpgWriter.setOutput(sos);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedImage capture = ImageIO.read(file);
                IIOImage outputImage = new IIOImage(capture, null, null);
                jpgWriter.write(null, outputImage, jpgWriteParam);
                photo.setSize(bStream.size());
                String name = file.getName();
                photo.setName(name);
                photo.setByteArray(bStream.toByteArray());
                photos.add(photo);
                sos.flush();
                bis.close();*/
            } else {
                System.out.println("НЕ СЖИМАЕМ!");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int theByte;
                while((theByte = bis.read()) != -1) bos.write(theByte);
                bos.close();
                photo.setByteArray(bos.toByteArray());
                photo.setName(file.getName());
                photo.setSize(bos.size());
                photos.add(photo);
            }
        }

        return photos;
    }
}
