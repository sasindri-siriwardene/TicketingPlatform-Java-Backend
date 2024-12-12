package org.ticket.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.ticket.configuration.SystemConfig;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*") // Allow all origins for CORS
public class ConfigController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Runs the vendor thread to add tickets to the pool.
     */
    @GetMapping("/api/config")
    public SystemConfig getConfig() throws IOException {
        // Replace with your file path
        System.out.println("fetching config");
        File jsonFile = new File("src/main/resources/config.json");
        System.out.println(objectMapper.readValue(jsonFile, SystemConfig.class));
        return objectMapper.readValue(jsonFile, SystemConfig.class);
    }
    /**
     * Sets the system configuration to the JSON file.
     *
     * @param config the system configuration to set
     * @return the updated system configuration
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/api/config")
    public SystemConfig setConfig(@RequestBody SystemConfig config) throws IOException {

        String jsonString = objectMapper.writeValueAsString(config);
        System.out.println("Setting config: " + jsonString);
        File jsonFile = new File("src/main/resources/config.json");
        objectMapper.writeValue(jsonFile, config);
        return objectMapper.readValue(jsonFile, SystemConfig.class);
    }
}

