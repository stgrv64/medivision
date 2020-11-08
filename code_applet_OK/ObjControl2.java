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

// class de contrôle des curseurs (diffusion, reaction, etc..

class ObjControl2 extends JPanel implements ChangeListener
{
	private 	static TextField 	ech_txt , diff_txt , reac_txt , testNB_txt ;
	private		static ObjCanva	can ;
	private		static TitledBorder tt ;
	private		static JSlider		ech , diff , reac ; 
	
	public      static JSlider testNB ; 	
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
		
		FLC1.setLayout(FL) ;  FLC1.add(ech) 	 ;	FLC1.add(ech_txt) ; 	FLC1.add(new Label("Scale")) ; 	
		FLC2.setLayout(FL) ;  FLC2.add(diff) 	 ;	FLC2.add(diff_txt) ;	FLC2.add(new Label("Reaction")) ;	 	
		FLC3.setLayout(FL) ;  FLC3.add(reac) 	 ;	FLC3.add(reac_txt) ;	FLC3.add(new Label("Diffusion")) ; 
		FLC4.setLayout(FL) ;  FLC4.add(testNB) ;	FLC4.add(testNB_txt) ;	FLC4.add(new Label("Test NB")) ; 	
		
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
