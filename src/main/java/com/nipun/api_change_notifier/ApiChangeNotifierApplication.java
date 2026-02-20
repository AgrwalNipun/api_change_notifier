package com.nipun.api_change_notifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.nipun.api_change_notifier.models.Api;
import com.nipun.api_change_notifier.models.Commit;
import com.nipun.api_change_notifier.models.Project;
import com.nipun.api_change_notifier.services.FileIteratingService;
import com.nipun.api_change_notifier.services.NotifierService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiChangeNotifierApplication implements CommandLineRunner {

    private final FileIteratingService fileIteratingService;
    private final NotifierService notifierService;

    public ApiChangeNotifierApplication(FileIteratingService fileIteratingService,
            NotifierService notifierService) {
        this.fileIteratingService = fileIteratingService;
        this.notifierService = notifierService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiChangeNotifierApplication.class, args);
    }

    @Override
    public void run(String... args) {



ParserConfiguration configuration = new ParserConfiguration();
configuration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17); 
// or JAVA_14 minimum

StaticJavaParser.setConfiguration(configuration);

        if (args.length < 4) {
            System.err.println("❌ Invalid arguments provided.");
            System.err.println(
                    "Usage: java -jar notifier.jar <path-to-project> <repoID> <commitID> <emails (comma-separated)>");
            System.exit(1);
        }

        String targetDir = args[0];
        String repoID = args[1];
        String commitID = args[2];
        String emailsRaw = args[3];

        List<String> userEmails = Arrays.asList(emailsRaw.split(","));

        System.out.println("🔍 Scanning project at: " + targetDir);
        System.out.println("🆔 Repo ID: " + repoID);
        System.out.println("📌 Commit ID: " + commitID);
        System.out.println("📧 Emails: " + userEmails);

        File dir = new File(targetDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("❌ Path does not exist or is not a directory: " + targetDir);
            System.exit(1);
        }

        long start = System.currentTimeMillis();

        Project project = fileIteratingService.processProject(dir);
        project.setGithubId(repoID);
        project.setUsersEmail(userEmails);
        project.setName(dir.getName());

        Commit commit = new Commit();
        commit.setCommitId(commitID);
        commit.setProject(project);
        project.setCommits(new ArrayList<>(List.of(commit)));

        System.out.println("\n✅ Scan complete. Found " + project.getApis().size() + " API endpoint(s):");
        for (Api api : project.getApis()) {
            System.out.println("  [" + api.getMethod() + "] " + api.getEndPoint());
        }

        // POST detected endpoints to the external receiver
        notifierService.postEndpoints(project.getApis(), project);

        long end = System.currentTimeMillis();
        System.out.println("\n⏱ Total time = " + (end - start) + "ms");
    }
}
