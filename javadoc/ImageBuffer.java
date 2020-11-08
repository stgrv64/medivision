import 	java.awt.image.MemoryImageSource ;
import  java.awt.Graphics ;
import  java.awt.image.ColorModel ;

//###################################################################################################################################

public class ImageBuffer extends Images
{
	protected 	int					pixels[] ;
	protected 	MemoryImageSource 	mem ;
	private		int					a,b,A,B ;
	//=======================================================================================================================
	public  int[][] getBuffer()
	{
		int buffer[][] = new int[X][Y] ;
		
		try {
			for(int i=0;i<X;i++)
			for(int j=0;j<Y;j++)
			{	
				buffer[i][j] = pixels[i+j*X] ;
			}
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e) {
			System.out.println("Exception dans ImageBuffer.getBuffer()") ;  }
			
		return buffer ;
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
	public 	void	map(Graphics g)
	{
		try {
			mem.newPixels() ;
			img = createImage(mem) ;
			g.drawImage(img,0,0,this);
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
	public 	void 	mapArea(Graphics g,int a,int b,int A,int B,int scale) 
	{
		try {
			mem = new MemoryImageSource(A,B,pixels,X*b+a, X) ;
			img = createImage(mem) ;
			g.drawImage(img,0,0,X*scale,Y*scale,this) ;
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
	public 	void 	mapArea(Graphics g,int a,int b,int A,int B) 
	{
		try {
			if ( this.a != a || this.b!=b || this.A != A || this.B!=B )
			{
				this.a = a ; this.b=b ; this.A = A ; this.B=B ;
				mem = new MemoryImageSource(A,B,pixels,X*b+a, X) ;
			}
			img = createImage(mem) ;
			g.drawImage(img,0,0,X,Y,this) ;
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
}
//###################################################################################################################################