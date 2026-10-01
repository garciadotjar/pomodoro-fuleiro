package com.garciadotjar.pomodorofuleiro.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    private static final String APP_NAME = "PomodoroFuleiro";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

    private Config config;
    private final Path pathConfigFile;

    public ConfigManager(Config config) {
        this.config = config;

        //pegar o path para salvar as configs em windows e linux.
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            String appData = System.getenv("APPDATA");
            if(appData==null || appData.isBlank()){
                throw new IllegalStateException("APPDATA não está definido");
            }

            this.pathConfigFile = Path.of(
                    appData,
                    APP_NAME,
                    "config.json");

        }else if (os.contains("linux")) {
            String userPath = System.getProperty("user.home");
            if (userPath == null || userPath.isBlank()) {
                throw new IllegalStateException("user.home não está definido");
            }

            this.pathConfigFile = Path.of(
                    userPath,
                    ".config",
                    APP_NAME,
                    "config.json"
            );
        }else{
            throw new UnsupportedOperationException("Sistema operacional inválido");
        }

        try{
            Files.createDirectories(pathConfigFile.getParent());
            if (!Files.exists(pathConfigFile)) {
                exportConfig();  //cria o arquivo se nao existe ainda
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exportConfig(){
        try{
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(pathConfigFile.toFile(), config);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void importConfig() {
        try {
            config = objectMapper.readValue(
                    pathConfigFile.toFile(),
                    Config.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Config getConfig() {
        return config;
    }
}
