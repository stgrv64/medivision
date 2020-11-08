import  java.awt.Graphics ;
import  java.awt.Color ;
import  java.util.Stack ;
import	java.lang.Math ;
import  java.awt.Font ;
//####################################################################################################################################################
class NarrowBand extends Front 
{	
	//=======================================================================================================================
	NarrowBand(Stack pile,ObjCanvas can,int echelle,int tailleNarrowBand)
	{
		D 	= 2.0 ;
		R 	= 0.0 ;
		TNB = 6.0 ;
		
		contexte 	= 0 ;
		front		= new Piles(N) 	;
		front.Copie(0,pile) 	;	
		p 			= new Points() ;
		
		this.can   	 = can ;
		this.echelle = echelle ;
		this.tailleNarrowBand = tailleNarrowBand ;
		
		dim 		= (int)(DIM/echelle) ;
		
		sm 			= new boolean	[dim][dim] ;
		cd 			= new double	[dim][dim] ;
		ls 			= new double	[dim][dim] ;
	}
	//=======================================================================================================================
	// 	CarteDistanceOptimale
	// 	Calcul
	//  Dessin
	//=======================================================================================================================
	public void 	CarteDistanceOptimale()
	{
		int           x,y,vvx,vvy,dis,distance_carre , m;
        Points        p , sommet ;
		
        dis = 0 ;

        while ( dis <= tailleNarrowBand )
        {       
                while ( !front.PileVide(dis) )
                {       
                		p 	= front.Depiler(dis);
 						
                        front.Empiler(tailleNarrowBand + 2,new Points(p.x,p.y));
                        
                        sommet  = Voisin(p,sm,dim);
                        
                        while (sommet.indic > 0 )
                        {
                            x   = sommet.x;
                            y   = sommet.y;
                            vvx = sommet.vx;
                            vvy = sommet.vy;
                            
                            distance_carre = vvx*vvx + vvy*vvy;
                                
                            if ( distance_carre > tailleNarrowBand ) 
                                front.Empiler(tailleNarrowBand+1,sommet);
                            else   	
                            	front.Empiler(distance_carre,sommet);
                                
                            sm[x][y] = true ;
                                
                            if      ( cd[x][y] > 0 )
		                    	cd[x][y] =  0.0 +  Math.sqrt((double)distance_carre);
		                        		
                            else if ( cd[x][y] < 0 )
                                cd[x][y] =  0.0 -  Math.sqrt((double)distance_carre);
                            
                            m = 100 + (int)( cd[x][y]/(echelle))*4 ;
                            
                            g.setColor(new Color(m,m,m)) ;
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
        /*
        while ( ! front.PileVide(tailleNarrowBand + 2)  )
        {
                p = front.Depiler(tailleNarrowBand + 2);
        }
        */
	}
	//=======================================================================================================================
	public  void 	Calcul() 
	{
		Points   	po ;
        int    		i,j,x,y;
        double   	lsx,lsy,lsxy,lsxx,lsyy,max;
        double   	F_prop , F_curv , F_adv ;
        double   	courbure,c,g,h,ii,k,l,m,n,o,q,r,s,w,courbures = 0.15 ;

		po = new Points() ;
		
        while ( ! front.PileVide(tailleNarrowBand + 2)  )
        {
                po = front.Depiler(tailleNarrowBand + 2);
                
                i = po.x;
                j = po.y;
                x = po.vx;
                y = po.vy;
                
                po.Change(i,j,0,0,0);
                
                front.Empiler(tailleNarrowBand + 2+1,new Points(i,j));
                max       = 0.0 ;
                
                	m         = cd[i][j];
                	l         = cd[i-1][j];	
                	n         = cd[i+1][j];
                	h         = cd[i][j-1];	
                	r         = cd[i][j+1];
                	s         = cd[i+1][j+1];	
                	q         = cd[i-1][j+1];	
                	ii        = cd[i+1][j-1];	
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
                        courbure  = ( lsxx*lsy*lsy - 2*lsy*lsx*lsxy + lsyy*lsx*lsx )  / Math.pow( lsx*lsx + lsy*lsy ,1.5) ;
                	else    courbure = 0.0;
                
                	max = Maxi(max,Math.abs(courbure));

                	if ( Math.abs(courbure) < courbures ) courbure = Sgn(courbure)*courbures*0.0;

					/*
                	if (test_image==TRUE)
                        F_prop    = 1/(1+sqr(gradient[i+j*dim]/dependance_gradient)) ;
                	else    
                	*/
                	F_prop    = 1.0 ;

                	F_curv    = D * courbure * Math.sqrt( lsx*lsx + lsy*lsy ) ;
                	F_adv     = R ;

                	ls[i][j] = cd[i][j] + T * F_prop * (F_curv + F_adv);

                
        }

        while ( ! front.PileVide(tailleNarrowBand + 2+1) )
        {
            po = front.Depiler(tailleNarrowBand + 2+1) ;
            i = po.x;
            j = po.y;
                
            cd[i][j] = ls[i][j];
                
            if ( TestPoint(ls,i,j,dim,TNB) == 1 )
	        {
               	front.Empiler(0,new Points(i,j));
                sm[i][j] = true;
               	cd[i][j] = 0.0 ;
	        }
            else    sm[i][j] = false;
        }
	}
	//=======================================================================================================================
	public  void 	Dessin(Graphics g) 
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
	public  void 	Dessin2(Graphics g) 
	{
		int 	val ;
		
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			val = 255 - 10 * (int)Math.abs(cd[i][j]) ;
			
			if ( val >=0 )
			
				g.setColor(new Color(val,val,val)) ;
				
			else
				g.setColor(new Color(0,0,0)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//=======================================================================================================================
	public  void 	DessinNiveauZero(Graphics g) 
	{
		Points p = new  Points() ;
		
		g.setColor(new Color(255,0,0)) ;
		
		for(int i=0;i<front.TaillePile(0);i++)
        {	
        	p = front.Element(0,i) ;
        	
			g.fillRect( (p.x)*echelle , (p.y)*echelle , echelle ,echelle ) ;
		}
	}
	//=======================================================================================================================
	public  void 	Dessin3(Graphics g) 
	{
		Points p = new  Points() ;
		
		g.setColor(new Color(0,0,255)) ;
		g.fillRect(0,0,100,100) ;
		
		for(int j=0;j<tailleNarrowBand;j++)
		for(int i=0;i<front.TaillePile(j);i++)
        {	
        	g.setColor(new Color(0,0,255)) ;
			g.fillRect(0,0,100,100) ;
        	p = front.Element(j,i) ;
        	
        	if (  (255-j*10) >= 0 )
        		g.setColor(new Color(255-j*10,255-j*10,255-j*10)) ;
        	else
        		g.setColor(new Color(255,0,0)) ;
        		
			g.fillRect( (p.x)*echelle , (p.y)*echelle , echelle ,echelle ) ;
		}
	}
	//=======================================================================================================================
}

//####################################################################################################################################################
