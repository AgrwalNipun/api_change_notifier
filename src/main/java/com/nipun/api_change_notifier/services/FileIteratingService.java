package com.nipun.api_change_notifier.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FileIteratingService {

ParsingService ps = new ParsingService();


public  List<File> scanJavaFiles(File rootDir) {

    List<File> files = new ArrayList<>();

    if (!rootDir.exists()) return files;

    for (File file : rootDir.listFiles()) {

        if (file.isDirectory()) {
            files.addAll(scanJavaFiles(file));
        } else if (file.getName().endsWith(".java")) {
            System.out.println("FileName"+file.getName()+"////////");
            files.add(file);
            ps.parse(file);
        }
    }
    return files;
}


}
