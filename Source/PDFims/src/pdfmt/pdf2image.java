/*  This file is part of PDFCleaner.
 *  The class displays the help screen.
 *
 *  Copyright (C) 2012  Sourabh Gandhe
 *                      
 *	
 *  PDFCleaner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PDFCleaner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PDFCleaner. Check for file named COPYING.
 *  If not, see <http://www.gnu.org/licenses/>.
*/


package pdfmt;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageOutputStream;
import javax.media.jai.PlanarImage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.sun.media.imageio.plugins.tiff.BaselineTIFFTagSet;
import com.sun.media.imageio.plugins.tiff.TIFFDirectory;
import com.sun.media.imageio.plugins.tiff.TIFFField;
import com.sun.media.imageio.plugins.tiff.TIFFTag;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

public class pdf2image { 
	 public static final int FAILURE = 2;
	    public static final int BLANK = 0;
	    public static final int NOTBLANK = 1;
	    // value where we can consider that this is a blank image
	    // can be much higher depending of the TIF source 
	    // (ex. scanner or fax)
	    public static final int BLANK_THRESHOLD = 1000;

	private static final char[] INCH_RESOLUTION_UNIT = new char[] {2}; 
	private static final long[][] X_DPI_RESOLUTION = new long[][] {{150, 1}}; 
	private static final long[][] Y_DPI_RESOLUTION = new long[][] {{150, 1}}; 
	private static final char[] BITS_PER_SAMPLE = new char[] {1}; 
	private static final char[] COMPRESSION = new char[] {BaselineTIFFTagSet.COMPRESSION_LZW}; 
	private static final int HEIGHT = 1650; 

	/** 
	 * ::Constructor() 
	 */ 
	pdf2image() { 

	} 

	/** 
	 * Convert a PDF document to a TIF file 
	 */ 
	public boolean convertPDF(String pdf, String destPdf) { 
		try { 
			convert(pdf,"C://test//3.tiff",destPdf); 
			return true; 
		} catch (IOException e) { 
			return false; 
		} 
	} 


