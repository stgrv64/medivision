import  java.awt.Color ;
import  java.awt.Graphics ;
import	java.lang.Math ;

import  Points ;
import  Piles ;
import  ImageBufferSynthese ;

//####################################################################################################################################################
public abstract class Front 
{	
	protected 	Graphics				g ;
	protected 	double					ls[][] , cd[][] , gr[][] ;
	protected 	boolean 				sm[][] ;
	protected 	int						contexte , echelle , tailleNarrowBand ;
	protected   int						XBase = 350 , YBase = 350 , X , Y ;
	protected 	final double			T   = 2.0  ;
	protected 	double					D    ;
	protected 	double					R    ;
	protected 	double					TNB  ;
	protected   double					IMG = 0.0 ;
	
	protected   Images 					im ;
	protected 	Piles					front ;
	protected 	ObjCanva				can ;
	protected 	Points  				p ;
	protected   ImageBufferSynthese   	map , map2 ;
	//=======================================================================================================================
	public abstract void Dessin(Graphics g) ;
	public abstract void CarteDistanceOptimale(Graphics g) ;
	public abstract void Calcul() ;
	//=======================================================================================================================
	public void		setImage(double[][] im,int X,int Y)
	{
		this.ls = new double[X][Y] ;
		this.ls = im ;
	}
	//=======================================================================================================================
	public void		setGradient(double[][] gr,int X,int Y)
	{
		this.gr = new double[X][Y] ;
		this.gr = gr ;
	}
	//=======================================================================================================================
	public void  	setImageTrue()
	{
		IMG = 1.0 ;
	}	
	//=======================================================================================================================*/
	public void 	ChangerDiffusion(double D)
	{
		this.D = D ;
	}
	//=======================================================================================================================
	public void 	ChangerReaction(double R)
	{
		this.R = R ;
	}
	//=======================================================================================================================
	public 	void 	ChangerTestNarrowBand(double TNB)
	{
		this.TNB = TNB ;
	}
	//=======================================================================================================================
	public 	void 	ChangerDim()
	{
		X = (int)(XBase/echelle) ;
		Y = (int)(YBase/echelle) ;
	}
	//=======================================================================================================================
	public 	void 	ChangerEchelle(int echelle)
	{
		this.echelle = echelle ;
	}
	//=======================================================================================================================
	public 	void 	SetGraphics(Graphics g)
	{
		this.g = g ;
	}
	//=======================================================================================================================
	public 	void 	Reset()
	{
		front.Reset() ;
	}
	//=======================================================================================================================
	public 	void 	Labellisation(Graphics g)
	{
		int      		i,j,indice=0 ;
        boolean 		dj[][] ;
        
		dj 	= new boolean[X][Y] ;
		
		for(i=0;i<X;i++) 
        for(j=0;j<Y;j++) 
        {	
        	dj[i][j] = false ;
        	sm[i][j] = false ;
        	cd[i][j] = -1.0 ;
        }
        
        while( !front.PileVide(0) )
        {
        	p = (Points)front.Depiler(0) ;
        	try
        	{
        		cd[p.x][p.y]  	= 0.0 ;
        		dj[p.x][p.y] 	= true ;
        	}
        	catch(java.lang.ArrayIndexOutOfBoundsException e)
        	{
        		if ( p.x >= X && p.y >= Y ) 
        		{	
        			cd[X-1][Y-1]  = 0.0 ;
        			dj[X-1][Y-1] = true ;
        		}
        		else if ( p.x >= X && p.y < Y)
        		{	
        			cd[X-1][p.y]  = 0.0 ;
        			dj[X-1][p.y] 	= true ;
        		}
        		else if ( p.x < X && p.y >= Y)
        		{	
        			cd[p.x][Y-1]  = 0.0 ;
        			dj[p.x][Y-1] 	= true ;
        		}
        	}
        }
        
        dj[0][0]         = true ; front.Empiler(0,new Points(0,0)) ;		
        dj[X-1][0]     = true ; front.Empiler(0,new Points(X-1,0)) ;	
        dj[0][Y-1]     = true ; front.Empiler(0,new Points(0,Y-1)) ;	
        dj[X-1][Y-1] = true ; front.Empiler(0,new Points(X-1,Y-1));	

        indice=0;

		g.setColor(new Color(0,0,0)) ;
			
        while( ! front.PilesVides() )
        
        {		while( ! front.PileVide(indice) )
                {       
                		/*p = new Points() ;*/
                		p = front.Depiler(indice);
                        i = p.x ;
                        j = p.y ;
                        if ((i-1)>0)   
                        	if (  ! dj[i-1][j]  )
                                {       
                                	/*p.x = i - 1 ;p.y = j ;*/
                                    front.Empiler(indice+1,new Points(i-1,j));
                                    dj[i-1][j] = true ;
                                    cd[i-1][j] = 1.0 ;
                                    g.fillRect((i-1)*echelle,j*echelle,echelle,echelle) ;
                                }

                        if ((j-1)>0)   
                        	if ( ! dj[i][j-1] )
                                {       
                                	/*p.x = i  ;p.y = j -1 ;*/
                                	front.Empiler(indice+1,new Points(i,j-1));
                                    dj[i][j-1] = true ;
                                    cd[i][j-1] = 1.0 ;
                                    g.fillRect(i*echelle,(j-1)*echelle,echelle,echelle) ;
                                }

                        if ((j+1)<Y) 
                        	if ( ! dj[i][j+1] )
                                {       
                                	/*p.x = i ;p.y = j + 1 ;*/
                                	front.Empiler(indice+1,new Points(i,j+1));
                                    dj[i][j+1] = true ;
                                    cd[i][j+1] = 1.0 ;
                                    g.fillRect(i*echelle,(j+1)*echelle,echelle,echelle) ;
                                }

                        if ((i+1)<X) 
                        	if ( ! dj[i+1][j] )
                                {       
                                	/*p.x = i + 1 ;p.y = j ;*/
                                	front.Empiler(indice+1,new Points(i+1,j));
                                    dj[i+1][j] = true ;
                                    cd[i+1][j] = 1.0 ;
                                    g.fillRect((i+1)*echelle,j*echelle,echelle,echelle) ;
                                }

                }
                indice++;
        }

        for(i=0;i<X;i++)
        for(j=0;j<Y;j++)
        {       
        	if ( cd[i][j] == 0.0 )
            {       
                sm[i][j]= true ;
                front.Empiler(0,new Points(i,j));
            }
        }
     
		
	}
	//=======================================================================================================================
	public	Points 	Voisin(Points p,boolean sm[][],int X,int Y)
	{
        Points  voisin;
        int     x,y,vvx,vvy;

        x       =  p.x ;
        y       =  p.y ;
        vvx     =  p.vx   ;
        vvy     =  p.vy   ;
        
        voisin  =  new Points(0,0,0,0,0);
	
		if ( x>2 && x<X-3 && y>2 && y<Y-3 )
		{
        	if ( !sm[x+1][y] )	voisin.Change(x+1,y,vvx+1,vvy,1);
        	if ( !sm[x-1][y] )	voisin.Change(x-1,y,vvx-1,vvy,1);
        	if ( !sm[x][y-1] )	voisin.Change(x,y-1,vvx,vvy-1,1);
        	if ( !sm[x][y+1] )	voisin.Change(x,y+1,vvx,vvy+1,1);
		}
        return(voisin);
	}
	//=======================================================================================================================
	public 	int     TestPoint(double lss[][],int i,int j,double test_narrow_band)
	{
        double 	a=10,b=10,c=10,d=10,g,h,ii,l,n,m,q,r,s ;
		
		try {
        	m       = lss[i][j];
        	l       = lss[i-1][j];
        	n       = lss[i+1][j];
        	h       = lss[i][j-1];
        	q       = lss[i-1][j+1];
        	ii      = lss[i+1][j-1];
        	g       = lss[i-1][j-1];
        	r       = lss[i][j+1];
        	s       = lss[i+1][j+1];
        	a       = g+h+l+m;
        	b       = h+ii+m+n;
        	c       = l+m+q+r;
        	d       = m+n+r+s; 
        }	
		catch(java.lang.ArrayIndexOutOfBoundsException e) {
        	System.out.println("Exception recue à front.TestPoint() i = "+i+" , j = "+j) ; }
        
        if ( Math.abs(a+b+c+d) < test_narrow_band )
            	return(1) ;

        else    return(0) ;
	}
	//=======================================================================================================================
	public void 	Copie()
	{
		int   	i,j ;
		try {
			for(i=0;i<X;i++)
        	for(j=0;j<Y;j++) {	
        		ls[i][j] = cd[i][j] ;}
        	}	
        catch(java.lang.ArrayIndexOutOfBoundsException e) {
        	System.out.println("Exception recue à front.Copie() : copie arrêtée."); }
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
