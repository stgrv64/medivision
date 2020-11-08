import  java.awt.Color ;
import  java.awt.Font ;
import  java.util.Stack ;
import	java.lang.Math ;
import  java.awt.Graphics ;
//####################################################################################################################################################
class Brut extends Front 
{	
	//=======================================================================================================================
	Brut(Stack pile,ObjCanvas can,int echelle,int tailleNarrowBand)
	{
		D 	= 2.0 ;
		R 	= 0.0 ;
		
		contexte 	= 0 ;
		
		front		= new Piles(N+2) 	;
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
 						
                        front.Empiler(tailleNarrowBand + 2,p);
                        
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
        while ( ! front.PileVide(tailleNarrowBand + 2)  )
        {
                p = front.Depiler(tailleNarrowBand + 2);
        }
	}
	//=======================================================================================================================
	public void 	Calcul()
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
	
}

//####################################################################################################################################################
