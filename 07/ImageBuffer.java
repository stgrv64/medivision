import 	java.awt.image.MemoryImageSource ;
import  java.awt.Graphics ;

//###################################################################################################################################

public class ImageBuffer extends Images
{
	protected 	int					pixels[] ;
	protected 	MemoryImageSource 	mem ;
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
	public 	void	Map(Graphics g)
	{
		try {
			mem.newPixels() ;
			img = createImage(mem) ;
			g.drawImage(img,0,0,this);
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
	public 	void 	MapZone(Graphics g,int a,int b,int A,int B) 
	{
		try {
			mem = new MemoryImageSource(A,B, pixels,X*b+a, X) ;
			img = createImage(mem) ;
			g.drawImage(img,0,0,350,350,this) ;
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
}
//###################################################################################################################################