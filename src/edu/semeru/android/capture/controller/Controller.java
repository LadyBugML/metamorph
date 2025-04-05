/*******************************************************************************
 * Copyright (c) 2016, SEMERU
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 *******************************************************************************/
/**
 * Created by Kevin Moran on Aug 28, 2016
 */
package edu.semeru.android.capture.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.lang.reflect.Field;

import org.apache.commons.lang.SystemUtils;

import edu.semeru.android.capture.helpers.CmdProcessBuilder;

/**
 * @author KevinMoran
 *
 */
public class Controller {

    public static boolean IS_OS_UNIX = SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX;

    public static void captureUIDump(String outputFile)
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand1;
        String[] uiCommand2;
        String[] uiCommand3;

        System.out.println("Getting Device Ready...");

        if (IS_OS_UNIX) {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "mkdir", "/sdcard/uimonkeyautomator" };
            System.out.println("Capturing UI-Dump xml...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "/system/bin/uiautomator", "dump", "/sdcard/uimonkeyautomator/ui_dump.xml" };
            System.out.println("Saving UI-Dump to specified File path...");
            uiCommand3 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "pull", "/sdcard/uimonkeyautomator/ui_dump.xml", outputFile };
        } else {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "shell", "mkdir", "/sdcard/uimonkeyautomator" };
            System.out.println("Capturing UI-Dump xml...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "shell", "/system/bin/uiautomator", "dump", "/sdcard/uimonkeyautomator/ui_dump.xml" };
            System.out.println("Saving UI-Dump to specified File path...");
            uiCommand3 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "pull", "/sdcard/uimonkeyautomator/ui_dump.xml", outputFile };
        }

        String output1 = CmdProcessBuilder.executeCommand(uiCommand1);
        String output2 = CmdProcessBuilder.executeCommand(uiCommand2);
        String output3 = CmdProcessBuilder.executeCommand(uiCommand3);

    }

    public static void captureScreenshot(String outputFile)
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand1;
        String[] uiCommand2;

        System.out.println("Capturing Screenshot...");
        if (IS_OS_UNIX) {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "/system/bin/screencap", "-p", "/sdcard/screen.png" };
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "pull", "/sdcard/screen.png", outputFile };
        } else {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "shell", "/system/bin/screencap", "-p", "/sdcard/screen.png" };
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "pull", "/sdcard/screen.png", outputFile };

        }

        String output1 = CmdProcessBuilder.executeCommand(uiCommand1);
        System.out.println(output1);
        String output2 = CmdProcessBuilder.executeCommand(uiCommand2);
        System.out.println(output2);
    }

    public static Process startVideoCapture(String adbPath)
            throws InterruptedException, IOException {
        String[] uiCommand;

        System.out.println("Capturing Screenshot...");
        if (IS_OS_UNIX) {
            uiCommand = new String[] { adbPath,
                    "shell", "screenrecord", "/sdcard/video.mp4", "--bit-rate", "8000000" };
        } else {
            uiCommand = new String[] { adbPath,
                    "shell", "screenrecord", "/sdcard/video.mp4", "--bit-rate", "8000000" };

        }

        // create a new process
        System.out.println("Creating Process");

        ProcessBuilder builder = new ProcessBuilder(uiCommand);
        Process videoprocess = builder.start();

        return videoprocess;
    }
    
    public static void pullVideo(String outputFile, String adbPath)
            throws InterruptedException, IOException {
        String[] uiCommand;

        System.out.println("Capturing Screenshot...");
        if (IS_OS_UNIX) {
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand = new String[] {adbPath,
                    "pull", "/sdcard/video.mp4", outputFile };
        } else {
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand = new String[] { adbPath,
                    "pull", "/sdcard/video.mp4", outputFile };

        }

         String output = CmdProcessBuilder.executeCommand(uiCommand);
         System.out.println(output);
    }
    
    public static Process startGetEventCapture(String outputPath, String adbPath)
            throws InterruptedException, IOException {
        String[] uiCommand;

        System.out.println("Capturing getevent...");
        if (IS_OS_UNIX) {
            uiCommand = new String[] { adbPath,
                    "shell", "getevent", "-t"};
        } else {
            uiCommand = new String[] {adbPath,
                    "shell", "getevent", "-t"};

        }

        // create a new process
        System.out.println("Creating Process");
        File testoutput = new File(outputPath);
        testoutput.createNewFile();
        ProcessBuilder builder = new ProcessBuilder(uiCommand);
        builder.redirectOutput(testoutput);
        Process geteventprocess = builder.start();

        return geteventprocess;
    }

    public static String getAndroidSDKPath() {

        String androidSDKPath = "";

        if (SystemUtils.IS_OS_MAC) {
            androidSDKPath = "lib" + File.separator + "AndroidSDK-Mac";
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            androidSDKPath = "lib" + File.separator + "AndroidSDK-Windows";
        }

        if (SystemUtils.IS_OS_LINUX) {
            androidSDKPath = "lib" + File.separator + "AndroidSDK-Linux";
        }

        return androidSDKPath;

    }

    public static synchronized long getProcessPID(Process p) {
        long pid = -1;

        try {
          if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
            Field f = p.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            pid = f.getLong(p);
            f.setAccessible(false);
          }
        } catch (Exception e) {
          pid = -1;
        }
        return pid;
      }
    
    public static String getNewLocation(int x, int y, int sizeX, int sizeY) {
        double percentageX = 0.05;// 5% * 2 = 10%
        double percentageY = 0.3;
        int minX = (int) (sizeX / 2d - (sizeX * percentageX));
        int minY = (int) (sizeY * percentageY);
        int maxX = (int) (sizeX / 2d + (sizeX * percentageX));
        int maxY = sizeY - minY;
        String result = "";

        if (y <= maxY) {
            if (y <= minY) {
                result = "Top";
            } else {
                result = "Center";
            }
        } else {
            result = "Bottom";
        }

        if (x <= maxX) {
            if (x <= minX) {
                result += " left";
            } else {
                result += "";
            }
        } else {
            result += " right";
        }
        return result;
    }

    public static File createConfigFile(String androidSdkPath, String aaptPath, String apkPath, String getEventFilePath,
                                        String outputPath, int avdPort, int adbPort, int executionNum) throws IOException {
        
        String pythonScriptsPath = String.format("%s" + File.separator + "lib" + File.separator + "python-scripts" + File.separator, System.getProperty("user.dir"));
        FileWriter configFileWriter = new FileWriter(outputPath + File.separator + "config.yaml");

        String configuration = new StringBuilder()
            .append("androidSDKPath: " + androidSdkPath + "\n")
            .append("pythonScriptsPath: " + pythonScriptsPath + "\n")
            .append("aaptPath: " + aaptPath + "\n")
            .append("apkPath: " + apkPath + "\n")
            .append("getEventFile: " + getEventFilePath + "\n")
            .append("outputFolder: " + outputPath + "\n")
            .append("avdPort: " + avdPort + "\n")
            .append("adbPort: " + adbPort + "\n")
            .append("executionNum: " + executionNum + "\n")
            .toString();

        configFileWriter.write(configuration);
        configFileWriter.close();

        File configFileHandle = new File(outputPath + File.separator + "config.yaml");
        return configFileHandle;
    }
    
	public static int runJarWithConfig(File configFile, File extractedJar) {
		if (extractedJar == null) {
			System.err.println("Failed to extract JAR.");
			return 1;
		}
	
		if (configFile == null || !configFile.exists()) {
			System.err.println("Config file is missing. JAR execution aborted.");
			return 1;
		}
	
		try {
			System.out.println("Running JAR with config: " + configFile.getAbsolutePath());
			
			ProcessBuilder processBuilder = new ProcessBuilder(
				"java", "-jar", extractedJar.getAbsolutePath(), "--config", configFile.getAbsolutePath()
			);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
	
			// Capture and print JAR output
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(extractedJar.getName() + ": " + line);
				}
			}
	
			int exitCode = process.waitFor();
			System.out.println(extractedJar.getName() + " execution finished with exit code: " + exitCode);
            return 0;
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
            return 1;
		}
	}

    public static String getSDKPath() {
        String userhome = System.getProperty("user.home");
        Path sdkPath;
        if (SystemUtils.IS_OS_LINUX) {
            sdkPath = Paths.get(userhome, "Android", "Sdk");
        } else if (SystemUtils.IS_OS_MAC) {
            sdkPath = Paths.get(userhome,  "Library", "Android", "sdk");
        } else {
            sdkPath = Paths.get(userhome, "AppData", "Local", "Android", "Sdk");
        }

        if (Files.exists(sdkPath)) {
            System.out.println("Found Android SDK location at " + sdkPath.toString());
            return sdkPath.toString();
        }

        return "";
    }

    public static String getADBPath() {
        String sdkPath = getSDKPath();
        if (sdkPath.length() == 0) {
            return "";
        }

        Path adbPath;
        if (IS_OS_UNIX)
            adbPath = Paths.get(sdkPath, "platform-tools", "adb");
        else
            adbPath = Paths.get(sdkPath, "platform-tools", "adb.exe");
        
        if (Files.exists(adbPath)) {
            return adbPath.toString();
        }

        return "";
    }

    public static String getAAPTPath() {
        String sdkPath = getSDKPath();
        if (sdkPath.length() == 0) {
            return "";
        }

        Path aaptPath;
        if (IS_OS_UNIX)
            aaptPath = Paths.get(sdkPath, "build-tools");
        else
            aaptPath = Paths.get(sdkPath, "build-tools");
        
        try (Stream<Path> dirs = Files.walk(aaptPath, 1)) {
            Object[] versionPaths = dirs.filter(Files::isDirectory)
                .filter(path -> !path.equals(aaptPath))
                .filter(path -> path.getFileName().toString().contains("."))
                .map(Path::toString)
                .sorted()
                .toArray();
            
            if (versionPaths.length > 0) {
                String newest = (String)versionPaths[versionPaths.length-1];
                return newest;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}