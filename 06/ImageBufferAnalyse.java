import java.awt.MediaTracker ;
import java.awt.Image ;
import java.awt.image.PixelGrabber ;
import java.awt.image.MemoryImageSource ;

//###################################################################################################################################

class ImageBufferAnalyse extends ImageBuffer
{
	private MediaTracker 		tracker ;
	private PixelGrabber 		pg ;
	private Image 				img  ;
	//=======================================================================================================================
	ImageBufferAnalyse(String path,Applet ap, int echelle)
	{
		this.echelle = echelle; 
		
		img = ap.getImage(ap.getCodeBase(), path );
		tracker = new MediaTracker(this) ;
 		tracker.addImage(img, 0) ;
 		try { tracker.waitForID(0); } catch (InterruptedException e ) {}
 			
 		X	=	img.getWidth(this);
 		Y	=	img.getHeight(this);
 		
 		pixels2 	= new int[X*Y];
		pixels = new int[X*Y*echelle*echelle];

		pg 		= new PixelGrabber(img, 0, 0, X, Y, pixels2, 0, X);
 		try {pg.grabPixels();} catch (InterruptedException e) {}
 		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			for(int m=0;m<echelle;m++)
			for(int n=0;n<echelle;n++)
				pixels[ i*echelle+m + (j*echelle+n)*echelle*X  ] = pixels[i+j*X] ;
		}
		mem = new MemoryImageSource(X*echelle, Y*echelle, pixels, 0, X*echelle) ;
		img = createImage(mem) ;
	}
	//=======================================================================================================================
}
//###################################################################################################################################