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
 * Created by Kevin Moran on Dec 29, 2016
 */
package edu.semeru.android.capture.helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

import org.apache.commons.lang.SystemUtils;

/**
 * @author KevinMoran
 *
 */
public class PIDHelper {

	/**
	 * @author KevinMoran
	 * Description: This method takes as input two .png images and outputs the difference image using
	 * Perceptual Image Differencing to the desired location.  The difference image will contain black
	 * pixels for areas that are similar and red pixels for areas that are the same.
	 * 
	 * @param appScreenshotPath: Path to the screenshot from the app implementation.
	 * @param mockUpImagePath: Path to a static image of a design mock-up screen.
	 * @param outputPath: The desired output path for the difference image (Must end in .png).
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void generateDifferenceImage(String appScreenshotPath, String mockUpImagePath, String outputPath) throws InterruptedException, IOException{

		if(SystemUtils.IS_OS_MAC){ // Check to see if GVT is being run on a Mac

			System.out.println("Running Perceptual Image Differencing for Mac...");
			String[] pidMacCommand = {"./libs" + File.separator + "pid-mac" + File.separator + "perceptualdiff", appScreenshotPath, mockUpImagePath, "-output", outputPath};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}//End check for Mac OS

		if(SystemUtils.IS_OS_WINDOWS){ // Check to see if GVT is being run on Windows.

			System.out.println("Running Perceptual Image Differencing for Windows...");
			String[] pidMacCommand = {"libs" + File.separator + "pid-windows" + File.separator + "perceptualdiff.exe", appScreenshotPath, mockUpImagePath, "-output", outputPath};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}// End check for Windows

		if(SystemUtils.IS_OS_LINUX){ //Check to see if GVT is being run on Linux.

			System.out.println("Running Perceptual Image Differencing for Linux...");
			String[] pidMacCommand = {"libs" + File.separator + "pid-linux" + File.separator + "perceptualdiff", appScreenshotPath, mockUpImagePath, "-output", outputPath};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}// End check for Linux

	}// End generateDifferenceImage() method

	/**
	 * @author KevinMoran
	 * Description: This method takes as input two .png images and outputs the difference image using
	 * Perceptual Image Differencing to the desired location.  The difference image will contain black
	 * pixels for areas that are similar and red pixels for areas that are the same.
	 * 
	 * @param appScreenshotPath: Path to the screenshot from the app implementation.
	 * @param mockUpImagePath: Path to a static image of a design mock-up screen.
	 * @param outputPath: The desired output path for the difference image (Must end in .png).
	 * @throws InterruptedException
	 * @throws IOException
	 */
	
