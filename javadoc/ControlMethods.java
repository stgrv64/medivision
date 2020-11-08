import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

//####################################################################################################################################################

public class ControlMethods extends JPanel implements ActionListener,ItemListener
{
    private 	TextField 			sur ;
    private		String				type="" ;
    private 	Choice				methode11 , methode12 , methode13 ;
 	private		Button				b1 , b2 , b3 , b4 , b5 , b6 , b7 , b8 ;
 	private		TitledBorder 		tt ;
    private		Objets				objet;
    private		Canva				can ;
    private		ControlParameters	con ;
 	private 	int 				scale ; 
		
	//=======================================================================================================================
    ControlMethods(Canva can,ControlParameters con) 
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
		methode11	= new Choice() ;
		methode11.addItemListener(new methode11Changed()); 
		methode11.add("Brute");
		methode11.add("Narrow Band");
		methode11.add("Fast Marching");
		methode11.add("Hermes");
		//-------------------------------------------------------------------------------------------------------
		methode12	= new Choice() ;
		methode12.addItemListener(new methode12Changed()); 
		methode12.add("Segmentation");
		methode12.add("Filtering");
		methode12.add("Synthesis");
		//-------------------------------------------------------------------------------------------------------
		methode13	= new Choice() ;
		methode13.addItemListener(new methode13Changed()); 
		methode13.add("Zero level");
		methode13.add("All levels");
		methode13.add("Negative levels");
		//-------------------------------------------------------------------------------------------------------
		BevelBorder 	border = new BevelBorder(BevelBorder.LOWERED);
		
		// FLA1x -------------------------------------------------------------------------------------------------------
		
		FLA11.setLayout(FL) ;	
			FLA11.add(methode11) ;
		
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
			FLA21.add(methode12) ;
		
		FLA22.setLayout(FL) ;
		
		FLA23.setLayout(FL) ;
			b5 = new Button("Load");b5.addActionListener(this);FLA23.add(b5);
			b6 = new Button("Free");b6.addActionListener(this);FLA23.add(b6);
			
		FLA21.setBackground(new Color(120,120,120)) ; 
		FLA22.setBackground(new Color(120,120,120)) ; 
		FLA23.setBackground(new Color(120,120,120)) ; 
		
		// FLBx -----------------------------------------------------------------------------------------------------
		
		FLB1.setLayout(FL) ;	
			FLB1.add(methode13) ;
		
		FLB2.setLayout(FL) ;	
		FLB3.setLayout(FL) ;	
			
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
		if ( type.equals("Run") )	can.propagation() ;	
		if ( type.equals("Stp") )	can.stopThread() ;	
		if ( type.equals("Load"))	can.loadImage() ;	
		if ( type.equals("Free"))	can.freeImage() ;	
	}
	//=======================================================================================================================
	public void itemStateChanged(ItemEvent e) 
	{
		System.out.println("methode1 "+e.getItem() + " choisie...") ; 
		
		if ( e.getItem().equals("Narrow Band") ) 	con.testNB.enable() ;  
		else										con.testNB.disable() ; 
		
		//testNB.show() ;
		// testNB.setSize(new Dimension(100,10)) ;
		repaint() ;
		can.changeMethode1( methode11.getSelectedItem() ) ;
	}
	//=======================================================================================================================
	class methode11Changed implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			can.changeMethode1( methode11.getSelectedItem() ) ;
		}
	}
	//=======================================================================================================================
	class methode12Changed implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			can.changeMethode2( methode12.getSelectedItem() ) ;
		}
	}
	//=======================================================================================================================
	class methode13Changed implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			can.changeMethode3( methode13.getSelectedItem() ) ;
		}
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
