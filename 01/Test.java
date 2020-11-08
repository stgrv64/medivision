import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.lang.Math ;
import java.util.Stack;
import Points ;
/*import Libre;*/
import Objets;
import Front ;
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
public class Test extends Applet 
{
    private ObjControls controls;    
    private ObjCanvas 	canva ;
    private Label		lab ;
    
    //---------------------------------------------------------------------------------------------------------------------------
    public void init() 
    {
		setLayout(new BorderLayout());
		
		canva		= new ObjCanvas() ;
		controls 	= new ObjControls(canva) ;
		
		add("North",lab = new Label("Une application utilisant un héritage d'objet et une pile d'objets")) ;
		
		add("Center",canva )   ;
		add("South",controls ) ;
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public void destroy() 
    {
        remove(controls);
        remove(canva);
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public void start() 
    {
		canva.setEnabled(true);
		controls.setEnabled(true);
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public void stop() 
    {
		controls.setEnabled(false);
		canva.setEnabled(false);
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public void processEvent(AWTEvent e) 
    {
        if (e.getID() == Event.WINDOW_DESTROY) 
            System.exit(0);
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public static void main(String args[]) 
    {
		Frame	f 		= new Frame("ObjTest2");
		Test	test 	= new Test();

		test.init();
		test.start();

		f.add("Center", test);
		f.setSize(200, 200);
		f.show();
    }
	//---------------------------------------------------------------------------------------------------------------------------
    public String getAppletInfo() {
        return "Un test de simulation numérique avec les méthodes Levelset (courbes de niveaux)";
    }
    //---------------------------------------------------------------------------------------------------------------------------
}
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
class ObjCanvas	extends Canvas implements MouseListener, MouseMotionListener, Runnable
{
	public		Front		front ;
	private 	Objets		objet ;
	private 	Points		po , po_old  , pp;
	
	private 	Stack		pile,pile2,pile_po,pile_po2 ;
	private 	Thread		frThread ;
	private		Graphics	gg ;
	
	public 		int			contexte, echelle , indic   ;
	private 	volatile boolean		threadSuspended ;
	
	//---------------------------------------------------------------------------------------------------------------------------
	public ObjCanvas()
	{
		contexte 	= 0 ;
		echelle  	= 1 ;
		indic       = 0 ;
		pile2 		= new Stack() ;
		po			= new Points(1,1) ;
		po_old		= new Points(0,0) ;
		pp			= new Points(0,0) ;
		
		threadSuspended = true ;
		
		addMouseMotionListener(this);
		addMouseListener(this);

	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void mouseMoved(MouseEvent e)  		{}
	public void mouseEntered(MouseEvent e)  	{}
	public void mouseExited(MouseEvent e) 		{}
	public void mouseClicked(MouseEvent e) 		{}
	//---------------------------------------------------------------------------------------------------------------------------
	public void mousePressed(MouseEvent e)  	
	{
		if ( contexte != 8 )
		{	
			pile_po 	= new Stack() ;
			pile_po2 	= new Stack() ;
			/*paramstring = e.paramString() ; */
			contexte = 4 ;
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------
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
	//---------------------------------------------------------------------------------------------------------------------------
	public void mouseReleased(MouseEvent e) 
	{
		if ( contexte == 4 ) contexte = 5 ;
		/*this.dessin(100);	*/
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public synchronized void paint(Graphics g)
	{
		
		if ( indic != 0 )
		{
			g.setColor(new Color(0,0,0)) ;
			g.setFont(new Font("Comic Sans MS",1,30)) ;
			g.drawString("INDIC = "+indic,10,50) ;
			indic=0 ;	
		}
		else
		switch(contexte)
		{	case 0 :
				setBackground(new Color(220,245,156)) ;
				break ;
			case 1 /* dessin objet vient de "ajouter" , "Rectangle" "Ellipse" */:
				objet.dessin(g,echelle) ; 
				break ;
			case 2 :
				while(!pile.empty())
				{	objet = (Objets)pile.pop() ;		
					objet.dessin(g,echelle) ; 
					pile2.push(objet);
				}
				while(!pile2.empty())
				{	objet = (Objets)pile2.pop() ;
					pile.push(objet);
				}
				break ;
			case 3 :
				while(!pile.empty())
				{	objet = (Objets)pile.pop() ;		
					objet.dessin(g,echelle) ; 
				}
				break ;
			case 4 :
				while(!pile_po.empty())
				{	
					pp = (Points)pile_po.pop() ;	
					g.fillRect( pp.x*echelle , pp.y*echelle , echelle ,echelle ) ;
					pile_po2.push(pp);
				}
				while(!pile_po2.empty())
				{	pp = (Points)pile_po2.pop() ;
					pile_po.push(pp);
				}
				break ;
			case 5 :
				g.setFont(new Font("Comic Sans MS",1,30)) ;
				g.drawString("Vous ne pouvez pas encore faire ça !",10,50) ;
				break ;
			case 6 :
				front.Labellisation(g,echelle) ;
				break ;
			case 7 :
				front.CarteDistanceOptimale(g,echelle,30000,30002) ;
				/*front.CarteDistance(g,echelle) ;*/
				break ;
			case 8 :
				setBackground(new Color(0,0,0)) ;
				g.setColor(new Color(255,255,255)) ;
				Propager(g,echelle) ;
				//contexte = 9 ;
				break ;
			case 9 :
				setBackground(new Color(0,0,0)) ;
				g.setColor(new Color(255,255,255)) ;
				
				front.dessin(g,echelle) ;
				break ;
			default :
				break ;
		}
		// on dessine tout grâce au contexte graphique du  canvas (en redirigeant si besoin est)
		// ce qui permet effectivement d'avoir qqchose de dessiné dans le canvas
	}
	//-----------------------------------------------------------------------------------------
	public  void 	Propager(Graphics g,int echelle)
	{
		this.gg 	 = g ;
		this.echelle = echelle ;
		
		threadSuspended = false ;	
		run() ;	
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void stopper()
	{
		StopperThread() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public  void propager()
	{
		if ( contexte!=9 )
		{	
			contexte = 9 ;
			frThread = new Thread(this) ;
			frThread.setPriority(Thread.MIN_PRIORITY) ;
			threadSuspended = false ;	
			frThread.start() ;
			//repaint() ;		
		}
		else
			ReprendreThread() ;
	}
	//-----------------------------------------------------------------------------------------
	public synchronized void StopperThread()
	{	
		if (   ! threadSuspended )
		{	
			threadSuspended = true ;
       	}
    }
	//-----------------------------------------------------------------------------------------
	public synchronized void ReprendreThread()
	{	
		if (   threadSuspended )
		{	
			notify() ;
       	}
    }
	//---------------------------------------------------------------------------------------------------------------------------
	public  void run()
	{
		//repaint() ;
		
		while(true){	
			
			try 
			{
				synchronized(this) 						
				{
                    while ( threadSuspended )
                        wait();							
                }
            }
                        
			catch(java.lang.InterruptedException e) 	{
 				System.out.println("Interrupted !"); 	
 			}
			
			front.copie() ;
			front.CalculBrut() ;
			repaint() ;
			//front.dessin(gg,echelle) ;										
		} 			
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void carte()
	{
		contexte = 7 ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void labelliser(Stack pile)
	{
		contexte = 6 ;
		front = new Front(pile,this,echelle) ;
		
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void message()
	{
		contexte = 5 ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void dessin(int i)
	{
		indic = i ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void dessin(Objets objet)
	{
		contexte = 1 ;
		this.objet = objet ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void	dessin(Stack pile)
	{
		contexte = 2 ;
		this.pile = pile ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void	reset(Stack pile)
	{
		contexte = 3 ;
		this.pile = pile ;
		repaint() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void ChangerEchelle(int echelle)
	{
		this.echelle = echelle ; 
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public	Stack	RetourPile()
	{
		return pile_po ;
	}
	
}

//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo

class ObjControls extends Panel implements ActionListener,ItemListener
{
    private 	TextField 	vol ;
    private 	TextField 	sur ;
    private 	TextField 	ech ;
    private		String		type, old_type;
    private		Objets		objet;
    private 	int 		m,n,o,p,o2,p2 ;
    private 	Stack 		collection,pile ;
 	private		ObjCanvas	can ;
 	private 	int 		echelle ; 
 	private 	Choice		ch , methode  ;
	
	//---------------------------------------------------------------------------------------------------------------------------
    public ObjControls(ObjCanvas can) 
    {
    	this.can = can ;
    	
    	m=n=o=p=o2=p2=0 ;
    	type 					= "" ;
    	old_type 				= "" ;
    	
    	collection  = new Stack() ;
    	
    	//---------------------------------------------------------
		ch  	= new Choice() ;
		ch.addItemListener(this); 
		ch.add("1");
		ch.add("2");
		ch.add("3");
		ch.add("5");
		ch.add("10");
		ch.add("20");
		//---------------------------------------------------------
		methode	= new Choice() ;
		methode.addItemListener(this); 
		methode.add("Brute");
		methode.add("Narrow Band");
		methode.add("Fast Marching");
		methode.add("Hermes");
		
		//---------------------------------------------------------
		Button			bouton = null ;
		Panel 			PP 		= new Panel();
		Panel			Phaut 	= new Panel();
		Panel			Pbas 	= new Panel();
		Panel			Pmilieu = new Panel();
		
		BorderLayout	BL  = new BorderLayout(1,1) ;
		GridLayout		GL	= new GridLayout(2,2,1,1) ;
		GridLayout		GL2	= new GridLayout(4,2,1,1) ;
		FlowLayout		FL  = new FlowLayout(FlowLayout.LEFT,5,5) ;
		FlowLayout		FL2 = new FlowLayout(FlowLayout.LEFT,1,3) ;
		//---------------------------------------------------------
		Pmilieu.setLayout(GL2) ;
		
		Pmilieu.add(new Label("Etape 1 ="));
		bouton = new Button("Labelliser");
		bouton.addActionListener(this);
		Pmilieu.add(bouton);
		
		Pmilieu.add(new Label("Etape 2 ="));
		bouton = new Button("Construire carte distance");
		bouton.addActionListener(this);
		Pmilieu.add(bouton);
		
		Pmilieu.add(new Label("Etape 3 ="));
		bouton = new Button("Propager");
		bouton.addActionListener(this);
		Pmilieu.add(bouton);
		
		Pmilieu.add(new Label("Etape 4 ="));
		bouton = new Button("Stopper");
		bouton.addActionListener(this);
		Pmilieu.add(bouton);
		//---------------------------------------------------------
		Phaut.setLayout(GL) ;
		Phaut.add(new Label("Echelle : "));
		Phaut.add(ch);
		Phaut.add(new Label("Methode : "));
		Phaut.add(methode);
		//---------------------------------------------------------
		Pbas.setLayout(FL) ;
		
		bouton = new Button("Ellipse");
		bouton.addActionListener(this);
		Pbas.add(bouton);
		
		bouton = new Button("Rectangle");
		bouton.addActionListener(this);
		Pbas.add(bouton);
		
		bouton = new Button("Ajouter");
		bouton.addActionListener(this);
		Pbas.add(bouton);
		
		bouton = new Button("Dépiler");
		bouton.addActionListener(this);
		Pbas.add(bouton);
		
		bouton = new Button("Reset");
		bouton.addActionListener(this);
		Pbas.add(bouton);
		//---------------------------------------------------------
		PP.setLayout(BL) ;
		
		PP.add("North",Phaut) ;
		PP.add("Center",Pbas) ;
		PP.add("South",Pmilieu) ;
    	
		add(PP) ;
    }
    //---------------------------------------------------------------------------------------------------------------------------
    public  void actionPerformed(ActionEvent ev) 
    {	
    	type = ev.getActionCommand() ;
    	
    	m  = (int)(Math.random() *  150 ) ;
		n  = (int)(Math.random() *  150 ) ;
		o  = (int)(Math.random() *  150 ) ;
		p  = (int)(Math.random() *  150 ) ;
		o2 = (int)(Math.random() * (150-o) );
		p2 = (int)(Math.random() * (150-p) );
		
		//--------------------------------------------------------------
		if (   type.equals("Ajouter") )	
		{	
			if ( can.contexte == 5 ) 
			{	
				pile 		= new Stack() ;		
				pile		= can.RetourPile() ;
				Libre obj 	= new Libre(pile) ;	
				obj.RemplissagePoints() ;		
				collection.push(obj) ;			
				objet 		= obj ;				
			}
				
			if ( ! old_type.equals("Ajouter" ) && ! old_type.equals("Dépiler" ) ) 
				collection.push(objet) ;
		}	
		//--------------------------------------------------------------
		if ( type.equals("Ellipse") )								
    		objet = new Ellipse(m,n,o,p) ;
    	
    	if (   type.equals("Rectangle") )	
    		objet = new Rect(m,n,o2,p2) ;
    	
    	if ( type.equals("Dépiler") )		if ( ! collection.empty() )	
    		can.dessin(collection) ;
    			
		if ( type.equals("Rectangle") || type.equals("Ellipse") ) 	
			can.dessin(objet) ;
			
		if ( type.equals("Ajouter") )		if ( objet != null ) 		
			can.dessin(objet) ;
			
		if ( type.equals("Reset") )			if ( ! collection.empty() )	
			can.reset(collection) ;
			
		if ( type.equals("Labelliser") )	if ( pile != null ) 							
			can.labelliser(pile) ;
			
		if ( type.equals("Construire carte distance") )				
			can.carte() ;
			
		if ( type.equals("Propager") )								
			can.propager() ;
		
		if ( type.equals("Stopper") )								
			can.stopper() ;		
		
		old_type = type ;
		
    }
	//---------------------------------------------------------------------------------------------------------------------------
	public void itemStateChanged(ItemEvent e) 
	{
		//FE.Afficher(FE.CH.getSelectedItem());
		echelle = (int)Val(ch.getSelectedItem()) ;	
		can.ChangerEchelle(echelle) ;
		
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public double 	Val(String s)
	{
		return  Double.valueOf(s).doubleValue();;		
	}
}
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
