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


class ObjControl extends JPanel implements ActionListener,ItemListener
{
    private 	TextField 	sur ;
    private		String		type="" ;
    private 	Choice		choice1 , methode2 , methode3 ;
 	private		Button		b1 , b2 , b3 , b4 , b5 , b6 , b7 , b8 ;
 	private		TitledBorder tt ;
    private		Objets		objet;
    private		ObjCanva	objcanva ;
    private		ObjControl2	objcontrol2 ;
 	private 	int 		echelle ; 
		
	//=======================================================================================================================
    ObjControl(ObjCanva objcanva, ObjControl2 objcontrol2) 
    {
    	this.objcanva    = objcanva ;
  		this.objcontrol2 = objcontrol2 ;
  		
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
		choice1	= new Choice() ;
		
		choice1.addItemListener(this); 
		choice1.add("Brute");
		choice1.add("Narrow Band");
		choice1.add("Fast Marching");
		choice1.add("Hermes");
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
		FLA11.add(choice1) ;
		
		FLA12.setLayout(FL) ;
		b2 = new Button("Rst");  b2.addActionListener(this);FLA12.add(b2);
			
		FLA13.setLayout(FL) ;
		b1 = new Button("Add");  b1.addActionListener(this);FLA13.add(b1);
		b3 = new Button("Run");  b3.addActionListener(this);FLA13.add(b3);
		b4 = new Button("Stp");  b4.addActionListener(this);FLA13.add(b4);
		
		FLA11.setBackground(new Color(120,120,120)) ; 
		FLA12.setBackground(new Color(120,120,120)) ; 
		FLA13.setBackground(new Color(120,120,120)) ; 
		
		// FLA2x -----------------------------------------------------------------------------------------------------
		
		FLA21.setLayout(FL) ;	
		FLA21.add(methode2) ;
		
		FLA22.setLayout(FL) ;
		
		FLA23.setLayout(FL) ;
		
		b5 = new Button("Load");  b5.addActionListener(this);FLA23.add(b5);
		b6 = new Button("Free");  b6.addActionListener(this);FLA23.add(b6);
			
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
    	
    	if ( type.equals("Add") )	objcanva.Add() ;
		if ( type.equals("Rst") )	objcanva.Reset() ;	
		if ( type.equals("Run") )	objcanva.Propager() ;	
		if ( type.equals("Stp") )	objcanva.StopperThread() ;	
		if ( type.equals("Load"))	objcanva.ChargerImage() ;	
		if ( type.equals("Free"))	objcanva.EnleverImage() ;		
	}
	//=======================================================================================================================
	public void itemStateChanged(ItemEvent e) 
	{
		System.out.println("Methode "+e.getItem() + " choisie...") ; 
		
		if ( e.getItem().equals("Narrow Band") ) 	objcontrol2.testNB.enable() ;  
		else										objcontrol2.testNB.disable() ; 
		
		//testNB.show() ;
		// testNB.setSize(new Dimension(100,10)) ;
		repaint() ;
		objcanva.ChangerMethode( choice1.getSelectedItem() ) ;
	}
	//=======================================================================================================================
}
//####################################################################################################################################################
