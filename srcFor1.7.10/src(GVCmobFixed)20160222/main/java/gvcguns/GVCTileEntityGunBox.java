package gvcguns; 
 

import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.inventory.IInventory; 
import net.minecraft.item.ItemStack; 
import net.minecraft.nbt.NBTTagCompound; 
import net.minecraft.nbt.NBTTagList; 
import net.minecraft.tileentity.TileEntity; 
import net.minecraftforge.common.util.Constants; 
 
 
public class GVCTileEntityGunBox extends TileEntity implements IInventory 
{ 
	private ItemStack[] items = new ItemStack[9]; 
 
 
 	// NBTの実装 
 	//--------------------------------------------------------------------------------------- 
 	/* 
 	 * NBT(Named By Tag)の読み込み. 
 	 * TileEntityやEntity, ItemStackのように実行中にインスタンスを生成するようなクラスはフィールドを別途保存しておく必要がある. 
 	 * そのためにNBTという形式で保存/読み込みをしている. 
 	 */ 
 
 
 	/* 
 	 * フィールドをNBTから読み込むメソッド. 
 	 */ 
 	@Override 
 	public void readFromNBT(NBTTagCompound nbtTagCompound) 
 	{ 
 		super.readFromNBT(nbtTagCompound); 

 
 		/* 
 		 * NBTTagCompoundから"Items"タグがついたNBTTagListを取り出す. 
 		 * 1.7からNBTTypeが引数に必要になった. 
 		 * 引数とNBTTypeの対応は以下の通り. 
 		 * 0 => "END",    1 => "BYTE",   2 => "SHORT",     3 => "INT", 
 		 * 4 => "LONG",   5 => "FLOAT",  6 => "DOUBLE",    7 => "BYTE[]", 
 		 * 8 => "STRING", 9 => "LIST",  10 => "COMPOUND", 11 => "INT[]" 
 		 * 
 		 * 今回はNBTCompoundなので10を渡す. 
 		 * net.minecraftforge.common.util.Constantsに各NBTごとの定数が用意されているので利用する. 
 		 */ 
 		NBTTagList itemsTagList = nbtTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND); 
 
 
		this.items = new ItemStack[this.getSizeInventory()]; 
 
 
 		/* 
 		 * "Items"タグがついかNBTTagListから, "Slot"タグのものを順次取り出す. 
8 		 */ 
 		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); ++tagCounter) 
 		{ 
 			NBTTagCompound itemTagCompound = (NBTTagCompound)itemsTagList.getCompoundTagAt(tagCounter); 
 
 
 			/* 
54 			 * byteなので容量の節約のため. 
55 			 * スロット番号は今回0~9なので, intやshortでは大きすぎる. 
56 			 */ 
 			byte slotIndex = itemTagCompound.getByte("Slot"); 
 
 
 			if (slotIndex >= 0 && slotIndex < this.items.length) 
 			{ 
 				/* 
62 				 * NBTTagCompoundからItemStackのインスタンスを得るメソッドを利用する. 
63 				 */ 
 				this.items[slotIndex] = ItemStack.loadItemStackFromNBT(itemTagCompound); 
 			} 
 		} 
 	} 
 
 
 	/* 
70 	 * フィールドの保存のためにNBTに変換するメソッド. 
71 	 */ 
 	@Override 
 	public void writeToNBT(NBTTagCompound nbtTagCompound) 
 	{ 
 		super.writeToNBT(nbtTagCompound); 
 		NBTTagList itemsTagList = new NBTTagList(); 
 
 
 		for (int slotIndex = 0; slotIndex < this.items.length; ++slotIndex) 
 		{ 
 			/* 
81 			 * ItemStackがあるスロットのデータだけ保存する. 
82 			 */ 
 			if (this.items[slotIndex] != null) 
 			{ 
 				NBTTagCompound itemTagCompound = new NBTTagCompound(); 
 
 
 				/* 
88 				 * itemTagCompoundにスロット番号とItemStackの情報を書き込む. 
89 				 */ 
 				itemTagCompound.setByte("Slot", (byte)slotIndex); 
 				this.items[slotIndex].writeToNBT(itemTagCompound); 
 
 
 				/* 
 				 * itemTagListにitemTagCompoundを追加する. 
 				 */ 
 				itemsTagList.appendTag(itemTagCompound); 
 			} 
 		} 
 
 
 		/* 
 		 * NBTTagListに変換された情報を引数のNBTTagCompoundにまとめて渡す. 
 		 */ 
 		nbtTagCompound.setTag("Items", itemsTagList); 
 	} 
 
 
 
 
 	//IInventoryの実装 
 	//--------------------------------------------------------------------------------------- 
 	/* 
110 	 * IInventoryはインベントリ機能を提供するインタフェース. 
111 	 * インベントリに必要なメソッドを適切にオーバーライドする. 
112 	 */ 
 
 
 	/* 
115 	 * Inventoryの要素数を返すメソッド. 
116 	 */ 
 	@Override 
 	public int getSizeInventory() 
 	{ 
 		return 9; 
 	} 
 
 
 	/* 
124 	 * スロットの中身を返すメソッド. 
125 	 * 引数はスロット番号 
126 	 */ 
 	@Override 
 	public ItemStack getStackInSlot(int slotIndex) 
 	{ 
 		if (slotIndex >= 0 && slotIndex < this.items.length) 
 		{ 
 			return this.items[slotIndex]; 
 		} 
 		return null; 
 	} 
 
 
 	/* 
138 	 * スロットの中身のスタックサイズを変更するメソッド. 
139 	 * (メソッド名はおそらくdecrement stack size) 
140 	 * 引数は(スロット番号, 分割するスタック数) 
141 	 * 戻り値は分割後のItemStack 
142 	 */ 
 	@Override 
 	public ItemStack decrStackSize(int slotIndex, int splitStackSize) 
 	{ 
 		if (this.items[slotIndex] != null) 
 		{ 
 			/* 
149 			 * 引数のスタック数より現在のスタック数が少ない場合は移動になる. 
150 			 */ 
 			if (this.items[slotIndex].stackSize <= splitStackSize) 
 			{ 
 				ItemStack tmpItemStack = items[slotIndex]; 
 				this.items[slotIndex] = null; 
 
 
 				/* 
157 				 * スロットの変更を通知. 
158 				 */ 
 				this.markDirty(); 
 
 
 				return tmpItemStack; 
 			} 
 
 
 			/* 
 			 * 引数のスタック数より現在のスタック数が多い場合は分割する. 
 			 */ 
 			ItemStack splittedItemStack = this.items[slotIndex].splitStack(splitStackSize); 
 
 
 			/* 
170 			 * 分割後にスタック数が0になった場合は空にする. 
171 			 */ 
 			if (this.items[slotIndex].stackSize == 0) 
 			{ 
 				this.items[slotIndex] = null; 
 			} 
 
 
 			this.markDirty(); 
 

 			return splittedItemStack; 
 		} 
 		return null; 
 	} 
 
 
 	/* 
185 	 * コンテナが閉じられたときに, スロットにアイテムを保持できないタイプ(エンチャント台やワークベンチなど)の場合, 
186 	 * 引数のスロットのItemStackを返すメソッド. 
187 	 */ 
 	@Override 
 	public ItemStack getStackInSlotOnClosing(int slotIndex) 
 	{ 
 		return this.items[slotIndex]; 
 	} 
 
 
 	/* 
195 	 * InventoryにItemStackを入れるメソッド. 
196 	 * 引数は(スロット番号, そのスロットに入れるItemStack) 
197 	 */ 
 	@Override 
 	public void setInventorySlotContents(int slotIndex, ItemStack itemStack) 
 	{ 
 		this.items[slotIndex] = itemStack; 
 
 
 		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) 
 		{ 
 			/* 
206 			 * スロットのスタック数制限を超えている場合, 制限を課す. 
207 			 */ 
 			itemStack.stackSize = getInventoryStackLimit(); 
 		} 
 		this.markDirty(); 
 	} 
 
 
 	/* 
214  	 * Inventoryの名前 
215  	 * (ここではunlocalizedNameだが, 金床で名前をつける場合の処理はやや異なるので, かまどやチェストのコードを参考のこと) 
216  	 */ 
 	@Override 
 	public String getInventoryName() 
 	{ 
 		return "container.tileEntityGuiSample"; 
 	} 
 
 
 	/* 
224 	 * 金床で名前をつけたかどうか 
225 	 * 今回はつけないのでfalse 
226 	 */ 
 	@Override 
 	public boolean hasCustomInventoryName() 
 	{ 
 		return false; 
 	} 
 
 
 	/* 
234 	 * 1スロットあたりの最大スタック数 
235 	 */ 
 	@Override 
 	public int getInventoryStackLimit() 
 	{ 
 		return 64; 
 	} 
 
 
 	/* 
243 	 * 主にContainerで利用する, GUIを開けるかどうかを判定するメソッド. 
244 	 */ 
 	@Override 
 	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
 	{ 
 		/* 
249 		 * この座標にあるTileEntityがこのクラスでないならfalse 
250 		 */ 
 		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) 
 		{ 
 			return false; 
 		} 
 
 
 		/* 
257 		 * その場合でも, プレイヤーとブロックの距離が離れすぎている場合もfalse 
258 		 */ 
 		return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D; 
 	} 
 
 
 	@Override 
 	public void openInventory() 
 	{ 
 		/* 
266 		 *  今回は利用しない. 
267 		 *  利用する場合は, Containerのコンストラクタでこのメソッドを呼ぶ必要がある. 
268 		 */ 
 	} 
 
 
 	@Override 
 	public void closeInventory() 
 	{ 
 		/* 
275 		 *  今回は利用しない. 
276 		 *  利用する場合は, ContainerのonContainerClosedメソッドでこのメソッドを呼ぶ必要がある. 
277 		 */ 
 	} 
 

 	/* 
281 	 * trueではHopperでアイテムを送れるようになる. 
282 	 */ 
 	@Override 
 	public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) 
 	{ 
 		return true; 
 	} 
} 
