import  java.awt.Graphics ;
import  java.awt.Color ;
import  java.util.Stack ;
import	java.lang.Math ;
import  java.awt.Font ;
import  Operateurs ;
//####################################################################################################################################################
class NarrowBand extends Front 
{	
	//=======================================================================================================================
	NarrowBand(double D, double R, double TNB,Stack pile,ObjCanvas can,int echelle,int nbre_piles,int tailleNarrowBand)
	{
		contexte 	= 0 ;
		front		= new Piles(nbre_piles) 	;
		front.Copie(0,pile) 	;	
		System.out.println("taille de la pile NB à l'init = " + pile.size() ) ;
		p 			= new Points() ;
		
		this.D 		= D ;
		this.R 		= R ;
		this.TNB 	= TNB ;
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
 						
 						if ( p.x > 1 && p.x < dim-1 && p.y > 1 && p.y < dim-1 )
 						
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
        double   	F_prop , F_curv , F_adv ;
        double   	courbure,courbures = 0.15 ;

		po = new Points() ;
		
        while ( ! front.PileVide(tailleNarrowBand + 2)  )
        {
            po = front.Depiler(tailleNarrowBand + 2);
                
            i = po.x;
            j = po.y;
            x = po.vx;
            y = po.vy;
                
            po.Change(i,j,0,0,0);
                
            front.Empiler(tailleNarrowBand + 2 + 1 , new Points(i,j));
               
            F_adv     = R ;
			ls[i][j] = cd[i][j] + T * ( Operateurs.F_curv(cd,i,j,D) + F_adv);
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
               	
               	map.setPixel(i,j,255,echelle) ;
	        }
            else    
            {
            	sm[i][j] = false;
            	map.setPixel(i,j,0,echelle) ;
            }
        }
	}
	//=======================================================================================================================
	public  void 	Dessin(Graphics g) 
	{
		map.Afficher(g) ;
	}
	//=======================================================================================================================
	public  void 	Dessin3(Graphics g) 
	{
		int 	val ;
		
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			val = 255 - 10 * (int)Math.abs(cd[i][j]) ;
			
			if ( val >=0 )
			
				g.setColor(new Color(0,val,val)) ;
				
			else
				g.setColor(new Color(0,0,0)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//=======================================================================================================================
	public  void 	Dessin2(Graphics g) 
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
	public  void 	DessinCarteDistance(Graphics g) 
	{
		Points p = new  Points() ;
		
		g.setColor(new Color(255,0,0)) ;
		
		for(int j=0;j<tailleNarrowBand;j++)
		for(int i=0;i<front.TaillePile(j);i++)
        {	
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
