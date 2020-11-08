import 	java.awt.*;
import  java.awt.event.*;
import  java.util.Stack ;
import	java.lang.Math ;
import  Points ;
import  Piles ;
import  ObjCanvas ;
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
public class Front 
{	
	private Piles			front;
	private double			ls[][] , cd[][] ;
	private boolean 		sm[][] ;
	private Points  		p ;
	private int				N = 100000 , dim , contexte , echelle ;
	private final int		DIM = 400  ;
	private final double	T   = 0.5  ;
	private final double	D   = 2.1  ;
	private final double	R   = 0.0  ;
	private ObjCanvas		can ;
	private Graphics		g ;
	
	//-----------------------------------------------------------------------------------------
	Front(Stack pile,ObjCanvas can,int echelle)
	{
		contexte 	= 0 ;
		front		= new Piles(N) 	;
		front.Copie(0,pile) 	;	/* transfert d'objet de la pile sur front[0] */
		p 			= new Points() ;
		this.can   	= can ;
		
		dim 		= (int)(DIM/echelle) ;
		
		sm 			= new boolean	[dim][dim] ;
		cd 			= new double	[dim][dim] ;
		ls 			= new double	[dim][dim] ;
	}
	//--------------------------------------------------------------------------------------------- 
	public void 	Labellisation(Graphics g,int echelle)
	{
		/*dessin(g,echelle,0) ;*/
		
		int      		i,j,indice=0 ;
        boolean 		dj[][] ;
        Points  		p ;
		
		p 	= new Points() ;	
		dj 	= new boolean[dim][dim] ;
		
		g = g ;
		//----------------------------------------------------------------------------------------------
        // initialisation des variables locales : création is = 1 (front) et dj=false        
        //----------------------------------------------------------------------------------------------
        
        for(i=0;i<dim;i++) 
        for(j=0;j<dim;j++) 
        {	
        	dj[i][j] = false ;
        	sm[i][j] = false ;
        	cd[i][j] = -1.0 ;
        }
        
        while( !front.PileVide(0) )
        {
        	p = (Points)front.Depiler(0) ;
        	cd[p.x][p.y]  	= 0.0 ;
        	dj[p.x][p.y] 	= true ;
        }
        
        
        //-----------------------------------------------------------------
        // labellisation intérieur / extérieur au front ( is = 1)               
        //-----------------------------------------------------------------

        dj[0][0]         = true ; front.Empiler(0,new Points(0,0)) ;		
        dj[dim-1][0]     = true ; front.Empiler(0,new Points(dim-1,0)) ;	
        dj[0][dim-1]     = true ; front.Empiler(0,new Points(0,dim-1)) ;	
        dj[dim-1][dim-1] = true ; front.Empiler(0,new Points(dim-1,dim-1));	

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

                        if ((j+1)<dim) 
                        	if ( ! dj[i][j+1] )
                                {       
                                	/*p.x = i ;p.y = j + 1 ;*/
                                	front.Empiler(indice+1,new Points(i,j+1));
                                    dj[i][j+1] = true ;
                                    cd[i][j+1] = 1.0 ;
                                    g.fillRect(i*echelle,(j+1)*echelle,echelle,echelle) ;
                                }

                        if ((i+1)<dim) 
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

        //----------------------------------------------------------------------------------
        // front => pile(0) et soumouv = TRUE ;  intérieur labellisé => cd = -1 -------    
        //----------------------------------------------------------------------------------
	
        for(i=0;i<dim;i++)
        for(j=0;j<dim;j++)
        {       
        	if ( cd[i][j] == 0.0 )
            {       
                sm[i][j]= true ;
                front.Empiler(0,new Points(i,j));
            }
        }
     
		
	}
	//-----------------------------------------------------------------------------------------
	public void 	CarteDistance(Graphics g,int echelle)
	{
		double 	val , min  ;
		int		i,j,n , x , y ;
		Points	p1 ;
		
		p1 = new Points() ;
		/*dim = (int)(DIM/echelle) ;*/
		val = 0.0 ;
		
		g.setFont(new Font("Helvetica",1,15)) ;
		g.setColor(new Color(0,0,255)) ;
		g.drawString("echelle = " + echelle ,100,120) ;
		g.drawString("dim     = " + dim ,100,140) ;
		
		for(i=0;i<1000000;i++) i=i ;
		
		/*dessin(g,echelle,0) ; */
		
		for(i=0;i<dim;i++) 
        for(j=0;j<dim;j++) 
		{
		  	min = 10000 ;
		  	
		  	for(n=0 ; n < front.TaillePile(0) ; n++)
			{
				p 	= front.Element(0,n) ;
				val = Math.sqrt( ((p.x)-i)*((p.x)-i) + ((p.y)-j)*((p.y)-j) ) ;
				
				if ( val < min ) min = val ;
			}
        	cd[i][j] = cd[i][j] * min ;
        	
        	x = (int)(min/5.0) * 20  ;
        	y = Math.abs( (int)((double)x/(double)255)) ;
        	x = x - y*255 ;
        	
        	if ( cd[i][j] > 0  )	g.setColor(new Color(x	,150+y*40,150)) ;
        	if ( cd[i][j] < 0  )	g.setColor(new Color(150+y*40,x	,150)) ;
        	if ( cd[i][j] == 0 )	g.setColor(new Color(255,255,255)) ;
        		
        	p1.x = i ;
        	p1.y = j ;
        	p1.dessin(g,echelle) ;
        }
        contexte = 1 ;
	}
	//-----------------------------------------------------------------------------------------
	Points 	Voisin(Points p,boolean sm[][],int dim)
	{
        Points  voisin;
        int     x,y,vvx,vvy;

        x       =  p.x ;
        y       =  p.y ;
        vvx     =  p.vx   ;
        vvy     =  p.vy   ;
        
        voisin  =  new Points(0,0,0,0,0);
	
		if ( x>1 && x<dim-1 && y>1 && y<dim-1 )
		{
        	if ( !sm[x+1][y] )	voisin.Change(x+1,y,vvx+1,vvy,1);
        	if ( !sm[x-1][y] )	voisin.Change(x-1,y,vvx-1,vvy,1);
        	if ( !sm[x][y-1] )	voisin.Change(x,y-1,vvx,vvy-1,1);
        	if ( !sm[x][y+1] )	voisin.Change(x,y+1,vvx,vvy+1,1);
		}
        return(voisin);
	}
	//-----------------------------------------------------------------------------------------
	public void 	CarteDistanceOptimale(Graphics g,int echelle,int nb_max1,int piles_l_set)
	{
		int           x,y,vvx,vvy,dis,distance_carre , m;
        Points        p , sommet ;
		
        dis = 0 ;

        while ( dis <= nb_max1 )
        {       
                while ( !front.PileVide(dis) )
                {       
                		p 	= front.Depiler(dis);
 						
                        front.Empiler(piles_l_set,p);
                        
                        sommet  = Voisin(p,sm,dim);
                        
                        while (sommet.indic > 0 )
                        {
                            x   = sommet.x;
                            y   = sommet.y;
                            vvx = sommet.vx;
                            vvy = sommet.vy;
                            
                            distance_carre = vvx*vvx + vvy*vvy;
                                
                            if ( distance_carre > nb_max1 ) 
                                front.Empiler(nb_max1+1,sommet);
                            else   	
                            	front.Empiler(distance_carre,sommet);
                                
                            sm[x][y] = true ;
                                
                            if      ( cd[x][y] > 0 )
		                    	cd[x][y] =  0.0 +  Math.sqrt((double)distance_carre);
		                        		
                            else if ( ls[x][y] < 0 )
                                cd[x][y] =  0.0 -  Math.sqrt((double)distance_carre);
                            
                            m = 100 + (int)( cd[x][y]/5.0)*4 ;
                            
                            g.setColor(new Color(m,m,m)) ;
							g.fillRect(x*echelle,y*echelle,echelle,echelle) ;
							
		                	sommet = Voisin(p,sm,dim);
                        }
                }
                dis++;
        }
        while ( ! front.PileVide(nb_max1+1)  )
        {
                p = front.Depiler(nb_max1+1);
                sm[p.x][p.y] = false ;
        }
	}
	//-----------------------------------------------------------------------------------------
	public void 	copie()
	{
		int   	i,j ;
		
		for(i=0;i<dim;i++)
        for(j=0;j<dim;j++)
        {	
        	ls[i][j] = cd[i][j] ;
        }	
	}
	
