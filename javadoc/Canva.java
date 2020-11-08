
import java.awt.event.*;
import java.applet.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.awt.Graphics ;
import java.awt.Color ;
import java.awt.Font ;
import java.awt.Dimension ;
import java.util.Stack ;

//####################################################################################################################################################

public class Canva	extends JPanel implements MouseListener, MouseMotionListener, Runnable
{
	private final  int				BRUT = 1 ;
	private final  int				NARROW_BAND = 2 ;
	private final  int				FAST_MARCHING = 3 ;
	private final  int				HERMES = 4 ;
	
	private final  int				ZERO = 1 ;
	private final  int				ALL = 2 ;
	private final  int				NEGATIVE = 3 ;
	
	private 	Objets				objet ;
	private 	Front				front ;
	private 	Points				p, po , po_old  , pp;
	private		Images				im ;
	
	private 	Stack				pile , pile2 , pile_po , pile_po2 , collection , pilefinale ;
	private		Thread				thisThread  ;
	private 	Graphics			g ;
	private	 	Test				ap ;
	private	 	String				path ;
	
	private		double				reaction , diffusion , tnb ;
	private 	int					contexte, scale , indic , methode1 , methode2 , methode3 , compteur , i ,j , numframe , X , Y , DIM ;
	private 	boolean 			threadSuspended  ;
	
	
	//=======================================================================================================================
	public Canva(Test ap,int numframe)
	{
		DIM 			= ap.DIM ;
		this.numframe 	= numframe ;
		this.ap 		= ap ;
		
		methode1		= BRUT ;
		
		contexte 	= 0 ;
		scale  		= 3 ;
		indic       = 0 ;
		compteur	= 0 ;
		reaction	= 0 ;
		diffusion	= 2 ;
		tnb			= 6 ;
		X			= DIM ;
		Y			= DIM ;
		pile2 		= new Stack() ;
		
		po			= new Points() ;
		po_old		= new Points() ;
		pp			= new Points() ;
		p 			= new Points() ;
		
		threadSuspended = true ;
		
		//path = "angio.gif" ;
		
		path = "bloc.jpg" ;
		
		//path = "isobares.gif" ;
		
		// setPreferredSize(new Dimension(340,340));
		
		addMouseMotionListener(this);
		addMouseListener(this);
		
	}
	//=======================================================================================================================
	public void mouseMoved(MouseEvent e)  {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	//=======================================================================================================================
	public void mousePressed(MouseEvent e)  
	{
		if ( contexte != 8 )
		{	
			pile_po 	= new Stack() ;
			pile_po2 	= new Stack() ;
			contexte = 4 ;
		}
	}
	//=======================================================================================================================
	public void mouseDragged(MouseEvent e)
	{
		if ( contexte == 4 )
		{
			po.x	=	(int)((double)e.getX()/(double)scale)	;
			po.y	= 	(int)((double)e.getY()/(double)scale)	;
			
			if ( (po.x != po_old.x) || (po.y != po_old.y)  ) 
			{
				pile_po.push( new Points(po.x,po.y) ) ;
				repaint() ;
				po_old.x = po.x ;
				po_old.y = po.y ;
			}
		}
	}
	//=======================================================================================================================
	public void mouseReleased(MouseEvent e) 
	{
		if ( contexte == 4 ) contexte = 5 ;
	}
	//=======================================================================================================================
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g) ;
		
