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
class ImageBuffered extends Applet
{
	private MediaTracker 		tracker ;
	private PixelGrabber 		pg ;
	private MemoryImageSource 	mem ;
	private Image 				img ;
	private int					pixels[] ;
	private int					R , G , B , alpha , nb ;
	private int 				X, Y , contexte ;
	//=======================================================================================================================
	// constructeur permettant d'utiliser une image vierge (image de synthèse, etc..)
		
	ImageBuffered(int X, int Y)
	{
		this.X	= X ;
 		this.Y	= Y ;
 		
		pixels 	= new int[X*Y];
		
		for(int i=0;i<X;i++)
		  for(int j=0;j<Y;j++)	
			pixels[i+j*X] = 0 ;
			
		mem = new MemoryImageSource(X, Y, pixels, 0, X) ;
	}
	//=======================================================================================================================
	// constructeur permettant d'ouvrir une image préexistante (*.jpg, etc ...) dont le chemin est ap.getCodeBase
	
	ImageBuffered(String path,Applet ap)
	{
		img = ap.getImage(ap.getCodeBase(), path );
		tracker = new MediaTracker(this) ;
 		tracker.addImage(img, 0) ;
 		try { tracker.waitForID(0); } catch (InterruptedException e ) {}
 			
 		X	=	img.getWidth(this);
 		Y	=	img.getHeight(this);
 		
		pixels 	= new int[X*Y];

		pg 		= new PixelGrabber(img, 0, 0, X, Y, pixels, 0, X);
 		try {pg.grabPixels();} catch (InterruptedException e) {}
 		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			alpha 	= ( pixels[i+j*X] >> 24 ) & 0xff       ;
			R 	 	= (( pixels[i+j*X] >> 16 ) & 0xff  )  ;
			G 	 	= (( pixels[i+j*X] >> 8 ) & 0xff   )  ;
			B 	 	=  ( pixels[i+j*X] & 0xff         )   ;
			nb = (R+G+B)/3 ;
			
			pixels[i+j*X] = ( alpha << 24 ) | ( R << 16 ) | ( G << 8 ) | B  ;
		}
		img = createImage(new MemoryImageSource(X,Y,pixels,0,X));
	}
	//=======================================================================================================================
	// effet visuel générique sur le contenu buffer d'une image
	
	public 	void	Effet(Graphics g)
	{
		double 	val ;
		int		x, y;
		
		alpha = 255 ;
		
		for(int k=0;k<X;k++)
		{
			System.out.println("k = "+k) ; 
			
		  	for(int i=0;i<X;i++)
		  	for(int j=0;j<Y;j++)
		 	{	
		 		val = Math.sqrt( (X/2-i)*(X/2-i) + (Y/2-j)*(Y/2-j) ) * (double)k   ;
		 		y 	= (int)(val / 256.0 ) ;
		 		x 	= (int)val - y * 256 ;
		 		
				pixels[i+j*X] = ( alpha << 24 ) | ( k << 16 ) | ( k << 8 ) | 0 ;
		 	}
		 	
		 	mem.newPixels() ;
			img = createImage(mem);
			
			g.drawImage(img,0,0,this);
			//Afficher(g) ;
		}
	}
	//=======================================================================================================================
	// effet visuel permettant de voir la vitesse de défilement en fonction de la taille de l'image

	public 	void	Effet2(Graphics g)
	{
		double 	val ;
		int		x, y;
		
		alpha = 255 ;
		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		 	pixels[i+j*X] = ( alpha << 24 ) | ( (int)(i/2) << 16 ) | ( (int)(j/2) << 8 ) | 0 ;
		
		mem.newPixels() ;
		img = createImage(mem);
		
		for(int k=20;k<200;k++)
		{
			System.out.println("k = "+k) ; 
			
			mem.newPixels() ;
			img = createImage(mem);
			
		  	g.drawImage(img,10,10,this);
		  	g.setColor(new Color(255,255,200)) ;
		  	g.fillOval(k,k,20,20) ;
		}
	}
	//=======================================================================================================================
	public 	void	setPixel(int x,int y,int Rr,int Gg,int Bb,int echelle)
	{
		try
		{
			for(int i=0;i<echelle;i++)
			for(int j=0;j<echelle;j++)
				pixels[(x*echelle+i)+(y*echelle+j)*X] = ( 255 << 24 ) | ( Rr << 16 ) | ( Gg << 8 ) | Bb ;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e) {}
	}
	//=======================================================================================================================
	public 	void	setPixel(int x,int y,int NB,int echelle)
	{
		try
		{
			for(int i=0;i<echelle;i++)
			for(int j=0;j<echelle;j++)
				pixels[(x*echelle+i)+(y*echelle+j)*X] = ( 255 << 24 ) | ( NB << 16 ) | ( NB << 8 ) | NB ;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e) {}
	}
	//=======================================================================================================================
	public 	void	Afficher(Graphics g)
	{
		mem.newPixels() ;
		img = createImage(mem);
		g.drawImage(img,0,0,this);
	}
	//=======================================================================================================================
}
//###################################################################################################################################