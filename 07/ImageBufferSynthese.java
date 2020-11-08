import java.awt.image.MemoryImageSource ;

//###################################################################################################################################

class ImageBufferSynthese extends ImageBuffer
{
	//=======================================================================================================================
	ImageBufferSynthese(int X, int Y)
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
}
//###################################################################################################################################