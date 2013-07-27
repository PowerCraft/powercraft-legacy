package powercraft.api.block;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PC_BlockInfo {

	public String name();


	public String blockid();


	public int defaultid();


	public Class<? extends PC_ItemBlock> itemBlock() default PC_ItemBlock.class;


	public Class<? extends PC_TileEntity> tileEntity() default PC_TileEntity.class;

}
