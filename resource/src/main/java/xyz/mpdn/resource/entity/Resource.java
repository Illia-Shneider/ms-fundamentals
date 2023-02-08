package xyz.mpdn.resource.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.InputStream;

@Entity
@Data
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
}
