package com.app.monitoring.repository;

import com.app.monitoring.entity.ServerLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ServerLogRepository extends JpaRepository<ServerLog, Long> {
    //List<ServerLog> findByServerIdAndChangedAtBetween(Long serverId, Instant from, Instant to);
}
