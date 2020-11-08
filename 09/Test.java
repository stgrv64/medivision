import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.lang.Math ;
import java.util.Stack ;

import Points ;
import Objets ;
import Operateurs ;
import ImageBufferAnalyseGradient ;

//####################################################################################################################################################

public class Test extends Applet implements ActionListener
{
    private ObjControl 	control[]  ;
    private ObjControl2 control2[]  ;    
    private ObjCanva 	canva[]  ;
    private JFrame		fra1[] , fra2[] , fra3[] ;
    private JPanel		pa ;
    private	JScrollPane sp[] ;
    private Button		bou , exit , help ;
    private Label		lab ;
    private int			incr = -1 ;
    
    public	static final int	DIM = 350 ;
    
    //=======================================================================================================================
    public void init() 
    {	
    	fra1     = new JFrame[5] ;
    	fra2     = new JFrame[5] ;
    	fra3     = new JFrame[5] ;
    	canva    = new ObjCanva[5] ;
    	control  = new ObjControl[5] ;
    	control2 = new ObjControl2[5] ;
    	sp		 = new JScrollPane[5] ;
    	
    	pa = new JPanel() ;
    	pa.setLayout(new FlowLayout()) ;
    	
    	bou  = new Button("View +") ;	bou.addActionListener(this) ;
    	exit = new Button("View -") ;	exit.addActionListener(this) ;
    	help = new Button("Help") ;		help.addActionListener(this) ;
    	
    	pa.setBackground(new Color(220,120,170));
    	pa.add(help) ;
		pa.add(bou) ;
		pa.add(exit) ;
		
		setLayout(new BorderLayout()) ;
		setBackground(new Color(220,120,170));
		add("North",new Label("    - L'approche LEVELSET en action -")) ;
		add("Center",pa) ;
		add("South",new Label(" - S. Gravois - stgrv@hotmail.com - © 2003 -")) ;
		setSize(290,80) ;
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
		fra3[a].setSize(x+10,y+50) ; fra2[a].show(); 
    }
    //=======================================================================================================================
    public  void actionPerformed(ActionEvent ev) 
    {	
    	if ( ev.getActionCommand().equals("View -") )	
    	{
    		//System.exit(0);
    		canva[incr].setEnabled(false) ;
    		control[incr].setEnabled(false);
    		control2[incr].setEnabled(false);
    		fra1[incr].setEnabled(false) ;
    		fra2[incr].setEnabled(false) ;
    		fra1[incr].hide() ;
    		fra2[incr].hide() ;
    		fra3[incr].hide() ;
    		remove(fra1[incr]) ;
    		remove(fra2[incr]) ;
    		remove(fra3[incr]) ;
    		incr-- ;
    	}
    	if ( ev.getActionCommand().equals("View +") && incr < 4 )	
    	{	
    		incr ++;	
    		
    		canva[incr]		= new ObjCanva(this,incr) ;
    		control2[incr]	= new ObjControl2(canva[incr]) ;
			control[incr]	= new ObjControl(canva[incr],control2[incr]) ;
			
			
			canva[incr].setEnabled(true);
			control[incr].setEnabled(true);
			control2[incr].setEnabled(true);
			
			canva[incr].setBackground(new Color(200,200,200));
			control[incr].setBackground(new Color(0,0,0));
			control2[incr].setBackground(new Color(0,0,0));
			
			sp[incr] = new JScrollPane(canva[incr]);
        	sp[incr].setPreferredSize(new Dimension(DIM,DIM));
        	
        	canva[incr].setPreferredSize(new Dimension(DIM+5,DIM+5));
        	canva[incr].revalidate() ;
        	
    		fra1[incr] = new JFrame() ;	fra1[incr].setTitle("Control "+incr);		fra1[incr].getContentPane().add(control[incr]) ; 	fra1[incr].setSize(300,330) ; 	fra1[incr].setLocation(incr*20		, 	150 + incr*20 ); 	fra1[incr].setVisible(true); 	fra1[incr].show() ;
			fra2[incr] = new JFrame() ; fra2[incr].setTitle("Parameters "+incr);	fra2[incr].getContentPane().add(control2[incr]) ; 	fra2[incr].setSize(350,230) ; 	fra2[incr].setLocation(300+incr*20	, 	400  + incr*20 ); 	fra2[incr].setVisible(true); 	fra2[incr].show() ;
			fra3[incr] = new JFrame() ; fra3[incr].setTitle("Canva "+incr);			fra3[incr].getContentPane().add(sp[incr]) ; 		fra3[incr].setSize(370,400) ; 	fra3[incr].setLocation(300+incr*20	,	incr*20 ); 			fra3[incr].setVisible(true); 	fra3[incr].show() ;
		}	
    }
	//=======================================================================================================================
}