	//-----------------------------------------------------------------------------------------
	public void 	CalculBrut()
	{
		int   	i,j,x,y;
        double  lsx,lsy,lsxy,lsxx,lsyy,max;
        double  F_prop , F_curv , F_adv ;
        double  courbure,c,g,h,ii,k,l,m,n,o,q,r,s,w;

        for(i=2;i<dim-2;i++)
        for(j=2;j<dim-2;j++)
        {
                m         = ls[i][j];
                l         = ls[i-1][j];	
                n         = ls[i+1][j];
                h         = ls[i][j-1];	
                r         = ls[i][j+1];
                s         = ls[i+1][j+1];	
                q         = ls[i-1][j+1];	
                ii        = ls[i+1][j-1];	
                g         = ls[i-1][j-1];	
                c         = ls[i][j-2];	
                w         = ls[i][j+2];	
                k         = ls[i-2][j];
                o         = ls[i+2][j];	
                lsx       = (n-l)/2;
                lsy       = (r-h)/2;
                lsxy      = (g+s-q-ii)/4;
                lsxx      = (o+k-2*m)/4;
                lsyy      = (w+c-2*m)/4;
                
                if ( Math.pow( (lsx*lsx + lsy*lsy ) ,1.5) != 0 )
                	 courbure  = (lsxx*lsy*lsy - 2*lsy*lsx*lsxy + lsyy*lsx*lsx ) / Math.pow( (lsx*lsx + lsy*lsy) ,1.5) ;
                else courbure = 0;

                /*F_prop    = 1/(1+sqr(gradient[i+j*dim]/DEPENDANCE_GRADIENT)) ;*/
                F_curv    = D * courbure * Math.sqrt( lsx*lsx + lsy*lsy ) ;
                F_adv     = R ;

                cd[i][j] = ls[i][j]+ T * (F_curv + F_adv);
        }
	}
	//-----------------------------------------------------------------------------------------
	public  void 	dessin() 
	{
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			if ( ls[i][j]>0 ) 
				g.setColor(new Color(80,80,80)) ;
			else
				g.setColor(new Color(200,200,200)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//-----------------------------------------------------------------------------------------
	public   void 	dessin(Graphics g,int echelle) 
	{
		
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			if ( ls[i][j]>0 ) 
				g.setColor(new Color(0,0,0)) ;
			else
				g.setColor(new Color(255,255,255)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//-----------------------------------------------------------------------------------------
	public  void 	coucou() 
	{
		g.fillOval(100,100,50,50) ;
	}
	//-----------------------------------------------------------------------------------------	
}

//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
