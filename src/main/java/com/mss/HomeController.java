package com.mss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping
    public String getInfo() throws IOException {
        log.info("Hello World");
        String vmHostname = getVmHostName();
        String commitHash = getCommitHash();
        return "<h1>VM Hostname is: <span style='color:blue;'>" + vmHostname + "</span></h1>"
                + "<h2>Commit Hash is: " + commitHash + "</h2>";
    }

    // Fetch the VM Hostname from the environment variable
    private String getVmHostName() throws UnknownHostException {
        String vmHostName = System.getenv("VM_HOSTNAME");
        if (vmHostName == null) {
            vmHostName = InetAddress.getLocalHost().getHostName();
        }
        return (vmHostName != null) ? vmHostName : "unknown";
    }

    private String getCommitHash() throws IOException {
        String commitHash = System.getenv("COMMIT_HASH");
        if (commitHash == null) {
            String gitRepo = "https://github.com/Tanver-Ahammed/spring-security.git";
            ProcessBuilder builder = new ProcessBuilder("git", "ls-remote", gitRepo, "refs/heads/play");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] parts = line.split("\\s+");
                commitHash = parts[0];
            }
        }
        return (commitHash != null) ? commitHash : "unknown";
    }

}
