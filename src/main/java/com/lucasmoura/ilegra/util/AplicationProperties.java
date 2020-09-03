package com.lucasmoura.ilegra.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix= "ilegra")
public class AplicationProperties {
	
	@Value("directoryPathIn")
	private String directoryPathIn;
    
	@Value("directoryPathOut")
	private String directoryPathOut;
	
	@Value("homePath")
	private String homePath;

	@Value("fileNameOut")
    private String fileNameOut;

	public String getDirectoryPathIn() {
		return directoryPathIn;
	}

	public void setDirectoryPathIn(String directoryPathIn) {
		this.directoryPathIn = directoryPathIn;
	}

	public String getDirectoryPathOut() {
		return directoryPathOut;
	}

	public void setDirectoryPathOut(String directoryPathOut) {
		this.directoryPathOut = directoryPathOut;
	}

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	public String getFileNameOut() {
		return fileNameOut;
	}

	public void setFileNameOut(String fileNameOut) {
		this.fileNameOut = fileNameOut;
	}

    
	
}
