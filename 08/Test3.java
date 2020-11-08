import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.lang.Math ;
import java.util.Stack;

//####################################################################################################################################################

public class Test extends Applet implements ActionListener
{
    private ObjControls controls[]  ;    
    private ObjCanvas 	canva[]  ;
    private JFrame		fra1[] , fra2[] ;
    private JPanel		pa ;
    private	JScrollPane sp[] ;
    private Button		bou , exit , help ;
    private Label		lab ;
    private int			incr = -1 ;
    //=======================================================================================================================
    public void init() 
    {	
    	fra1     = new JFrame[5] ;
    	fra2     = new JFrame[5] ;
    	canva    = new ObjCanvas[5] ;
    	controls = new ObjControls[5] ;
    	sp		 = new JScrollPane[5];
    	
    	pa = new JPanel() ;
    	pa.setLayout(new FlowLayout()) ;
    	
    	bou  = new Button("View +") ;	bou.addActionListener(this) ;
    	exit = new Button("View -") ;	exit.addActionListener(this) ;
    	help = new Button("Help") ;		help.addActionListener(this) ;
    	
    	pa.setBackground(new Color(200,100,150));
    	pa.add(help) ;
		pa.add(bou) ;
		pa.add(exit) ;
		
		setLayout(new BorderLayout()) ;
		setBackground(new Color(200,100,150));
		add("North",new Label("    - L'approche LEVELSET en action -")) ;
		add("Center",pa) ;
		add("South",new Label(" - S. Gravois - stgrv@hotmail.com - © 2003 -")) ;
		setSize(250,80) ;
    }
	//=======================================================================================================================
    public void destroy() 
    {
    }
	//=======================================================================================================================
    public void start() 
    {
		
    }
	//=======================================================================================================================
    public void stop() 
    {
		if (controls[incr]	!=null) 	controls[incr].setEnabled(false);
		if (canva[incr]		!=null)		canva[incr].setEnabled(false);
    }
	//=======================================================================================================================
    public void processEvent(AWTEvent e) 
    {
        if (e.getID() == Event.WINDOW_DESTROY || e.getID() == Event.ESCAPE ) 
            System.exit(0);
    }
	//=======================================================================================================================
    public void setSize(int a,int x,int y)
    {	
		fra2[a].setSize(x+10,y+50) ; fra2[a].show(); 
    }
    //=======================================================================================================================
    public  void actionPerformed(ActionEvent ev) 
    {	
    	if ( ev.getActionCommand().equals("View -") )	
    	{
    		// remove() ;
    	}
    	if ( ev.getActionCommand().equals("View +") && incr < 4 )	
    	{	
    		incr ++;	
    		
    		canva[incr]		= new ObjCanvas(this,incr) ;
			controls[incr]	= new ObjControls(canva[incr]) ;
			
			canva[incr].setEnabled(true);
			controls[incr].setEnabled(true);
			
			canva[incr].setBackground	(new Color(incr*50,255-incr*50,255));
			controls[incr].setBackground(new Color(incr*50,255-incr*50,255));
			
			sp[incr] = new JScrollPane(canva[incr]);
        	sp[incr].setPreferredSize(new Dimension(350,350));
        	
        	canva[incr].setPreferredSize(new Dimension(360,360));
        	canva[incr].revalidate() ;
        	
    		fra1[incr] = new JFrame() ;	fra1[incr].setTitle("Controls "+incr);	fra1[incr].setBackground(new Color(incr*50,incr*50,incr*50));	fra1[incr].getContentPane().add(controls[incr]) ; 	fra1[incr].setSize(370,390) ; 	fra1[incr].setLocation(100+(2*incr)*20,100); 	fra1[incr].setVisible(true); 	fra1[incr].show() ;
			fra2[incr] = new JFrame() ; fra2[incr].setTitle("Canvas "+incr);	fra2[incr].setBackground(new Color(incr*50,incr*50,incr*50));	fra2[incr].getContentPane().add(sp[incr]) ; 		fra2[incr].setSize(360,400) ; 	fra2[incr].setLocation(150+(2*incr+1)*20,150); 	fra2[incr].setVisible(true); 	fra2[incr].show() ;
		}	
    }
	//=======================================================================================================================
}

