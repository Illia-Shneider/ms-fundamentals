package xyz.mpdn.resource.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ProcessorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProcessorApplication.class, args);
	}
}
