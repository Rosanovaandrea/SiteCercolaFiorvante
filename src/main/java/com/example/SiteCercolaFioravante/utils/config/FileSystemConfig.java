package com.example.SiteCercolaFioravante.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

@Configuration
@Profile("!test")
public class FileSystemConfig {
    @Bean
    public FileSystem getFileSystem(){
        return FileSystems.getDefault();
    }
}
