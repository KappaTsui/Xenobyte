package forgefuck.team.xenobyte.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.Type;

import forgefuck.team.xenobyte.asm.hooks.NoclipState;

//import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;

public class WorldRendererClassTransformer implements IClassTransformer{

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!transformedName.equals("net.minecraft.client.renderer.WorldRenderer")) return basicClass;

		try {
			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(basicClass);
			classReader.accept(classNode, 0);

			for (MethodNode m: classNode.methods) {

				if (m.name.equals("updateRenderer") || (m.name.equals("a") && m.desc.equals("(Lsv;)V"))) {
					AbstractInsnNode cur = m.instructions.getFirst();

					while(
						!(cur instanceof MethodInsnNode)
						|| (!((MethodInsnNode)cur).owner.equals("blm") && !((MethodInsnNode)cur).owner.equals("net/minecraft/client/renderer/RenderBlocks"))
						|| (!((MethodInsnNode)cur).name.equals("a") && !((MethodInsnNode)cur).name.equals("setRenderFromInside"))
						|| !((MethodInsnNode)cur).desc.equals("(Z)V")) cur = cur.getNext();

					m.instructions.remove(cur.getPrevious());
					m.instructions.insertBefore(cur, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(NoclipState.class), "isEnabled", "()Z", false));

					while(
						!(cur instanceof MethodInsnNode)
						|| (!((MethodInsnNode)cur).owner.equals("blm") && !((MethodInsnNode)cur).owner.equals("net/minecraft/client/renderer/RenderBlocks"))
						|| (!((MethodInsnNode)cur).name.equals("b") && !((MethodInsnNode)cur).name.equals("setRenderAllFaces"))
						|| !((MethodInsnNode)cur).desc.equals("(Z)V")) cur = cur.getNext();

					m.instructions.remove(cur.getPrevious());
					m.instructions.insertBefore(cur, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(NoclipState.class), "isEnabled", "()Z", false));
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
