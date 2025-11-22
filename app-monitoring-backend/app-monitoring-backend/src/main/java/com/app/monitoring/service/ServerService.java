package com.app.monitoring.service;

import com.app.monitoring.entity.Server;
import com.app.monitoring.repository.ServerRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;
import java.net.URL;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final EmailService emailService;

    public ServerService(ServerRepository serverRepository,EmailService emailService) {
        this.serverRepository = serverRepository;
        this. emailService = emailService;
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("server not found"));
        boolean connectionStatus = checkServerStatus(server.getUrl());
        server.setUp(connectionStatus);
        return server;
    }

    public Server createServer(Server server) {
        Optional<Server> existing = serverRepository.findByUrl(server.getUrl());
        if (existing.isPresent()) {
            throw new RuntimeException("Server already exists with this URL");
        }
        // When adding, immediately check its current status
        boolean status = checkServerStatus(server.getUrl());
        server.setUp(status);
        return serverRepository.save(server);
    }

    public Server updateServer(Long id, Server updatedServer) {
        Server serverDb = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Server not found"));
        serverDb.setName(updatedServer.getName());
        serverDb.setUrl(updatedServer.getUrl());

        return serverRepository.save(serverDb);
    }

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    }


    // Run every 1 minute (60000 ms)
    @Scheduled(fixedRate = 60000)
    public void monitorServers() {
        List<Server> servers = serverRepository.findAll();

        for (Server server : servers) {
            boolean isRunning = checkServerStatus(server.getUrl());

            if (isRunning && !server.isUp()) {
                // Server came back up
                server.setUp(true);
                serverRepository.save(server);
                emailService.sendEmail(
                        "masumshahriar11@gmail.com",
                        "✅ Server is UP: " + server.getName(),
                        "Your server " + server.getName() + " is now running again."
                );
            } else if (!isRunning && server.isUp()) {
                // Server went down
                server.setUp(false);
                serverRepository.save(server);
                emailService.sendEmail(
                        "masumshahriar11@gmail.com",
                        "⚠️ Server is DOWN: " + server.getName(),
                        "Your server " + server.getName() + " is currently not reachable!"
                );
            }
        }
    }

    private boolean checkServerStatus(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            int code = connection.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }


}

