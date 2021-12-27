package com.dradon.wormnotif;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Constructor;

@Mod(modid = WormNotifMod.MODID, version = WormNotifMod.VERSION)
public class WormNotifMod
{
    public static final String MODID = "wormnotif";
    public static final String VERSION = "1.0";

    private PositionedSoundRecord psr;
    private float volume = 1.0f;
    private float pitch = 1.0f;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        try {
            // Using the private constructor to make the sound, because the 3 PositionedSoundRecord.create that are suggested does not allow AttenuationType or volume customization and the public constructor does not allow
            Constructor<PositionedSoundRecord> constructor = PositionedSoundRecord.class.getDeclaredConstructor(ResourceLocation.class, float.class, float.class, boolean.class, int.class, ISound.AttenuationType.class, float.class, float.class, float.class);
            constructor.setAccessible(true);
            this.psr = constructor.newInstance(new ResourceLocation("mob.wither.spawn"), volume, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onChatClient(ClientChatReceivedEvent event) {
        System.out.println(event.message.getUnformattedText());
        if(event.message.getUnformattedText().equals("You hear the sound of something approaching...")) {
            // Title work, subtitle does not?
            Minecraft.getMinecraft().ingameGUI.displayTitle(EnumChatFormatting.RED+"WORM SPAWNED", null, 500, 500, 500);
            Minecraft.getMinecraft().ingameGUI.displayTitle(null, EnumChatFormatting.RED+"GO KILL IT NOW!", 500, 500, 500);
            Minecraft.getMinecraft().getSoundHandler().playSound(psr);
        }
    }
}
