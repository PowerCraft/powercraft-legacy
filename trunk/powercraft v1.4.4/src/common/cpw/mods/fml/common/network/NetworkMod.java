package cpw.mods.fml.common.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.src.Packet131MapData;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NetworkMod
{

boolean clientSideRequired() default false;

boolean serverSideRequired() default false;

String[] channels() default {};

String versionBounds() default "";

Class <? extends IPacketHandler > packetHandler() default NULL.class;

Class <? extends ITinyPacketHandler > tinyPacketHandler() default NULL.class;

Class <? extends IConnectionHandler > connectionHandler() default NULL.class;

SidedPacketHandler clientPacketHandlerSpec() default @SidedPacketHandler(channels = {}, packetHandler = NULL.class);

SidedPacketHandler serverPacketHandlerSpec() default @SidedPacketHandler(channels = {}, packetHandler = NULL.class);

    static interface NULL extends IPacketHandler, IConnectionHandler, ITinyPacketHandler {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface VersionCheckHandler { }

    public @interface SidedPacketHandler
    {
        String[] channels();
        Class <? extends IPacketHandler > packetHandler();
    }
}
