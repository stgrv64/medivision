import 	java.awt.*;
import  	java.applet.*;
import  	java.util.Stack ;
import		java.lang.Math ;
import		Objets ;
import 	Points ;
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
public class Libre extends Objets  
{	
	private Stack	pile  ;
	private Points	p , p1 , p2 ;
	
	//-----------------------------------------------------------------------------------------
	Libre(Stack pile)
	{
		this.pile 	= pile ; 
		p			= new Points(0,0) ;
	}
	//-----------------------------------------------------------------------------------------
	public  int 	Taille() 
	{
		return pile.size() ;
	}
	//-----------------------------------------------------------------------------------------
	public  Stack 	retourPile() 
	{
		return pile ;
	}
	//-----------------------------------------------------------------------------------------
	public  void 	dessin(Graphics g,int echelle) 
	{
		for(int i=0 ; i < pile.size() ; i++)			
		{	
			p = (Points)pile.elementAt(i) ;		
			p.dessin(g,echelle) ; 		
		}
	}
	//-----------------------------------------------------------------------------------------
	// remplissage de la pile "point" pour avoir un contour fermé de la forme => très important !!
	//-----------------------------------------------------------------------------------------
	public void  	RemplissagePoints()
	{
	  Stack	  pile2 = new Stack() ;
	  int     incr , i , j , i1 , j1 , i2 ,  j2 ;
      double  a ;	
      		
      p1	= new Points(0,0) ;
	  p2	= new Points(0,0) ;
			
	  if ( !pile.empty() ) 
	  {
		p1 			= (Points)pile.elementAt(0) ;
			
		for(incr=1 ; incr < pile.size() ; incr++)
		{	
			p2 			= (Points)pile.elementAt(incr) ;	
			
	        i1 = p1.x ;
    	    i2 = p2.x ;
        	j1 = p1.y ;
       		j2 = p2.y ;
        
        	if ( Math.abs(i2-i1)> Math.abs(j2-j1) )
        	{       
        			a = (double)(j2-j1)/(double)(i2-i1) ;
        			
                	if ( i2 > i1 )
                		for (i=i1+1 ; i<i2 ; i++ )
                		   	pile2.push(new Points(i, (int)(a * (double)(i-i1)+j1 ) )) ;
                		
                	else
                	if ( i1 > i2 )
                		for (i=i2+1 ; i<i1 ; i++ )
                	       	pile2.push(new Points(i, (int)(a * (double)(i-i2)+j2 ) )) ;
            }
        	else
        	{       
        			a = (double)(i2-i1)/(double)(j2-j1) ;
        			
                	if ( j2 > j1 )
                		for (j=j1+1 ; j<j2 ; j++ )
                			pile2.push(new Points( (int)(a * (double)(j-j1)+i1) , j )) ;
                	else
                	if ( j1 > j2 )
                		for (j=j2+1 ; j<j1 ; j++ )
                	       	pile2.push(new Points((int)(a * (double)(j-j2)+i2) , j )) ;
            }
        	p1 = p2 ;
        }
      }
      while ( !pile2.empty() ) 
      {
      		pile.push((Points)pile2.pop()) ;
      }
	  
	}
	//-----------------------------------------------------------------------------------------
}

//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
