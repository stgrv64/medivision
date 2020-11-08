
//###################################################################################################################################

public class ImageBuffer extends Images
{
	private 	int					pixels[], pixels2[] , pixelszone[];
	protected 	MemoryImageSource 	mem ;
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
	public 	void	Update()
	{
		try {
			mem.newPixels() ;
			img = createImage(mem) ;
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
	public 	void	Map(Graphics g)
	{
		try {
			g.drawImage(img,0,0,this);
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
	public abstract	void 	MapZone(Graphics g,int a,int b,int A,int B) 
	{
		if (  )
		try {
			g.drawImage(img,0,0,this);
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
}
//###################################################################################################################################