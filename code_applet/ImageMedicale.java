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
	private Image 				img ;
	private int					pixels[] , R , G , B , alpha ;
	private int 				X, Y , contexte ;
	private MediaTracker 		tracker ;
	private PixelGrabber 		pg ;
	private MemoryImageSource 	mem ;
	//=======================================================================================================================
	ImageMedicale(int X, int Y)
	{
		this.X	= X ;
 		this.Y	= Y ;
 		
		pixels 	= new int[X*Y];
		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)	
			pixels[i+j*X] = 0 ;
	}
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
 		
		for(i=0;i<X;i++)
		for(j=0;j<Y;j++)
		{	
			alpha 	= ( pixels[i+j*X] >> 24 ) & 0xff       ;
			R 	 	= (( pixels[i+j*X] >> 16 ) & 0xff  ) + 1 ;
			G 	 	= (( pixels[i+j*X] >> 8 ) & 0xff   ) + 1 ;
			B 	 	=  ( pixels[i+j*X] & 0xff         )  + 1 ;
			nb = (R+G+B)/3 ;
			
			pixels[i+j*X] = ( alpha << 24 ) | ( R << 16 ) | ( G << 8 ) | B  ;
		}
		img = createImage(new MemoryImageSource(X,Y,pixels,0,X));
	}
	//=======================================================================================================================
	public 	void	Afficher(Graphics g)
	{
		g.drawImage(img,0,0,this);
	}
	//=======================================================================================================================
	public 	void	AfficherBuffer(Graphics g)
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