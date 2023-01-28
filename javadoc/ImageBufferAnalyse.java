import java.awt.MediaTracker ;
import java.awt.Image ;
import java.awt.image.PixelGrabber ;
import java.awt.image.MemoryImageSource ;

import javax.imageio.ImageIO;

//###################################################################################################################################

public class ImageBufferAnalyse extends ImageBuffer
{
	private MediaTracker 		tracker ;
	private PixelGrabber 		pg ;
	private Image 				img  ;
	//=======================================================================================================================
	ImageBufferAnalyse(String path) {

		// img = ap.getImage(ap.getCodeBase(), path );
		img = ImageIO.read(new File(path)) ;
		
		tracker = new MediaTracker(this) ;
 		tracker.addImage(img, 0) ;
 		try { tracker.waitForID(0); } catch (InterruptedException e ) {}
 			
 		X	=	img.getWidth(this);
 		Y	=	img.getHeight(this);
 		
 		pixels = new int[X*Y];

		pg 		= new PixelGrabber(img, 0, 0, X, Y, pixels, 0, X);
 		try {pg.grabPixels();} catch (InterruptedException e) {}
 		
		mem = new MemoryImageSource(X, Y, pixels, 0, X) ;
		img = createImage(mem) ;
	}
	//=======================================================================================================================
}
//###################################################################################################################################