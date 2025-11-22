package com.app.monitoring.repository;

import com.app.monitoring.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server,Long> {
    Optional<Server> findByUrl(String url);
    //List<Server> findByOwnerId(Long ownerId);
}