//####################################################################################################################################################

class ObjCanva	extends JPanel implements MouseListener, MouseMotionListener, Runnable
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
	private 	int					contexte, echelle , indic , methode , compteur , i ,j , numframe , X , Y , DIM ;
	private 	boolean 			threadSuspended  ;
	
	
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
			System.out.println("Chargement image à echelle " + echelle ) ; 
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
//####################################################################################################################################################

class ObjControl2 extends JPanel implements ChangeListener
{
	private 	TextField 	ech_txt , diff_txt , reac_txt , testNB_txt ;
	public		JSlider		ech , diff , reac , testNB ; 	
	private		ObjCanva	can ;
	private		TitledBorder tt ;
	//=======================================================================================================================
    ObjControl2(ObjCanva can) 
    {
    	this.can = can ;
    	
    	GridLayout	GL	= new GridLayout(4,1) ;
    	FlowLayout	FL	= new FlowLayout(FlowLayout.LEFT) ;
    	
    	BevelBorder 	border = new BevelBorder(BevelBorder.LOWERED);
    	
    	JPanel 	PP 	  = new JPanel();
    	JPanel	FLC1  = new JPanel();
		JPanel	FLC2  = new JPanel();
		JPanel	FLC3  = new JPanel();
		JPanel	FLC4  = new JPanel();
		
		ech_txt 	= new TextField("3") ; 
    	diff_txt 	= new TextField("2") ; 
    	reac_txt 	= new TextField("0") ; 
    	testNB_txt	= new TextField("6") ; 
    	
		ech 	= new JSlider(1, 20, 3);	ech.addChangeListener(this);
		diff 	= new JSlider(-50, 50, 20);	diff.addChangeListener(this);
		reac 	= new JSlider(-20, 20, 0);	reac.addChangeListener(this);
		testNB 	= new JSlider(1, 15, 6);	testNB.addChangeListener(this);
		testNB.disable() ; 	
		
		FLC1.setLayout(FL) ;FLC1.add(ech) 	 ;	FLC1.add(ech_txt) ; 	FLC1.add(new Label("Scale")) ; 	
		FLC2.setLayout(FL) ;FLC2.add(diff) 	 ;	FLC2.add(diff_txt) ;	FLC2.add(new Label("Reaction")) ;	 	
		FLC3.setLayout(FL) ;FLC3.add(reac) 	 ;	FLC3.add(reac_txt) ;	FLC3.add(new Label("Diffusion")) ; 
		FLC4.setLayout(FL) ;FLC4.add(testNB) ;	FLC4.add(testNB_txt) ;	FLC4.add(new Label("Test NB")) ; 	
		
		FLC1.setBackground(new Color(120,120,120)) ; 
		FLC2.setBackground(new Color(120,120,120)) ; 
		FLC3.setBackground(new Color(120,120,120)) ; 
		FLC4.setBackground(new Color(120,120,120)) ; 
		
		PP.setBackground(new Color(0,0,0)) ;
		PP.setLayout(GL) ;	
		tt = new TitledBorder(border,"Propagation parameters",TitledBorder.CENTER,TitledBorder.ABOVE_TOP) ;
		tt.setTitleColor(new Color(150,150,255)) ;
		PP.setBorder(tt);
		PP.add(FLC1) ;
		PP.add(FLC2) ;
		PP.add(FLC3) ;
		PP.add(FLC4) ;
		
		add(PP) ;
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

class ObjControl extends JPanel implements ActionListener,ItemListener
{
    private 	TextField 	sur ;
    private		String		type="" ;
    private 	Choice		methode1 , methode2 , methode3 ;
 	private		Button		b1 , b2 , b3 , b4 , b5 , b6 , b7 , b8 ;
 	private		TitledBorder tt ;
    private		Objets		objet;
    private		ObjCanva	can ;
    private		ObjControl2	con ;
 	private 	int 		echelle ; 
		
	//=======================================================================================================================
    ObjControl(ObjCanva can,ObjControl2 con) 
    {
    	this.can = can ;
  		this.con = con ;
  		
  		JPanel 			PP = new JPanel();
		
		JPanel			PA 	= new JPanel();
		JPanel			PB 	= new JPanel();
		JPanel			PC 	= new JPanel();
		
		JPanel			P1 	= new JPanel();
		JPanel			P2  = new JPanel();
		
		JPanel			FLA11  = new JPanel();
		JPanel			FLA12  = new JPanel();
		JPanel			FLA13  = new JPanel();
		
		JPanel			FLA21  = new JPanel();
		JPanel			FLA22  = new JPanel();
		JPanel			FLA23  = new JPanel();
		
		JPanel			FLB1  = new JPanel();
		JPanel			FLB2  = new JPanel();
		JPanel			FLB3  = new JPanel();
		
		FlowLayout		FL	= new FlowLayout(FlowLayout.CENTER) ;
		
		GridLayout		GL	 = new GridLayout(2,1) ;
		GridLayout		GLA	 = new GridLayout(1,2) ;
		GridLayout		GLA1 = new GridLayout(3,1) ;
		GridLayout		GLA2 = new GridLayout(3,1) ;
		GridLayout		GLB	 = new GridLayout(3,1) ;
		
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
		
		// FLA1x -------------------------------------------------------------------------------------------------------
		
		FLA11.setLayout(FL) ;	
			FLA11.add(methode1) ;
		
		FLA12.setLayout(FL) ;
			b2 = new Button("Rst");b2.addActionListener(this);FLA12.add(b2);
			
		FLA13.setLayout(FL) ;
			b1 = new Button("Add");b1.addActionListener(this);FLA13.add(b1);
			b3 = new Button("Run");b3.addActionListener(this);FLA13.add(b3);
			b4 = new Button("Stp");b4.addActionListener(this);FLA13.add(b4);
		
		FLA11.setBackground(new Color(120,120,120)) ; 
		FLA12.setBackground(new Color(120,120,120)) ; 
		FLA13.setBackground(new Color(120,120,120)) ; 
		
		// FLA2x -----------------------------------------------------------------------------------------------------
		
		FLA21.setLayout(FL) ;	
			FLA21.add(methode2) ;
		
		FLA22.setLayout(FL) ;
		
		FLA23.setLayout(FL) ;
			b5 = new Button("Load");b5.addActionListener(this);FLA23.add(b5);
			b6 = new Button("Free");b6.addActionListener(this);FLA23.add(b6);
			
		FLA21.setBackground(new Color(120,120,120)) ; 
		FLA22.setBackground(new Color(120,120,120)) ; 
		FLA23.setBackground(new Color(120,120,120)) ; 
		
		// FLBx -----------------------------------------------------------------------------------------------------
		
		FLB1.setLayout(FL) ;	
			b1 = new Button("coucou");	b1.addActionListener(this);	FLB1.add(b1);
		
		FLB2.setLayout(FL) ;	
			b1 = new Button("c'est ");	b1.addActionListener(this);	FLB2.add(b1);
			
		FLB3.setLayout(FL) ;	
			b1 = new Button("moi");		b1.addActionListener(this);	FLB3.add(b1);
			
		FLB1.setBackground(new Color(120,120,120)) ; 
		FLB2.setBackground(new Color(120,120,120)) ; 
		FLB3.setBackground(new Color(120,120,120)) ; 
			
		// P1 -------------------------------------------------------------------------------------------------------
		P1.setBackground(new Color(0,0,0)) ;
		P1.setLayout(GLA1) ;
		tt = new TitledBorder(border,"Levelset",TitledBorder.CENTER,TitledBorder.ABOVE_TOP) ;
		tt.setTitleColor(new Color(150,150,255)) ;
		P1.setBorder(tt);
		P1.add(FLA11) ;
		P1.add(FLA12) ;
		P1.add(FLA13) ;
		// P2 -------------------------------------------------------------------------------------------------------
		P2.setBackground(new Color(0,0,0)) ;
		P2.setLayout(GLA2) ;
		tt = new TitledBorder(border,"Image",TitledBorder.CENTER,TitledBorder.ABOVE_TOP) ;
		tt.setTitleColor(new Color(150,150,255)) ;
		P2.setBorder(tt);
		P2.add(FLA21) ;
		P2.add(FLA22) ;
		P2.add(FLA23) ;
		// PA -------------------------------------------------------------------------------------------------------
		PA.setLayout(GLA) ;
		PA.add(P1) ;
		PA.add(P2) ;
		// PB -------------------------------------------------------------------------------------------------------
		PB.setBackground(new Color(0,0,0)) ;
		PB.setLayout(GLB) ;	
		tt = new TitledBorder(border,"Mapping methods",TitledBorder.CENTER,TitledBorder.ABOVE_TOP) ;
		tt.setTitleColor(new Color(150,150,255)) ;
		PB.setBorder(tt);
		PB.add(FLB1) ;
		PB.add(FLB2) ;
		PB.add(FLB3) ;
		// PP -------------------------------------------------------------------------------------------------------
		
		PP.setLayout(GL) ;
		
		PP.add(PA) ;
		PP.add(PB) ;
		
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
		System.out.println("Methode "+e.getItem() + " choisie...") ; 
		
		if ( e.getItem().equals("Narrow Band") ) 	con.testNB.enable() ;  
		else										con.testNB.disable() ; 
		
		//testNB.show() ;
		// testNB.setSize(new Dimension(100,10)) ;
		repaint() ;
		can.ChangerMethode( methode1.getSelectedItem() ) ;
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
