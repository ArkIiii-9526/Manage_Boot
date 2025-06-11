package com.manage.boot.controller;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Component
@Profile("dev")
public class DevBrowserLauncher implements ApplicationListener<ApplicationReadyEvent> {


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String url = getDefaultUrl();
        openBrowser(url);
    }

    private String getDefaultUrl() {
        int port = 8076;
        String contextPath = "/";
        return String.format("http://localhost:%d%s", port, contextPath);
    }

    private void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                // 使用 AWT 桌面功能打开浏览器
                Desktop.getDesktop().browse(new URI(url));
            } else {
                // 备用命令方式（适用于Linux服务器和WSL）
                executeFallbackCommand(url);
            }
        } catch (Exception e) {
            printManualAccessHint(url, e.getMessage());
        }
    }

    private void executeFallbackCommand(String url) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            new ProcessBuilder("cmd", "/c", "start", url).start();
        } else if (os.contains("mac")) {
            new ProcessBuilder("open", url).start();
        } else {
            new ProcessBuilder("xdg-open", url).start();
        }
    }

    private void printManualAccessHint(String url, String error) {
        String separator = "=".repeat(80);
        String message = String.format("\n\n%s\n应用已启动，但浏览器打开失败！\n原因: %s\n请手动访问: %s\n%s",
                separator, error, url, separator);

        System.err.println(message);
    }
}
