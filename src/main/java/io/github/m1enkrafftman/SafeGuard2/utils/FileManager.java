package io.github.m1enkrafftman.SafeGuard2.utils;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.heuristics.DataConfiguration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	
	private SafeGuard2 safeguard2; 
	private DataConfiguration myDataConfig;
	private OutputManager myOutputManager;
	
	public final String sgLineSeparator = System.getProperty("line.separator");
	public File sgDirectory;
	public File sgDirectoryConfig;
	
	public FileManager(SafeGuard2 sg) {
		safeguard2 = sg;
		myDataConfig = sg.getDataConfig();
		myOutputManager = sg.getOutput();
		sgDirectory = safeguard2.getDataFolder();
		sgDirectoryConfig = new File(sgDirectory + File.separator + "config");
	}
	
	public void read() {
		if (!sgDirectory.exists()) {
			myOutputManager.log("Creating SafeGuard directory.");
			sgDirectory.mkdir();
		}
		if (!sgDirectoryConfig.exists())  {
			myOutputManager.log("Creating SafeGuard config file.");
			sgDirectoryConfig.mkdir();
		}

		BufferedReader bufferedFileReader = null;

		try {
			bufferedFileReader = new BufferedReader(new FileReader(sgDirectoryConfig));

			String currentLine;
			
			while ((currentLine = bufferedFileReader.readLine()) != null) {
				String[] words = currentLine.split("=");
				if(words[0].equalsIgnoreCase("walk")) {
					myDataConfig.setWalk(Double.parseDouble(words[1]));
				}
				if(words[0].equalsIgnoreCase("sneak")) {
					myDataConfig.setSneak(Double.parseDouble(words[1]));
				}
				if(words[0].equalsIgnoreCase("sprint")) {
					myDataConfig.setSprint(Double.parseDouble(words[1]));
				}
			}		
		} catch (FileNotFoundException e) {
			//So basically you're screwed here.
		} catch (IOException e) {
			//No, seriously; you're completely fucked now.
		} finally {
			try {
				if (bufferedFileReader != null) {
					bufferedFileReader.close();
				}
			} catch (IOException e) {
			}
		}

		myOutputManager.log("Walk configuration set to: " + myDataConfig.getWalk());
		myOutputManager.log("Sneak configuration set to: " + myDataConfig.getSneak());
		myOutputManager.log("Sprint configuration set to: " + myDataConfig.getSprint());
	}
	
	public void write() {
		BufferedWriter bufferedWriter = null;
		try {
			myOutputManager.log("Writing output...");
			bufferedWriter = new BufferedWriter(new FileWriter(sgDirectoryConfig, false));

			bufferedWriter.write("walk=" + myDataConfig.getWalk()+
					"\nsneak=" + myDataConfig.getSneak()+
					"\nsprint=" + myDataConfig.getSprint());
			
		} catch (FileNotFoundException e) {
			//Who cares?
		} catch (IOException e) {
			//No, seriously?
		} finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
