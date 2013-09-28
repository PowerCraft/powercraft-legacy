import java.util.Arrays;

import weasel.interpreter.io.WeaselClassFile;


public class Test {

	public static void main(String[] args){
		WeaselClassFile wcf = new WeaselClassFile();
		wcf.wClass = new WeaselClassFile.WeaselClass();
		wcf.wClass.name = "Test";
		wcf.wClass.annotations = new WeaselClassFile.WeaselAnnotation[0];
		wcf.wClass.typeParams = new WeaselClassFile.WeaselClass[0];
		wcf.superClasses = new WeaselClassFile.WeaselClass[0];
		wcf.fields = new WeaselClassFile.WeaselField[0];
		wcf.methods = new WeaselClassFile.WeaselMethod[1];
		wcf.methods[0] = new WeaselClassFile.WeaselMethod();
		wcf.methods[0].name = "f1";
		wcf.methods[0].params = new WeaselClassFile.WeaselClass[0];
		wcf.methods[0].returnType = new WeaselClassFile.WeaselClass();
		wcf.methods[0].returnType.name = "Test";
		wcf.methods[0].returnType.annotations = new WeaselClassFile.WeaselAnnotation[0];
		wcf.methods[0].returnType.typeParams = new WeaselClassFile.WeaselClass[0];
		wcf.methods[0].throwClasses = new WeaselClassFile.WeaselClass[0];
		wcf.methods[0].byteCodes = new WeaselClassFile.WeaselByteCode[0];
		wcf.innerClasses = new WeaselClassFile[0];
		byte[] bArray = WeaselClassFile.save(wcf, 1);
		System.out.println(Arrays.toString(bArray));
		wcf = WeaselClassFile.load(bArray);
		System.out.println(wcf);
	}
	
}
