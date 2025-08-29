package com.example.SiteCercolaFioravante.config;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.nio.file.FileSystem;

@org.springframework.context.annotation.Configuration
@Profile("test")
public class FIleSystemTestConfig {

    @Bean
    public FileSystem getFileSystem(){
        return Jimfs.newFileSystem(Configuration.forCurrentPlatform());
    }
}