		if ( indic != 0 )
		{
			g.setColor(new Color(255,255,255)) ;
			g.setFont(new Font("Comic Sans MS",1,30)) ;
			g.drawString("INDIC = "+indic,10,50) ;
			indic=0 ;	
		}
		else
		switch(contexte)
		{	case 0 :
				setBackground(new Color(200,200,200)) ;
				try {
					im.mapArea(g,0,0,X,Y,scale) ;
					System.out.println("Affichage image") ;
				}
				catch(java.lang.NullPointerException e) {}
				break ;
			case 1 :
				if (im!=null)	im.mapArea(g,0,0,X,Y,scale) ;
				for(int i=0 ; i< collection.size() ; i++)
				{
					objet = (Objets)collection.elementAt(i) ;
					
					try {	
						objet.paint(g,scale) ; 
					}
					catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 1"); }
				}
				break ;
			case 2 :
				if (im!=null)	im.mapArea(g,0,0,X,Y,scale) ;
				try 
				{	while(!pile.empty())
					{	objet = (Objets)pile.pop() ;		
						objet.paint(g,scale) ; 
						pile2.push(objet);
					}
					while(!pile2.empty())
					{	objet = (Objets)pile2.pop() ;
						pile.push(objet);
					} 
				}
				catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 2"); }
				break ;
			case 3 :
				if (im!=null)	im.mapArea(g,0,0,X,Y,scale) ;
				try 
				{	while(!pile.empty())
					{	objet = (Objets)pile.pop() ;		
						objet.paint(g,scale) ; 
					}
				}
				catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 3"); }
				break ;
			case 4 :
				// if (im!=null)	im.mapArea(g,0,0,X,Y,scale) ;
				try 
				{	while(!pile_po.empty())
					{	
						pp = (Points)pile_po.pop() ;	
						g.fillRect( pp.x*scale , pp.y*scale , scale ,scale ) ;
						pile_po2.push(pp);
					}
					while(!pile_po2.empty())
					{	pp = (Points)pile_po2.pop() ;
						pile_po.push(pp);
					} 
				}
				catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 4"); }
				break ;
			case 5 :
				break ;
			case 6 :
				g.setColor(new Color(255,255,255)) ;
				if  ( front != null )
				{	front.SetGraphics(g) ;
					front.Labellisation(g) ;
					front.distanceMap(g) ;
					Run() ;
				}
				break ;	
			case 10 :
				front.copy() ;
				front.distanceMap(g) ;
				front.calcul() ;
				front.paint(g) ;
				break ;	
			case 20 :
				// im.mapArea(g,0,0,im.getX(),im.getY(),scale) ;
				im.mapArea(g,0,0,X,Y,scale) ;
				//im.mapGradient(g) ;
				//im.paint(g) ;
				break ;
			case 30 :
				
			default :
				break ;
		}
	}
	//=======================================================================================================================
	public void freeImage()
	{
		contexte = 0 ; 
		if ( im!=null) im = null ;
		X = DIM ;
		Y = DIM ;
		
		setPreferredSize(new Dimension(X*scale,Y*scale)) ;
		revalidate() ;
		
		repaint() ;
	}
	//=======================================================================================================================
	public void loadImage()
	{
		if ( im==null) 
		{
			System.out.println("Chargement image à scale " + scale ) ; 
			im = new ImageBufferAnalyseGradient(path , ap ) ;
			X = im.getX() ;
			Y = im.getY() ;
			ap.setSize(numframe,X,Y) ;
		}
		setPreferredSize(new Dimension(X*scale,Y*scale)) ;
		revalidate() ;
		
		contexte = 20; 
					
		repaint() ;
	}
	//=======================================================================================================================
	public void Run()
	{
		if ( compteur>0)
		{	
			System.out.println("Thread number " + compteur + " déjà en cours : thread stopped.") ; 
			thisThread.stop() ;
			compteur-- ;
		}
		compteur++ ;
		System.out.println("New Thread number : "+compteur+" created.") ; 
		thisThread = new Thread(this) ;
		thisThread.setPriority(Thread.MIN_PRIORITY) ;
		thisThread.start() ;
	}
	//=======================================================================================================================
	public void propagation()
	{
		if ( contexte == 10 )
		{	
			recallThread() ;
		}
		else
		if ( contexte !=0 && pile!=null   )
		if ( ! pilefinale.empty() )
		{ 
			threadSuspended = false ;
			
			switch(methode1)
			{
				case BRUT :
					if ( pilefinale != null )
					{	front	= new Brut(im,diffusion,reaction,pilefinale,this,scale,100000) ;
						System.out.println("	New BRUT levelset created .."); }
					break ;
				case NARROW_BAND :
					if ( pile != null )
					{	front	= new NarrowBand(im,diffusion,reaction,tnb,pilefinale,this,scale,1000,6) ;
						System.out.println("	New NARROW BAND levelset created .."); }
					break ;
				default :
					break ;
			}
			
			contexte = 6 ;
			repaint() ;
		}

	}
	//=======================================================================================================================
	public void run()
	{
		contexte  = 10 ; 
		
		while(true){	
			try {
				synchronized(this) {
                    while ( threadSuspended )
                        wait();	}}
                        
			catch(java.lang.InterruptedException e) 	{
				System.out.println("Interruption intercepted .."); 
				System.out.println("Thread  number "+compteur+" interrupted ..."); 
			}
			repaint() ;
		} 			
	}
	//=======================================================================================================================
	public synchronized void stopThread()
	{
		if ( compteur>0) System.out.println("Thread temporary stopped .."); 
		
		if ( ! threadSuspended ){	
			threadSuspended = true ;
       	}
    }
	//=======================================================================================================================
	public synchronized void recallThread()
	{	
		if ( compteur>0) System.out.println("Thread recall .."); 
		
		if (   threadSuspended )
		{	
			threadSuspended = false ;
       		notify() ;
       	}
    }
	//=======================================================================================================================
	public void message()
	{
		contexte = 5 ;
		repaint() ;
	}
	//=======================================================================================================================
	public void paint(int i)
	{
		indic = i ;
		repaint() ;
	}
	//=======================================================================================================================
	public void	paint(Stack pile)
	{
		contexte = 2 ;
		if (this.pile!=null)	this.pile = pile ;
		repaint() ;
	}
	//=======================================================================================================================
	public void	Add()
	{
		if ( pilefinale == null )	pilefinale 	= new Stack() ;
		if ( pile == null )			pile 		= new Stack() ;		
		if ( collection == null )	collection  = new Stack() ;
			
		if ( contexte == 5)
		{
			pile			= pile_po ;
			Libre objet 	= new Libre(pile) ;		
			objet.RemplissagePoints() ;	
			collection.push(objet) ;			
		}
					
		for(int i=0 ; i< pile.size() ; i++)
			pilefinale.push(pile.elementAt(i)) ;
			
		contexte = 1 ;
		
		repaint() ;
	}
	//=======================================================================================================================
	public void	Reset()
	{
		if (  collection != null )	 if ( ! collection.empty() )	
			while(!collection.empty())
				collection.pop() ;
					
		if ( pile != null )	 if ( ! pile.empty() )	
			while(!pile.empty())
				pile.pop() ;
			
		if ( pilefinale != null )	 if ( ! pilefinale.empty() )	
			while(!pilefinale.empty())
				pilefinale.pop() ;
				
		if (pile!=null) 		while( ! pile.empty() )			pile.pop() ;
		if (pile_po!=null)		while( ! pile_po.empty() )		pile_po.pop() ;
		if (pile2!=null)		while( ! pile2.empty() )		pile2.pop() ;
		if (pile_po2!=null)		while( ! pile_po2.empty() )		pile_po2.pop() ;
			
		if ( front!=null ) front.Reset() ;
		
		if ( compteur > 0 )
		{	
			System.out.println("Thread "+compteur+" finished ..."); 
			compteur--; 
			thisThread.stop() ;
		}
		contexte = 0 ;
		repaint() ;
	}
	//=======================================================================================================================
	public void changeMethode1(String s)
	{
		if ( s.equals("Brute"))			methode1 = BRUT ;
		if ( s.equals("Narrow Band"))	methode1 = NARROW_BAND ;
		if ( s.equals("Fast Marching"))	methode1 = FAST_MARCHING ;
		if ( s.equals("Hermes"))		methode1 = HERMES ;	
	}
	//=======================================================================================================================
	public void changeMethode2(String s)
	{
	}
	//=======================================================================================================================
	public void changeMethode3(String s)
	{
		if ( s.equals("Zero level"))		methode3 = ZERO ;
		if ( s.equals("All levels"))		methode3 = ALL ;
		if ( s.equals("Negative levels"))	methode3 = NEGATIVE ;
		
		if ( front!=null)
		{	
			front.changeMethode3(methode3) ;
		}
	}
	//=======================================================================================================================
	public void changeScale(int scale)
	{
		this.scale = scale ; 
		
		if ( front!=null)
		{	
			front.changeScale(scale) ;
		
			if ( threadSuspended ) 
				front.changeDim() ;
		}
		
		setPreferredSize(new Dimension(X*scale,Y*scale)) ;
		revalidate() ;
		
		repaint() ;
	}
	//=======================================================================================================================
	public void changeDiffusion(double D)
	{
		diffusion = D ;
		if ( front!=null)
			front.changeDiffusion(D) ;
	}
	//=======================================================================================================================
	public void changeReaction(double R)
	{
		reaction = R ;
		if ( front!=null)
			front.changeReaction(R) ;
	}
	//=======================================================================================================================
	public void changeTestNarrowBand(double TNB)
	{
		tnb = TNB ;
		if ( front!=null)
			front.changeTestNarrowBand(TNB) ;
	}
	//=======================================================================================================================
}
//####################################################################################################################################################