import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.lang.Math ;
import java.util.Stack ;

class ObjCanva	extends JPanel implements MouseListener, MouseMotionListener, Runnable
{
	private static final  int				BRUT = 1 ;
	private static final  int				NARROW_BAND = 2 ;
	private static final  int				FAST_MARCHING = 3 ;
	private static final  int				HERMES = 4 ;
	
	private      Objets				objet ;
	private 	 Front				front ;
	private 	 Points				p, po , po_old  , pp;
	private		 Images				im ;
	
	private 	 Stack				pile , pile2 , pile_po , pile_po2 , collection , pilefinale ;
	private		 Thread				thisThread  ;
	private 	 Graphics			g ;
	private	 	 Test				ap ;
	private	 	 String				path ;
	
	private		 double     reaction , diffusion , tnb ;
	private 	 int		contexte, echelle , indic , methode , compteur , i ,j , numframe , X , Y , DIM ;
	private 	 boolean    threadSuspended  ;
	
	
	//=======================================================================================================================
	public ObjCanva(Test ap,int numframe)
	{
		DIM = ap.DIM ;
		this.numframe = numframe ;
		this.ap = ap ;
		
		methode		= BRUT ;
		
		contexte 	= 0 ;
		echelle  	= 3 ;
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
			po.x	=	(int)((double)e.getX()/(double)echelle)	;
			po.y	= 	(int)((double)e.getY()/(double)echelle)	;
			
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
					im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
					System.out.println("Affichage image") ;
				}
				catch(java.lang.NullPointerException e) {}
				break ;
			case 1 :
				if (im!=null)	im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
				for(int i=0 ; i< collection.size() ; i++)
				{
					objet = (Objets)collection.elementAt(i) ;
					
					try {	
						objet.dessin(g,echelle) ; 
					}
					catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 1"); }
				}
				break ;
			case 2 :
				if (im!=null)	im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
				try 
				{	while(!pile.empty())
					{	objet = (Objets)pile.pop() ;		
						objet.dessin(g,echelle) ; 
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
				if (im!=null)	im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
				try 
				{	while(!pile.empty())
					{	objet = (Objets)pile.pop() ;		
						objet.dessin(g,echelle) ; 
					}
				}
				catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 3"); }
				break ;
			case 4 :
				// if (im!=null)	im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
				try 
				{	while(!pile_po.empty())
					{	
						pp = (Points)pile_po.pop() ;	
						g.fillRect( pp.x*echelle , pp.y*echelle , echelle ,echelle ) ;
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
					front.CarteDistanceOptimale(g) ;
					Run() ;
				}
				break ;	
			case 10 :
				front.Copie() ;
				front.CarteDistanceOptimale(g) ;
				front.Calcul() ;
				front.Dessin(g) ;
				break ;	
			case 20 :
				// im.MapZone(g,0,0,im.getX(),im.getY(),echelle) ;
				im.MapZoneEchelle(g,0,0,X,Y,echelle) ;
				//im.MapGradient(g) ;
				//im.dessin(g) ;
				break ;
			case 30 :
				
			default :
				break ;
		}
	}
	//=======================================================================================================================
	public void EnleverImage()
	{
		contexte = 0 ; 
		if ( im!=null) im = null ;
		X = DIM ;
		Y = DIM ;
		
		setPreferredSize(new Dimension(X*echelle,Y*echelle)) ;
		revalidate() ;
		
		repaint() ;
	}
	//=======================================================================================================================
	public void ChargerImage()
	{
		if ( im==null) 
		{
			System.out.println("Chargement image � echelle " + echelle ) ; 
			im = new ImageBufferAnalyseGradient(path , ap ) ;
			X = im.getX() ;
			Y = im.getY() ;
			ap.setSize(numframe,X,Y) ;
		}
		setPreferredSize(new Dimension(X*echelle,Y*echelle)) ;
		revalidate() ;
		
		contexte = 20; 
					
		repaint() ;
	}
	//=======================================================================================================================
	public void Run()
	{
		if ( compteur>0)
		{	
			System.out.println("Thread number " + compteur + " d�j� en cours : thread stopped.") ; 
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
	public void Propager()
	{
		if ( contexte == 10 )
		{	
			ReprendreThread() ;
		}
		else
		if ( contexte !=0 && pile!=null   )
		if ( ! pilefinale.empty() )
		{ 
			threadSuspended = false ;
			
			switch(methode)
			{
				case BRUT :
					if ( pilefinale != null )
					{	front	= new Brut(im,diffusion,reaction,pilefinale,this,echelle,100000) ;
						System.out.println("	New BRUT levelset created .."); }
					break ;
				case NARROW_BAND :
					if ( pile != null )
					{	front	= new NarrowBand(im,diffusion,reaction,tnb,pilefinale,this,echelle,1000,6) ;
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
	public synchronized void StopperThread()
	{
		if ( compteur>0) System.out.println("Thread temporary stopped .."); 
		
		if ( ! threadSuspended ){	
			threadSuspended = true ;
       	}
    }
	//=======================================================================================================================
	public synchronized void ReprendreThread()
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
	public void dessin(int i)
	{
		indic = i ;
		repaint() ;
	}
	//=======================================================================================================================
	public void	dessin(Stack pile)
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
	public void ChangerMethode(String s)
	{
		if ( s.equals("Brute"))			methode = BRUT ;
		if ( s.equals("Narrow Band"))	methode = NARROW_BAND ;
		if ( s.equals("Fast Marching"))	methode = FAST_MARCHING ;
		if ( s.equals("Hermes"))		methode = HERMES ;	
	}
	//=======================================================================================================================
	public void ChangerEchelle(int echelle)
	{
		this.echelle = echelle ; 
		
		if ( front!=null)
		{	
			front.ChangerEchelle(echelle) ;
		
			if ( threadSuspended ) 
				front.ChangerDim() ;
		}
		
		setPreferredSize(new Dimension(X*echelle,Y*echelle)) ;
		revalidate() ;
		
		repaint() ;
	}
	//=======================================================================================================================
	public void ChangerDiffusion(double D)
	{
		diffusion = D ;
		if ( front!=null)
			front.ChangerDiffusion(D) ;
	}
	//=======================================================================================================================
	public void ChangerReaction(double R)
	{
		reaction = R ;
		if ( front!=null)
			front.ChangerReaction(R) ;
	}
	//=======================================================================================================================
	public void ChangerTestNarrowBand(double TNB)
	{
		tnb = TNB ;
		if ( front!=null)
			front.ChangerTestNarrowBand(TNB) ;
	}
	//=======================================================================================================================
}
