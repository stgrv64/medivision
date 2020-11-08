import java.awt.*;
import java.applet.*;
import java.util.Stack ;
//=================================================================================
public abstract class Objets extends Object
{	
	protected	static	int 	dim = 400 ;  
	protected			Stack	pile ;	
	protected			int		x,y ;			// position des objets de type ellipse et rectangle
	protected			int		tnr ;			// taille de la narrow band
	protected			int		dimx = dim , 
								dimy = dim ;	// la taille de la zone utile du canvas
	//-------------------------------------------------------------------------
	public abstract void 	dessin(Graphics g,int echelle) ;
	/*public abstract Stack   Transfert(int dim) ;*/
	//-------------------------------------------------------------------------
	
}
//=================================================================================

