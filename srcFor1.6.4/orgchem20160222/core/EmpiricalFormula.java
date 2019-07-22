package orgchem.core;

public class EmpiricalFormula {
	
	public final String name;
	public final IonData[] cation;
	public final IonData[] anion;
	public final int[] ratio;
	public EmpiricalFormula(IonData cation_,IonData anion_){
		cation=new IonData[1];
		cation[0]=cation_;
		anion=new IonData[1];
		anion[0]=anion_;
		ratio=new int[2];
		ratio[0]=cation[0].charge;
		ratio[1]=-anion[0].charge;
		name=cation_.name.substring(0, cation_.name.length()-3)+anion_.name.substring(0, anion_.name.length()-4);
	}
	public EmpiricalFormula(IonData[] cation_,IonData[] anion_,int[] ratio_,String formula){
		cation=cation_;
		anion=anion_;
		ratio=ratio_;
		name=formula;
	}
}