	public static void generateDifferenceImageBW(String imageOne, String imageTwo, String outputPath, String outputFile) throws InterruptedException, IOException{

		BufferedImage designBI = null;
		BufferedImage implemBI = null;
		try {
			designBI = ImageIO.read(new File(imageOne));
			implemBI = ImageIO.read(new File(imageTwo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Make sure images are the same size
		
		  BufferedImage blackAndWhiteImg1 = new BufferedImage(
			        designBI.getWidth(), designBI.getHeight(),
			        BufferedImage.TYPE_BYTE_BINARY);
		  
		  BufferedImage blackAndWhiteImg2 = new BufferedImage(
			        designBI.getWidth(), designBI.getHeight(),
			        BufferedImage.TYPE_BYTE_BINARY);
		
		  Graphics2D g2d = blackAndWhiteImg1.createGraphics();
		  g2d.drawImage(designBI, 0, 0, null);
		  
		  Graphics2D g2d2 = blackAndWhiteImg2.createGraphics();
		  g2d2.drawImage(implemBI, 0, 0, null);
		  
		  String designFile = imageOne.substring(imageOne.lastIndexOf("/")+1, imageOne.lastIndexOf(".")-1);
		  String implemFile = imageTwo.substring(imageTwo.lastIndexOf("/")+1, imageTwo.lastIndexOf(".")-1);
		  
		  designFile = outputPath + File.separator + "implement" + File.separator + designFile + "-bw.png";
		  implemFile = outputPath + File.separator + "design" + File.separator + implemFile + "-bw.png";
		  
		  try {
			ImageIO.write(blackAndWhiteImg1, "png", new File(designFile));
			  ImageIO.write(blackAndWhiteImg2, "png", new File(implemFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(SystemUtils.IS_OS_MAC){ // Check to see if GVT is being run on a Mac

			System.out.println("Running Perceptual Image Differencing for Mac...");
			String[] pidMacCommand = {"./libs" + File.separator + "pid-mac" + File.separator + "perceptualdiff", designFile, implemFile, "-output", outputFile};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}//End check for Mac OS

		if(SystemUtils.IS_OS_WINDOWS){ // Check to see if GVT is being run on Windows.

			System.out.println("Running Perceptual Image Differencing for Windows...");
			String[] pidMacCommand = {"libs" + File.separator + "pid-windows" + File.separator + "perceptualdiff.exe", designFile, implemFile, "-output", outputFile};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}// End check for Windows

		if(SystemUtils.IS_OS_LINUX){ //Check to see if GVT is being run on Linux.

			System.out.println("Running Perceptual Image Differencing for Linux...");
			String[] pidMacCommand = {"libs" + File.separator + "pid-linux" + File.separator + "perceptualdiff", designFile, implemFile, "-output", outputFile};

			String outputMac = CmdProcessBuilder.executeCommand(pidMacCommand);

			if(outputMac.contains("Image dimensions do not match")){ // If PID fails stop execution and print error message.

				throw new IllegalArgumentException("Images must have exact same pixel dimensions!");

			}// End check for failed PID

		}// End check for Linux

	}// End generateDifferenceImage() method
	
	/**
	 * @author KevinMoran
	 * Description: This method takes as input a difference image, and an area of that difference image and outputs
	 * the percentage of pixels that are different for the difference image for the specified area. *IMPORTANT: 
	 * The area to be analyzed must be within the image bounds. For example if the image size is 1440 in the x
	 * direction, the maximum value is 1439, as the range is from 0-1439 pixels.
	 * 
	 * @param differneceImagePath - Full path to the difference created using PID.
	 * @param x - the top left x coordinate of the bounding box to be analyzed.
	 * @param y - the top y coordinate of the bounding box to be analyzed.
	 * @param height - The height of the bounding box to be analyzed.
	 * @param width - The width of the bounding box to be analyzed.
	 * @return This method returns a float with percentage value of the difference.
	 * @throws IOException
	 */
	public static float getPixelDifference(String differneceImagePath, int x, int y, int height, int width) throws IOException{

		// Set up float to hold difference percentage, rgb values, and two arrays to hold the sets of pixels
		// that are similar and different.
		float percentDiff = 0;
		int rgb = 0;
		List<String> similarPixels = new ArrayList<String>();
		List<String> differencePixels = new ArrayList<String>();

		//Read in the Image

		BufferedImage differenceImg = null;
		differenceImg = ImageIO.read(new File(differneceImagePath));

		// Iterate through the image from the top left hand corner to
		// bottom left hand corner following a column-major order.

		for(int xCursor = x; xCursor < width+x; xCursor++){
			for(int yCursor = y; yCursor < height+y; yCursor++){
				// Get the RGB value.  The PID image will have pure RED pixels
				// values for all pixels that are different, and pure black for
				// all those that are similar.
				//System.out.println("x: " + xCursor + " y: " + yCursor);
				rgb = differenceImg.getRGB(xCursor, yCursor);
				if(rgb == Color.red.getRGB() || rgb == Color.blue.getRGB()){
					differencePixels.add(xCursor+","+yCursor);
				}else{
					similarPixels.add(xCursor+","+yCursor);
				}// End conditional for checking whether or not the pixels are different (e.g. Red)

			}// End loop for iterating through y values.
		}// End loop for iterating through x values.

		// Get total number of pixels
		int totalNumPixels = similarPixels.size() + differencePixels.size();

		// Calculate the total percentage of pixels that are different.
		percentDiff = (differencePixels.size() * 100.0f) / totalNumPixels; 

		return percentDiff;

	}// End get PixelDifference() 

}
