import java.awt.Point ;
import java.awt.Graphics ;
import java.awt.Color ;
//==============================================================================
class Points extends java.awt.Point
{
	public int x,y,vx,vy,indic ;
	//-------------------------------------------------------------------
	Points()
	{
		super(0,0);
		x = 0 ;
		y = 0 ;
	}
	//-------------------------------------------------------------------
	Points(int a,int b)
	{
		super(a,b);
		x = a ;
		y = b ;
	}
	//-------------------------------------------------------------------
	Points(int a,int b,int c,int d,int e)
	{
		super(a,b);
		x  = a ;
		y  = b ;
		vx = c ;
		vy = d ;
		indic = e ;
	}
	//-------------------------------------------------------------------
	public void Change(int a,int b,int c,int d,int e)
	{
		x  		= a ;
		y  		= b ;
		vx 		= c ;
		vy 		= d ;
		indic 	= e ;
	}
	//-------------------------------------------------------------------
	public void 	dessin(Graphics g,Color c,int echelle)
	{
		g.setColor(c) ;
		g.fillRect( x*echelle , y*echelle , echelle ,echelle ) ;
		
	}
	//-------------------------------------------------------------------
	public void 	dessin(Graphics g,int echelle)
	{
		g.fillRect( x*echelle , y*echelle , echelle ,echelle ) ;
	}
	//-------------------------------------------------------------------
}
//===============================================================================
