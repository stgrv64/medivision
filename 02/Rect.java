import 	java.awt.* ;
import  java.util.Stack ;
import	Objets ;
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
public class Rect extends Objets
{	
	private int 		x,y,width,height ;
	/*private Rectangle	rect ;*/
	//-------------------------------------------------------------------------------------
    Rect(int x, int y,int width , int height)
	{
		/*rect = new Rectangle(x,y,width,height) ; */
		
		/* inconvenient = on utilise 2* plus de mémoire en utilisant l'agrégation */
		
		this.x 		= x ;
		this.y 		= y ;
		this.width 	= width ;
		this.height = height ;
	}
	//--------------------------------------------------------------------------------------
    public  void 	dessin(Graphics g,int echelle) 
	{
		g.setColor(Color.red) ;
		g.drawRect( x * echelle , y * echelle , (x+width) * echelle , (x+height) * echelle );			
	}
	//--------------------------------------------------------------------------------------
	/*
	public  Stack    Transfert(int dim)
	{
		Stack pile = new Stack() ;
		
		for(int i=0 ; i<dim ; i++)
		for(int j=0 ; j<dim ; j++)
			if ( rect.contains(i,j) )
				pile.push(new Points(i,j)) ;
		
		return pile ;
	}
	//--------------------------------------------------------------------------------------
	*/
}
//oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
