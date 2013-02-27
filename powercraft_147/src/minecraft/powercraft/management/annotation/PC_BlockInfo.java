package powercraft.management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import powercraft.management.block.PC_ItemBlock;
import powercraft.management.tileentity.PC_TileEntity;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PC_BlockInfo {

	public final static class PC_FakeItemBlock extends PC_ItemBlock{

		private PC_FakeItemBlock() {
			super(-1);
		}

		@Override
		public Object msg(int msg, Object... obj) {
			return null;
		}
		
	}
	
	public final static class PC_FakeTileEntity extends PC_TileEntity{}
	
	public Class<? extends PC_ItemBlock> itemBlock() default PC_FakeItemBlock.class;
	public Class<? extends PC_TileEntity> tileEntity() default PC_FakeTileEntity.class;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface ConfigEntry {
		
		public String entryName();
		public String[] comment() default {};
		
	}
	
}
