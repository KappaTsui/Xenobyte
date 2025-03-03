package forgefuck.team.xenobyte.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import forgefuck.team.xenobyte.asm.hooks.NoclipState;

//import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

public class ItemRendererClassTransformer implements IClassTransformer{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!transformedName.equals("net.minecraft.client.renderer.ItemRenderer")) return basicClass;

		try {
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);
			classReader.accept(classNode, 0);

			for (MethodNode m: classNode.methods) {

				if (m.name.equals("renderOverlays") || (m.name.equals("b") && m.desc.equals("(F)V"))) {
					AbstractInsnNode cur = m.instructions.getFirst();
					LabelNode label = new LabelNode();

					for(int i = 0; i < 5; i++) {
						while(!(cur instanceof LabelNode)) cur = cur.getNext();
						cur = cur.getNext();
					}	for(int i = 0; i < 2; i++) cur = cur.getNext();

					m.instructions.insertBefore(cur, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(NoclipState.class), "isEnabled", "()Z", false));
					m.instructions.insertBefore(cur, new JumpInsnNode(Opcodes.IFEQ, label));

					for(int i = 0; i < 28; i++) {
						while(!(cur instanceof LabelNode)) cur = cur.getNext();
						cur = cur.getNext();
					}

					m.instructions.insert(cur, label);
				}

			}

			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(classWriter);
			return classWriter.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return basicClass;
	}

}
