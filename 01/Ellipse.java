import 	java.awt.* ;
import  java.awt.geom.Ellipse2D.Double ;
import  java.util.Stack ;
import	Objets ;
//=================================================================================
public class Ellipse extends Objets
{	//-------------------------------------------------------------------------
	private int					x,y,width,height ;
	/*private Ellipse2D.Double	elli ;*/
	//-------------------------------------------------------------------------
	Ellipse(int x , int y, int width, int height)
	{
		/*elli = new Ellipse2D.Double((double)x,(double)y,(double)width,(double)height) ;*/
		
		/* inconvenient = on utilise 2* plus de mémoire en utilisant l'agrégation */
		
		this.width  = width ;
		this.height = height ;
		this.x      = x ;
		this.y      = y ;
	}
	//-------------------------------------------------------------------------
	public  void 	dessin(Graphics g,int echelle) 
	{
		g.setColor(Color.blue) ;
		g.drawOval(x*echelle , y*echelle , width*echelle , height*echelle );
	}
	//--------------------------------------------------------------------------------------
	/*
	public  Stack    Transfert(int dim)
	{
		Stack pile = new Stack() ;
		
		for(int i=0 ; i<dim ; i++)
		for(int j=0 ; j<dim ; j++)
			if ( elli.contains((double)i,(double)j) )
					pile.push(new Points(i,j)) ;
		
		return pile ;
	}
	//-------------------------------------------------------------------------
	*/
}
//=================================================================================
