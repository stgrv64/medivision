import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.lang.Math ;
import java.util.Stack ;

//import Points ;
//import Objets ;
//import Operateurs ;
//import ImageBufferAnalyseGradient ;

//##################################################################

//####################################################################################################################################################

public class Test extends JFrame implements ActionListener
{
    private ObjControl 	control[]  ;
    private ObjControl2 control2[]  ;    
    private ObjCanva 	canva[]  ;
    private JFrame		fra1[] , fra2[] , fra3[] ;
    private JPanel		pa ;
    private	JScrollPane sp[] ;
    private Button		bou , exit , help ;
    private Label		lab ;
    private static int			incr = -1 ;
    
    public	static final int	DIM = 350 ;
    
    //=======================================================================================================================
    public static void main(String[] args) {
        
        /*
        Frame frame = new Frame("Medivision");
        frame.addWindowListener(applet);
        frame.add("Center", applet);
        frame.setSize(350, 250);
        frame.show();
        */
       
        Test mytest = new Test();
		mytest.setLayout(new GridLayout(1,1));
		mytest.setVisible(true);
        
        // frame.add(applet );
    }
    //=======================================================================================================================
    public void Test() 
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
		add("South",new Label(" - S. Gravois - stgrv@hotmail.com - 2003 -")) ;
		setSize(290,80) ;

		// pa.show();
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

			canva[incr].setVisible(false) ;
    		control[incr].setVisible(false);
    		control2[incr].setVisible(false);

    		fra1[incr].setEnabled(false) ;
    		fra2[incr].setEnabled(false) ;
			fra3[incr].setEnabled(false) ;

    		fra1[incr].setVisible(false);
    		fra2[incr].setVisible(false);
    		fra3[incr].setVisible(false);

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
        	
    		fra1[incr] = new JFrame() ;
    		fra1[incr].setTitle("Control "+incr);		
    		fra1[incr].getContentPane().add(control[incr]) ; 	
    		fra1[incr].setSize(300,330) ; 	
    		fra1[incr].setLocation(incr*20		, 	150 + incr*20 ); 	
    		fra1[incr].setVisible(true); 	fra1[incr].show() ;
    		
			fra2[incr] = new JFrame() ; 
			fra2[incr].setTitle("Parameters "+incr);	
			fra2[incr].getContentPane().add(control2[incr]) ; 	
			fra2[incr].setSize(350,230) ; 	
			fra2[incr].setLocation(300+incr*20	, 	400  + incr*20 ); 	
			fra2[incr].setVisible(true); 	fra2[incr].show() ;
			
			fra3[incr] = new JFrame() ; 
			fra3[incr].setTitle("Canva "+incr);			
			fra3[incr].getContentPane().add(sp[incr]) ; 		
			fra3[incr].setSize(370,400) ; 	
			fra3[incr].setLocation(300+incr*20	,	incr*20 ); 			
			fra3[incr].setVisible(true); 	
			
			// fra3[incr].show() ;
		}	
    }
	//=======================================================================================================================
}

//####################################################################################################################################################
