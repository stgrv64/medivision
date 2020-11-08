import java.awt.Point ;
import java.awt.Graphics ;
import java.awt.Color ;

//==============================================================================
public class Points extends java.awt.Point
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
	public void 	paint(Graphics g,Color c,int scale)
	{
		g.setColor(c) ;
		g.fillRect( x*scale , y*scale , scale ,scale ) ;
		
	}
	//-------------------------------------------------------------------
	public void 	paint(Graphics g,int scale)
	{
		g.fillRect( x*scale , y*scale , scale ,scale ) ;
	}
	//-------------------------------------------------------------------
}
//===============================================================================
