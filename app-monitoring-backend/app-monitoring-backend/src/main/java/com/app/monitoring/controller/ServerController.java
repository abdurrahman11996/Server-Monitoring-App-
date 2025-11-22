package com.app.monitoring.controller;

import com.app.monitoring.entity.Server;
import com.app.monitoring.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
@CrossOrigin(origins = "*")
public class ServerController {

    @Autowired
    ServerService serverService;

    // 🔹 GET all servers
    @GetMapping
    public List<Server> getAll() {
        return serverService.getAllServers();
    }

    // 🔹 GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Server> getById(@PathVariable Long id) {
        Server server = serverService.getServerById(id);
        return ResponseEntity.ok(server);
    }

    // 🔹 CREATE new server
    @PostMapping
    public Server create(@RequestBody Server server) {
        return serverService.createServer(server);
    }

    // 🔹 UPDATE existing server
    @PutMapping("/{id}")
    public ResponseEntity<Server> update(@PathVariable Long id, @RequestBody Server updated) {
        return ResponseEntity.ok(serverService.updateServer(id, updated));
    }

    // 🔹 DELETE a server
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }
}
