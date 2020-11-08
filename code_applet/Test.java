import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.lang.Math ;
import java.util.Stack;
import Points ;
import Objets;

//####################################################################################################################################################

public class Test extends Applet 
{
    private ObjControls controls  ;    
    private ObjCanvas 	canva  ;
    private Label		lab ;
    private Frame		fra ;
    //=======================================================================================================================
    public void init() 
    {
		setLayout(new GridLayout(1,2));
		setBackground(new Color(70,60,50))  ;
		
		canva		= new ObjCanvas(this) ;
		canva.setBackground(new Color(200,200,250)) ;
		controls 	= new ObjControls(canva) ;
		
		add(controls) ;
		add(canva)    ;
    }
	//=======================================================================================================================
    public void destroy() 
    {
        remove(controls);
        remove(canva);
    }
	//=======================================================================================================================
    public void start() 
    {
		canva.setEnabled(true);
		controls.setEnabled(true);
    }
	//=======================================================================================================================
    public void stop() 
    {
		controls.setEnabled(false);
		canva.setEnabled(false);
    }
	//=======================================================================================================================
    public void processEvent(AWTEvent e) 
    {
        if (e.getID() == Event.WINDOW_DESTROY) 
            System.exit(0);
    }
	//=======================================================================================================================
    public String getAppletInfo() {
        return "Un test de simulation numérique avec les méthodes Levelset (courbes de niveaux)";
    }
    //=======================================================================================================================
}

//####################################################################################################################################################

class ObjCanvas	extends Canvas implements MouseListener, MouseMotionListener, Runnable
{
	private final  int			BRUT = 1 ;
	private final  int			NARROW_BAND = 2 ;
	private final  int			FAST_MARCHING = 3 ;
	private final  int			HERMES = 4 ;
	
