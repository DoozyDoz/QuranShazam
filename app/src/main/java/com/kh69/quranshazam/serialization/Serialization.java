package com.kh69.quranshazam.serialization;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kh69.quranshazam.fingerprinting.KeyPoint;


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serialization {

    // The serialized <songId,keypoints> hash table is inside the "serialized"
    // directory in a file called "hashmap.ser"
    public static void serializeHashMap(Map<Long, List<KeyPoint>> hashMap, String storageRef, Context ctx) {
        String filePath = ctx.getFilesDir().getPath() + "/hashmap2.ser";
        File   f;

        try {
            f = new File(filePath);
            OutputStream       fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hashMap);
            oos.close();
            fos.close();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Uri    fileHash = Uri.fromFile(f);
            String root     = Environment.getExternalStorageDirectory().toString();
            File   myDir    = new File(root + "/audio_repo");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            
            String fname = "hashmap2.ser";
            File   file  = new File(myDir, fname);
            if (file.exists())
                file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            storageRef.child("hashmap2.ser").putFile(fileHash).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (IOException ex) {
            System.out.println("Input/output error " + ex);
        }
    }

    public static void downloadFile(String storageRef, Context ctx) {
        File fileNameOnDevice = new File(ctx.getExternalFilesDir(null) + "/" + "hashmap2.ser");

        storageRef.child("hashmap2.ser").getFile(fileNameOnDevice).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                return;
            }
        });

        System.out.println("Archivo: " + fileNameOnDevice.getAbsolutePath());
    }

    public static Map<Long, List<KeyPoint>> fillHashMap(String storageRef, Context ctx) {
        Map<Long, List<KeyPoint>> hashMap = new HashMap<>();

        downloadFile(storageRef, ctx);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String location = ctx.getExternalFilesDir(null) + "/" + "hashmap2.ser";

        FileInputStream   fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(location);
            ois = new ObjectInputStream(fis);

            try {
                while (true) {
                    hashMap = (Map<Long, List<KeyPoint>>) ois.readObject();
                }
            } catch (EOFException eof) {
                System.out.println("End of file exception.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hashMap;
    }
}