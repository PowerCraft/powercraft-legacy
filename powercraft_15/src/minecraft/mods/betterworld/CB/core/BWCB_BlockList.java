package mods.betterworld.CB.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BWCB_BlockList {
	
	private ArrayList<String>allList=new ArrayList<String>();

	public static ArrayList blockStoneNormalName = new ArrayList();
	public static ArrayList blockStoneNormalTexture = new ArrayList();
	public static ArrayList<Integer> blockStoneNormalLight = new ArrayList<Integer>();
	public static ArrayList blockStoneResistentName = new ArrayList();
	public static ArrayList blockStoneResistentTexture = new ArrayList();
	public static ArrayList<Integer> blockStoneResistentLight = new ArrayList<Integer>();
	public static ArrayList blockPlanksNormalName = new ArrayList();
	public static ArrayList blockPlanksNormalTexture = new ArrayList();
	public static ArrayList <Integer>blockPlanksNormalLight = new ArrayList<Integer>();
	public static ArrayList blockPlanksResistentName = new ArrayList();
	public static ArrayList blockPlanksResistentTexture = new ArrayList();
	public static ArrayList<Integer> blockPlanksResistentLight = new ArrayList<Integer>();
	public static ArrayList blockGlassNormalName = new ArrayList();
	public static ArrayList blockGlassNormalTexture = new ArrayList();
	public static ArrayList<Integer>blockGlassNormalLight = new ArrayList<Integer>();
	public static ArrayList blockGlassResistentName = new ArrayList();
	public static ArrayList blockGlassResistentTexture = new ArrayList();
	public static ArrayList <Integer>blockGlassResistentLight = new ArrayList<Integer>();	
	public static ArrayList blockStonePlanksNormalName = new ArrayList();
	public static ArrayList blockStonePlanksNormalTexture = new ArrayList();
	public static ArrayList<Integer> blockStonePlanksNormalLight = new ArrayList<Integer>();
	
	private File f=new File("BetterWorld/BWCB_BlockList.cfg");
		
	public void checkDir() throws IOException
	{
		File myDir = new File("BetterWorld");
		if(!myDir.exists())
		{
			myDir.mkdir();
		}
		else{}
	}
	
	public void checkConfFile()
	{
		File confFile = new File("BetterWorld/BWCB_BlockList.cfg");
		if(!confFile.exists())
		{
			writeConfFile();
		}
		else{}
	}
	
	public void writeConfFile()
	{
		InputStream stream = this.getClass().getResourceAsStream("/mods/betterworld/CB/config/BWCB_BlockList.cfg");
	    if (stream == null) {
	        //send your exception or warning
	    }
	    OutputStream resStreamOut;
	    int readBytes;
	    byte[] buffer = new byte[4096];
	    try {
	        resStreamOut = new FileOutputStream(new File("BetterWorld/BWCB_BlockList.cfg"));
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	            System.out.println("Storing Block config file to disk");
	        }
	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	}
		public void readConfFile() throws IOException
		{
			checkDir();
			checkConfFile();
			BufferedReader br= null;
			String in ="";
			try {
				br = new BufferedReader(new FileReader(f));
				try {
					in = br.readLine();
				} catch (IOException e) {
					System.out.println("Block config File READ ERROR!");
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.out.println("FILE NOT FOUND!");
				e.printStackTrace();
			}
			while(in!=null){
			allList.add(in);
		 	in=br.readLine();
			
			}
		}
		public void sortArrays()
		{		
			for (int i = 0; i < allList.size(); i++) {
				String[] temp = allList.get(i).split(":");
				
				if (temp[0].equals("stone") || temp[0].equals("wood"))
				{
					if(temp[3].equals("false"))// resistence
					{
					blockStonePlanksNormalName.add(temp[1]);
					blockStonePlanksNormalTexture.add(temp[2]);
					blockStonePlanksNormalLight.add(Integer.valueOf(temp[4]));
					}else{}
				}
				if (temp[0].equals("stone"))
				{
					if(temp[3].equals("false"))// resistence
					{
						blockStoneNormalName.add(temp[1]);
						blockStoneNormalTexture.add(temp[2]);
						blockStoneNormalLight.add(Integer.valueOf(temp[4]));

					}
					else
					{
						blockStoneResistentName.add(temp[1]);
						blockStoneResistentTexture.add(temp[2]);
						blockStoneResistentLight.add(Integer.valueOf(temp[4]));
					}
				}
				else if (temp[0].equals("wood"))
				{
					if(temp[3].equals("false"))// resistence
					{
						blockPlanksNormalName.add(temp[1]);
						blockPlanksNormalTexture.add(temp[2]);
						blockPlanksNormalLight.add(Integer.valueOf(temp[4]));
					}
					else
					{
						blockPlanksResistentName.add(String.valueOf(temp[1]));
						blockPlanksResistentTexture.add(String.valueOf(temp[2]));
						blockPlanksResistentLight.add(Integer.valueOf(temp[4]));
					}
				}
				else if (temp[0].equals("glass"))
				{
					if(temp[3].equals("false"))
					{
						blockGlassNormalName.add(String.valueOf(temp[1]));
						blockGlassNormalTexture.add(String.valueOf(temp[2]));
						blockGlassNormalLight.add(Integer.valueOf(temp[4]));
					}
					else
					{
						blockGlassResistentName.add(String.valueOf(temp[1]));
						blockGlassResistentTexture.add(String.valueOf(temp[2]));
						blockGlassResistentLight.add(Integer.valueOf(temp[4]));
					}
				}			
			}
			
				System.out.println("BetterWorld CB Stone Block added:           "+blockStoneNormalName.size());
				System.out.println("BetterWorld CB resistant Stone Block added: "+blockStoneResistentName.size());
				System.out.println("BetterWorld CB Wood Block added:            "+blockPlanksNormalName.size());
				System.out.println("BetterWorld CB resistant Wood Block added:  "+blockPlanksResistentName.size());
				System.out.println("BetterWorld CB Glass Block added:           "+blockGlassNormalName.size());
				System.out.println("BetterWorld CB resistant Glass Block added: "+blockGlassResistentName.size());		
		}
		

}
