package com.example.devopsapp.controller;

import com.example.devopsapp.model.AppMetadata;
import com.example.devopsapp.model.GitMetadata;
import com.example.devopsapp.service.AppService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value = "/health", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<?> health(@RequestHeader(value = "Accept", defaultValue = "") String accept) {
        Map<String, String> health = appService.getHealthStatus();
        if (accept.contains("text/html")) {
            return ResponseEntity.ok(appService.wrapInHtml("System Health", 
                "Status: <b>" + health.get("status") + "</b><br>" + health.get("message"), "heart-pulse"));
        }
        return ResponseEntity.ok(health);
    }

    @GetMapping(value = "/hello", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<String> hello(@RequestParam(required = false) String name, @RequestHeader(value = "Accept", defaultValue = "") String accept) {
        String greeting = appService.getGreeting(name);
        if (accept.contains("text/html")) {
            return ResponseEntity.ok(appService.wrapInHtml("Greeting", greeting, "message-square"));
        }
        return ResponseEntity.ok(greeting);
    }

    @GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<?> info(@RequestHeader(value = "Accept", defaultValue = "") String accept) {
        AppMetadata info = appService.getAppMetadata();
        if (accept.contains("text/html")) {
            return ResponseEntity.ok(appService.wrapInHtml("Application Info", 
                "Name: <b>" + info.name() + "</b><br>Version: " + info.version() + "<br>Java: " + info.javaVersion(), "info"));
        }
        return ResponseEntity.ok(info);
    }

    @GetMapping(value = "/git", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<?> git(@RequestHeader(value = "Accept", defaultValue = "") String accept) {
        GitMetadata git = appService.getGitInfo();
        if (accept.contains("text/html")) {
            return ResponseEntity.ok(appService.wrapInHtml("Git Repository", 
                "Branch: <b>" + git.branch() + "</b><br>Commit: <code>" + git.commitId() + "</code><br>Message: <i>" + git.commitMessage() + "</i>", "git-branch"));
        }
        return ResponseEntity.ok(git);
    }

    @GetMapping(value = "/metrics-view", produces = MediaType.TEXT_HTML_VALUE)
    public String metricsView() {
        return appService.wrapInHtml("System Metrics", 
            "Live Prometheus metrics are being collected.<br><br><a href='/actuator/prometheus' style='color: #00f2fe;'>View Raw Data</a>", "bar-chart-3");
    }
}
