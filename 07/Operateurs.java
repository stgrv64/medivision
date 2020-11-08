import	java.lang.Math ;

//##################################################################################################################################

public final class Operateurs
{
	//=======================================================================================================================
	public static double[][] 	getGradient(int[] pixels,int X,int Y)
	{
		double 	gr[][] = new double[X][Y];
		
		for(int i=1;i<X-1;i++)
        for(int j=1;j<Y-1;j++)
        {	
        	gr[i][j] = Gradient(pixels,i,j,X) ;
        }	
        return gr ;
	}
	//=======================================================================================================================
	public static double[][] 	getGradient(int[][] im,int X,int Y)
	{
		double 	gr[][] = new double[X][Y];
		
		for(int i=1;i<X-1;i++)
        for(int j=1;j<Y-1;j++)
        {	
        	gr[i][j] = Gradient(im,i,j) ;
        }	
        return gr ;
	}
	//=======================================================================================================================
	public static double[][] 	getGradient(double[][] im,int X,int Y)
	{
		double 	gr[][] = new double[X][Y];
		
		for(int i=1;i<X-1;i++)
        for(int j=1;j<Y-1;j++)
        {	
        	gr[i][j] = Gradient(im,i,j) ;
        }	
        return gr ;
	}
	//=======================================================================================================================
	public static double  		Gradient(int[] pixels,int i,int j,int X)
	{
		double 	gx , gy ;
		double 	a,b,c,d ;
		try {
			a = (setRed(pixels[i+1+j*X])   + setGreen(pixels[i+1+j*X])   + setBlue(pixels[i+1+j*X]))/3 ;
			b = (setRed(pixels[i-1+j*X])   + setGreen(pixels[i-1+j*X])   + setBlue(pixels[i-1+j*X]))/3 ;
			c = (setRed(pixels[i+(j+1)*X]) + setGreen(pixels[i+(j+1)*X]) + setBlue(pixels[i+(j+1)*X]))/3 ;
			d = (setRed(pixels[i+(j-1)*X]) + setGreen(pixels[i+(j-1)*X]) + setBlue(pixels[i+(j-1)*X]))/3 ;
		
			gx      = ( a - b ) / 2.0 ;
        	gy      = ( c - d ) / 2.0 ;
        }
        catch(java.lang.ArrayIndexOutOfBoundsException e) {
        	gx = 0.0 ;
        	gy = 0.0 ;
        }
        return  ( Math.sqrt( gx*gx + gy*gy ) ) ;
    }
	//=======================================================================================================================
	public static double  		Gradient(int[][] im,int i,int j)
	{
		double gx , gy ;
		
		gx      = ( im[i+1][j] - im[i-1][j] ) / 2.0 ;
        gy      = ( im[i][j+1] - im[i][j-1] ) / 2.0 ;
        
        return  ( Math.sqrt( gx*gx + gy*gy ) ) ;
    }
	//=======================================================================================================================
	public static double  		Gradient(double[][] im,int i,int j)
	{
		double gx , gy ;
		
		gx      = ( im[i+1][j] - im[i-1][j] ) / 2.0 ;
        gy      = ( im[i][j+1] - im[i][j-1] ) / 2.0 ;
        
        return  ( Math.sqrt( gx*gx + gy*gy ) ) ;
    }
	//=======================================================================================================================*/
	public static void 	Divergence()
	{
		
	}
	//=======================================================================================================================
	public static void 	Laplacien()
	{
		
	}
	//=======================================================================================================================
	public static void 	Rotationnel()
	{
		
	}
	//=======================================================================================================================
	public static double F_curv(double cd[][], int i,int j,double D)
	{
		double    courbure , lsx,lsy,lsxy,lsxx,lsyy,c,g,h,ii,k,l,m,n,o,q,r,s,w ;
		try 
		{	m         = cd[i][j];
        	l         = cd[i-1][j];	
        	n         = cd[i+1][j];
        	h         = cd[i][j-1];	
        	r         = cd[i][j+1];
        	s         = cd[i+1][j+1];	
        	q         = cd[i-1][j+1];	
        	ii         = cd[i+1][j-1];	
        	g         = cd[i-1][j-1];	
        	c         = cd[i][j-2];	
        	w         = cd[i][j+2];	
        	k         = cd[i-2][j];
        	o         = cd[i+2][j];	
        	lsx       = (n-l)/2;
        	lsy       = (r-h)/2;
        	lsxy      = (g+s-q-ii)/4;
        	lsxx      = (o+k-2*m)/4;
        	lsyy      = (w+c-2*m)/4;
                
        if ( Math.pow(lsx*lsx + lsy * lsy,1.5) != 0 )
                courbure = ( lsxx*lsy*lsy - 2*lsy*lsx*lsxy + lsyy*lsx*lsx )  / Math.pow( lsx*lsx + lsy*lsy ,1.5) ;
        else    courbure =  0.0;
        
        	return ( D * courbure * Math.sqrt( lsx*lsx + lsy*lsy ) ) ;
    	}
    	catch(java.lang.ArrayIndexOutOfBoundsException e)
    	{
    		return 1.0 ;
    	}
	}
	//=======================================================================================================================
	public 	static double 	Sgn(double a)
	{
		if ( a>=0.0) return  1.0  ;
		else		return  -1.0 ; 
	}
	//=======================================================================================================================
	public	static int 	setRed(int c) 
	{
		return (( c >> 16 ) & 0xff );
	}
	//=======================================================================================================================
	public	static int 	setGreen(int c) 
	{
		return (( c >> 8 ) & 0xff )  ;
	}
	//=======================================================================================================================
	public	static int 	setBlue(int c) 
	{
		return ( c & 0xff ) ;
	}
	//=======================================================================================================================
	public 	double 	Maxi(double a, double b)
	{
		if ( a>b) return a ;
		else	return   b ; 
	}
	//=======================================================================================================================
}
//##################################################################################################################################