//####################################################################################################################################################

class ObjCanvas	extends JPanel implements MouseListener, MouseMotionListener, Runnable
{
	private final  int				BRUT = 1 ;
	private final  int				NARROW_BAND = 2 ;
	private final  int				FAST_MARCHING = 3 ;
	private final  int				HERMES = 4 ;
	
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
	public 		int					contexte, echelle , indic , methode , compteur , i ,j , numframe;
	private 	boolean 			threadSuspended  ;
	
	
	//=======================================================================================================================
	public ObjCanvas(Test ap,int numframe)
	{
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
				setBackground(new Color(100,100,100)) ;
				try {
					im.MapZoneEchelle(g,0,0,im.getX(),im.getY(),echelle) ;
					System.out.println("Affichage image") ;
				}
				catch(java.lang.NullPointerException e) {}
				break ;
			case 1 :
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
				g.setColor(new Color(255,255,255)) ;
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
				g.setColor(new Color(255,255,255)) ;
				try 
				{	while(!pile.empty())
					{	objet = (Objets)pile.pop() ;		
						objet.dessin(g,echelle) ; 
					}
				}
				catch(java.lang.NullPointerException e) { System.out.println("Une exception a eu lieu dans le contexte 3"); }
				break ;
			case 4 :
				// g.setColor(new Color(255,255,255)) ;
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
				g.setColor(new Color(0,0,0)) ;
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
				im.MapZoneEchelle(g,0,0,im.getX(),im.getY(),echelle) ;
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
		repaint() ;
	}
	//=======================================================================================================================
	public void ChargerImage()
	{
		if ( im==null) 
		{
			System.out.println("Chargement image à echelle " + echelle ) ; 
			im = new ImageBufferAnalyseGradient(path , ap ) ;
			ap.setSize(numframe,im.getX(),im.getY()) ;
		}
		setPreferredSize(new Dimension(im.getX()*echelle,im.getY()*echelle)) ;
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
		//if ( threadSuspended )
		{	
			if ( s.equals("Brute"))			methode = BRUT ;
			if ( s.equals("Narrow Band"))	methode = NARROW_BAND ;
			if ( s.equals("Fast Marching"))	methode = FAST_MARCHING ;
			if ( s.equals("Hermes"))		methode = HERMES ;
		}
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

//####################################################################################################################################################

class ObjControls extends JPanel implements ActionListener,ItemListener, ChangeListener
{
    private 	TextField 	ech_txt , diff_txt , reac_txt , testNB_txt ;
    private 	TextField 	sur ;
    private		String		type ;
    private		Objets		objet;
    private 	int 		m,n,o,p,o2,p2 ;
    private		ObjCanvas	can ;
 	private 	int 		echelle ; 
 	private 	Choice		methode1 , methode2 , methode3 ;
 	private		Button		b1 , b2 , b3 , b4 , b5 , b6 , b7 , b8 ;
	private		JSlider		ech , diff , reac , testNB ;
	
	//=======================================================================================================================
    public ObjControls(ObjCanvas can) 
    {
    	this.can = can ;
    	
    	m=n=o=p=o2=p2=0 ;
    	type 					= "" ;
    	
    	Button			bouton 	= null ;
    	
		JPanel 			PP 	= new JPanel();
		JPanel			PA 	= new JPanel();
		JPanel			PB 	= new JPanel();
		JPanel			P2 	= new JPanel();
		JPanel			P3  = new JPanel();
		
		JPanel			FL21  = new JPanel();
		JPanel			FL22  = new JPanel();
		JPanel			FL23  = new JPanel();
		JPanel			FL24  = new JPanel();
		
		JPanel			FL31  = new JPanel();
		JPanel			FL32  = new JPanel();
		JPanel			FL33  = new JPanel();
		JPanel			FL34  = new JPanel();
		
		JPanel			FLB1  = new JPanel();
		JPanel			FLB2  = new JPanel();
		JPanel			FLB3  = new JPanel();
		JPanel			FLB4  = new JPanel();
		
		FlowLayout		FL	= new FlowLayout(FlowLayout.LEFT) ;
		
		GridLayout		GL	= new GridLayout(2,1,1,1) ;
		GridLayout		GLA	= new GridLayout(1,2,1,1) ;
		GridLayout		GLB	= new GridLayout(4,1,1,1) ;
		
		GridLayout		GL2	= new GridLayout(4,1,1,1) ;
		GridLayout		GL3 = new GridLayout(4,1,1,1) ;
		
    	//-------------------------------------------------------------------------------------------------------
    	ech_txt 	= new TextField("3") ; 
    	diff_txt 	= new TextField("2") ; 
    	reac_txt 	= new TextField("0") ; 
    	testNB_txt	= new TextField("6") ; 
    	//-------------------------------------------------------------------------------------------------------
		ech 	= new JSlider(1, 20, 3);
		//ech.setSize(100,5) ;
		ech.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		diff 	= new JSlider(-50, 50, 20);
		diff.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		reac 	= new JSlider(-20, 20, 0);
		reac.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		testNB 	= new JSlider(1, 15, 6);
		testNB.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		methode1	= new Choice() ;
		methode1.addItemListener(this); 
		methode1.add("Brute");
		methode1.add("Narrow Band");
		methode1.add("Fast Marching");
		methode1.add("Hermes");
		//-------------------------------------------------------------------------------------------------------
		methode2	= new Choice() ;
		methode2.addItemListener(this); 
		methode2.add("Segmentation");
		methode2.add("Filtering");
		methode2.add("Synthesis");
		//-------------------------------------------------------------------------------------------------------
		
		BevelBorder 	border = new BevelBorder(BevelBorder.LOWERED);
		
		// FLBx --------------------------------------------------------------------------------------------------
		
		FLB1.setLayout(FL) ;	
		FLB1.add(ech) 	 ;	FLB1.add(ech_txt) ; 	FLB1.add( new Label("Scale      : ")) ; 
		
		FLB2.setLayout(FL) ;
		FLB2.add(diff) 	 ;	FLB2.add(diff_txt) ;	FLB2.add( new Label("Diffusion  : ")) ;	
		
		FLB3.setLayout(FL) ;
		FLB3.add(reac) 	 ;	FLB3.add(reac_txt) ;	FLB3.add( new Label("Reaction   : ")) ; 
		
		FLB4.setLayout(FL) ;
		FLB4.add(testNB) ;	FLB4.add(testNB_txt) ;	FLB4.add( new Label("Test NB    : ")) ; 	
		
		// PB -------------------------------------------------------------------------------------------------------
		PB.setBackground(new Color(50,150,200)) ;
		PB.setLayout(GLB) ;	
		PB.setBorder(new TitledBorder(border,"Propagation parameters",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
		PB.add(FLB1) ;
		PB.add(FLB2) ;
		PB.add(FLB3) ;
		PB.add(FLB4) ;
		
		// FL2x -------------------------------------------------------------------------------------------------------
		
		FL21.setLayout(FL) ;	
		
			FL21.add(new Label("Method : ")) ;
		
		FL22.setLayout(FL) ;
		
			FL22.add(methode1) ;
		
		// FL23.setLayout(FL) ;
		
		FL24.setLayout(FL) ;
		
			b1 = new Button("Add");
			b1.addActionListener(this);
			FL24.add(b1);
		
			b2 = new Button("Rst");
			b2.addActionListener(this);
			FL24.add(b2);
			
			b3 = new Button("Run");
			b3.addActionListener(this);
			FL24.add(b3);
		
			b4 = new Button("Stp");
			b4.addActionListener(this);
			FL24.add(b4);
		
		// P2 -------------------------------------------------------------------------------------------------------
		P2.setBackground(new Color(50,150,200)) ;
		P2.setLayout(GL2) ;
		P2.setBorder(new TitledBorder(border,"Levelset",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
		P2.add(FL21) ;
		P2.add(FL22) ;
		P2.add(FL23) ;
		P2.add(FL24) ;
		
		// FL3x -----------------------------------------------------------------------------------------------------
		
		FL31.setLayout(FL) ;	
			
			FL31.add(new Label("Method : ")) ;
		
		FL32.setLayout(FL) ;
		
			FL32.add(methode2) ;
		
		FL33.setLayout(FL) ;
		
		FL34.setLayout(FL) ;
		
			b5 = new Button("Load");
			b5.addActionListener(this);
			FL34.add(b5);
		
			b6 = new Button("Free");
			b6.addActionListener(this);
			FL34.add(b6);
		
		// P3 -------------------------------------------------------------------------------------------------------
		P3.setBackground(new Color(50,150,200)) ;
		P3.setLayout(GL3) ;
		P3.setBorder(new TitledBorder(border,"Image",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
		P3.add(FL31) ;
		P3.add(FL32) ;
		P3.add(FL33) ;
		P3.add(FL34) ;
		
		// PA -------------------------------------------------------------------------------------------------------
		
		PA.setLayout(GLA) ;
		PA.add(P2) ;
		PA.add(P3) ;
		
		// PP -------------------------------------------------------------------------------------------------------
		
		PP.setLayout(GL) ;
		
		PP.add(PA) ;
		PP.add(PB) ;
		
		//  -------------------------------------------------------------------------------------------------------
		// setBackground(new Color(150,170,220))  ;
		
    	add(PP) ;
    }
    //=======================================================================================================================
    public  void actionPerformed(ActionEvent ev) 
    {	
    	type = ev.getActionCommand() ;
    	
    	if ( type.equals("Add") )	can.Add() ;
		if ( type.equals("Rst") )	can.Reset() ;	
		if ( type.equals("Run") )	can.Propager() ;	
		if ( type.equals("Stp") )	can.StopperThread() ;	
		if ( type.equals("Load"))	can.ChargerImage() ;	
		if ( type.equals("Free"))	can.EnleverImage() ;		
	}
	//=======================================================================================================================
	public void itemStateChanged(ItemEvent e) 
	{
		System.out.println("  méthode "+e.getItem() + " choisie...") ; 
		can.ChangerMethode( methode1.getSelectedItem() ) ;
	}
	//=======================================================================================================================
	public void stateChanged(ChangeEvent e) {
	      
	    if ( (JSlider)e.getSource() == ech ){	
	    	can.ChangerEchelle( 	ech.getValue() ) ;
	    	ech_txt.setText(  Operateurs.Val(	ech.getValue()) );
	    }		    
	    if ( (JSlider)e.getSource() == diff ){	
	    	can.ChangerDiffusion( (double)diff.getValue() / 10.0 ) ;
	    	diff_txt.setText( Operateurs.Val( (double)diff.getValue() / 10.0 ) );
	    }		    
	    if ( (JSlider)e.getSource() == reac ){	
	    	can.ChangerReaction(  (double)reac.getValue() / 10.0 ) ;
	    	reac_txt.setText( Operateurs.Val( (double)reac.getValue() / 10.0 ) );	
	    }		    
	    if ( (JSlider)e.getSource() == testNB ){	
	    	can.ChangerTestNarrowBand( (double)testNB.getValue() ) ;	   	
	    	testNB_txt.setText( Operateurs.Val(	(double)testNB.getValue() ) );
	    }
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
