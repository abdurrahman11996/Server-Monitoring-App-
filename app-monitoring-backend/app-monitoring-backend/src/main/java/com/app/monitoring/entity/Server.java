package com.app.monitoring.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "servers")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    //private String ipAddress;
    private String port;
    private Boolean status=false;
//    @ManyToOne
//    private User owner;
    private String description;
    private Instant createdAt = Instant.now();

    @Column(name = "is_up",nullable = false)
    private boolean isUp = true;


}

