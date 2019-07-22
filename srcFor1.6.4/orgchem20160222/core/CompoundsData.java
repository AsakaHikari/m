package orgchem.core;

import java.util.HashMap;
import java.util.Map;

public class CompoundsData {
	public static CompoundsData 
	H2O=new CompoundsData("water","H2O",18,75),
	CO2=new CompoundsData("carbon dioxide","CO2",44,30),
	H2=new CompoundsData("hydrogen","H2",2,29),
	O2=new CompoundsData("oxygen","O2",32,29),
	N2=new CompoundsData("nitrogen","N2",28,29),
	NH3=new CompoundsData("ammonia","NH3",17,37),
	Cl2=new CompoundsData("clorine","Cl2",71,34),
	
	methane=new CompoundsData("methane","CH4",16,36),
	ethane=new CompoundsData("ethane","(CH3)2",30,53),
	propane=new CompoundsData("propane","CH3CH2CH3",44,73),
	butane=new CompoundsData("butane","CH3(CH2)2CH3",58,-144,-1),
	pentane=new CompoundsData("pentane","CH3(CH2)3CH3",72,-131,36),
	isopentane=new CompoundsData("isopentane","CH3CH(CH3)CH2CH3",72,-159,27),
	hexane=new CompoundsData("hexane","CH3(CH2)4CH3",86,-95,69),
	largeAlkanes=new CompoundsData("largeAlkanes","CH3(CH2)xCH3",130,20,300),
	cyclohexane=new CompoundsData("cyclohexane","C6H12",86,-126,81),
	
	benzene=new CompoundsData("benzene","C6H6",78,5,80),
	naphthalene=new CompoundsData("naphthalene","C10H8",128,80,218),
	phenole=new CompoundsData("phenole","(C6H5)OH",95,40,181),
	toluene=new CompoundsData("toluene","(C6H5)CH3",93,-94,110),
	chlorobenzene=new CompoundsData("chlorobenzene","(C6H5)Cl",112,-45,132),
	o_nitrotoluen=new CompoundsData("o_nitrotoluen","(C6H4)CH3,2-NO2",137,-9,222),
	p_nitrotoluen=new CompoundsData("p_nitrotoluen","(C6H4)CH3,3-NO2",137,16,230),
	n_nitrotoluen=new CompoundsData("n_nitrotoluen","(C6H4)CH3,4-NO2",137,51,238),
	dinitrotoluen_2_6=new CompoundsData("2,6-dinitrotoluen","(C6H3)CH3,2-NO2,6-NO2",182,68,275),
	dinitrotoluen_2_4=new CompoundsData("2,4-dinitrotoluen","(C6H3)CH3,2-NO2,4-NO2",182,71,300),
	trinitrotoluen_2_4_6=new CompoundsData("2,4,6-trinitrotoluen","(C6H2)CH3,2-NO2,4-NO2,6-NO2",227,80,240),
	largePAHs=new CompoundsData("largePAHs","CxHx",1300,400,600),
	
	H2SO4=new CompoundsData("sulfulic acid","H2SO4",98,10,290),
	HNO3=new CompoundsData("nitric acid","HNO3",63,-42,83),
	mixedAcid=new CompoundsData("mixed acid","NO2+2HSO4+H3O",162,-16,186),
	NaOH=new CompoundsData("sodium hydroxide","NaOH",39,318,1388);
 public final String name;
 public final String formula;
 public final EmpiricalFormula eFormula;
 public final short weight;
 public final int id;
 /**J/mol.K*/
 public final int heatCapacity;
 public static Map<Integer,CompoundsData> compoundsMap=new HashMap();
 public static int size=0;
 
 	public CompoundsData(String name_,String formula_,int weight_,int heatCapacity_){
 		name=name_;
 		formula=formula_;
 		weight=(short)weight_;
 		heatCapacity=heatCapacity_;
 		eFormula=null;
 		id=getSize();
 		addToMap();
 	}
 	
 	/*
 	private CompoundsData(String name_,EmpiricalFormula formula_,int weight_,int meltingPoint_,int boilingPoint_){
 		name=name_;
 		eFormula=formula_;
 		weight=(short)weight_;
 		meltingPoint=meltingPoint_;
 		boilingPoint=boilingPoint_;
 		formula=null;
 		id=getSize();
 		addToMap();
 	}
 	*/
 	public String getFormula(){
 		return formula==null?eFormula.name:formula;
 	}
 	private void addToMap(){
 		compoundsMap.put(size, this);
 		size++;
 	}
 	private int getSize(){
 		return size;
 	}
}
