# Android-Video-and-Trace-Recorder (AVTR)

![AVTR Screenshot](/resources/ui.png)

## Overview

This is a small GUI-based tool for capturing screen recordings and getevent traces from an Android device that is connected to a PC or Mac. It's written in Java Swing and includes the nessecary Andorid SDK component bundled in to perform the recording process.

## Installation

*  Simply import the Java project into Eclipse and run the `MainScreen.java` class.
*  You can also use the pre-packaged applications from the [CI Builds](https://gitlab.com/SEMERU-Code/Android/Android-Video-and-Trace-Recorder/-/artifacts/master/download?job=BuildAVTR).

## Features

*  Plug and Play: Simply plug in an Android device to your computer running the AVTR and click capture to start the recording process.
*  Capable of recording 3:00 mins of video and getevent actions (Android limitation)
*  Automatically saves two files to the specified output path that represent the video and trace file.

## Known Bugs and Limitations

*  Currently there is non-deterministic bug where the getevent trace that gets captured is inomplete or missing. (We are working on solving this problem now)

