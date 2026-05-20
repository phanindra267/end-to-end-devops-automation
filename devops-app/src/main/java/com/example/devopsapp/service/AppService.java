package com.example.devopsapp.service;

import com.example.devopsapp.model.AppMetadata;
import com.example.devopsapp.model.GitMetadata;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AppService {

    public Map<String, String> getHealthStatus() {
        return Map.of("status", "UP", "message", "Application is healthy");
    }

    public String getGreeting(String name) {
        return "Hello, " + (name != null ? name : "DevOps") + "!";
    }

    public AppMetadata getAppMetadata() {
        return new AppMetadata(
                "devops-app",
                "0.0.1-SNAPSHOT",
                "Spring Boot Cloud-Native Application",
                System.getProperty("java.version")
        );
    }

    public GitMetadata getGitInfo() {
        String branch = executeCommand("git rev-parse --abbrev-ref HEAD");
        String commitId = executeCommand("git rev-parse --short HEAD");
        String message = executeCommand("git log -1 --pretty=%B");

        return new GitMetadata(
                branch != null ? branch : "unknown",
                commitId != null ? commitId : "unknown",
                message != null ? message.trim() : "unknown"
        );
    }

    private String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            java.util.Scanner s = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
            return s.hasNext() ? s.next().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public String wrapInHtml(String title, String content, String icon) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s | DevOps App</title>
                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;600;800&display=swap" rel="stylesheet">
                <script src="https://unpkg.com/lucide@latest"></script>
                <style>
                    body {
                        background: #0f172a;
                        color: #f8fafc;
                        font-family: 'Outfit', sans-serif;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        min-height: 100vh;
                        margin: 0;
                        background-image: radial-gradient(at 0%% 0%%, hsla(253,16%%,7%%,1) 0, transparent 50%%), radial-gradient(at 100%% 0%%, hsla(225,39%%,30%%,1) 0, transparent 50%%);
                    }
                    .card {
                        background: rgba(30, 41, 59, 0.7);
                        backdrop-filter: blur(12px);
                        padding: 3rem;
                        border-radius: 32px;
                        border: 1px solid rgba(255, 255, 255, 0.1);
                        text-align: center;
                        max-width: 500px;
                        width: 90%%;
                        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
                    }
                    .icon {
                        color: #00f2fe;
                        margin-bottom: 1.5rem;
                    }
                    h1 { font-size: 2.5rem; margin-bottom: 1rem; background: linear-gradient(to right, #00f2fe, #4facfe); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
                    .content { font-size: 1.25rem; color: #94a3b8; line-height: 1.6; }
                    .btn {
                        margin-top: 2rem;
                        display: inline-block;
                        padding: 0.75rem 2rem;
                        background: rgba(255, 255, 255, 0.05);
                        border-radius: 12px;
                        text-decoration: none;
                        color: #f8fafc;
                        transition: all 0.3s ease;
                        border: 1px solid rgba(255, 255, 255, 0.1);
                    }
                    .btn:hover { background: rgba(255, 255, 255, 0.1); transform: translateY(-2px); }
                </style>
            </head>
            <body>
                <div class="card">
                    <div class="icon"><i data-lucide="%s" size="64"></i></div>
                    <h1>%s</h1>
                    <div class="content">%s</div>
                    <a href="/" class="btn">Back to Dashboard</a>
                </div>
                <script>lucide.createIcons();</script>
            </body>
            </html>
            """.formatted(title, icon, title, content);
    }
}
