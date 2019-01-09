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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.lang.SystemUtils;

import edu.semeru.android.capture.helpers.CmdProcessBuilder;

/**
 * @author KevinMoran
 *
 */
public class Controller {

    public static void captureUIDump(String outputFile)
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand1;
        String[] uiCommand2;
        String[] uiCommand3;

        System.out.println("Getting Device Ready...");

        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
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
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
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

    public static Process startVideoCapture()
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand1;

        System.out.println("Capturing Screenshot...");
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "screenrecord", "/sdcard/video.mp4" };
        } else {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "shell", "screenrecord", "/sdcard/video.mp4" };

        }

        // create a new process
        System.out.println("Creating Process");

        ProcessBuilder builder = new ProcessBuilder(uiCommand1);
        Process videoprocess = builder.start();

        return videoprocess;
        
        // String output1 = CmdProcessBuilder.executeCommand(uiCommand1);
        // System.out.println(output1);
        // String output2 = CmdProcessBuilder.executeCommand(uiCommand2);
        // System.out.println(output2);
    }
    
    public static void pullVideo(String outputFile)
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand2;

        System.out.println("Capturing Screenshot...");
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "pull", "/sdcard/video.mp4", outputFile };
        } else {
            System.out.println("Saving Screenshot to specified File path...");
            uiCommand2 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb.exe",
                    "pull", "/sdcard/video.mp4", outputFile };

        }

        
        // String output1 = CmdProcessBuilder.executeCommand(uiCommand1);
        // System.out.println(output1);
         String output2 = CmdProcessBuilder.executeCommand(uiCommand2);
         System.out.println(output2);
    }
    
    public static Process startGetEventCapture(String outputPath)
            throws InterruptedException, IOException {

        String androidSDKPath = getAndroidSDKPath();
        
        String[] uiCommand1;

        System.out.println("Capturing getevent...");
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "getevent"};
        } else {
            uiCommand1 = new String[] { androidSDKPath + File.separator + "platform-tools" + File.separator + "adb",
                    "shell", "getevent"};

        }

        // create a new process
        System.out.println("Creating Process");

        File testoutput = new File(outputPath);
        testoutput.createNewFile();
        ProcessBuilder builder = new ProcessBuilder(uiCommand1);
        builder.redirectOutput(testoutput);
        Process geteventprocess = builder.start();

        return geteventprocess;
        
        // String output1 = CmdProcessBuilder.executeCommand(uiCommand1);
        // System.out.println(output1);
        // String output2 = CmdProcessBuilder.executeCommand(uiCommand2);
        // System.out.println(output2);
    }

    public static String getAndroidSDKPath() {

        String androidSDKPath = "";

        if (SystemUtils.IS_OS_MAC) {
            androidSDKPath = "libs" + File.separator + "AndroidSDK-Mac";
        }

        if (SystemUtils.IS_OS_WINDOWS) {
            androidSDKPath = "libs" + File.separator + "AndroidSDK-Windows";
        }

        if (SystemUtils.IS_OS_LINUX) {
            androidSDKPath = "libs" + File.separator + "AndroidSDK-Linux";
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

}
