import java.awt.Color ;
import java.awt.Graphics ;
import java.awt.MediaTracker ;
import java.awt.Image ;
import java.awt.image.PixelGrabber ;
import java.awt.image.MemoryImageSource ;
import java.applet.Applet;
import java.lang.String ;

//###################################################################################################################################

class ImageBuffer extends Applet
{
	private MediaTracker 		tracker ;
	private PixelGrabber 		pg ;
	private MemoryImageSource 	mem ;
	private Image 				img  ;
	private int					pixels[] , pixels2[] ;
	private int					R , G , B , alpha , nb , echelle ;
	private int 				X, Y , contexte ;
	//=======================================================================================================================
	ImageBuffer(int X, int Y)
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
	ImageBuffered(String path,Applet ap, int echelle)
	{
		this.echelle = echelle; 
		
		img = ap.getImage(ap.getCodeBase(), path );
		tracker = new MediaTracker(this) ;
 		tracker.addImage(img, 0) ;
 		try { tracker.waitForID(0); } catch (InterruptedException e ) {}
 			
 		X	=	img.getWidth(this);
 		Y	=	img.getHeight(this);
 		
 		pixels 	= new int[X*Y];
		pixels2 = new int[X*Y*echelle*echelle];

		pg 		= new PixelGrabber(img, 0, 0, X, Y, pixels, 0, X);
 		try {pg.grabPixels();} catch (InterruptedException e) {}
 		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			for(int m=0;m<echelle;m++)
			for(int n=0;n<echelle;n++)
				pixels2[ i*echelle+m + (j*echelle+n)*echelle*X  ] = pixels[i+j*X] ;
		}
		mem = new MemoryImageSource(X*echelle, Y*echelle, pixels2, 0, X*echelle) ;
		img = createImage(mem) ;
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
	public 	void	setPixel(int x,int y,int Rr,int Gg,int Bb)
	{
		try {
			pixels[x+y*X] = ( 255 << 24 ) | ( Rr << 16 ) | ( Gg << 8 ) | Bb ;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e) {}
	}
	//=======================================================================================================================
	public 	void	setPixel(int x,int y,int NB)
	{
		try
		{
			pixels[x+y*X] = ( 255 << 24 ) | ( NB << 16 ) | ( NB << 8 ) | NB ;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e) {}
	}
	//=======================================================================================================================
	public 	int	getX()
	{
		return X ;
	}
	//=======================================================================================================================
	public 	int	getY()
	{
		return Y ;
	}
	//=======================================================================================================================
	public 	int[][]	getBuffer()
	{
		int t[][] ;
		t = new int[X][Y] ;
		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		 	t[i][j] = pixels[i+j*X] ;
		 	
		return t ;
	}
	//=======================================================================================================================
	public 	void	dessin(Graphics g)
	{
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{ 
			g.setColor(new Color( ( pixels[i+j*X] >> 16 ) & 0xff , ( pixels[i+j*X] >> 8 ) & 0xff ,  pixels[i+j*X] & 0x0f )) ;
			g.fillRect(i*echelle,j*echelle,echelle,echelle) ;
		}
	}
	//=======================================================================================================================
	public 	void	Revalidate()
	{
		mem.newPixels() ;
		img = createImage(mem) ;
	}
	//=======================================================================================================================
	public 	void	Afficher(Graphics g)
	{
		mem.newPixels() ;
		img = createImage(mem) ;
		g.drawImage(img,0,0,this);
	}
	//=======================================================================================================================
}
//###################################################################################################################################