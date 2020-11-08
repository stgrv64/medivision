import 	java.awt.image.MemoryImageSource ;
import  java.awt.Graphics ;
import 	java.awt.Image ;
import 	java.lang.String ;
import 	java.awt.Color ;
import  java.awt.Font ;
import 	java.applet.Applet ;
import  java.io.* ;

//###################################################################################################################################

class ImageBufferAnalyseGradient extends ImageBufferAnalyse
{
	private	double  			gr[][] ;
	private int 				pixels_gra[] ;
	private MemoryImageSource 	mem_gra ;
	private Image				gra ;
	private double 				max = 0.0 ;
	//=======================================================================================================================
	ImageBufferAnalyseGradient(String path,Applet ap)
	{
		super(path,ap) ;
		
		int 	hist[] ;
		double 	val ;
		
		gr 			= new double[X][Y] ;
		pixels_gra 	= new int[X*Y] ;
		hist		= new int[26] ;
		
		for(int i=0;i<26;i++)
			hist[i]=0 ;
			
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			try {
				gr[i][j] = Operateurs.Gradient(pixels,i,j,X) ;
				if ( gr[i][j] > max)	max = gr[i][j] ;
			}
			catch(java.lang.ArrayIndexOutOfBoundsException e) {
				gr[i][j] = 0.0 ;
			}
		}
		
		val = 255.0 / max ;
		
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{
			hist[ (int)( gr[i][j] * val /30.0)]++ ;
		}
		int incr=25 ;
		
		while( hist[incr] < 100 && incr > 0)
			incr-- ;
			
		if ( incr!=0 )
			val = val * ( 25 / incr ) ;
	
		for(int i=0;i<X;i++)
		for(int j=0;j<Y;j++)
		{	
			pixels_gra[i+j*X] = ( 255 << 24 ) | ( (int)(val * gr[i][j]) << 16 ) | ( (int)(val * gr[i][j]) << 8 ) | (int)(val * gr[i][j]) ;
		}
		
		mem_gra = new MemoryImageSource(X, Y, pixels_gra, 0, X) ;
	}
	//=======================================================================================================================
	public 	double[][]	getGradient()
	{
		return gr ;
	}
	//=======================================================================================================================
	public 	void	MapGradient(Graphics g)
	{
		try {
			mem_gra.newPixels() ;
			gra = createImage(mem_gra) ;
			g.drawImage(gra,0,0,this);
		}
		catch(java.lang.NullPointerException e) {}
		
	}
	//=======================================================================================================================
	public 	void 	MapZoneGradient(Graphics g,int a,int b,int A,int B) 
	{
		try {
			mem_gra = new MemoryImageSource(A,B, pixels_gra,X*b+a, X) ;
			gra = createImage(mem_gra) ;
			g.drawImage(gra,0,0,X,Y,this) ;
		}
		catch(java.lang.NullPointerException e) {}
	}
	//=======================================================================================================================
}
//###################################################################################################################################