package cn.solarmoon.spyglassofcurios.client.models;
// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

import static cn.solarmoon.spyglassofcurios.client.Render.followBodyRotations;

public class SpyglassModel extends HumanoidModel<LivingEntity> {
	private final ModelPart part1;
	private final ModelPart part2;

	public SpyglassModel(ModelPart root) {
		super(root);
		this.part1 = root.getChild("part1");
		this.part2 = root.getChild("part2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("part1", CubeListBuilder.create().texOffs(0, 2).addBox(-9.0F, -13.5F, 7.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));
		partdefinition.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(0, 7).addBox(-9.5F, -8.5F, 6.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.part1.setPos(0.0F, 12.0F, 0.0F); // 这将模型部分的位置设置为玩家的腰部
		this.part2.setPos(0.0F, 12.0F, 0.0F); // 这将模型部分的位置设置为玩家的腰部
		followBodyRotations(entity, this);
	}

}
