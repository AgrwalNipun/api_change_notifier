package com.nipun.api_change_notifier;

import com.nipun.api_change_notifier.models.ChangeLog;
import com.nipun.api_change_notifier.services.ChangeDetectorService;
import com.nipun.api_change_notifier.services.FileIteratingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
import java.util.List;

@SpringBootApplication
public class ApiChangeNotifierApplication implements CommandLineRunner {

    private final FileIteratingService fileIteratingService;
    private final ChangeDetectorService changeDetectorService;

    public ApiChangeNotifierApplication(FileIteratingService fileIteratingService,ChangeDetectorService changeDetectorService) {
        this.fileIteratingService = fileIteratingService;
        this.changeDetectorService = changeDetectorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiChangeNotifierApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File projectDir = new File("C:\\Users\\nipun\\Desktop\\spring boot\\roadmap2");

        long start = System.currentTimeMillis();
        
        // fileIteratingService.processProject(projectDir);
        List<ChangeLog> changes = changeDetectorService.getChanges();
        // System.out.println(changes);
        for(ChangeLog change : changes){
            System.out.println(change+"///////////////////");
        }

        long end = System.currentTimeMillis();
        
        System.out.println("Total time = " + (end - start) + "ms");
    }
}