import java.awt.Color ;
import java.awt.Font ;
import java.awt.Image;
import java.util.Stack ;
import java.awt.Graphics ;
import java.applet.Applet;
import java.lang.String ;
import java.awt.image.*;
import java.awt.*;
//###################################################################################################################################
class ImageMedicale extends Applet
{
	private Image 			img ;
	private int				im[][] ;
	private int 			X=0, Y=0 , contexte ;
	private MediaTracker 	tracker ;
	private PixelGrabber 	pg ;
	
	//=======================================================================================================================
	ImageMedicale(String path,Applet ap)
	{
		int 	pixels[] , R , G , B ;
		
		img = ap.getImage(ap.getCodeBase(), path );
		tracker = new MediaTracker(this) ;
 		tracker.addImage(img, 0) ;
 		try { tracker.waitForID(0); } catch (InterruptedException e ) {}
 			
 		X	=	img.getWidth(this);
 		Y	=	img.getHeight(this);
 		
		pixels 	= new int[X*Y];
		im  	= new int[X][Y] ;

		pg 		= new PixelGrabber(img, 0, 0, X, Y, pixels, 0, X);
 		try {pg.grabPixels();} catch (InterruptedException e) {}
 		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			R = (pixels[i+j*X] >> 16) & 0xff ;
			G = (pixels[i+j*X] >>  8) & 0xff;
			B = (pixels[i+j*X] ) & 0xff;
			im[i][j] = (int)((int)( R + G + B ) / 3) ;	
		}
	}
	//=======================================================================================================================
	public 	void	Afficher(Graphics g)
	{
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{
			g.setColor(new Color(im[i][j],im[i][j],im[i][j])) ;
			g.fillRect(i,j,1,1) ;
		}
	}
	//=======================================================================================================================
}
//###################################################################################################################################