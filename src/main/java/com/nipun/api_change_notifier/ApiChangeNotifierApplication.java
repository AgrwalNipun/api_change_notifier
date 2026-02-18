package com.nipun.api_change_notifier;

import java.io.File;

import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.Project;
import com.nipun.api_change_notifier.services.FileIteratingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiChangeNotifierApplication implements CommandLineRunner {

    private final FileIteratingService fileIteratingService;

    public ApiChangeNotifierApplication(FileIteratingService fileIteratingService) {
        this.fileIteratingService = fileIteratingService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiChangeNotifierApplication.class, args);
    }

    @Override
    public void run(String... args) {

        if (args.length == 0 || args[0].isBlank()) {
            System.err.println("❌ No target directory provided.");
            System.err.println("Usage: java -jar notifier.jar <path-to-project>");
            System.exit(1);
        }

        String targetDir = args[0];
        System.out.println("🔍 Scanning project at: " + targetDir);

        File dir = new File(targetDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("❌ Path does not exist or is not a directory: " + targetDir);
            System.exit(1);
        }

        long start = System.currentTimeMillis();

        Project project = fileIteratingService.processProject(dir);

        System.out.println("\n✅ Scan complete. Found " + project.getApis().size() + " API endpoint(s):");
        for (Api api : project.getApis()) {
            System.out.println("  [" + api.getMethod() + "] " + api.getEndPoint());
        }

        long end = System.currentTimeMillis();
        System.out.println("\n⏱ Total time = " + (end - start) + "ms");
    }
}
