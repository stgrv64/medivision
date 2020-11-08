import  java.awt.* ;
import  java.util.Stack ;
import	java.lang.Math ;
import  Points ;
//==============================================================================
public class Piles
{	
	private	Stack 	[] 	pile ;
	private int 	[]	taillepile ;
	private	int 		nombre ;
	private int			incr = 0 ;
	private Points		pointcourant ;
	//-------------------------------------------------------------------
	Piles(int nombre)
	{
		this.nombre 	= nombre ;
		pointcourant 	= new Points(0,0) ;
		pile			= new Stack[nombre] ;
		taillepile		= new int[nombre] ;
		
		for(int i=0;i<nombre;i++)	
		{
			pile[i]			= new Stack() ;
			taillepile[i] 	= 0 ;
		}	
	}
	//-------------------------------------------------------------------
	public void 	Reset()
	{
		for(int i=0;i<nombre;i++)	
		{
			pile[i].clear() ;		
			taillepile[i] 	= 0 ;
		}	
		
	}
	//-------------------------------------------------------------------
	public Points 	Element(int n,int i)
	{
		return (Points)pile[n].elementAt(i) ;
	}
	//-------------------------------------------------------------------
	public void 	Copie(int n,Stack p)
	{
		for(int i=0 ; i< p.size() ; i++)
		{
			Empiler(n,(Points)p.elementAt(i)) ;
		}
	}
	//-------------------------------------------------------------------
	public void 	Transfert(int n,Stack p)
	{
		while( !p.empty() )
		{
			Empiler(n,(Points)p.pop()) ;
		}
	}
	//-------------------------------------------------------------------all_fifos_empty
	public boolean 	PilesVides()
	{
		if ( incr==0 )
			return true ;
		else 
			return false ;
	}
	//-------------------------------------------------------------------fifo_len
	public int      TaillePile(int i)
	{
		return taillepile[i] ;
	}
	//-------------------------------------------------------------------fifo_add
	public void     Empiler(int i,Points p)
	{
		pointcourant = p ;
		pile[i].push(p) ;
		taillepile[i] ++ ;
		incr++;
	}
	//-------------------------------------------------------------------fifo_first
	public Points  	Depiler(int i)
	{
		if ( taillepile[i]>0 ) 
		{	
			taillepile[i]-- ;
			incr--;
			pointcourant = (Points)pile[i].pop() ;
			return pointcourant ;
		}
		else 
			return null ;
	}
	//-------------------------------------------------------------------fifo_empty
	public boolean  PileVide(int i)
	{
		if ( taillepile[i]==0 )	return true ;
		else 					return false ;
	}
	//-------------------------------------------------------------------
	public void 	DessinPile(int num,Graphics g)
	{
		g.setColor(new Color(50+num*5,10+num*6,num*6)) ;
		g.fillRect(num*10 , 400-taillepile[num]*2,9,taillepile[num]*2 ) ;
	}
	//-------------------------------------------------------------------
	public void 	DessinPile2(int num,Graphics g)
	{
		g.setColor(new Color(50+num*5,10+num*6,num*6)) ;
		g.fillRect(num*10 , 400-taillepile[num]*2,9,taillepile[num]*2 ) ;
		g.setColor(new Color(112,89,153)) ;
		g.fillRect(num*10 , 0 ,9, 400-taillepile[num]*2 ) ;
		
	}
	//-------------------------------------------------------------------
	public void 	Dessin(Graphics g)
	{
		for(int i=0; i<nombre ; i++ )
		{
			DessinPile(i,g) ;
		}
	}
	//-------------------------------------------------------------------
	public void 	Dessin2(Graphics g)
	{
		for(int i=0; i<nombre ; i++ )
		{
			DessinPile2(i,g) ;
		}
	}
	//-------------------------------------------------------------------
	public void 	DessinPointCourant(Graphics g)
	{
		g.setColor(new Color(0,0,0)) ;
		g.fillOval( 400 + pointcourant.x-2 ,pointcourant.y-2 , 5 , 5 ) ;
	}
	//-------------------------------------------------------------------
}
//==============================================================================