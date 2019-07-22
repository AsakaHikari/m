package orgchem.core;

import java.util.ArrayList;
import java.util.List;

public class ChemicalReaction {
	
	public final CompoundsData[] necessaryCompounds;
	public final int[] ratio;
	public final CompoundsData[] resultCompounds;
	public final int[] resultRatio;
	public final EnergySource energySource;
	/**ex)[C](celcius),[EU/mol](IC2),[LightLevel],[mm/tick]*/
	public final int energyValue;
	public final EnergySource resultEnergySource;
	public final int resultEnergyValue;
	/**[mol/1000tick]*/
	public final int speed;
	public static List<ChemicalReaction>[] reactionList=new ArrayList[CompoundsData.size];
	
	public ChemicalReaction(CompoundsData[] necessaryCompounds_,int[] ratio_,CompoundsData[] resultCompounds_,int[] resultRatio_,EnergySource energySource_,int energyValue_,EnergySource resultEnergySource_,int resultEnergyValue_,int speed_){
		necessaryCompounds=necessaryCompounds_;
		ratio=ratio_;
		resultCompounds=resultCompounds_;
		resultRatio=resultRatio_;
		energySource=energySource_;
		energyValue=energyValue_;
		resultEnergySource=resultEnergySource_;
		resultEnergyValue=resultEnergyValue_;
		speed=speed_;
		for(int i=0;i<necessaryCompounds_.length;i++){
			getReactionList()[necessaryCompounds_[i].id].add(this);
		}
	}
	
	public List<ChemicalReaction>[] getReactionList(){return reactionList;}
	
}
