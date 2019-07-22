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
 
 
 	// NBT�̎��� 
 	//--------------------------------------------------------------------------------------- 
 	/* 
 	 * NBT(Named By Tag)�̓ǂݍ���. 
 	 * TileEntity��Entity, ItemStack�̂悤�Ɏ��s���ɃC���X�^���X�𐶐�����悤�ȃN���X�̓t�B�[���h��ʓr�ۑ����Ă����K�v������. 
 	 * ���̂��߂�NBT�Ƃ����`���ŕۑ�/�ǂݍ��݂����Ă���. 
 	 */ 
 
 
 	/* 
 	 * �t�B�[���h��NBT����ǂݍ��ރ��\�b�h. 
 	 */ 
 	@Override 
 	public void readFromNBT(NBTTagCompound nbtTagCompound) 
 	{ 
 		super.readFromNBT(nbtTagCompound); 

 
 		/* 
 		 * NBTTagCompound����"Items"�^�O������NBTTagList�����o��. 
 		 * 1.7����NBTType�������ɕK�v�ɂȂ���. 
 		 * ������NBTType�̑Ή��͈ȉ��̒ʂ�. 
 		 * 0 => "END",    1 => "BYTE",   2 => "SHORT",     3 => "INT", 
 		 * 4 => "LONG",   5 => "FLOAT",  6 => "DOUBLE",    7 => "BYTE[]", 
 		 * 8 => "STRING", 9 => "LIST",  10 => "COMPOUND", 11 => "INT[]" 
 		 * 
 		 * �����NBTCompound�Ȃ̂�10��n��. 
 		 * net.minecraftforge.common.util.Constants�ɊeNBT���Ƃ̒萔���p�ӂ���Ă���̂ŗ��p����. 
 		 */ 
 		NBTTagList itemsTagList = nbtTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND); 
 
 
		this.items = new ItemStack[this.getSizeInventory()]; 
 
 
 		/* 
 		 * "Items"�^�O������NBTTagList����, "Slot"�^�O�̂��̂��������o��. 
8 		 */ 
 		for (int tagCounter = 0; tagCounter < itemsTagList.tagCount(); ++tagCounter) 
 		{ 
 			NBTTagCompound itemTagCompound = (NBTTagCompound)itemsTagList.getCompoundTagAt(tagCounter); 
 
 
 			/* 
54 			 * byte�Ȃ̂ŗe�ʂ̐ߖ�̂���. 
55 			 * �X���b�g�ԍ��͍���0~9�Ȃ̂�, int��short�ł͑傫������. 
56 			 */ 
 			byte slotIndex = itemTagCompound.getByte("Slot"); 
 
 
 			if (slotIndex >= 0 && slotIndex < this.items.length) 
 			{ 
 				/* 
62 				 * NBTTagCompound����ItemStack�̃C���X�^���X�𓾂郁�\�b�h�𗘗p����. 
63 				 */ 
 				this.items[slotIndex] = ItemStack.loadItemStackFromNBT(itemTagCompound); 
 			} 
 		} 
 	} 
 
 
 	/* 
70 	 * �t�B�[���h�̕ۑ��̂��߂�NBT�ɕϊ����郁�\�b�h. 
71 	 */ 
 	@Override 
 	public void writeToNBT(NBTTagCompound nbtTagCompound) 
 	{ 
 		super.writeToNBT(nbtTagCompound); 
 		NBTTagList itemsTagList = new NBTTagList(); 
 
 
 		for (int slotIndex = 0; slotIndex < this.items.length; ++slotIndex) 
 		{ 
 			/* 
81 			 * ItemStack������X���b�g�̃f�[�^�����ۑ�����. 
82 			 */ 
 			if (this.items[slotIndex] != null) 
 			{ 
 				NBTTagCompound itemTagCompound = new NBTTagCompound(); 
 
 
 				/* 
88 				 * itemTagCompound�ɃX���b�g�ԍ���ItemStack�̏�����������. 
89 				 */ 
 				itemTagCompound.setByte("Slot", (byte)slotIndex); 
 				this.items[slotIndex].writeToNBT(itemTagCompound); 
 
 
 				/* 
 				 * itemTagList��itemTagCompound��ǉ�����. 
 				 */ 
 				itemsTagList.appendTag(itemTagCompound); 
 			} 
 		} 
 
 
 		/* 
 		 * NBTTagList�ɕϊ����ꂽ����������NBTTagCompound�ɂ܂Ƃ߂ēn��. 
 		 */ 
 		nbtTagCompound.setTag("Items", itemsTagList); 
 	} 
 
 
 
 
 	//IInventory�̎��� 
 	//--------------------------------------------------------------------------------------- 
 	/* 
110 	 * IInventory�̓C���x���g���@�\��񋟂���C���^�t�F�[�X. 
111 	 * �C���x���g���ɕK�v�ȃ��\�b�h��K�؂ɃI�[�o�[���C�h����. 
112 	 */ 
 
 
 	/* 
115 	 * Inventory�̗v�f����Ԃ����\�b�h. 
116 	 */ 
 	@Override 
 	public int getSizeInventory() 
 	{ 
 		return 9; 
 	} 
 
 
 	/* 
124 	 * �X���b�g�̒��g��Ԃ����\�b�h. 
125 	 * �����̓X���b�g�ԍ� 
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
138 	 * �X���b�g�̒��g�̃X�^�b�N�T�C�Y��ύX���郁�\�b�h. 
139 	 * (���\�b�h���͂����炭decrement stack size) 
140 	 * ������(�X���b�g�ԍ�, ��������X�^�b�N��) 
141 	 * �߂�l�͕������ItemStack 
142 	 */ 
 	@Override 
 	public ItemStack decrStackSize(int slotIndex, int splitStackSize) 
 	{ 
 		if (this.items[slotIndex] != null) 
 		{ 
 			/* 
149 			 * �����̃X�^�b�N����茻�݂̃X�^�b�N�������Ȃ��ꍇ�͈ړ��ɂȂ�. 
150 			 */ 
 			if (this.items[slotIndex].stackSize <= splitStackSize) 
 			{ 
 				ItemStack tmpItemStack = items[slotIndex]; 
 				this.items[slotIndex] = null; 
 
 
 				/* 
157 				 * �X���b�g�̕ύX��ʒm. 
158 				 */ 
 				this.markDirty(); 
 
 
 				return tmpItemStack; 
 			} 
 
 
 			/* 
 			 * �����̃X�^�b�N����茻�݂̃X�^�b�N���������ꍇ�͕�������. 
 			 */ 
 			ItemStack splittedItemStack = this.items[slotIndex].splitStack(splitStackSize); 
 
 
 			/* 
170 			 * ������ɃX�^�b�N����0�ɂȂ����ꍇ�͋�ɂ���. 
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
185 	 * �R���e�i������ꂽ�Ƃ���, �X���b�g�ɃA�C�e����ێ��ł��Ȃ��^�C�v(�G���`�����g��⃏�[�N�x���`�Ȃ�)�̏ꍇ, 
186 	 * �����̃X���b�g��ItemStack��Ԃ����\�b�h. 
187 	 */ 
 	@Override 
 	public ItemStack getStackInSlotOnClosing(int slotIndex) 
 	{ 
 		return this.items[slotIndex]; 
 	} 
 
 
 	/* 
195 	 * Inventory��ItemStack�����郁�\�b�h. 
196 	 * ������(�X���b�g�ԍ�, ���̃X���b�g�ɓ����ItemStack) 
197 	 */ 
 	@Override 
 	public void setInventorySlotContents(int slotIndex, ItemStack itemStack) 
 	{ 
 		this.items[slotIndex] = itemStack; 
 
 
 		if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) 
 		{ 
 			/* 
206 			 * �X���b�g�̃X�^�b�N�������𒴂��Ă���ꍇ, �������ۂ�. 
207 			 */ 
 			itemStack.stackSize = getInventoryStackLimit(); 
 		} 
 		this.markDirty(); 
 	} 
 
 
 	/* 
214  	 * Inventory�̖��O 
215  	 * (�����ł�unlocalizedName����, �����Ŗ��O������ꍇ�̏����͂��قȂ�̂�, ���܂ǂ�`�F�X�g�̃R�[�h���Q�l�̂���) 
216  	 */ 
 	@Override 
 	public String getInventoryName() 
 	{ 
 		return "container.tileEntityGuiSample"; 
 	} 
 
 
 	/* 
224 	 * �����Ŗ��O���������ǂ��� 
225 	 * ����͂��Ȃ��̂�false 
226 	 */ 
 	@Override 
 	public boolean hasCustomInventoryName() 
 	{ 
 		return false; 
 	} 
 
 
 	/* 
234 	 * 1�X���b�g������̍ő�X�^�b�N�� 
235 	 */ 
 	@Override 
 	public int getInventoryStackLimit() 
 	{ 
 		return 64; 
 	} 
 
 
 	/* 
243 	 * ���Container�ŗ��p����, GUI���J���邩�ǂ����𔻒肷�郁�\�b�h. 
244 	 */ 
 	@Override 
 	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
 	{ 
 		/* 
249 		 * ���̍��W�ɂ���TileEntity�����̃N���X�łȂ��Ȃ�false 
250 		 */ 
 		if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) 
 		{ 
 			return false; 
 		} 
 
 
 		/* 
257 		 * ���̏ꍇ�ł�, �v���C���[�ƃu���b�N�̋��������ꂷ���Ă���ꍇ��false 
258 		 */ 
 		return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D; 
 	} 
 
 
 	@Override 
 	public void openInventory() 
 	{ 
 		/* 
266 		 *  ����͗��p���Ȃ�. 
267 		 *  ���p����ꍇ��, Container�̃R���X�g���N�^�ł��̃��\�b�h���ĂԕK�v������. 
268 		 */ 
 	} 
 
 
 	@Override 
 	public void closeInventory() 
 	{ 
 		/* 
275 		 *  ����͗��p���Ȃ�. 
276 		 *  ���p����ꍇ��, Container��onContainerClosed���\�b�h�ł��̃��\�b�h���ĂԕK�v������. 
277 		 */ 
 	} 
 

 	/* 
281 	 * true�ł�Hopper�ŃA�C�e���𑗂��悤�ɂȂ�. 
282 	 */ 
 	@Override 
 	public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) 
 	{ 
 		return true; 
 	} 
} 
