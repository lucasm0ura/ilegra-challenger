package com.lucasmoura.ilegra.service.impl;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lucasmoura.ilegra.service.WatcherService;
import com.lucasmoura.ilegra.util.AplicationProperties;

@Component
public class WatcherServiceImpl implements WatcherService {
	
	Logger logger = LoggerFactory.getLogger(WatcherServiceImpl.class);
	
    @Autowired
    private AplicationProperties applicationProperties;
    
    @Autowired
    private ReadFileServiceImpl readFileProccess;

	@Override
	public void start() {
		System.out.println("Entrou no Start");
		try (WatchService service = FileSystems.getDefault().newWatchService()) {
			final String inputPath = applicationProperties.getHomePath() + applicationProperties.getDirectoryPathIn();
			
			Map<WatchKey, Path> keyMap = new HashMap<>();
			Path path = Paths.get(inputPath);
			logger.info("Watching " + inputPath);
			keyMap.put(path.register(service, StandardWatchEventKinds.ENTRY_CREATE), path);

			WatchKey watchKey;

			do {
				watchKey = service.take();
//				Path eventDir = keyMap.get(watchKey);
                for (WatchEvent<?> watchEvent: watchKey.pollEvents()) {
                    logger.info("File found: " + watchEvent.context().toString());
                    final String filePath = inputPath + watchEvent.context().toString();
                    readFileProccess.process(filePath);
                }				
				
			} while (watchKey.reset());
		} catch (IOException | InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		}	

	}

}
