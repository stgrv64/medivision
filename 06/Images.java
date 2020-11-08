
//###################################################################################################################################

public abstract class Images extends Applet
{
	protected int R , G , B , echelle , X , Y ;
	//=======================================================================================================================
	public abstract	void 	Map(Graphics g) ;
	public abstract	void 	setPixel(int x,int y,int Rr,int Gg,int Bb,int echelle) ;
	public abstract	void	setPixel(int x,int y,int NB,int echelle) ;
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