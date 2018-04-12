package jaguars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class JaguarsGman {
    public static void main(String[] args) {
        System.out.println("Project root path: " + System.getProperty("user.dir"));
        File wd = new File(".");
        System.out.println("Working Directory path: " + wd.getAbsolutePath());
        SpringApplication.run(JaguarsGman.class, args);
    }
}
