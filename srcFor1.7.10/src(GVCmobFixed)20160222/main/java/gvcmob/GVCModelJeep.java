// Date: 2015/02/25 22:50:34
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package gvcmob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GVCModelJeep extends ModelBase
{
  //fields
    ModelRenderer coverLF1;
    ModelRenderer coverLF2;
    ModelRenderer coverLF3;
    ModelRenderer coverLF4;
    ModelRenderer coverLF5;
    ModelRenderer coverRF1;
    ModelRenderer coverRF2;
    ModelRenderer coverRF3;
    ModelRenderer coverRF4;
    ModelRenderer coverRF5;
    ModelRenderer coverRB1;
    ModelRenderer coverRB2;
    ModelRenderer coverRB3;
    ModelRenderer coverRB4;
    ModelRenderer coverRB5;
    ModelRenderer coverLB1;
    ModelRenderer coverLB2;
    ModelRenderer coverLB3;
    ModelRenderer coverLB4;
    ModelRenderer coverLB5;
    ModelRenderer coverBC1L;
    ModelRenderer coverBC2L;
    ModelRenderer coverBC3L;
    ModelRenderer coverBC4L;
    ModelRenderer coverBC5L;
    ModelRenderer coverBC1R;
    ModelRenderer coverBC2R;
    ModelRenderer coverBC3R;
    ModelRenderer coverBC4R;
    ModelRenderer coverBC5R;
    ModelRenderer bottom1;
    ModelRenderer bottom2;
    ModelRenderer bottom3;
    ModelRenderer bottom4;
    ModelRenderer bottom5;
    ModelRenderer bottom6;
    ModelRenderer screenL1;
    ModelRenderer screenL2;
    ModelRenderer screenL3;
    ModelRenderer screenL4;
    ModelRenderer screenR1;
    ModelRenderer screenR2;
    ModelRenderer screenR3;
    ModelRenderer screenR4;
    ModelRenderer esR1;
    ModelRenderer esR2;
    ModelRenderer esR3;
    ModelRenderer esR4;
    ModelRenderer esR5;
    ModelRenderer esR6;
    ModelRenderer esL1;
    ModelRenderer esL2;
    ModelRenderer esL3;
    ModelRenderer esL4;
    ModelRenderer esL5;
    ModelRenderer esL6;
    ModelRenderer esLF1;
    ModelRenderer esLF2;
    ModelRenderer esLF3;
    ModelRenderer esLF4;
    ModelRenderer esLF5;
    ModelRenderer esLF6;
    ModelRenderer esRF1;
    ModelRenderer esRF2;
    ModelRenderer esRF3;
    ModelRenderer esRF4;
    ModelRenderer esRF5;
    ModelRenderer esRF6;
    ModelRenderer BscL;
    ModelRenderer BscR;
    ModelRenderer backdoor;
    ModelRenderer frontR;
    ModelRenderer frontL;
    ModelRenderer lightL;
    ModelRenderer lightR;
    ModelRenderer bodyM1;
    ModelRenderer bodyM2;
    ModelRenderer bodyF1;
    ModelRenderer bodyF2;
    ModelRenderer bodyF3;
    ModelRenderer bodyF4;
    ModelRenderer bodyF5;
    ModelRenderer bodyF6;
    ModelRenderer bodyF7;
    ModelRenderer bodyF8;
    ModelRenderer bodyF9;
    ModelRenderer bodyF10;
    ModelRenderer bodyF11;
    ModelRenderer bodyF12;
    ModelRenderer toyota;
    ModelRenderer exF1;
    ModelRenderer exF2;
    ModelRenderer wheelLF1;
    ModelRenderer wheelLF2;
    ModelRenderer wheelLF3;
    ModelRenderer wheelLF4;
    ModelRenderer wheelLF5;
    ModelRenderer wheelLF6;
    ModelRenderer wheelLF7;
    ModelRenderer wheelRF1;
    ModelRenderer wheelRF2;
    ModelRenderer wheelRF3;
    ModelRenderer wheelRF4;
    ModelRenderer wheelRF5;
    ModelRenderer wheelLR6;
    ModelRenderer wheelRF7;
    ModelRenderer wheelLB1;
    ModelRenderer wheelLB2;
    ModelRenderer wheelLB3;
    ModelRenderer wheelLB4;
    ModelRenderer wheelLB5;
    ModelRenderer wheelLB6;
    ModelRenderer wheelLB7;
    ModelRenderer wheelRB1;
    ModelRenderer wheelRB2;
    ModelRenderer wheelRB3;
    ModelRenderer wheelRB4;
    ModelRenderer wheelRB5;
    ModelRenderer wheelRB6;
    ModelRenderer wheelRF71;
    ModelRenderer stick1;
    ModelRenderer stick2;
  
  public GVCModelJeep()
  {
    textureWidth = 256;
    textureHeight = 512;
    
      coverLF1 = new ModelRenderer(this, 57, 134);
      coverLF1.addBox(-2.9F, -9.2F, -2F, 5, 1, 4);
      coverLF1.setRotationPoint(14F, 18F, -46F);
      coverLF1.setTextureSize(256, 512);
      coverLF1.mirror = true;
      setRotation(coverLF1, 0.7853982F, 0F, 0F);
      coverLF2 = new ModelRenderer(this, 93, 141);
      coverLF2.addBox(-3F, -8F, -5F, 5, 1, 10);
      coverLF2.setRotationPoint(14F, 18F, -46F);
      coverLF2.setTextureSize(256, 512);
      coverLF2.mirror = true;
      setRotation(coverLF2, 0F, 0F, 0F);
      coverLF3 = new ModelRenderer(this, 57, 154);
      coverLF3.addBox(-2.9F, -9.2F, -2F, 5, 1, 4);
      coverLF3.setRotationPoint(14F, 18F, -46F);
      coverLF3.setTextureSize(256, 512);
      coverLF3.mirror = true;
      setRotation(coverLF3, -0.7853982F, 0F, 0F);
      coverLF4 = new ModelRenderer(this, 39, 127);
      coverLF4.addBox(-3F, -5F, -8F, 5, 3, 1);
      coverLF4.setRotationPoint(14F, 18F, -46F);
      coverLF4.setTextureSize(256, 512);
      coverLF4.mirror = true;
      setRotation(coverLF4, 0F, 0F, 0F);
      coverLF5 = new ModelRenderer(this, 39, 160);
      coverLF5.addBox(-3F, -5F, 7F, 5, 4, 1);
      coverLF5.setRotationPoint(14F, 18F, -46F);
      coverLF5.setTextureSize(256, 512);
      coverLF5.mirror = true;
      setRotation(coverLF5, 0F, 0F, 0F);
      coverRF1 = new ModelRenderer(this, 38, 134);
      coverRF1.addBox(-4.1F, -9.2F, -2F, 5, 1, 4);
      coverRF1.setRotationPoint(-12F, 18F, -46F);
      coverRF1.setTextureSize(256, 512);
      coverRF1.mirror = true;
      setRotation(coverRF1, 0.7853982F, 0F, 0F);
      coverRF2 = new ModelRenderer(this, 62, 141);
      coverRF2.addBox(-4F, -8F, -5F, 5, 1, 10);
      coverRF2.setRotationPoint(-12F, 18F, -46F);
      coverRF2.setTextureSize(256, 512);
      coverRF2.mirror = true;
      setRotation(coverRF2, 0F, 0F, 0F);
      coverRF3 = new ModelRenderer(this, 38, 154);
      coverRF3.addBox(-4.1F, -9.2F, -2F, 5, 1, 4);
      coverRF3.setRotationPoint(-12F, 18F, -46F);
      coverRF3.setTextureSize(256, 512);
      coverRF3.mirror = true;
      setRotation(coverRF3, -0.7853982F, 0F, 0F);
      coverRF4 = new ModelRenderer(this, 26, 127);
      coverRF4.addBox(-4F, -5F, -8F, 5, 3, 1);
      coverRF4.setRotationPoint(-12F, 18F, -46F);
      coverRF4.setTextureSize(256, 512);
      coverRF4.mirror = true;
      setRotation(coverRF4, 0F, 0F, 0F);
      coverRF5 = new ModelRenderer(this, 26, 160);
      coverRF5.addBox(-4F, -5F, 7F, 5, 4, 1);
      coverRF5.setRotationPoint(-12F, 18F, -46F);
      coverRF5.setTextureSize(256, 512);
      coverRF5.mirror = true;
      setRotation(coverRF5, 0F, 0F, 0F);
      coverRB1 = new ModelRenderer(this, 0, 134);
      coverRB1.addBox(-4.1F, -9.2F, -2F, 5, 1, 4);
      coverRB1.setRotationPoint(-12F, 18F, 04F);
      coverRB1.setTextureSize(256, 512);
      coverRB1.mirror = true;
      setRotation(coverRB1, 0.7853982F, 0F, 0F);
      coverRB2 = new ModelRenderer(this, 0, 141);
      coverRB2.addBox(-4F, -8F, -5F, 5, 1, 10);
      coverRB2.setRotationPoint(-12F, 18F, 04F);
      coverRB2.setTextureSize(256, 512);
      coverRB2.mirror = true;
      setRotation(coverRB2, 0F, 0F, 0F);
      coverRB3 = new ModelRenderer(this, 0, 154);
      coverRB3.addBox(-4.1F, -9.2F, -2F, 5, 1, 4);
      coverRB3.setRotationPoint(-12F, 18F, 04F);
      coverRB3.setTextureSize(256, 512);
      coverRB3.mirror = true;
      setRotation(coverRB3, -0.7853982F, 0F, 0F);
      coverRB4 = new ModelRenderer(this, 0, 127);
      coverRB4.addBox(-4F, -5F, -8F, 5, 4, 1);
      coverRB4.setRotationPoint(-12F, 18F, 04F);
      coverRB4.setTextureSize(256, 512);
      coverRB4.mirror = true;
      setRotation(coverRB4, 0F, 0F, 0F);
      coverRB5 = new ModelRenderer(this, 0, 160);
      coverRB5.addBox(-4F, -5F, 7F, 5, 4, 1);
      coverRB5.setRotationPoint(-12F, 18F, 04F);
      coverRB5.setTextureSize(256, 512);
      coverRB5.mirror = true;
      setRotation(coverRB5, 0F, 0F, 0F);
      coverLB1 = new ModelRenderer(this, 19, 134);
      coverLB1.addBox(-2.9F, -9.2F, -2F, 5, 1, 4);
      coverLB1.setRotationPoint(14F, 18F, 04F);
      coverLB1.setTextureSize(256, 512);
      coverLB1.mirror = true;
      setRotation(coverLB1, 0.7853982F, 0F, 0F);
      coverLB2 = new ModelRenderer(this, 31, 141);
      coverLB2.addBox(-3F, -8F, -5F, 5, 1, 10);
      coverLB2.setRotationPoint(14F, 18F, 04F);
      coverLB2.setTextureSize(256, 512);
      coverLB2.mirror = true;
      setRotation(coverLB2, 0F, 0F, 0F);
      coverLB3 = new ModelRenderer(this, 19, 154);
      coverLB3.addBox(-2.9F, -9.2F, -2F, 5, 1, 4);
      coverLB3.setRotationPoint(14F, 18F, 04F);
      coverLB3.setTextureSize(256, 512);
      coverLB3.mirror = true;
      setRotation(coverLB3, -0.7853982F, 0F, 0F);
      coverLB4 = new ModelRenderer(this, 13, 127);
      coverLB4.addBox(-3F, -5F, -8F, 5, 4, 1);
      coverLB4.setRotationPoint(14F, 18F, 04F);
      coverLB4.setTextureSize(256, 512);
      coverLB4.mirror = true;
      setRotation(coverLB4, 0F, 0F, 0F);
      coverLB5 = new ModelRenderer(this, 13, 160);
      coverLB5.addBox(-3F, -5F, 7F, 5, 4, 1);
      coverLB5.setRotationPoint(14F, 18F, 04F);
      coverLB5.setTextureSize(256, 512);
      coverLB5.mirror = true;
      setRotation(coverLB5, 0F, 0F, 0F);
      coverBC1L = new ModelRenderer(this, 139, 145);
      coverBC1L.addBox(-4F, -8F, -5F, 1, 6, 10);
      coverBC1L.setRotationPoint(14F, 18F, 04F);
      coverBC1L.setTextureSize(256, 512);
      coverBC1L.mirror = true;
      setRotation(coverBC1L, 0F, 0F, 0F);
      coverBC2L = new ModelRenderer(this, 95, 158);
      coverBC2L.addBox(-4F, -5F, -8F, 1, 3, 3);
      coverBC2L.setRotationPoint(14F, 18F, 04F);
      coverBC2L.setTextureSize(256, 512);
      coverBC2L.mirror = true;
      setRotation(coverBC2L, 0F, 0F, 0F);
      coverBC3L = new ModelRenderer(this, 104, 158);
      coverBC3L.addBox(-4F, -5F, 5F, 1, 3, 3);
      coverBC3L.setRotationPoint(14F, 18F, 04F);
      coverBC3L.setTextureSize(256, 512);
      coverBC3L.mirror = true;
      setRotation(coverBC3L, 0F, 0F, 0F);
      coverBC4L = new ModelRenderer(this, 196, 158);
      coverBC4L.addBox(-3.9F, -9.2F, -2F, 1, 3, 4);
      coverBC4L.setRotationPoint(14F, 18F, 04F);
      coverBC4L.setTextureSize(256, 512);
      coverBC4L.mirror = true;
      setRotation(coverBC4L, -0.7853982F, 0F, 0F);
      coverBC5L = new ModelRenderer(this, 185, 158);
      coverBC5L.addBox(-3.9F, -9.2F, -2F, 1, 3, 4);
      coverBC5L.setRotationPoint(14F, 18F, 04F);
      coverBC5L.setTextureSize(256, 512);
      coverBC5L.mirror = true;
      setRotation(coverBC5L, 0.7853982F, 0F, 0F);
      coverBC1R = new ModelRenderer(this, 115, 145);
      coverBC1R.addBox(1F, -8F, -5F, 1, 6, 10);
      coverBC1R.setRotationPoint(-12F, 18F, 04F);
      coverBC1R.setTextureSize(256, 512);
      coverBC1R.mirror = true;
      setRotation(coverBC1R, 0F, 0F, 0F);
      coverBC2R = new ModelRenderer(this, 77, 158);
      coverBC2R.addBox(1F, -5F, -8F, 1, 3, 3);
      coverBC2R.setRotationPoint(-12F, 18F, 04F);
      coverBC2R.setTextureSize(256, 512);
      coverBC2R.mirror = true;
      setRotation(coverBC2R, 0F, 0F, 0F);
      coverBC3R = new ModelRenderer(this, 86, 158);
      coverBC3R.addBox(1F, -5F, 5F, 1, 3, 3);
      coverBC3R.setRotationPoint(-12F, 18F, 04F);
      coverBC3R.setTextureSize(256, 512);
      coverBC3R.mirror = true;
      setRotation(coverBC3R, 0F, 0F, 0F);
      coverBC4R = new ModelRenderer(this, 174, 158);
      coverBC4R.addBox(0.9F, -9.2F, -2F, 1, 3, 4);
      coverBC4R.setRotationPoint(-12F, 18F, 04F);
      coverBC4R.setTextureSize(256, 512);
      coverBC4R.mirror = true;
      setRotation(coverBC4R, -0.7853982F, 0F, 0F);
      coverBC5R = new ModelRenderer(this, 163, 158);
      coverBC5R.addBox(0.9F, -9.2F, -2F, 1, 3, 4);
      coverBC5R.setRotationPoint(-12F, 18F, 04F);
      coverBC5R.setTextureSize(256, 512);
      coverBC5R.mirror = true;
      setRotation(coverBC5R, 0.7853982F, 0F, 0F);
      bottom1 = new ModelRenderer(this, 0, 41);
      bottom1.addBox(-11F, 16F, 7F, 22, 1, 32);
      bottom1.setRotationPoint(0F, 0F, -20F);
      bottom1.setTextureSize(256, 512);
      bottom1.mirror = true;
      setRotation(bottom1, 0F, 0F, 0F);
      bottom2 = new ModelRenderer(this, 0, 63);
      bottom2.addBox(11F, 16F, 32F, 3, 1, 7);
      bottom2.setRotationPoint(0F, 0F, -20F);
      bottom2.setTextureSize(256, 512);
      bottom2.mirror = true;
      setRotation(bottom2, 0F, 0F, 0F);
      bottom3 = new ModelRenderer(this, 78, 41);
      bottom3.addBox(-14F, 16F, 32F, 3, 1, 7);
      bottom3.setRotationPoint(0F, 0F, -20F);
      bottom3.setTextureSize(256, 512);
      bottom3.mirror = true;
      setRotation(bottom3, 0F, 0F, 0F);
      bottom4 = new ModelRenderer(this, 0, 41);
      bottom4.addBox(11F, 16F, 7F, 3, 1, 9);
      bottom4.setRotationPoint(0F, 0F, -20F);
      bottom4.setTextureSize(256, 512);
      bottom4.mirror = true;
      setRotation(bottom4, 0F, 0F, 0F);
      bottom5 = new ModelRenderer(this, 0, 52);
      bottom5.addBox(-14F, 16F, 7F, 3, 1, 9);
      bottom5.setRotationPoint(0F, 0F, -20F);
      bottom5.setTextureSize(256, 512);
      bottom5.mirror = true;
      setRotation(bottom5, 0F, 0F, 0F);
      bottom6 = new ModelRenderer(this, 109, 48);
      bottom6.addBox(-14F, 16F, -18F, 28, 1, 25);
      bottom6.setRotationPoint(0F, 0F, -20F);
      bottom6.setTextureSize(256, 512);
      bottom6.mirror = true;
      setRotation(bottom6, 0F, 0F, 0F);
      screenL1 = new ModelRenderer(this, 39, 76);
      screenL1.addBox(14F, 2F, 8F, 1, 14, 8);
      screenL1.setRotationPoint(0F, 0F, -20F);
      screenL1.setTextureSize(256, 512);
      screenL1.mirror = true;
      setRotation(screenL1, 0F, 0F, 0F);
      screenL2 = new ModelRenderer(this, 61, 77);
      screenL2.addBox(14F, 2F, 32F, 1, 14, 7);
      screenL2.setRotationPoint(0F, 0F, -20F);
      screenL2.setTextureSize(256, 512);
      screenL2.mirror = true;
      setRotation(screenL2, 0F, 0F, 0F);
      screenL3 = new ModelRenderer(this, 42, 83);
      screenL3.addBox(14F, 2F, 16F, 1, 8, 16);
      screenL3.setRotationPoint(0F, 0F, -20F);
      screenL3.setTextureSize(256, 512);
      screenL3.mirror = true;
      setRotation(screenL3, 0F, 0F, 0F);
      screenL4 = new ModelRenderer(this, 72, 109);
      screenL4.addBox(13.7F, 2F, 7F, 1, 14, 1);
      screenL4.setRotationPoint(0F, 0F, -20F);
      screenL4.setTextureSize(256, 512);
      screenL4.mirror = true;
      setRotation(screenL4, 0F, 0F, 0F);
      screenR1 = new ModelRenderer(this, 0, 76);
      screenR1.addBox(-15F, 2F, 8F, 1, 14, 8);
      screenR1.setRotationPoint(0F, 0F, -20F);
      screenR1.setTextureSize(256, 512);
      screenR1.mirror = true;
      setRotation(screenR1, 0F, 0F, 0F);
      screenR2 = new ModelRenderer(this, 22, 77);
      screenR2.addBox(-15F, 2F, 32F, 1, 14, 7);
      screenR2.setRotationPoint(0F, 0F, -20F);
      screenR2.setTextureSize(256, 512);
      screenR2.mirror = true;
      setRotation(screenR2, 0F, 0F, 0F);
      screenR3 = new ModelRenderer(this, 3, 83);
      screenR3.addBox(-15F, 2F, 16F, 1, 8, 16);
      screenR3.setRotationPoint(0F, 0F, -20F);
      screenR3.setTextureSize(256, 512);
      screenR3.mirror = true;
      setRotation(screenR3, 0F, 0F, 0F);
      screenR4 = new ModelRenderer(this, 67, 109);
      screenR4.addBox(-14.7F, 2F, 7F, 1, 14, 1);
      screenR4.setRotationPoint(0F, 0F, -20F);
      screenR4.setTextureSize(256, 512);
      screenR4.mirror = true;
      setRotation(screenR4, 0F, 0F, 0F);
      esR1 = new ModelRenderer(this, 179, 131);
      esR1.addBox(-15F, 10F, 16F, 1, 1, 3);
      esR1.setRotationPoint(0F, 0F, -20F);
      esR1.setTextureSize(256, 512);
      esR1.mirror = true;
      setRotation(esR1, 0F, 0F, 0F);
      esR2 = new ModelRenderer(this, 179, 136);
      esR2.addBox(-15F, 11F, 16F, 1, 1, 2);
      esR2.setRotationPoint(0F, 0F, -20F);
      esR2.setTextureSize(256, 512);
      esR2.mirror = true;
      setRotation(esR2, 0F, 0F, 0F);
      esR3 = new ModelRenderer(this, 179, 140);
      esR3.addBox(-15F, 12F, 16F, 1, 1, 1);
      esR3.setRotationPoint(0F, 0F, -20F);
      esR3.setTextureSize(256, 512);
      esR3.mirror = true;
      setRotation(esR3, 0F, 0F, 0F);
      esR4 = new ModelRenderer(this, 188, 131);
      esR4.addBox(-15F, 10F, 29F, 1, 1, 3);
      esR4.setRotationPoint(0F, 0F, -20F);
      esR4.setTextureSize(256, 512);
      esR4.mirror = true;
      setRotation(esR4, 0F, 0F, 0F);
      esR5 = new ModelRenderer(this, 188, 136);
      esR5.addBox(-15F, 11F, 30F, 1, 1, 2);
      esR5.setRotationPoint(0F, 0F, -20F);
      esR5.setTextureSize(256, 512);
      esR5.mirror = true;
      setRotation(esR5, 0F, 0F, 0F);
      esR6 = new ModelRenderer(this, 188, 140);
      esR6.addBox(-15F, 12F, 31F, 1, 1, 1);
      esR6.setRotationPoint(0F, 0F, -20F);
      esR6.setTextureSize(256, 512);
      esR6.mirror = true;
      setRotation(esR6, 0F, 0F, 0F);
      esL1 = new ModelRenderer(this, 161, 131);
      esL1.addBox(14F, 10F, 16F, 1, 1, 3);
      esL1.setRotationPoint(0F, 0F, -20F);
      esL1.setTextureSize(256, 512);
      esL1.mirror = true;
      setRotation(esL1, 0F, 0F, 0F);
      esL2 = new ModelRenderer(this, 161, 136);
      esL2.addBox(14F, 11F, 16F, 1, 1, 2);
      esL2.setRotationPoint(0F, 0F, -20F);
      esL2.setTextureSize(256, 512);
      esL2.mirror = true;
      setRotation(esL2, 0F, 0F, 0F);
      esL3 = new ModelRenderer(this, 161, 140);
      esL3.addBox(14F, 12F, 16F, 1, 1, 1);
      esL3.setRotationPoint(0F, 0F, -20F);
      esL3.setTextureSize(256, 512);
      esL3.mirror = true;
      setRotation(esL3, 0F, 0F, 0F);
      esL4 = new ModelRenderer(this, 170, 131);
      esL4.addBox(14F, 10F, 29F, 1, 1, 3);
      esL4.setRotationPoint(0F, 0F, -20F);
      esL4.setTextureSize(256, 512);
      esL4.mirror = true;
      setRotation(esL4, 0F, 0F, 0F);
      esL5 = new ModelRenderer(this, 170, 136);
      esL5.addBox(14F, 11F, 30F, 1, 1, 2);
      esL5.setRotationPoint(0F, 0F, -20F);
      esL5.setTextureSize(256, 512);
      esL5.mirror = true;
      setRotation(esL5, 0F, 0F, 0F);
      esL6 = new ModelRenderer(this, 170, 140);
      esL6.addBox(14F, 12F, 31F, 1, 1, 1);
      esL6.setRotationPoint(0F, 0F, -20F);
      esL6.setTextureSize(256, 512);
      esL6.mirror = true;
      setRotation(esL6, 0F, 0F, 0F);
      esLF1 = new ModelRenderer(this, 134, 131);
      esLF1.addBox(14F, 10F, -21F, 1, 1, 3);
      esLF1.setRotationPoint(0F, 0F, -20F);
      esLF1.setTextureSize(256, 512);
      esLF1.mirror = true;
      setRotation(esLF1, 0F, 0F, 0F);
      esLF2 = new ModelRenderer(this, 134, 136);
      esLF2.addBox(14F, 11F, -20F, 1, 1, 2);
      esLF2.setRotationPoint(0F, 0F, -20F);
      esLF2.setTextureSize(256, 512);
      esLF2.mirror = true;
      setRotation(esLF2, 0F, 0F, 0F);
      esLF3 = new ModelRenderer(this, 134, 140);
      esLF3.addBox(14F, 12F, -19F, 1, 1, 1);
      esLF3.setRotationPoint(0F, 0F, -20F);
      esLF3.setTextureSize(256, 512);
      esLF3.mirror = true;
      setRotation(esLF3, 0F, 0F, 0F);
      esLF4 = new ModelRenderer(this, 125, 131);
      esLF4.addBox(14F, 10F, -34F, 1, 1, 3);
      esLF4.setRotationPoint(0F, 0F, -20F);
      esLF4.setTextureSize(256, 512);
      esLF4.mirror = true;
      setRotation(esLF4, 0F, 0F, 0F);
      esLF5 = new ModelRenderer(this, 125, 136);
      esLF5.addBox(14F, 11F, -34F, 1, 1, 2);
      esLF5.setRotationPoint(0F, 0F, -20F);
      esLF5.setTextureSize(256, 512);
      esLF5.mirror = true;
      setRotation(esLF5, 0F, 0F, 0F);
      esLF6 = new ModelRenderer(this, 125, 140);
      esLF6.addBox(14F, 12F, -34F, 1, 1, 1);
      esLF6.setRotationPoint(0F, 0F, -20F);
      esLF6.setTextureSize(256, 512);
      esLF6.mirror = true;
      setRotation(esLF6, 0F, 0F, 0F);
      esRF1 = new ModelRenderer(this, 143, 131);
      esRF1.addBox(-15F, 10F, -34F, 1, 1, 3);
      esRF1.setRotationPoint(0F, 0F, -20F);
      esRF1.setTextureSize(256, 512);
      esRF1.mirror = true;
      setRotation(esRF1, 0F, 0F, 0F);
      esRF2 = new ModelRenderer(this, 143, 136);
      esRF2.addBox(-15F, 11F, -34F, 1, 1, 2);
      esRF2.setRotationPoint(0F, 0F, -20F);
      esRF2.setTextureSize(256, 512);
      esRF2.mirror = true;
      setRotation(esRF2, 0F, 0F, 0F);
      esRF3 = new ModelRenderer(this, 143, 140);
      esRF3.addBox(-15F, 12F, -34F, 1, 1, 1);
      esRF3.setRotationPoint(0F, 0F, -20F);
      esRF3.setTextureSize(256, 512);
      esRF3.mirror = true;
      setRotation(esRF3, 0F, 0F, 0F);
      esRF4 = new ModelRenderer(this, 152, 131);
      esRF4.addBox(-15F, 10F, -21F, 1, 1, 3);
      esRF4.setRotationPoint(0F, 0F, -20F);
      esRF4.setTextureSize(256, 512);
      esRF4.mirror = true;
      setRotation(esRF4, 0F, 0F, 0F);
      esRF5 = new ModelRenderer(this, 152, 136);
      esRF5.addBox(-15F, 11F, -20F, 1, 1, 2);
      esRF5.setRotationPoint(0F, 0F, -20F);
      esRF5.setTextureSize(256, 512);
      esRF5.mirror = true;
      setRotation(esRF5, 0F, 0F, 0F);
      esRF6 = new ModelRenderer(this, 152, 140);
      esRF6.addBox(-15F, 12F, -19F, 1, 1, 1);
      esRF6.setRotationPoint(0F, 0F, -20F);
      esRF6.setTextureSize(256, 512);
      esRF6.mirror = true;
      setRotation(esRF6, 0F, 0F, 0F);
      BscL = new ModelRenderer(this, 60, 109);
      BscL.addBox(12F, 2F, 38.5F, 2, 14, 1);
      BscL.setRotationPoint(0F, 0F, -20F);
      BscL.setTextureSize(256, 512);
      BscL.mirror = true;
      setRotation(BscL, 0F, 0F, 0F);
      BscR = new ModelRenderer(this, 52, 109);
      BscR.addBox(-14F, 2F, 38.5F, 2, 14, 1);
      BscR.setRotationPoint(0F, 0F, -20F);
      BscR.setTextureSize(256, 512);
      BscR.mirror = true;
      setRotation(BscR, 0F, 0F, 0F);
      backdoor = new ModelRenderer(this, 0, 109);
      backdoor.addBox(-12F, -14F, 0F, 24, 14, 1);
      backdoor.setRotationPoint(0F, 16F, 19F);
      backdoor.setTextureSize(256, 512);
      backdoor.mirror = true;
      setRotation(backdoor, 0F, 0F, 0F);
      frontR = new ModelRenderer(this, 97, 215);
      frontR.addBox(-14.9F, 0F, -15F, 15, 8, 15);
      frontR.setRotationPoint(0F, -8F, -27F);
      frontR.setTextureSize(256, 512);
      frontR.mirror = true;
      setRotation(frontR, 0.7853982F, 0F, 0F);
      frontL = new ModelRenderer(this, 97, 191);
      frontL.addBox(-0.1F, 0F, -15F, 15, 8, 15);
      frontL.setRotationPoint(0F, -8F, -27F);
      frontL.setTextureSize(256, 512);
      frontL.mirror = true;
      setRotation(frontL, 0.7853982F, 0F, 0F);
      lightL = new ModelRenderer(this, 78, 50);
      lightL.addBox(0F, -2F, 0F, 5, 3, 2);
      lightL.setRotationPoint(9F, 7F, -56.5F);
      lightL.setTextureSize(256, 512);
      lightL.mirror = true;
      setRotation(lightL, 0F, 0F, 0F);
      lightR = new ModelRenderer(this, 78, 56);
      lightR.addBox(-5F, -2F, 0F, 5, 3, 2);
      lightR.setRotationPoint(-9F, 7F, -56.5F);
      lightR.setTextureSize(256, 512);
      lightR.mirror = true;
      setRotation(lightR, 0F, 0F, 0F);
      bodyM1 = new ModelRenderer(this, 0, 166);
      bodyM1.addBox(-15F, 2F, -18F, 30, 14, 25);
      bodyM1.setRotationPoint(0F, 0F, -20F);
      bodyM1.setTextureSize(256, 512);
      bodyM1.mirror = true;
      setRotation(bodyM1, 0F, 0F, 0F);
      bodyM2 = new ModelRenderer(this, 87, 166);
      bodyM2.addBox(-15F, -8F, -7F, 30, 10, 14);
      bodyM2.setRotationPoint(0F, 0F, -20F);
      bodyM2.setTextureSize(256, 512);
      bodyM2.mirror = true;
      setRotation(bodyM2, 0F, 0F, 0F);
      bodyF1 = new ModelRenderer(this, 100, 24);
      bodyF1.addBox(-11F, 10F, -34F, 22, 7, 16);
      bodyF1.setRotationPoint(0F, 0F, -20F);
      bodyF1.setTextureSize(256, 512);
      bodyF1.mirror = true;
      setRotation(bodyF1, 0F, 0F, 0F);
      bodyF2 = new ModelRenderer(this, 0, 206);
      bodyF2.addBox(-14F, 0F, -18F, 28, 8, 18);
      bodyF2.setRotationPoint(0F, 2F, -38F);
      bodyF2.setTextureSize(256, 512);
      bodyF2.mirror = true;
      setRotation(bodyF2, 0.0523599F, 0F, 0F);
      bodyF3 = new ModelRenderer(this, 39, 233);
      bodyF3.addBox(-1F, 0F, -18F, 1, 7, 18);
      bodyF3.setRotationPoint(15F, 3F, -38F);
      bodyF3.setTextureSize(256, 512);
      bodyF3.mirror = true;
      setRotation(bodyF3, 0F, 0.0523599F, 0F);
      bodyF4 = new ModelRenderer(this, 0, 233);
      bodyF4.addBox(0F, 0F, -18F, 1, 7, 18);
      bodyF4.setRotationPoint(-15F, 3F, -38F);
      bodyF4.setTextureSize(256, 512);
      bodyF4.mirror = true;
      setRotation(bodyF4, 0F, -0.0523599F, 0F);
      bodyF5 = new ModelRenderer(this, 0, 16);
      bodyF5.addBox(-14.5F, -1F, 0F, 29, 3, 1);
      bodyF5.setRotationPoint(0F, 12F, -58F);
      bodyF5.setTextureSize(256, 512);
      bodyF5.mirror = true;
      setRotation(bodyF5, 0F, 0F, 0F);
      bodyF6 = new ModelRenderer(this, 0, 21);
      bodyF6.addBox(-15F, -1F, 1F, 30, 3, 3);
      bodyF6.setRotationPoint(0F, 12F, -58F);
      bodyF6.setTextureSize(256, 512);
      bodyF6.mirror = true;
      setRotation(bodyF6, 0F, 0F, 0F);
      bodyF7 = new ModelRenderer(this, 0, 29);
      bodyF7.addBox(-14.5F, 1.9F, 0.5F, 29, 1, 4);
      bodyF7.setRotationPoint(0F, 12F, -58F);
      bodyF7.setTextureSize(256, 512);
      bodyF7.mirror = true;
      setRotation(bodyF7, 0F, 0F, 0F);
      bodyF8 = new ModelRenderer(this, 0, 35);
      bodyF8.addBox(-14F, 2.9F, 0.7F, 28, 1, 4);
      bodyF8.setRotationPoint(0F, 12F, -58F);
      bodyF8.setTextureSize(256, 512);
      bodyF8.mirror = true;
      setRotation(bodyF8, 0F, 0F, 0F);
      bodyF9 = new ModelRenderer(this, 70, 30);
      bodyF9.addBox(-9F, 0F, -1F, 18, 7, 3);
      bodyF9.setRotationPoint(0F, 4F, -56F);
      bodyF9.setTextureSize(256, 512);
      bodyF9.mirror = true;
      setRotation(bodyF9, 0F, 0F, 0F);
      bodyF10 = new ModelRenderer(this, 69, 21);
      bodyF10.addBox(-8.5F, 0.5F, -1.1F, 17, 6, 1);
      bodyF10.setRotationPoint(0F, 4F, -56F);
      bodyF10.setTextureSize(256, 512);
      bodyF10.mirror = true;
      setRotation(bodyF10, 0F, 0F, 0F);
      bodyF11 = new ModelRenderer(this, 62, 14);
      bodyF11.addBox(9F, 5F, -1F, 5, 2, 3);
      bodyF11.setRotationPoint(0F, 4F, -56F);
      bodyF11.setTextureSize(256, 512);
      bodyF11.mirror = true;
      setRotation(bodyF11, 0F, 0F, 0F);
      bodyF12 = new ModelRenderer(this, 79, 14);
      bodyF12.addBox(-14F, 5F, -1F, 5, 2, 3);
      bodyF12.setRotationPoint(0F, 4F, -56F);
      bodyF12.setTextureSize(256, 512);
      bodyF12.mirror = true;
      setRotation(bodyF12, 0F, 0F, 0F);
      toyota = new ModelRenderer(this, 0, 0);
      toyota.addBox(-1F, 6F, -37.3F, 2, 2, 1);
      toyota.setRotationPoint(0F, 0F, -20F);
      toyota.setTextureSize(256, 512);
      toyota.mirror = true;
      setRotation(toyota, 0F, 0F, 0F);
      exF1 = new ModelRenderer(this, 51, 0);
      exF1.addBox(12F, 10F, -36F, 3, 1, 2);
      exF1.setRotationPoint(0F, 0F, -20F);
      exF1.setTextureSize(256, 512);
      exF1.mirror = true;
      setRotation(exF1, 0F, 0F, 0F);
      exF2 = new ModelRenderer(this, 62, 0);
      exF2.addBox(-15F, 10F, -36F, 3, 1, 2);
      exF2.setRotationPoint(0F, 0F, -20F);
      exF2.setTextureSize(256, 512);
      exF2.mirror = true;
      setRotation(exF2, 0F, 0F, 0F);
      wheelLF1 = new ModelRenderer(this, 80, 87);
      wheelLF1.addBox(-3F, -3F, -6F, 4, 6, 12);
      wheelLF1.setRotationPoint(14F, 18F, -46F);
      wheelLF1.setTextureSize(256, 512);
      wheelLF1.mirror = true;
      setRotation(wheelLF1, 0F, 0F, 0F);
      wheelLF2 = new ModelRenderer(this, 80, 76);
      wheelLF2.addBox(-3F, -6F, -3F, 4, 3, 6);
      wheelLF2.setRotationPoint(14F, 18F, -46F);
      wheelLF2.setTextureSize(256, 512);
      wheelLF2.mirror = true;
      setRotation(wheelLF2, 0F, 0F, 0F);
      wheelLF3 = new ModelRenderer(this, 80, 107);
      wheelLF3.addBox(-3F, 3F, -3F, 4, 3, 6);
      wheelLF3.setRotationPoint(14F, 18F, -46F);
      wheelLF3.setTextureSize(256, 512);
      wheelLF3.mirror = true;
      setRotation(wheelLF3, 0F, 0F, 0F);
      wheelLF4 = new ModelRenderer(this, 101, 76);
      wheelLF4.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLF4.setRotationPoint(14F, 18F, -46F);
      wheelLF4.setTextureSize(256, 512);
      wheelLF4.mirror = true;
      setRotation(wheelLF4, 0.7853982F, 0F, 0F);
      wheelLF5 = new ModelRenderer(this, 101, 83);
      wheelLF5.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLF5.setRotationPoint(14F, 18F, -46F);
      wheelLF5.setTextureSize(256, 512);
      wheelLF5.mirror = true;
      setRotation(wheelLF5, 2.356194F, 0F, 0F);
      wheelLF6 = new ModelRenderer(this, 101, 107);
      wheelLF6.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLF6.setRotationPoint(14F, 18F, -46F);
      wheelLF6.setTextureSize(256, 512);
      wheelLF6.mirror = true;
      setRotation(wheelLF6, -0.7853982F, 0F, 0F);
      wheelLF7 = new ModelRenderer(this, 101, 91);
      wheelLF7.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLF7.setRotationPoint(14F, 18F, -46F);
      wheelLF7.setTextureSize(256, 512);
      wheelLF7.mirror = true;
      setRotation(wheelLF7, -2.356194F, 0F, 0F);
      wheelRF1 = new ModelRenderer(this, 120, 87);
      wheelRF1.addBox(-3F, -3F, -6F, 4, 6, 12);
      wheelRF1.setRotationPoint(-12F, 18F, -46F);
      wheelRF1.setTextureSize(256, 512);
      wheelRF1.mirror = true;
      setRotation(wheelRF1, 0F, 0F, 0F);
      wheelRF2 = new ModelRenderer(this, 120, 76);
      wheelRF2.addBox(-3F, -6F, -3F, 4, 3, 6);
      wheelRF2.setRotationPoint(-12F, 18F, -46F);
      wheelRF2.setTextureSize(256, 512);
      wheelRF2.mirror = true;
      setRotation(wheelRF2, 0F, 0F, 0F);
      wheelRF3 = new ModelRenderer(this, 120, 107);
      wheelRF3.addBox(-3F, 3F, -3F, 4, 3, 6);
      wheelRF3.setRotationPoint(-12F, 18F, -46F);
      wheelRF3.setTextureSize(256, 512);
      wheelRF3.mirror = true;
      setRotation(wheelRF3, 0F, 0F, 0F);
      wheelRF4 = new ModelRenderer(this, 141, 76);
      wheelRF4.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRF4.setRotationPoint(-12F, 18F, -46F);
      wheelRF4.setTextureSize(256, 512);
      wheelRF4.mirror = true;
      setRotation(wheelRF4, 0.7853982F, 0F, 0F);
      wheelRF5 = new ModelRenderer(this, 141, 83);
      wheelRF5.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRF5.setRotationPoint(-12F, 18F, -46F);
      wheelRF5.setTextureSize(256, 512);
      wheelRF5.mirror = true;
      setRotation(wheelRF5, 2.356194F, 0F, 0F);
      wheelLR6 = new ModelRenderer(this, 141, 107);
      wheelLR6.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelLR6.setRotationPoint(-12F, 18F, -46F);
      wheelLR6.setTextureSize(256, 512);
      wheelLR6.mirror = true;
      setRotation(wheelLR6, -0.7853982F, 0F, 0F);
      wheelRF7 = new ModelRenderer(this, 141, 91);
      wheelRF7.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRF7.setRotationPoint(-12F, 18F, -46F);
      wheelRF7.setTextureSize(256, 512);
      wheelRF7.mirror = true;
      setRotation(wheelRF7, -2.356194F, 0F, 0F);
      wheelLB1 = new ModelRenderer(this, 160, 87);
      wheelLB1.addBox(-3F, -3F, -6F, 4, 6, 12);
      wheelLB1.setRotationPoint(14F, 18F, 04F);
      wheelLB1.setTextureSize(256, 512);
      wheelLB1.mirror = true;
      setRotation(wheelLB1, 0F, 0F, 0F);
      wheelLB2 = new ModelRenderer(this, 160, 76);
      wheelLB2.addBox(-3F, -6F, -3F, 4, 3, 6);
      wheelLB2.setRotationPoint(14F, 18F, 04F);
      wheelLB2.setTextureSize(256, 512);
      wheelLB2.mirror = true;
      setRotation(wheelLB2, 0F, 0F, 0F);
      wheelLB3 = new ModelRenderer(this, 160, 107);
      wheelLB3.addBox(-3F, 3F, -3F, 4, 3, 6);
      wheelLB3.setRotationPoint(14F, 18F, 04F);
      wheelLB3.setTextureSize(256, 512);
      wheelLB3.mirror = true;
      setRotation(wheelLB3, 0F, 0F, 0F);
      wheelLB4 = new ModelRenderer(this, 181, 76);
      wheelLB4.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLB4.setRotationPoint(14F, 18F, 04F);
      wheelLB4.setTextureSize(256, 512);
      wheelLB4.mirror = true;
      setRotation(wheelLB4, 0.7853982F, 0F, 0F);
      wheelLB5 = new ModelRenderer(this, 181, 83);
      wheelLB5.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLB5.setRotationPoint(14F, 18F, 04F);
      wheelLB5.setTextureSize(256, 512);
      wheelLB5.mirror = true;
      setRotation(wheelLB5, 2.356194F, 0F, 0F);
      wheelLB6 = new ModelRenderer(this, 181, 107);
      wheelLB6.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLB6.setRotationPoint(14F, 18F, 04F);
      wheelLB6.setTextureSize(256, 512);
      wheelLB6.mirror = true;
      setRotation(wheelLB6, -0.7853982F, 0F, 0F);
      wheelLB7 = new ModelRenderer(this, 181, 91);
      wheelLB7.addBox(-2.9F, -6.2F, -2F, 4, 2, 4);
      wheelLB7.setRotationPoint(14F, 18F, 04F);
      wheelLB7.setTextureSize(256, 512);
      wheelLB7.mirror = true;
      setRotation(wheelLB7, -2.356194F, 0F, 0F);
      wheelRB1 = new ModelRenderer(this, 199, 87);
      wheelRB1.addBox(-3F, -3F, -6F, 4, 6, 12);
      wheelRB1.setRotationPoint(-12F, 18F, 04F);
      wheelRB1.setTextureSize(256, 512);
      wheelRB1.mirror = true;
      setRotation(wheelRB1, 0F, 0F, 0F);
      wheelRB2 = new ModelRenderer(this, 199, 76);
      wheelRB2.addBox(-3F, -6F, -3F, 4, 3, 6);
      wheelRB2.setRotationPoint(-12F, 18F, 04F);
      wheelRB2.setTextureSize(256, 512);
      wheelRB2.mirror = true;
      setRotation(wheelRB2, 0F, 0F, 0F);
      wheelRB3 = new ModelRenderer(this, 199, 107);
      wheelRB3.addBox(-3F, 3F, -3F, 4, 3, 6);
      wheelRB3.setRotationPoint(-12F, 18F, 04F);
      wheelRB3.setTextureSize(256, 512);
      wheelRB3.mirror = true;
      setRotation(wheelRB3, 0F, 0F, 0F);
      wheelRB4 = new ModelRenderer(this, 220, 76);
      wheelRB4.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRB4.setRotationPoint(-12F, 18F, 04F);
      wheelRB4.setTextureSize(256, 512);
      wheelRB4.mirror = true;
      setRotation(wheelRB4, 0.7853982F, 0F, 0F);
      wheelRB5 = new ModelRenderer(this, 220, 83);
      wheelRB5.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRB5.setRotationPoint(-12F, 18F, 04F);
      wheelRB5.setTextureSize(256, 512);
      wheelRB5.mirror = true;
      setRotation(wheelRB5, 2.356194F, 0F, 0F);
      wheelRB6 = new ModelRenderer(this, 220, 107);
      wheelRB6.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRB6.setRotationPoint(-12F, 18F, 04F);
      wheelRB6.setTextureSize(256, 512);
      wheelRB6.mirror = true;
      setRotation(wheelRB6, -0.7853982F, 0F, 0F);
      wheelRF71 = new ModelRenderer(this, 220, 91);
      wheelRF71.addBox(-3.1F, -6.2F, -2F, 4, 2, 4);
      wheelRF71.setRotationPoint(-12F, 18F, 04F);
      wheelRF71.setTextureSize(256, 512);
      wheelRF71.mirror = true;
      setRotation(wheelRF71, -2.356194F, 0F, 0F);
      stick1 = new ModelRenderer(this, 54, 126);
      stick1.addBox(-11F, 0F, 0F, 22, 2, 2);
      stick1.setRotationPoint(0F, 17F, -47F);
      stick1.setTextureSize(256, 512);
      stick1.mirror = true;
      setRotation(stick1, 0F, 0F, 0F);
      stick2 = new ModelRenderer(this, 103, 126);
      stick2.addBox(-11F, 0F, 0F, 22, 2, 2);
      stick2.setRotationPoint(0F, 17F, 03F);
      stick2.setTextureSize(256, 512);
      stick2.mirror = true;
      setRotation(stick2, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    coverLF1.render(f5);
    coverLF2.render(f5);
    coverLF3.render(f5);
    coverLF4.render(f5);
    coverLF5.render(f5);
    coverRF1.render(f5);
    coverRF2.render(f5);
    coverRF3.render(f5);
    coverRF4.render(f5);
    coverRF5.render(f5);
    coverRB1.render(f5);
    coverRB2.render(f5);
    coverRB3.render(f5);
    coverRB4.render(f5);
    coverRB5.render(f5);
    coverLB1.render(f5);
    coverLB2.render(f5);
    coverLB3.render(f5);
    coverLB4.render(f5);
    coverLB5.render(f5);
    coverBC1L.render(f5);
    coverBC2L.render(f5);
    coverBC3L.render(f5);
    coverBC4L.render(f5);
    coverBC5L.render(f5);
    coverBC1R.render(f5);
    coverBC2R.render(f5);
    coverBC3R.render(f5);
    coverBC4R.render(f5);
    coverBC5R.render(f5);
    bottom1.render(f5);
    bottom2.render(f5);
    bottom3.render(f5);
    bottom4.render(f5);
    bottom5.render(f5);
    bottom6.render(f5);
    screenL1.render(f5);
    screenL2.render(f5);
    screenL3.render(f5);
    screenL4.render(f5);
    screenR1.render(f5);
    screenR2.render(f5);
    screenR3.render(f5);
    screenR4.render(f5);
    esR1.render(f5);
    esR2.render(f5);
    esR3.render(f5);
    esR4.render(f5);
    esR5.render(f5);
    esR6.render(f5);
    esL1.render(f5);
    esL2.render(f5);
    esL3.render(f5);
    esL4.render(f5);
    esL5.render(f5);
    esL6.render(f5);
    esLF1.render(f5);
    esLF2.render(f5);
    esLF3.render(f5);
    esLF4.render(f5);
    esLF5.render(f5);
    esLF6.render(f5);
    esRF1.render(f5);
    esRF2.render(f5);
    esRF3.render(f5);
    esRF4.render(f5);
    esRF5.render(f5);
    esRF6.render(f5);
    BscL.render(f5);
    BscR.render(f5);
    backdoor.render(f5);
    frontR.render(f5);
    frontL.render(f5);
    lightL.render(f5);
    lightR.render(f5);
    bodyM1.render(f5);
    bodyM2.render(f5);
    bodyF1.render(f5);
    bodyF2.render(f5);
    bodyF3.render(f5);
    bodyF4.render(f5);
    bodyF5.render(f5);
    bodyF6.render(f5);
    bodyF7.render(f5);
    bodyF8.render(f5);
    bodyF9.render(f5);
    bodyF10.render(f5);
    bodyF11.render(f5);
    bodyF12.render(f5);
    toyota.render(f5);
    exF1.render(f5);
    exF2.render(f5);
    wheelLF1.render(f5);
    wheelLF2.render(f5);
    wheelLF3.render(f5);
    wheelLF4.render(f5);
    wheelLF5.render(f5);
    wheelLF6.render(f5);
    wheelLF7.render(f5);
    wheelRF1.render(f5);
    wheelRF2.render(f5);
    wheelRF3.render(f5);
    wheelRF4.render(f5);
    wheelRF5.render(f5);
    wheelLR6.render(f5);
    wheelRF7.render(f5);
    wheelLB1.render(f5);
    wheelLB2.render(f5);
    wheelLB3.render(f5);
    wheelLB4.render(f5);
    wheelLB5.render(f5);
    wheelLB6.render(f5);
    wheelLB7.render(f5);
    wheelRB1.render(f5);
    wheelRB2.render(f5);
    wheelRB3.render(f5);
    wheelRB4.render(f5);
    wheelRB5.render(f5);
    wheelRB6.render(f5);
    wheelRF71.render(f5);
    stick1.render(f5);
    stick2.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, null);
  }

}
