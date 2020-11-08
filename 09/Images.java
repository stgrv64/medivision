
import java.applet.Applet ;
import java.awt.Graphics ;
import java.awt.Image ;

//###################################################################################################################################

public abstract class Images extends Applet
{
	protected Image			img ;
	protected int 			R , G , B , X , Y , DIM = 350 ;
	//=======================================================================================================================
	public abstract	void 	Map(Graphics g) ;
	public abstract	void 	MapZone(Graphics g,int a,int b,int A,int B) ;
	public abstract	void 	MapZoneEchelle(Graphics g,int a,int b,int A,int B,int echelle) ;
	public abstract	void	setPixel(int x,int y,int Rr,int Gg,int Bb) ;
	public abstract	void	setPixel(int x,int y,int NB) ;
	//=======================================================================================================================
	public 	int	getX() {
		return X ;
	}
	//=======================================================================================================================
	public 	int	getY() {
		return Y ;
	}
	//=======================================================================================================================
}
//###################################################################################################################################