	/** 
	 * Convert a PDF document to a TIF file 
	 */ 
	protected static void convert(String pdf, String tif, String destPdf) throws IOException { 

	    org.icepdf.core.pobjects.Document pdffile = new org.icepdf.core.pobjects.Document();
	    
	    try { 
	        pdffile.setFile(pdf); 
	        
	    } catch (PDFException ex) { 
	      //  System.out.println("Error parsing PDF document " + ex); 
	    } catch (PDFSecurityException ex) { 
	      //  System.out.println("Error encryption not supported " + ex); 
	    } catch (FileNotFoundException ex) { 
	       // System.out.println("Error file not found " + ex); 
	    } catch (IOException ex) { 
	      //  System.out.println("Error handling PDF document " + ex); 
	    } 

	    int numPgs = pdffile.getNumberOfPages(); 
	    
	    try {
	        // step 1: create new reader
	        PdfReader r = new PdfReader(pdf);
	      //  System.out.println("File Lenght:"  + r.getFileLength());
	        RandomAccessFileOrArray raf = new RandomAccessFileOrArray(
	                pdf);
	       // System.out.println("Raf:" + raf);
	         Document document = new Document(r.getPageSizeWithRotation(1));
//	        // step 2: create a writer that listens to the document
	        PdfCopy writer = new PdfCopy(document, new FileOutputStream(destPdf));
//	        
//	        // step 3: we open the document
	        document.open();
//	        // step 4: we add content
	          PdfImportedPage page = null;
//	     
	        //loop through each page and if the bs is larger than 20 than we know it is not blank.
	        //if it is less than 20 than we don't include that blank page.
	        
	    
	    float scale = 2.084f; 
	    float rotation = 0f; 
	   
	    BufferedImage image[] = new BufferedImage[numPgs]; 

	    for (int i = 0; i < numPgs; i++) { 
	        
	        byte bContent[] = r.getPageContent(i+1, raf);
	       // System.out.println(bContent.toString());
	        
	        
	        
	        ByteArrayOutputStream bs = new ByteArrayOutputStream();
	        //write the content to an output stream
	        bs.write(bContent);
	         
	        //System.out.println("page content length of page " + i+1 + " = "
	          //      + bs.size());

	        /* 
	         * Generate the image: 
	         * Notes: 1275x1650 = 8.5 x 11 @ 150dpi ??? 
	         */ 
	        image[i] = (BufferedImage)pdffile.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
	        Iterator writers = ImageIO.getImageWritersByFormatName("TIFF"); 
	        if (writers == null || !writers.hasNext()) { 
	            throw new RuntimeException("No writers for available."); 
	            
	        } 
	        ImageWriter myWriter = (ImageWriter) writers.next(); 
	        myWriter.setOutput(new FileImageOutputStream(new File(tif))); 
	        myWriter.prepareWriteSequence(null); 
	        ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(image[i]); 
	        IIOMetadata imageMetadata = myWriter.getDefaultImageMetadata(imageType, null); 
	        imageMetadata = createImageMetadata(imageMetadata); 
	        myWriter.writeToSequence(new IIOImage(image[i], null, imageMetadata), null); 
	        
	        myWriter.dispose(); 
	        image[i]=null;
	        myWriter = null;
	        
	        FileInputStream in = new FileInputStream(tif);
	          FileChannel channel = in.getChannel();
	          ByteBuffer buffer = ByteBuffer.allocate((int)channel.size());
	          channel.read(buffer);
	          Image imageBlank;
	    
	               imageBlank = load(buffer.array());
	        
	               BufferedImage bufferedImage = imageToBufferedImage(imageBlank);
	               boolean isBlank;
	               isBlank = isBlank(bufferedImage);
	              // System.out.println("isblank "+ isBlank);
	               
	               if(isBlank==false){
	                   
	                   
	                
	                    page = writer.getImportedPage(r, i+1);
	                    writer.addPage(page);
	                    
	                
	               }
	               bs.close();
	             
	              System.gc();
	    }
	  
    	
	    document.close();
	    writer.close();
	    raf.close();
	    r.close();

	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        
	        
	        
	        

	    


	}

	 public static Image load(byte[] data) throws Exception {
	        Image image = null;
	        SeekableStream stream = new ByteArraySeekableStream(data);
	        String[] names = ImageCodec.getDecoderNames(stream);
	        ImageDecoder dec = 
	          ImageCodec.createImageDecoder(names[0], stream, null);
	        RenderedImage im = dec.decodeAsRenderedImage();
	        image = PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
	        // scale-down the image , maximum width : 500 px
	        // to preserve memory
	        Image imageScaled = 
	            image.getScaledInstance(500, -1,  Image.SCALE_SMOOTH);
	        return imageScaled;
	      }
	 
	 public static BufferedImage imageToBufferedImage(Image im) {
	        BufferedImage bi = new BufferedImage
	           (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
	        Graphics bg = bi.getGraphics();
	        bg.drawImage(im, 0, 0, null);
	        bg.dispose();
	        bg = null;
	        return bi;
	     }
	 
	 public static boolean isBlank(BufferedImage bi) throws Exception {
	        long count = 0;
	        long total = 0;
	        double totalVariance = 0;
	        double stdDev = 0;
	        int height = bi.getHeight();
	        int width = bi.getWidth();
	        
	        int[] pixels = new int[width * height];
	        PixelGrabber pg = new PixelGrabber
	            (bi, 0, 0, width, height, pixels, 0, width);
	        pg.grabPixels();
	        for (int j = 0; j < height; j++) {
	           for (int i = 0; i < width; i++) {
	              count++;
	              int pixel = pixels[j * width + i];
	              int red = (pixel >> 16) & 0xff;
	              int green = (pixel >> 8) & 0xff;
	              int blue = (pixel) & 0xff;
	              int pixelValue = new Color(red, green,blue,0).getRGB();
	              total += pixelValue;
	              double avg = total /count;
	              totalVariance += Math.pow(pixelValue - avg, 2);
	              stdDev = Math.sqrt(totalVariance / count);
	           }
	        }
	        return (stdDev < BLANK_THRESHOLD);
	    }

	/** 
	 * Save tiff 
	 */ 
//	@SuppressWarnings("unchecked") 
//	private static void save(BufferedImage[] b, String tif) throws IOException { 
//
//		//Get a TIFF writer and set its output. 
//		Iterator writers = ImageIO.getImageWritersByFormatName("TIFF"); 
//
//		if (writers == null || !writers.hasNext()) { 
//			throw new RuntimeException("No writers for available."); 
//		} 
//
//		ImageWriter writer = (ImageWriter) writers.next(); 
//		writer.setOutput(new FileImageOutputStream(new File(tif))); 
//		writer.prepareWriteSequence(null); 
//
//		for (int i = 0; i < b.length; i++) { 
//			ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(b[i]); 
//			IIOMetadata imageMetadata = writer.getDefaultImageMetadata(imageType, null); 
//			imageMetadata = createImageMetadata(imageMetadata); 
//			writer.writeToSequence(new IIOImage(b[i], null, imageMetadata), null); 
//		} 
//
//		writer.dispose(); 
//		writer = null; 
//		
//
//	}	

	/** 
	 * Return the metadata for the new TIF image 
	 */ 
	private static IIOMetadata createImageMetadata(IIOMetadata imageMetadata) throws IIOInvalidTreeException { 

		//Get the IFD (Image File Directory) which is the root of all the tags 
		//for this image. From here we can get all the tags in the image. 
		TIFFDirectory ifd = TIFFDirectory.createFromMetadata(imageMetadata); 

		//Create the necessary TIFF tags that we want to add to the image metadata 
		BaselineTIFFTagSet base = BaselineTIFFTagSet.getInstance(); 

		//Resolution tags... 
		TIFFTag tagResUnit = base.getTag(BaselineTIFFTagSet.TAG_RESOLUTION_UNIT); 
		TIFFTag tagXRes = base.getTag(BaselineTIFFTagSet.TAG_X_RESOLUTION); 
		TIFFTag tagYRes = base.getTag(BaselineTIFFTagSet.TAG_Y_RESOLUTION); 

		//BitsPerSample tag 
		TIFFTag tagBitSample = base.getTag(BaselineTIFFTagSet.TAG_BITS_PER_SAMPLE); 

		//Row and Strip tags... 
		TIFFTag tagRowStrips = base.getTag(BaselineTIFFTagSet.TAG_ROWS_PER_STRIP); 

		//Compression tag 
		TIFFTag tagCompression = base.getTag(BaselineTIFFTagSet.TAG_COMPRESSION); 

		//Set the tag values 
		TIFFField fieldResUnit = new TIFFField(tagResUnit, TIFFTag.TIFF_SHORT, 1, INCH_RESOLUTION_UNIT); 
		TIFFField fieldXRes = new TIFFField(tagXRes, TIFFTag.TIFF_RATIONAL, 1, X_DPI_RESOLUTION); 
		TIFFField fieldYRes = new TIFFField(tagYRes, TIFFTag.TIFF_RATIONAL, 1, Y_DPI_RESOLUTION); 
		TIFFField fieldBitSample = new TIFFField(tagBitSample, TIFFTag.TIFF_SHORT, 1, BITS_PER_SAMPLE); 
		TIFFField fieldRowStrips = new TIFFField(tagRowStrips, TIFFTag.TIFF_LONG, 1, new long[] {HEIGHT}); 
		TIFFField fieldCompression = new TIFFField(tagCompression, TIFFTag.TIFF_SHORT, 1, COMPRESSION); 

		//Cleanup the fields 
		//ifd.removeTIFFFields(); 

		//Add the new tag/value sets to the image metadata 
		ifd.addTIFFField(fieldResUnit); 
		ifd.addTIFFField(fieldXRes); 
		ifd.addTIFFField(fieldYRes); 
		ifd.addTIFFField(fieldBitSample); 
		ifd.addTIFFField(fieldRowStrips); 
		ifd.addTIFFField(fieldCompression); 

		return ifd.getAsMetadata(); 

	} 
	public static void main(String[] args) {
		//convertPDF("C://3.pdf", "C://3.tiff");
	
	}
} 