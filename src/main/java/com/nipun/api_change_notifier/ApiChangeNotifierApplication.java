package com.nipun.api_change_notifier;

import com.nipun.api_change_notifier.services.ChangeDetectorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiChangeNotifierApplication implements CommandLineRunner {

    private final ChangeDetectorService changeDetectorService;

    public ApiChangeNotifierApplication(ChangeDetectorService changeDetectorService) {
        this.changeDetectorService = changeDetectorService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiChangeNotifierApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String fileBefore = null;
        String fileAfter = null;

        for (int i = 0; i < args.length; i++) {
            if ("--head-path".equals(args[i]) && i + 1 < args.length) {
                fileBefore = args[i + 1];
            }
            if ("--base-path".equals(args[i]) && i + 1 < args.length) {
                fileAfter = args[i + 1];
            }
        }

        if (fileBefore == null || fileAfter == null) {
            throw new IllegalArgumentException(
                "Missing required inputs: --head-path and --base-path"
            );
        }

        long start = System.currentTimeMillis();

        changeDetectorService.getChanges(fileBefore, fileAfter);

        long end = System.currentTimeMillis();

        System.out.println("Total time = " + (end - start) + "ms");
    }
}
