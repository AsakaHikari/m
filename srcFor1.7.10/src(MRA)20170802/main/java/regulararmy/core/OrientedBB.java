package regulararmy.core;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class OrientedBB {
	public Vec3 origin;
	public Vec3 centreVec;
	public Vec3 widthVec,heightVec,depthVec;
	public float width,height,depth;
	public float rotateX, rotateY,rotateZ;

	public OrientedBB(Vec3 position,Vec3 rotationPoint, float width,
			float height, float depth,float rotateX,float rotateY,float rotateZ) {
		this.centreVec = rotationPoint.subtract(position);
		this.origin=rotationPoint;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.rotateX=rotateX;
		this.rotateY=rotateY;
		this.rotateZ=rotateZ;
		
		this.widthVec=this.centreVec.addVector(width/2, 0, 0);
		this.widthVec.rotateAroundX(rotateX);
		this.widthVec.rotateAroundY(rotateY);
		this.widthVec.rotateAroundZ(rotateZ);
		this.heightVec=this.centreVec.addVector(0,height/2,0);
		this.heightVec.rotateAroundX(rotateX);
		this.heightVec.rotateAroundY(rotateY);
		this.heightVec.rotateAroundZ(rotateZ);
		this.depthVec=this.centreVec.addVector(0,0,depth/2);
		this.depthVec.rotateAroundX(rotateX);
		this.depthVec.rotateAroundY(rotateY);
		this.depthVec.rotateAroundZ(rotateZ);
		this.centreVec.rotateAroundX(rotateX);
		this.centreVec.rotateAroundY(rotateY);
		this.centreVec.rotateAroundZ(rotateZ);
	}
	
	public OrientedBB(AxisAlignedBB aabb){
		this(Vec3.createVectorHelper((aabb.maxX+aabb.minX)/2,(aabb.maxY+aabb.minY)/2,(aabb.maxZ+aabb.minZ)/2),
				Vec3.createVectorHelper((aabb.maxX+aabb.minX)/2,(aabb.maxY+aabb.minY)/2,(aabb.maxZ+aabb.minZ)/2),
				(float)(aabb.maxX-aabb.minX),(float)(aabb.maxY-aabb.minY),(float)(aabb.maxZ-aabb.minZ),0f,0f,0f);
	}
	public OrientedBB(AxisAlignedBB aabb,Vec3 rotationPoint,float rotateX,float rotateY,float rotateZ){
		this(Vec3.createVectorHelper((aabb.maxX+aabb.minX)/2,(aabb.maxY+aabb.minY)/2,(aabb.maxZ+aabb.minZ)/2),
				rotationPoint,
				(float)(aabb.maxX-aabb.minX),(float)(aabb.maxY-aabb.minY),(float)(aabb.maxZ-aabb.minZ),rotateX,rotateY,rotateZ);
	}

	public boolean isCollidingWith(OrientedBB obb){
		Vec3 Ae1=this.centreVec.subtract(this.widthVec);
		Vec3 Ae2=this.centreVec.subtract(this.heightVec);
		Vec3 Ae3=this.centreVec.subtract(this.depthVec);
		Vec3 NAe1=Ae1.normalize();
		Vec3 NAe2=Ae2.normalize();
		Vec3 NAe3=Ae3.normalize();

		Vec3 Be1=obb.centreVec.subtract(obb.widthVec);
		Vec3 Be2=obb.centreVec.subtract(obb.heightVec);
		Vec3 Be3=obb.centreVec.subtract(obb.depthVec);
		Vec3 NBe1=Be1.normalize();
		Vec3 NBe2=Be2.normalize();
		Vec3 NBe3=Be3.normalize();
		
		Vec3 totc=this.origin.addVector(this.centreVec.xCoord, this.centreVec.yCoord, this.centreVec.zCoord);
		Vec3 oooc=obb.origin.addVector(obb.centreVec.xCoord, obb.centreVec.yCoord, obb.centreVec.zCoord);

		Vec3 Interval=totc.subtract(oooc);
		//System.out.println(" interval:"+D3DXVec3Length(Interval));
		//System.out.println(" this.centre:"+D3DXVec3Length(centreVec)+" this.origin:"+D3DXVec3Length(origin)+
			//	" obb.centre:"+D3DXVec3Length(obb.centreVec)+" obb.origin:"+D3DXVec3Length(obb.origin));
		
		// 分離軸 : Ae1
		float rA = D3DXVec3Length( Ae1 );
		float rB = LenSegOnSeparateAxis( NAe1, Be1, Be2, Be3 );
		float L = MathHelper.abs(D3DXVec3Dot( Interval, NAe1 ));
		if( L > rA + rB )
			return false; // 衝突していない

		// 分離軸 : Ae2
		rA = D3DXVec3Length( Ae2 );
		rB = LenSegOnSeparateAxis( NAe2, Be1, Be2, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, NAe2 ));
		if( L > rA + rB )
			return false;

		// 分離軸 : Ae3
		rA = D3DXVec3Length( Ae3 );
		rB = LenSegOnSeparateAxis( NAe3, Be1, Be2, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, NAe3 ));
		if( L > rA + rB )
			return false;

		// 分離軸 : Be1
		rA = LenSegOnSeparateAxis( NBe1, Ae1, Ae2, Ae3 );
		rB = D3DXVec3Length( Be1 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, NBe1 ));
		if( L > rA + rB )
			return false;

		// 分離軸 : Be2
		rA = LenSegOnSeparateAxis( NBe2, Ae1, Ae2, Ae3 );
		rB = D3DXVec3Length( Be2 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, NBe2 ));
		if( L > rA + rB )
			return false;

		// 分離軸 : Be3
		rA = LenSegOnSeparateAxis( NBe3, Ae1, Ae2, Ae3 );
		rB = D3DXVec3Length( Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, NBe3 ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C11
		Vec3 Cross=null;
		Cross=D3DXVec3Cross(  NAe1, NBe1 );
		rA = LenSegOnSeparateAxis( Cross, Ae2, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be2, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C12
		Cross=D3DXVec3Cross( NAe1, NBe2 );
		rA = LenSegOnSeparateAxis( Cross, Ae2, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C13
		Cross=D3DXVec3Cross( NAe1, NBe3 );
		rA = LenSegOnSeparateAxis( Cross, Ae2, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be2 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C21
		Cross=D3DXVec3Cross(NAe2, NBe1 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be2, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C22
		Cross=D3DXVec3Cross(NAe2, NBe2 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C23
		Cross=D3DXVec3Cross( NAe2, NBe3 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae3 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be2 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C31
		Cross=D3DXVec3Cross( NAe3, NBe1 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae2 );
		rB = LenSegOnSeparateAxis( Cross, Be2, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C32
		Cross=D3DXVec3Cross( NAe3, NBe2 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae2 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be3 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離軸 : C33
		Cross=D3DXVec3Cross( NAe3, NBe3 );
		rA = LenSegOnSeparateAxis( Cross, Ae1, Ae2 );
		rB = LenSegOnSeparateAxis( Cross, Be1, Be2 );
		L = MathHelper.abs(D3DXVec3Dot( Interval, Cross ));
		if( L > rA + rB )
			return false;

		// 分離平面が存在しないので「衝突している」
		return true;
	}

	public float LenSegOnSeparateAxis( Vec3 Sep, Vec3 e1, Vec3 e2){
		return LenSegOnSeparateAxis(Sep,e1,e2,null);
	}

	// 分離軸に投影された軸成分から投影線分長を算出
	public float LenSegOnSeparateAxis( Vec3 Sep, Vec3 e1, Vec3 e2, Vec3 e3)
	{
		// 3つの内積の絶対値の和で投影線分長を計算
		// 分離軸Sepは標準化されていること
		float r1 = MathHelper.abs(D3DXVec3Dot( Sep, e1 ));
		float r2 = MathHelper.abs(D3DXVec3Dot( Sep, e2 ));
		float r3 = e3!=null ? (MathHelper.abs(D3DXVec3Dot( Sep, e3 ))) : 0;
		return r1 + r2 + r3;
	}

	public float D3DXVec3Length(Vec3 vec){
		return (float) vec.lengthVector();
	}

	public float D3DXVec3Dot(Vec3 vec1,Vec3 vec2){
		return (float) vec1.dotProduct(vec2);
	}

	public Vec3 D3DXVec3Cross(Vec3 vec1,Vec3 vec2){
		
		return vec1.crossProduct(vec2);
	}
}
