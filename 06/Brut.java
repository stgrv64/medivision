import  java.awt.Color ;
import  java.awt.Font ;
import  java.util.Stack ;
import	java.lang.Math ;
import  java.awt.Graphics ;

import  Operateurs ;

//####################################################################################################################################################
class Brut extends Front 
{	
	//=======================================================================================================================
	Brut(double D, double R,Stack pile,ObjCanvas can,int echelle,int tailleNarrowBand)
	{
		contexte 	= 0 ;
		// IMG = 0.0 ;
		front		= new Piles(tailleNarrowBand+10) 	;
		front.Copie(0,pile) 	;	
		System.out.println("taille de la pile BRUT à l'init = " + pile.size() ) ;
		p 			= new Points() ;
		
		this.D 		= D ;
		this.R 		= R ;
		this.can   	 = can ;
		this.echelle = echelle ;
		this.tailleNarrowBand = tailleNarrowBand ;
		
		dim 		= (int)(DIM/echelle) ;
		
		sm 			= new boolean	[dim][dim] ;
		cd 			= new double	[dim][dim] ;
		ls 			= new double	[dim][dim] ;
		
		map  = new ImageBuffered(DIM,DIM) ;
		map2 = new ImageBuffered(DIM,DIM) ;
	}
	//=======================================================================================================================
	// 	CarteDistanceBrute
	//  CarteDistanceOptimale
	//  Calcul
	// 	Dessin
	//=======================================================================================================================
	public void 	CarteDistanceBrute()
	{
		double 	val , min  ;
		int		i,j,n , x , y ;
		Points	p1 ;
		
		p1 = new Points() ;
		val = 0.0 ;
		
		g.setFont(new Font("Helvetica",1,15)) ;
		g.setColor(new Color(0,0,255)) ;
		g.drawString("echelle = " + echelle ,100,120) ;
		g.drawString("dim     = " + dim ,100,140) ;
		
		for(i=0;i<1000000;i++) i=i ;
		
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
	//=======================================================================================================================
	public void 	CarteDistanceOptimale(Graphics g)
	{
		int           x,y,vvx,vvy,dis,distance_carre , m;
        Points        p , sommet ;
		
        dis = 0 ;

        while ( dis <= tailleNarrowBand )
        {       
                while ( !front.PileVide(dis) )
                {       
                		p 	= front.Depiler(dis);
 						
                        //front.Empiler(tailleNarrowBand + 2 , new Points(p.x,p.y) );
                        
                        sommet  = Voisin(p,sm,dim);
                        
                        while (sommet.indic > 0 )
                        {
                            x   = sommet.x;
                            y   = sommet.y;
                            vvx = sommet.vx;
                            vvy = sommet.vy;
                            
                            distance_carre = vvx*vvx + vvy*vvy;
                            
                            try 
                            {  
                            	if ( distance_carre > tailleNarrowBand ) 
                                	front.Empiler(tailleNarrowBand+1,sommet);
                            	else   	
                            		front.Empiler(distance_carre,sommet);
                            }
                            catch(java.lang.ArrayIndexOutOfBoundsException e)
                            {
                            	System.out.println("distance_carre = " + distance_carre) ;
                            }  
                            sm[x][y] = true ;
                                
                            if      ( cd[x][y] > 0 )
		                    	cd[x][y] =  0.0 +  Math.sqrt((double)distance_carre);
		                        		
                            else if ( cd[x][y] < 0 )
                                cd[x][y] =  0.0 -  Math.sqrt((double)distance_carre);
                            
                           
                            m = 100 + (int)( cd[x][y]/(echelle))*4 ;
                            try
                            {	
                            	g.setColor(new Color(m,m,m)) ;
                            }
                            catch(java.lang.IllegalArgumentException e)
                            {
                            	g.setColor(new Color(255,255,255)) ;
                            }
							g.fillRect(x*echelle,y*echelle,echelle,echelle) ;
							
		                	sommet = Voisin(p,sm,dim);
                        }
                }
                dis++;
        }
        while ( ! front.PileVide(tailleNarrowBand+1)  )
        {
                p = front.Depiler(tailleNarrowBand+1);
                sm[p.x][p.y] = false ;
        }
       
	}
	//=======================================================================================================================
	public  void 	Calcul()
	{
		double  F_prop , F_adv ;
		int		RED , GREEN , BLUE ;

        for(int i=2;i<dim-2;i++)
        for(int j=2;j<dim-2;j++)
        {
            // F_prop    = 1/(1+sqr(gradient[i+j*dim]/DEPENDANCE_GRADIENT)) ;
            F_adv     = R ;
			cd[i][j] = ls[i][j] + T * ( Operateurs.F_curv(cd,i,j,D) + F_adv);
			
			RED 	= Operateurs.setRed	 (   (int)cd[i][j] *1000) ;
			GREEN 	= Operateurs.setGreen(   (int)cd[i][j] *100) ;
			BLUE 	= Operateurs.setBlue (   (int)cd[i][j] *10) ;
			
			map2.setPixel( i , j , RED , GREEN , BLUE , echelle) ;
			
			if ( cd[i][j] <= 0.0  ) 
				map.setPixel(i,j,255,echelle) ;
			else
				map.setPixel(i,j,0,echelle) ;
        }
	}
	//=======================================================================================================================
	public  void 	Dessin(Graphics g) 
	{
		map2.Afficher(g) ;
	}
	//=======================================================================================================================
	public  void 	Dessin2(Graphics g) 
	{
		
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			if ( ls[i][j] > 0.0  ) 
				g.setColor(new Color(0,0,0)) ;
				
			if ( ls[i][j] <= 0.0 )
				g.setColor(new Color(255,255,255)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//=======================================================================================================================
	
}

//####################################################################################################################################################
