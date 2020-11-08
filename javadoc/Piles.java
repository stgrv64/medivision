import  java.awt.* ;
import  java.util.Stack ;
import  java.util.TreeMap ;

//##########################################################################################################

public class Piles extends TreeMap
{	
	private	Stack 	pile ;
	private int 	incr , taillepile[] , nombre ;
	private String  indice ;
	//============================================================================================
	Piles(int nombre)
	{
		super() ;
		this.nombre = nombre ;
		incr 		= 0 ;
		taillepile 	= new int[nombre] ;
	}
	//============================================================================================
	public String 	Val(int i)
	{
		return 	String.valueOf(i) ;
	}
	//============================================================================================
	public boolean 	PilesVides()
	{
		if ( incr==0 )	return true ;
		else 			return false ;
	}
	//============================================================================================
	public int      TaillePile(int i)
	{
		return taillepile[i] ;
	}
	//============================================================================================
	public Points 	Element(int n,int i)
	{
		 Points	pointcourant = new Points() ;
		pointcourant = null ;
		
		indice 		 = Val(i) ;
		
		if ( taillepile[i]>0 ) 
		{
			pile 		 = (Stack)get(indice) ;
			pointcourant = (Points)pile.elementAt(i) ;
			put(indice,pile) ;
		}
		
		return pointcourant ;
	}
	//============================================================================================
	public void     Empiler(int i,Points p)
	{
		 Points	pointcourant = new Points() ;
		pointcourant = null ;
		
		indice = Val(i) ;
		
		if ( containsKey(indice) )	pile = (Stack)get(indice) ;	
		else						pile = new Stack() ;
			
		pointcourant = p ;
		pile.push(pointcourant) ;
		put(indice,pile) ;
		
		taillepile[i]++;
		incr++;
	}
	//============================================================================================
	public Points  	Depiler(int i)
	{
		Points	pointcourant = new Points() ;
		
		pointcourant = null ;
		indice 		 = Val(i) ;
			
		if ( taillepile[i]>0 ) 
		{
			pile = (Stack)get(indice) ;
			pointcourant = (Points)pile.pop() ;
			
			incr--;
			taillepile[i]--;	
		}
		
		return pointcourant ;
	}
	//============================================================================================
	public boolean  PileVide(int i)
	{
		if ( taillepile[i]==0 )	return true ;
		else 					return false ;
	}
	//============================================================================================
	public void 	Reset()
	{
		incr = 0 ;
		 
		for(int i=0;i<nombre;i++)	
		{
			if ( taillepile[i]>0 ) 
			{
				pile = (Stack)get(indice) ;
				pile.clear() ;	
				taillepile[i] 	= 0 ;
			}
		}	
	}
	//============================================================================================
	public void 	copy(int n,Stack p)
	{
		for(int i=0 ; i< p.size() ; i++)
		{
			Empiler(n,(Points)p.elementAt(i)) ;
		}
	}
	//============================================================================================
	public void 	paintPile(int num,Graphics g)
	{
		g.setColor(new Color(50+num*5,10+num*6,num*6)) ;
		g.fillRect(num*10 , 400-taillepile[num]*2,9,taillepile[num]*2 ) ;
	}
	//-------------------------------------------------------------------
	public void 	paintPile2(int num,Graphics g)
	{
		g.setColor(new Color(50+num*5,10+num*6,num*6)) ;
		g.fillRect(num*10 , 400-taillepile[num]*2,9,taillepile[num]*2 ) ;
		g.setColor(new Color(112,89,153)) ;
		g.fillRect(num*10 , 0 ,9, 400-taillepile[num]*2 ) ;
		
	}
	//-------------------------------------------------------------------
	public void 	paint(Graphics g)
	{
		for(int i=0; i<nombre ; i++ )
		{
			paintPile(i,g) ;
		}
	}
	//-------------------------------------------------------------------
	public void 	paint2(Graphics g)
	{
		for(int i=0; i<nombre ; i++ )
		{
			paintPile2(i,g) ;
		}
	}
	/*-------------------------------------------------------------------
	public void 	paintPointCourant(Graphics g)
	{
		g.setColor(new Color(0,0,0)) ;
		g.fillOval( 400 + pointcourant.x-2 ,pointcourant.y-2 , 5 , 5 ) ;
	}
	//============================================================================================*/
}
//##########################################################################################################