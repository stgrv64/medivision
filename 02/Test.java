import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.lang.Math ;
import java.util.Stack;
import Points ;
import Objets;
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
public class Test extends Applet 
 {
    private ObjControls controls , controls2 ;    
    private ObjCanvas 	canva , canva2  ;
    private Label		lab ;
    private Frame		fra ;
    //---------------------------------------------------------------------------------------------------------------------------
    public void init() 
    {
		setLayout(new BorderLayout());
		
		canva		= new ObjCanvas() ;
		controls 	= new ObjControls(canva) ;
		
		add("North",lab = new Label("Une application de simulation numérique de fluide")) ;
		add("Center",canva )   ;
		add("South",controls ) ;
		/*
		fra = new Frame() ;
		fra.setLayout(new BorderLayout());
		canva2		= new ObjCanvas() ;
		controls2 	= new ObjControls(canva2) ;
		fra.add("North",lab = new Label("Une application de simulation numérique de fluide")) ;
		fra.add("Center",canva2 )   ;
		fra.add("South",controls2 ) ;
		
		fra.setSize(300,400) ;
		fra.show() ;
		*/
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
    public String getAppletInfo() {
        return "Un test de simulation numérique avec les méthodes Levelset (courbes de niveaux)";
    }
    //---------------------------------------------------------------------------------------------------------------------------
}
//ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo
class ObjCanvas	extends Canvas implements MouseListener, MouseMotionListener, Runnable
{
	private 	Objets			objet ;
	private 	Piles			front;
	private 	Points			p, po , po_old  , pp;
	
	private 	Stack			pile,pile2,pile_po,pile_po2 ;
	private		Thread			frontThread  ;
	private 	Graphics		g ;
	
	public 		int				contexte, echelle , indic ,N = 100000 , dim   ;
	private 	double			ls[][] , cd[][] ;
	private 	boolean 		sm[][] , threadSuspended  ;
	
	private 	final int		DIM = 400  ;
	private 	final double	T   = 0.5  ;
	private 	final double	D   = 2.0  ;
	private 	final double	R   = 0.0  ;
	
	//---------------------------------------------------------------------------------------------------------------------------
	public ObjCanvas()
	{
		contexte 	= 0 ;
		echelle  	= 1 ;
		indic       = 0 ;
		pile2 		= new Stack() ;
		
		po			= new Points() ;
		po_old		= new Points() ;
		pp			= new Points() ;
		p 			= new Points() ;
		
		threadSuspended = false ;
		
		
		
		addMouseMotionListener(this);
		addMouseListener(this);

	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void mouseMoved(MouseEvent e)  {}
	public void mouseEntered(MouseEvent e)  {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
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
				g.setColor(new Color(255,255,255)) ;
				while(!pile.empty())
				{	objet = (Objets)pile.pop() ;		
					objet.dessin(g,echelle) ; 
				}
				break ;
			case 4 :
				g.setColor(new Color(255,255,255)) ;
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
				g.setColor(new Color(255,255,255)) ;
				g.setFont(new Font("Comic Sans MS",1,30)) ;
				g.drawString("Vous ne pouvez pas encore faire ça !",10,50) ;
				break ;
			case 6 :
				g.setColor(new Color(255,255,255)) ;
				Labellisation(g) ;
				this.g = g ;
				CarteD() ;
				
				break ;
			case 7 :
				//CarteDistanceOptimale( g,50000,50002) ;
				break ;
			case 10 :
				dessin(g) ;
				break ;	
			case 11 :
				dessin2(g) ;
				break ;	
			default :
				break ;
		}
		// on dessine le dessin de l'objet grâce au contexte graphique du  canvas
		// ce qui permet effectivement d'avoir qqchose de dessiné dans le canvas
		
	}
	public void CarteD()
	{
		CarteDistanceOptimale(50000,50002) ;
		Runner() ;
	}
	public void Runner()
	{
		frontThread = new Thread(this) ;
		frontThread.setPriority(Thread.MIN_PRIORITY) ;
		frontThread.start() ;
	}
	//---------------------------------------------------------------------------------------------------------------------------
	public void Propager(Stack pile)
	{
		if ( contexte != 11)
		{
			threadSuspended = false ;
			
			dim 		= (int)(DIM/echelle) ;
			sm 			= new boolean	[dim][dim] ;
			cd 			= new double	[dim][dim] ;
			ls 			= new double	[dim][dim] ;
		
			front		= new Piles(N) 	;
			front.Copie(0,pile) 	;	
		
			contexte = 6 ;
			repaint() ;
		}
		else
		{	
			ReprendreThread() ;
		}
		//contexte = 7 ;
		//repaint() ;
		//contexte = 8 ;
		//repaint() ;
			
		//new Thread(this).run() ;
		
		//repaint() ;
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
	public void	Reset()
	{
		front.Reset() ;
		contexte = 3 ;
		repaint() ;
		contexte = 0 ;
		
		//frontThread.destroy() ;
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
	//---------------------------------------------------------------------------------------------------------------------------
	public void 	Labellisation(Graphics g)
	{
		//coucou(g) ;
		int      		i,j,indice=0 ;
        boolean 		dj[][] ;
        Points  		p ;
		
		p 	= new Points() ;	
		dj 	= new boolean[dim][dim] ;
		
		for(i=0;i<dim;i++) 
        for(j=0;j<dim;j++) 
        {	
        	dj[i][j] = false ;
        	sm[i][j] = false ;
        	cd[i][j] = -1.0 ;
        }
        //coucou(g) ;
        while( !front.PileVide(0) )
        {
        	p = (Points)front.Depiler(0) ;
        	try
        	{
        		cd[p.x][p.y]  	= 0.0 ;
        		dj[p.x][p.y] 	= true ;
        	}
        	catch(java.lang.ArrayIndexOutOfBoundsException e)
        	{
        		if ( p.x >= dim && p.y >= dim ) 
        		{	
        			cd[dim-1][dim-1]  = 0.0 ;
        			dj[dim-1][dim-1] = true ;
        		}
        		else if ( p.x >= dim && p.y < dim)
        		{	
        			cd[dim-1][p.y]  = 0.0 ;
        			dj[dim-1][p.y] 	= true ;
        		}
        		else if ( p.x < dim && p.y >= dim)
        		{	
        			cd[p.x][dim-1]  = 0.0 ;
        			dj[p.x][dim-1] 	= true ;
        		}
        	}
        }
        
        dj[0][0]         = true ; front.Empiler(0,new Points(0,0)) ;		
        dj[dim-1][0]     = true ; front.Empiler(0,new Points(dim-1,0)) ;	
        dj[0][dim-1]     = true ; front.Empiler(0,new Points(0,dim-1)) ;	
        dj[dim-1][dim-1] = true ; front.Empiler(0,new Points(dim-1,dim-1));	

        indice=0;

		g.setColor(new Color(250,250,200)) ;
			
        while( ! front.PilesVides() )
        
        {		while( ! front.PileVide(indice) )
                {       
                		/*p = new Points() ;*/
                		p = front.Depiler(indice);
                        i = p.x ;
                        j = p.y ;
                        if ((i-1)>0)   
                        	if (  ! dj[i-1][j]  )
                                {       
                                	/*p.x = i - 1 ;p.y = j ;*/
                                    front.Empiler(indice+1,new Points(i-1,j));
                                    dj[i-1][j] = true ;
                                    cd[i-1][j] = 1.0 ;
                                    g.fillRect((i-1)*echelle,j*echelle,echelle,echelle) ;
                                }

                        if ((j-1)>0)   
                        	if ( ! dj[i][j-1] )
                                {       
                                	/*p.x = i  ;p.y = j -1 ;*/
                                	front.Empiler(indice+1,new Points(i,j-1));
                                    dj[i][j-1] = true ;
                                    cd[i][j-1] = 1.0 ;
                                    g.fillRect(i*echelle,(j-1)*echelle,echelle,echelle) ;
                                }

                        if ((j+1)<dim) 
                        	if ( ! dj[i][j+1] )
                                {       
                                	/*p.x = i ;p.y = j + 1 ;*/
                                	front.Empiler(indice+1,new Points(i,j+1));
                                    dj[i][j+1] = true ;
                                    cd[i][j+1] = 1.0 ;
                                    g.fillRect(i*echelle,(j+1)*echelle,echelle,echelle) ;
                                }

                        if ((i+1)<dim) 
                        	if ( ! dj[i+1][j] )
                                {       
                                	/*p.x = i + 1 ;p.y = j ;*/
                                	front.Empiler(indice+1,new Points(i+1,j));
                                    dj[i+1][j] = true ;
                                    cd[i+1][j] = 1.0 ;
                                    g.fillRect((i+1)*echelle,j*echelle,echelle,echelle) ;
                                }

                }
                indice++;
        }

        for(i=0;i<dim;i++)
        for(j=0;j<dim;j++)
        {       
        	if ( cd[i][j] == 0.0 )
            {       
                sm[i][j]= true ;
                front.Empiler(0,new Points(i,j));
            }
        }
     
		
	}
	//-----------------------------------------------------------------------------------------
	public void 	CarteDistance()
	{
		double 	val , min  ;
		int		i,j,n , x , y ;
		Points	p1 ;
		
		p1 = new Points() ;
		val = 0.0 ;
		
		g.setFont(new Font("Helvetica",1,15)) ;
		g.setColor(new Color(0,0,255)) ;
		g.drawString("echelle = " + echelle ,100,120) ;
		g.drawString("dim     = " + dim ,100,140) ;
		
		for(i=0;i<1000000;i++) i=i ;
		
		for(i=0;i<dim;i++) 
        for(j=0;j<dim;j++) 
		{
		  	min = 10000 ;
		  	
		  	for(n=0 ; n < front.TaillePile(0) ; n++)
			{
				p 	= front.Element(0,n) ;
				val = Math.sqrt( ((p.x)-i)*((p.x)-i) + ((p.y)-j)*((p.y)-j) ) ;
				
				if ( val < min ) min = val ;
			}
        	cd[i][j] = cd[i][j] * min ;
        	
        	x = (int)(min/5.0) * 20  ;
        	y = Math.abs( (int)((double)x/(double)255)) ;
        	x = x - y*255 ;
        	
        	if ( cd[i][j] > 0  )	g.setColor(new Color(x	,150+y*40,150)) ;
        	if ( cd[i][j] < 0  )	g.setColor(new Color(150+y*40,x	,150)) ;
        	if ( cd[i][j] == 0 )	g.setColor(new Color(255,255,255)) ;
        		
        	p1.x = i ;
        	p1.y = j ;
        	p1.dessin(g,echelle) ;
        }
        contexte = 1 ;
	}
	//-----------------------------------------------------------------------------------------
	Points 	Voisin(Points p,boolean sm[][],int dim)
	{
        Points  voisin;
        int     x,y,vvx,vvy;

        x       =  p.x ;
        y       =  p.y ;
        vvx     =  p.vx   ;
        vvy     =  p.vy   ;
        
        voisin  =  new Points(0,0,0,0,0);
	
		if ( x>1 && x<dim-1 && y>1 && y<dim-1 )
		{
        	if ( !sm[x+1][y] )	voisin.Change(x+1,y,vvx+1,vvy,1);
        	if ( !sm[x-1][y] )	voisin.Change(x-1,y,vvx-1,vvy,1);
        	if ( !sm[x][y-1] )	voisin.Change(x,y-1,vvx,vvy-1,1);
        	if ( !sm[x][y+1] )	voisin.Change(x,y+1,vvx,vvy+1,1);
		}
        return(voisin);
	}
	//-----------------------------------------------------------------------------------------
	public void 	CarteDistanceOptimale(int nb_max1,int piles_l_set)
	{
		int           x,y,vvx,vvy,dis,distance_carre , m;
        Points        p , sommet ;
		
        dis = 0 ;

        while ( dis <= nb_max1 )
        {       
                while ( !front.PileVide(dis) )
                {       
                		p 	= front.Depiler(dis);
 						
                        //front.Empiler(piles_l_set,p);
                        
                        sommet  = Voisin(p,sm,dim);
                        
                        while (sommet.indic > 0 )
                        {
                            x   = sommet.x;
                            y   = sommet.y;
                            vvx = sommet.vx;
                            vvy = sommet.vy;
                            
                            distance_carre = vvx*vvx + vvy*vvy;
                                
                            if ( distance_carre > nb_max1 ) 
                                front.Empiler(nb_max1+1,sommet);
                            else   	
                            	front.Empiler(distance_carre,sommet);
                                
                            sm[x][y] = true ;
                                
                            if      ( cd[x][y] > 0 )
		                    	cd[x][y] =  0.0 +  Math.sqrt((double)distance_carre);
		                        		
                            else if ( cd[x][y] < 0 )
                                cd[x][y] =  0.0 -  Math.sqrt((double)distance_carre);
                            
                            m = 100 + (int)( cd[x][y]/(5.0/echelle))* echelle ;
                            try
                            {
                            	g.setColor(new Color(m,m,m)) ;
                            }
                            catch(java.lang.IllegalArgumentException e)
                            {
                            	g.setColor(new Color(255,255,255)) ;
                            }
							g.fillRect(x*echelle,y*echelle,echelle,echelle) ;
							
		                	sommet = Voisin(p,sm,dim);
                        }
                }
                dis++;
        }
        while ( ! front.PileVide(nb_max1+1)  )
        {
                p = front.Depiler(nb_max1+1);
                sm[p.x][p.y] = false ;
        }
        while ( ! front.PileVide(piles_l_set)  )
        {
                p = front.Depiler(piles_l_set);
        }
	}
	//-----------------------------------------------------------------------------------------
	public void 	Copie()
	{
		int   	i,j ;
		
		for(i=0;i<dim;i++)
        for(j=0;j<dim;j++)
        {	
        	ls[i][j] = cd[i][j] ;
        	
        	if ( Math.abs((int)ls[i][j]) <= 5 )
        		front.Empiler(Math.abs((int)ls[i][j]),new Points(i,j)) ;
     
        }	
	}
	
	//-----------------------------------------------------------------------------------------
	public void 	CalculBrut()
	{
		int   	i,j,x,y;
        double  lsx,lsy,lsxy,lsxx,lsyy,max;
        double  F_prop , F_curv , F_adv ;
        double  courbure,c,g,h,ii,k,l,m,n,o,q,r,s,w;

        for(i=2;i<dim-2;i++)
        for(j=2;j<dim-2;j++)
        {
                m         = ls[i][j];
                l         = ls[i-1][j];	
                n         = ls[i+1][j];
                h         = ls[i][j-1];	
                r         = ls[i][j+1];
                s         = ls[i+1][j+1];	
                q         = ls[i-1][j+1];	
                ii        = ls[i+1][j-1];	
                g         = ls[i-1][j-1];	
                c         = ls[i][j-2];	
                w         = ls[i][j+2];	
                k         = ls[i-2][j];
                o         = ls[i+2][j];	
                lsx       = (n-l)/2;
                lsy       = (r-h)/2;
                lsxy      = (g+s-q-ii)/4;
                lsxx      = (o+k-2*m)/4;
                lsyy      = (w+c-2*m)/4;
                
                if ( Math.pow( (lsx*lsx + lsy*lsy ) ,1.5) != 0 )
                	 courbure  = (lsxx*lsy*lsy - 2*lsy*lsx*lsxy + lsyy*lsx*lsx ) / Math.pow( (lsx*lsx + lsy*lsy) ,1.5) ;
                else courbure = 0;

                /*F_prop    = 1/(1+sqr(gradient[i+j*dim]/DEPENDANCE_GRADIENT)) ;*/
                F_curv    = D * courbure * Math.sqrt( lsx*lsx + lsy*lsy ) ;
                F_adv     = R ;

                cd[i][j] = ls[i][j]+ T * (F_curv + F_adv);
        }
	}
	//-----------------------------------------------------------------------------------------
	public void run()
	{
		//g.setColor(new Color(0,255,0)) ;
		//g.fillRect(100,100,300,300) ;
		setBackground(new Color(0,0,0)) ;
		
		contexte = 11 ; 
		
		while(true){	
			
			try 
			{
				//Thread.sleep(sommeil) ;
			
				synchronized(this) 						
				{
                    while ( threadSuspended )
                        wait();							
                }
            }
                        
			catch(java.lang.InterruptedException e) 	{
 				System.out.println("Interrupted !"); 	
 			}
			
			Copie() ;
			repaint() ;	
			CalculBrut() ;
			//repaint() ;		
			
			
		} 			
	}
	//-----------------------------------------------------------------------------------------
	public synchronized void StopperThread()
	{
		//	stop() ;	======> deprecated
	
		if ( ! threadSuspended ){	
			threadSuspended = true ;
       	}
    }
	//-----------------------------------------------------------------------------------------
	public synchronized void ReprendreThread()
	{	
		// le bloc de programme doit être synchronisé pour reprendre le "moniteur"
		//  suspend() ;	======> deprecated
		
		if (   threadSuspended )
		{	
			threadSuspended = false ;
       		notify() ;
       	}
    }
    //-----------------------------------------------------------------------------------------
	public  void 	dessin2(Graphics g) 
	{
		//setBackground(new Color(0,75,200)) ;
		int 		couleur ;
		
		for(int i=5 ; i>=0 ; i--)
        	while( !front.PileVide(i) )
        	{	
        		p = front.Depiler(i) ;
        		
        		couleur = 250 - i * 50 ;
				g.setColor(new Color(couleur,couleur,couleur)) ;
				g.fillRect( p.x*echelle , p.y*echelle , echelle ,echelle ) ;
			}
	}
	//-----------------------------------------------------------------------------------------
	public  void 	dessin(Graphics g) 
	{
		//setBackground(new Color(0,75,200)) ;
		
		for(int i=0;i<dim;i++)
        for(int j=0;j<dim;j++)
		{	
			if ( ls[i][j] > 0.0  ) 
				g.setColor(new Color(0,0,0)) ;
				
			if ( ls[i][j] < 0.0 )
				g.setColor(new Color(200,225,200)) ;
				
			g.fillRect( i*echelle , j*echelle , echelle ,echelle ) ;
		}
	}
	//-----------------------------------------------------------------------------------------
	public  void 	coucou(Graphics g) 
	{
		setBackground(new Color(100,100,100)) ;
		g.setColor(new Color(255,0,0)) ;
		g.fillRect(100,100,300,300) ;
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
		GridLayout		GL2	= new GridLayout(1,2,1,1) ;
		FlowLayout		FL  = new FlowLayout(FlowLayout.LEFT,5,5) ;
		FlowLayout		FL2 = new FlowLayout(FlowLayout.LEFT,1,3) ;
		//---------------------------------------------------------
		Pmilieu.setLayout(GL2) ;
		
		bouton = new Button("Propager");
		bouton.addActionListener(this);
		Pmilieu.add(bouton);
		
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
    	
    	//canvaThread = new Thread(this) ;
		//canvaThread.setPriotity(Thread.MIN_PRIORITY) ;
		//canvaThread.start() ;
		
		add(PP) ;
    }
    //---------------------------------------------------------------------------------------------------------------------------
    public synchronized void actionPerformed(ActionEvent ev) 
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
		{
			while(!collection.empty())
				collection.pop() ;
			can.Reset() ;
		}
			
		if ( type.equals("Propager") )	if ( pile != null ) 							
			can.Propager(pile) ;
			
		if ( type.equals("Stopper") )						
			can.StopperThread() ;
			
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
