import  java.awt.Graphics ;
import  java.awt.Color ;
import  java.util.Stack ;
import	java.lang.Math ;

//####################################################################################################################################################
public class NarrowBand extends Front 
{	
	//=======================================================================================================================
	NarrowBand(Images imag,double D, double R, double TNB,Stack pile,Canva can,int scale,int nbre_piles,int tailleNarrowBand)
	{
		IMG = 0.0 ;
		contexte 	= 0 ;
		front		= new Piles(nbre_piles) 	;
		front.copy(0,pile) 	;	
		System.out.println("taille de la pile NB à l'init = " + pile.size() ) ;
		p 			= new Points() ;
		
		this.D 		= D ;
		this.R 		= R ;
		this.TNB 	= TNB ;
		this.can   	 = can ;
		this.scale = scale ;
		this.tailleNarrowBand = tailleNarrowBand ;
		
		mapping = ZERO ;
		
		if ( imag!=null)
		{
			ima = imag ;
			XBase = ima.getX() ;
			YBase = ima.getY() ;
			
			this.gr = new double[XBase][YBase] ;
			this.gr = ((ImageBufferAnalyseGradient)ima).getGradient() ;
			
			im = new int[X][Y] ;
			im = ((ImageBuffer)ima).getBuffer() ;
			setImageTrue() ;
		}
		else
		{
			XBase = 350 ;
			YBase = 350 ;
			this.gr = new double[XBase][YBase] ;
		}
		X 		= (int)(XBase/scale) ;
		Y		= (int)(YBase/scale) ;
		
		if ( ima==null ) ima = new ImageBufferSynthese(X,Y) ;
		
		sm 			= new boolean	[X][Y] ;
		cd 			= new double	[X][Y] ;
		ls 			= new double	[X][Y] ;
		
	}
	//=======================================================================================================================
	// 	distanceMap
	// 	calcul
	//  paint
	//=======================================================================================================================
	public void 	distanceMap(Graphics g)
	{
		int           x,y,vvx,vvy,dis,distance_carre , m;
        Points        p , sommet ;
		
        dis = 0 ;

        while ( dis <= tailleNarrowBand )
        {       
                while ( !front.PileVide(dis) )
                {       
                		p 	= front.Depiler(dis);
 						
 						if ( p.x > 1 && p.x < X-1 && p.y > 1 && p.y < Y-1 )
 						
                        front.Empiler(tailleNarrowBand + 2,new Points(p.x,p.y));
                        
                        sommet  = neighbour(p,sm,X,Y);
                        
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
                            
                            m = 100 + (int)( cd[x][y]/(scale))*4 ;
                            
                            g.setColor(new Color(m,m,m)) ;
							g.fillRect(x*scale,y*scale,scale,scale) ;
							
		                	sommet = neighbour(p,sm,X,Y);
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
	public  void 	calcul() 
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
			ls[i][j] = cd[i][j] + T * ( Operateurs.F_curv(cd,i,j,D) + F_adv ) / ( 1.0 + gr[i][j] * gr[i][j] / 2 ) ;
        }

        while ( ! front.PileVide(tailleNarrowBand + 2+1) )
        {
            po = front.Depiler(tailleNarrowBand + 2+1) ;
            i = po.x;
            j = po.y;
                
            cd[i][j] = ls[i][j];
                
            if ( TestPoint(ls,i,j,TNB) == 1 )
	        {
               	front.Empiler(0,new Points(i,j));
                sm[i][j] = true;
               	cd[i][j] = 0.0 ;
        
               	ima.setPixel(i,j,255) ;
	        }
            else    
            {
            	ima.setPixel(i,j,0) ;
            	sm[i][j] = false;
            }
        }
	}
	//=======================================================================================================================
	public  void 	paint(Graphics g) 
	{
		ima.mapArea(g,0,0,X,Y,scale) ;
	}
	//=======================================================================================================================
}

//####################################################################################################################################################