	private 	Objets			objet ;
	private 	static Front	front ;
	private		ImageMedicale	im ;
	private 	Points			p, po , po_old  , pp;
	private 	Stack			pile,pile2,pile_po,pile_po2 ;
	private		Thread			thisThread  ;
	private 	Graphics		g ;
	public 		int				contexte, echelle , indic , methode , compteur ;
	private 	boolean 		threadSuspended  ;
	private	 	Applet			ap ;
	//=======================================================================================================================
	public ObjCanvas(Applet ap)
	{
		this.ap = ap ;
		
		methode		= NARROW_BAND ;
		contexte 	= 0 ;
		echelle  	= 3 ;
		indic       = 0 ;
		compteur	= 0 ;
		pile2 		= new Stack() ;
		
		po			= new Points() ;
		po_old		= new Points() ;
		pp			= new Points() ;
		p 			= new Points() ;
		
		threadSuspended = false ;
		
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
				//pp = po ;
				repaint() ;
				po_old.x = po.x ;
				po_old.y = po.y ;
			}
			//po_old = po ;
		}
		
	}
	//=======================================================================================================================
	public void mouseReleased(MouseEvent e) 
	{
		if ( contexte == 4 ) contexte = 5 ;
		/*this.dessin(100);	*/
	}
	//=======================================================================================================================
	public void paint(Graphics g)
	{
		
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
				setBackground(new Color(0,0,0)) ;
				g.setColor(new Color(255,255,255)) ;
				break ;
			case 1 :
				g.setColor(new Color(255,255,255)) ;
				objet.dessin(g,echelle) ; 
				break ;
			case 2 :
				g.setColor(new Color(255,255,255)) ;
				if (pile!=null && pile2!=null) {
				while(!pile.empty())
				{	objet = (Objets)pile.pop() ;		
					objet.dessin(g,echelle) ; 
					pile2.push(objet);
				}
				while(!pile2.empty())
				{	objet = (Objets)pile2.pop() ;
					pile.push(objet);
				} }
				break ;
			case 3 :
				g.setColor(new Color(255,255,255)) ;
				if (pile!=null)
				while(!pile.empty())
				{	objet = (Objets)pile.pop() ;		
					objet.dessin(g,echelle) ; 
				}
				break ;
			case 4 :
				g.setColor(new Color(255,255,255)) ;
				if (pile_po!=null && pile_po2!=null && pile_po!=null) {
				while(!pile_po.empty())
				{	
					pp = (Points)pile_po.pop() ;	
					g.fillRect( pp.x*echelle , pp.y*echelle , echelle ,echelle ) ;
					pile_po2.push(pp);
				}
				while(!pile_po2.empty())
				{	pp = (Points)pile_po2.pop() ;
					pile_po.push(pp);
				} }
				break ;
			case 5 :
				g.setColor(new Color(255,255,255)) ;
				g.setFont(new Font("Comic Sans MS",1,10)) ;
				g.drawString("Vous ne pouvez pas encore faire ça !",10,10) ;
				break ;
			case 6 :
				g.setColor(new Color(0,0,0)) ;
				front.SetGraphics(g) ;
				front.Labellisation() ;
				this.g = g ;
				front.CarteDistanceOptimale() ; 
				Run() ;
				break ;	
			case 7 :
				//CarteDistanceOptimale() ;
				break ;
			case 10 :
				front.Dessin(g) ;
				break ;	
			case 11 :
				//front.DessinNiveauZero(g) ;		/* nb uniquement */
				break ;	
			case 12 :
				//front.DessinCarteDistance(g) ;	/* nb uniquement */
				break ;	
			case 13 :
				im.Afficher(g) ;
				break ;
			default :
				break ;
		}
		// on dessine le dessin de l'objet grâce au contexte graphique du  canvas
		// ce qui permet effectivement d'avoir qqchose de dessiné dans le canvas
		
	}
	//=======================================================================================================================
	public void EnleverImage()
	{
		contexte = 0 ; 
		repaint() ;
	}
	//=======================================================================================================================
	public void ChargerImage()
	{
		im = new ImageMedicale("cartecalanques.gif" , ap ) ;
		contexte = 13; 
		repaint() ;
	}
	//=======================================================================================================================
	public void Run()
	{
		compteur++;
		System.out.println("New Thread "+compteur+" created ..") ; 
		
		if ( compteur == 1 )
		{	
			thisThread = new Thread(this) ;
			thisThread.setPriority(Thread.MIN_PRIORITY) ;
			thisThread.start() ;
		}
	}
	//=======================================================================================================================
	public void Propager(Stack pile)
	{
		if ( contexte == 10 )
		{	
			ReprendreThread() ;
		}
		else
		if ( contexte !=0 && pile!=null   )
		if ( ! pile.empty() )
		{ 
			System.out.println("New levelset created .."); 
			threadSuspended = false ;
			
			switch(methode)
			{
				case BRUT :
					if ( pile != null )
					front	= new Brut(pile,this,echelle,50000) ;
					break ;
				case NARROW_BAND :
					if ( pile != null )
					front	= new NarrowBand(pile,this,echelle,6) ;
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
		setBackground(new Color(0,0,0)) ;
		
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
			switch ( methode )
			{	
				case BRUT :
					front.Copie() ;               		/* brut */
					front.CarteDistanceOptimale() ;
					front.Calcul() ;
					repaint() ;	
					break ;
				case NARROW_BAND :
					front.Copie() ;   
					front.CarteDistanceOptimale() ;
					front.Calcul() ;
					repaint() ;	
				default :
					break ;
			}		
		} 			
	}
	//=======================================================================================================================
	public synchronized void StopperThread()
	{
		//	stop() ;	======> deprecated
		if ( compteur>0) System.out.println("Thread stopped .."); 
		
		if ( ! threadSuspended ){	
			threadSuspended = true ;
       	}
    }
	//=======================================================================================================================
	public synchronized void ReprendreThread()
	{	
		// le bloc de programme doit être synchronisé pour reprendre le "moniteur"
		//  suspend() ;	======> deprecated
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
	public void dessin(Objets objet)
	{
		contexte = 1 ;
		this.objet = objet ;
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
	public void	Reset()
	{
		if (pile!=null) 		while( ! pile.empty() )			pile.pop() ;
		if (pile_po!=null)		while( ! pile_po.empty() )		pile_po.pop() ;
		if (pile2!=null)		while( ! pile2.empty() )		pile2.pop() ;
		if (pile_po2!=null)		while( ! pile_po2.empty() )		pile_po2.pop() ;
			
		if ( front!=null ) front.Reset() ;
		
		contexte = 0 ;
		repaint() ;
		
		if ( compteur > 0 )
		{	
			System.out.println("Thread "+compteur+" finished ..."); 
			compteur--; 
			thisThread.stop() ;
		}
	}
	//=======================================================================================================================
	public void ChangerEchelle(int echelle)
	{
		this.echelle = echelle ; 
		if ( front!=null)
			front.ChangerEchelle(echelle) ;
	}
	//=======================================================================================================================
	public void ChangerDiffusion(double D)
	{
		if ( front!=null)
			front.ChangerDiffusion(D) ;
	}
	//=======================================================================================================================
	public void ChangerReaction(double R)
	{
		if ( front!=null)
			front.ChangerReaction(R) ;
	}
	//=======================================================================================================================
	public void ChangerTestNarrowBand(double TNB)
	{
		if ( front!=null)
			front.ChangerTestNarrowBand(TNB) ;
	}
	//=======================================================================================================================
	public	Stack	RetourPile()
	{
		return pile_po ;
	}
    //=======================================================================================================================
}

//####################################################################################################################################################

class ObjControls extends Panel implements ActionListener,ItemListener //, ChangeListener
{
    private 	TextField 	ech_txt , diff_txt , reac_txt , testNB_txt ;
    private 	TextField 	sur ;
    private		String		type, old_type;
    private		Objets		objet;
    private 	int 		m,n,o,p,o2,p2 ;
    private 	Stack 		collection,pile ;
 	private		ObjCanvas	can ;
 	private 	int 		echelle ; 
 	private 	Choice		methode1 , methode2 , methode3 ;
 	private		Button		b1 , b2 , b3 , b4 , b5 , b6 , b7 , b8 ;
	//private		JSlider		ech , diff , reac , testNB ;
	
	//=======================================================================================================================
    public ObjControls(ObjCanvas can) 
    {
    	this.can = can ;
    	
    	m=n=o=p=o2=p2=0 ;
    	type 					= "" ;
    	old_type 				= "" ;
    	
    	Button			bouton 	= null ;
    	/*
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
		*/
		Panel 			PP 	= new Panel();
		Panel			PA 	= new Panel();
		Panel			PB 	= new Panel();
		Panel			P2 	= new Panel();
		Panel			P3  = new Panel();
		Panel			FL21  = new Panel();
		Panel			FL22  = new Panel();
		Panel			FL23  = new Panel();
		Panel			FL24  = new Panel();
		Panel			FL31  = new Panel();
		Panel			FL32  = new Panel();
		Panel			FL33  = new Panel();
		Panel			FL34  = new Panel();
		Panel			FLB1  = new Panel();
		Panel			FLB2  = new Panel();
		Panel			FLB3  = new Panel();
		Panel			FLB4  = new Panel();
		
		FlowLayout		FL	= new FlowLayout(FlowLayout.LEFT) ;
		
		GridLayout		GL	= new GridLayout(2,1,1,1) ;
		GridLayout		GLA	= new GridLayout(1,2,1,1) ;
		GridLayout		GLB	= new GridLayout(4,1,1,1) ;
		
		GridLayout		GL2	= new GridLayout(4,1,1,1) ;
		GridLayout		GL3 = new GridLayout(4,1,1,1) ;
		
    	collection  = new Stack() ;
    	//-------------------------------------------------------------------------------------------------------
    	ech_txt 	= new TextField("3") ; 
    	diff_txt 	= new TextField("2") ; 
    	reac_txt 	= new TextField("0") ;  
    	testNB_txt	= new TextField("6") ; 
    	/*-------------------------------------------------------------------------------------------------------
		ech 	= new JSlider(0, 20, 3);
		ech.setSize(100,5) ;
		ech.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		diff 	= new JSlider(0, 30, 20);
		diff.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		reac 	= new JSlider(-20, 20, 0);
		reac.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------
		testNB 	= new JSlider(5, 15, 6);
		testNB.addChangeListener(this);
    	//-------------------------------------------------------------------------------------------------------*/
		methode1	= new Choice() ;
		methode1.addItemListener(this); 
		methode1.add("Brute");
		methode1.add("Narrow band");
		methode1.add("Fast marching");
		methode1.add("Hermes");
		//-------------------------------------------------------------------------------------------------------
		methode2	= new Choice() ;
		methode2.addItemListener(this); 
		methode2.add("Segmentation");
		methode2.add("Filtering");
		methode2.add("Synthesis");
		//-------------------------------------------------------------------------------------------------------
		
		//BevelBorder 	border = new BevelBorder(BevelBorder.LOWERED);
		
		// FLBx --------------------------------------------------------------------------------------------------
		
		FLB1.setLayout(FL) ;	
		/* FLB1.add(ech) 	 ; */	FLB1.add(ech_txt) ; 	FLB1.add( new Label("Scale     : ")) ; 
		
		FLB2.setLayout(FL) ;
		/* FLB2.add(diff) 	 ; */ 	FLB2.add(diff_txt) ;	FLB2.add( new Label("Diffusion : ")) ;	
		
		FLB3.setLayout(FL) ;
		/* FLB3.add(reac) 	 ; */	FLB3.add(reac_txt) ;	FLB3.add( new Label("Reaction  : ")) ; 
		
		FLB4.setLayout(FL) ;
		/* FLB4.add(testNB) ;  */	FLB4.add(testNB_txt) ;	FLB4.add( new Label("Test NB   : ")) ; 	
		
		// PB -------------------------------------------------------------------------------------------------------
		PB.setBackground(new Color(150,150,250)) ;
		PB.setLayout(GLB) ;	
		//PB.setBorder(new TitledBorder(border,"Propagation parameters",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
		PB.add(FLB1) ;
		PB.add(FLB2) ;
		PB.add(FLB3) ;
		PB.add(FLB4) ;
		
		// FL2x -------------------------------------------------------------------------------------------------------
		
		FL21.setLayout(FL) ;	
		
			FL21.add(new Label("Method : ")) ;
		
		FL22.setLayout(FL) ;
		
			FL22.add(methode1) ;
		
		FL23.setLayout(FL) ;
		
			b1 = new Button("Add");
			b1.addActionListener(this);
			FL23.add(b1);
		
			b2 = new Button("Reset");
			b2.addActionListener(this);
			FL23.add(b2);
		
		FL24.setLayout(FL) ;
		
			b3 = new Button("Run");
			b3.addActionListener(this);
			FL24.add(b3);
		
			b4 = new Button("Stop");
			b4.addActionListener(this);
			FL24.add(b4);
		
		// P2 -------------------------------------------------------------------------------------------------------
		P2.setBackground(new Color(150,150,250)) ;
		P2.setLayout(GL2) ;
		//P2.setBorder(new TitledBorder(border,"Levelset",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
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
		
			b6 = new Button("Erase");
			b6.addActionListener(this);
			FL34.add(b6);
		
		// P3 -------------------------------------------------------------------------------------------------------
		P3.setBackground(new Color(150,150,250)) ;
		P3.setLayout(GL3) ;
		//P3.setBorder(new TitledBorder(border,"Image",TitledBorder.CENTER,TitledBorder.ABOVE_TOP));
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
		
    	add(PP) ;
    }
    //=======================================================================================================================
    public  void actionPerformed(ActionEvent ev) 
    {	
    	type = ev.getActionCommand() ;
    	
    	m  = (int)(Math.random() *  150 ) ;
		n  = (int)(Math.random() *  150 ) ;
		o  = (int)(Math.random() *  150 ) ;
		p  = (int)(Math.random() *  150 ) ;
		o2 = (int)(Math.random() * (150-o) );
		p2 = (int)(Math.random() * (150-p) );
		
		//------------------------------------------------------------------------------------------------------------
		if (   type.equals("Add") )	
		{	
			if ( can.contexte == 5 ) 
			{	
				if (pile!=null)		pile 		= new Stack() ;		
				pile		= can.RetourPile() ;
				Libre obj 	= new Libre(pile) ;	
				obj.RemplissagePoints() ;		
				collection.push(obj) ;			
				objet 		= obj ;				
			}
			/*	
			if ( ! old_type.equals("Add" ) && ! old_type.equals("Dépiler" ) ) 
				collection.push(objet) ;
			*/
		}	
		/*------------------------------------------------------------------------------------------------------------
		if ( type.equals("Ellipse") )								
    		objet = new Ellipse(m,n,o,p) ;
    	if (   type.equals("Rectangle") )	
    		objet = new Rect(m,n,o2,p2) ;
    	if ( type.equals("Dépiler") )		if ( ! collection.empty() )	
    		can.dessin(collection) ;
    	if ( type.equals("Rectangle") || type.equals("Ellipse") ) 	
			can.dessin(objet) ;
		//------------------------------------------------------------------------------------------------------------*/
		if ( type.equals("Add") )		
		  if ( objet != null ) 		
			can.dessin(objet) ;
			
		if ( type.equals("Reset") )			
		{	
		  	if ( ! collection.empty() )	
				while(!collection.empty())
					collection.pop() ;
					
			can.Reset() ;
		}
			
		if ( type.equals("Run") )	
		  if ( pile != null ) 							
			can.Propager(pile) ;
			
		if ( type.equals("Stop") )						
			can.StopperThread() ;
			
		
		if ( type.equals("Load"))
			can.ChargerImage() ;
			
		if ( type.equals("Erase"))
			can.EnleverImage() ;
		
		old_type = type ;
		
    }
	//=======================================================================================================================
	public void itemStateChanged(ItemEvent e) 
	{
		/*
		if ( e.getItem() == methode1 )
			can.ChangerEchelle( (int)Val(ch.getSelectedItem()) ) ;
			
		if ( e.getItem() == methode2 )
			can.ChangerEchelle( (int)Val(ch.getSelectedItem()) ) ;
		*/
	}
	/*=======================================================================================================================
	public void stateChanged(ChangeEvent e) {
	      
	    if ( (JSlider)e.getSource() == ech )
	    {	
	    	can.ChangerEchelle( 	ech.getValue() ) ;
	    	ech_txt.setText( Val(	ech.getValue()) );
	    }	
	    
	    if ( (JSlider)e.getSource() == diff )
	    {	
	    	can.ChangerDiffusion( (double)diff.getValue() / 10.0 ) ;
	    	diff_txt.setText(Val( (double)diff.getValue() / 10.0 ) );
	    }	
	    
	    if ( (JSlider)e.getSource() == reac )
	    {	
	    	can.ChangerReaction( 	 (double)reac.getValue() / 10.0 ) ;
	    	reac_txt.setText(Val((double)reac.getValue() / 10.0 ) );	
	    }	
	    
	    if ( (JSlider)e.getSource() == testNB )
	    {	
	    	can.ChangerTestNarrowBand( (double)testNB.getValue() ) ;	   	
	    	testNB_txt.setText(Val(	(double)testNB.getValue() ) );
	    }
	}
	//=======================================================================================================================*/
	public String 	Val(int i)
	{
		return 	String.valueOf(i) ;
	}
	//=======================================================================================================================
	public String 	Val(double i)
	{
		return 	String.valueOf((double)i) ;
	}
	//=======================================================================================================================
	public double 	Val(String s)
	{
		return  Double.valueOf(s).doubleValue();;		
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
