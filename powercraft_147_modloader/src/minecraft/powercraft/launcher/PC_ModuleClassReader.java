package powercraft.launcher;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class PC_ModuleClassReader extends ClassVisitor {
	
	private int access;
	private String name;
	private String signature;
	private String superName;
	private String[] interfaces;
	private PC_AnnotationVisitor annotationVisitor;
	private boolean isClient;
	private PC_ModuleDiscovery discovery;
	
	public PC_ModuleClassReader(PC_ModuleDiscovery discovery) {
		super(Opcodes.ASM4);
		this.discovery = discovery;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;
		annotationVisitor = null;
		isClient=false;
    }
	
	@Override
	public void visitSource(String source, String debug) {
    }
	
	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if(desc.equals("Lpowercraft/launcher/PC_Module;")){
			annotationVisitor = new PC_AnnotationVisitor();
			return annotationVisitor;
		}else if(desc.equals("Lpowercraft/launcher/PC_ClientModule;")){
			isClient = true;
		}
		return null;
    }

    @Override
    public void visitAttribute(Attribute attr) {
    }
    
    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return null;
    }

    @Override
    public void visitEnd() {
    	if(isClient){
    		discovery.addClient(access, name, signature, superName, interfaces, annotationVisitor);
    	}else if(annotationVisitor!=null){
    		discovery.addCommon(access, name, signature, superName, interfaces, annotationVisitor);
    	}
    }
    
}
