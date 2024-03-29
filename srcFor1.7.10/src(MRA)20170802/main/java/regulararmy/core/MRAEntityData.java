package regulararmy.core;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import regulararmy.entity.EntityRegularArmy;
import regulararmy.entity.EntitySniperSkeleton;

public class MRAEntityData {
	public static Map<Class<? extends EntityRegularArmy>,MRAEntityData> classToData=new HashMap();
	public static Map<MRAEntityData,Class<? extends EntityRegularArmy>> dataToClass=new HashMap();
	public static int nextId;
	public Class<? extends EntityRegularArmy> entityClass;
	public String unlocalizedName;
	
	public float crowdCostPerBlock=1;
	public float fightRange=5;
	public float jumpHeight=1.2f;
	
	public float basicWeight=1;
	public int centreTier=0;
	public int minTier=0;
	public int maxTier=9999;
	public int numberOfMember=4;
	
	public MRAEntityData(Class<? extends EntityRegularArmy> entityClass,
			float crowdCostPerBlock, float fightRange, float jumpHeight,
			float basicWeight, int centreTier, int minTier, int maxTier,
			int numberOfMember,String unlocalizedName) {
		
		this.entityClass = entityClass;
		this.crowdCostPerBlock = crowdCostPerBlock;
		this.fightRange = fightRange;
		this.jumpHeight = jumpHeight;
		this.basicWeight = basicWeight;
		this.centreTier = centreTier;
		this.minTier = minTier;
		this.maxTier = maxTier;
		this.unlocalizedName=unlocalizedName;
		this.numberOfMember=numberOfMember;
	}
	
	public void activateThisData(){
		EntityRegistry.registerGlobalEntityID(entityClass, unlocalizedName, EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(entityClass, unlocalizedName, nextId++, MonsterRegularArmyCore.instance, 80, 1, true);
		
		classToData.put(entityClass, this);
		dataToClass.put(this, entityClass);
		MonsterRegularArmyCore.entityList.add(entityClass);
	}
}
