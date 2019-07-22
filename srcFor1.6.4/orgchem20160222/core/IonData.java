package orgchem.core;

public enum IonData {
	H("hydrogen ion","H+",1),
	Li("lithium ion","Li+",1),
	Be("beryllium ion","Be2+",2),
	B("boron ion","B3+",3),
	Na("sodium ion","Na+",1),
	Mg("magnesium ion","Mg2+",2),
	Al("alminium ion","Al3+",3),
	K("potassium ion","K+",1),
	Ca("calcium ion","Ca2+",2),
	Fe2("iron(II) ion","Fe2+",2),
	Fe3("iron(III) ion","Fe3+",3),
	Cu("copper(I) ion","Cu+",1),
	Cu2("copper(II) ion","Cu2+",2),
	Ag("silver ion","Ag+",1),
	PH4("phosphin ion","PH4+",1),
	NH4("anmonium ion","NH4+",1),
	H3O("hydronium ion","H3O+",1),
	NO2("nitronium ion","NO2+",1),
	
	N("nitride ion","N3-",-3),
	O("oxide ion","O2-",-2),
	F("fluoride ion","F-",-1),
	P("phosphorus ion","P3-",-3),
	S("sulfide ion","S2-",-2),
	Cl("chloride ion","Cl-",-1),
	OH("hydroxide ion","OH-",-1),
	CH3COO("acetate ion","CH3COO-",-1),
	C2("acetilyde ion","C22-",-2),
	C6H5COO("benzoate ion","C6H5COO-",-1),
	HCO3("hydrogencarbonate ion","HCO3-",-1),
	CO3("carbonate ion","CO32-",-2),
	CN("cyanide ion","CN-",-1),
	NO3("nitrate ion","NO3-",-1),
	PO4("phosphate ion","PO43-",-3),
	SO4("sulfate ion","SO42-",-2),
	HSO4("hydrogensulfate ion","HSO4-",-1);

public final String name;
public final String formula;
public final byte charge;
private IonData(String name_,String formula_,int charge_){
	name=name_;
	formula=formula_;
	charge=(byte)charge_;
}
}
