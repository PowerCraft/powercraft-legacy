package codechicken.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import cpw.mods.fml.common.FMLCommonHandler;

public class ConfigFile
{
	public static class TagOrderComparator implements Comparator<ConfigTag>
	{
		int sortMode;
		
		public TagOrderComparator(int sortMode)
		{
			this.sortMode = sortMode;
		}
		
		public int compare(ConfigTag o1, ConfigTag o2)
		{
			if(o1.position == o2.position)
			{
				if(o1.brace == o2.brace)
				{
					switch(sortMode)
					{
						case 1:
							if(o1.value == null)
								return o2.value == null ? 0 : 1;
							else if(o2.value == null)
								return -1;
							else
								return o1.value.compareTo(o2.value);
						default:
							return o1.name.compareTo(o2.name);
					}
				}
				else
				{
					return o1.brace ? 1 : -1;//braced one goes after
				}
			}
			else
			{
				return Integer.valueOf(o1.position).compareTo(o2.position);
			}
		}
		
	}
	
	public ConfigFile(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{
	            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
			}
		}
		this.file = file;
		maintags = new TreeMap<String, ConfigTag>();
		loadConfig();
	}
	
	private void loadConfig()
	{
		loading = true;
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader(file));
		}
		catch(FileNotFoundException e)
		{
            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
			return;
		}
		
		String comment = "";
		String bracequalifier="";
		boolean readingheader = true;
		try
		{
			while(true)
			{
				boolean headerline = false;
				String line = readLine(reader);
				if(line == null)
					break;
				
				if(line.startsWith("#"))
				{
					if(readingheader)
					{
						if(comment == null || comment.equals(""))
						{
							this.comment = line.substring(1);
						}
						else
						{
							this.comment = this.comment+":"+line.substring(1);
						}
						headerline = true;
					}
					else
					{
						if(comment == null || comment.equals(""))
						{
							comment = line.substring(1);
						}
						else
						{
							comment = comment+":"+line.substring(1);
						}
					}
				}
				else if(line.contains("="))
				{
					String qualifiedname = line.substring(0, line.indexOf("="));
					getTag(qualifiedname)
					.setComment(comment)
					.setValue(line.substring(line.indexOf("=")+1));
					comment = "";
					bracequalifier = qualifiedname;
				}
				else if(line.equals("{"))
				{
					getTag(bracequalifier).setComment(comment).useBraces().loadChildren(reader);
					comment = "";
					bracequalifier = "";
				}
				else if(line.equals(""))
				{
					comment = "";
					bracequalifier = "";
				}
				else
				{
					bracequalifier = line;
				}
				if(!headerline)
				{
					readingheader = false;
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
		}
		
		loading = false;
	}
	
	public static String readLine(BufferedReader reader) throws IOException
	{
		String line = reader.readLine();
		if(line != null)
			return line.replace("\t", "");
		return line;
	}
	
	public static String formatLine(String line)
	{
		line = line.replace("\t", "");
		if(line.startsWith("#"))
		{
			return line;
		}
		else if(line.contains("="))
		{
			line = line.substring(0, line.indexOf("=")).replace(" ", "")+line.substring(line.indexOf("="));
			return line;
		}
		else
		{
			line = line.replace(" ", "");
			return line;
		}
	}
	
	public static void writeLine(PrintWriter writer, String line, int tabs)
	{
		for(int i = 0; i < tabs; i++)
		{
			writer.print('\t');
		}
		writer.println(line);	
	}
	
	public ConfigTag getTag(String tagname, boolean createnew)
	{
		tagname = tagname.replace('_', ' ');
		int dotpos = tagname.indexOf(".");
		String basetagname = dotpos == -1 ? tagname : tagname.substring(0, dotpos);
		ConfigTag basetag = maintags.get(basetagname);
		if(basetag == null)
		{
			if(!createnew)return null;
			
			basetag = getNewTag(basetagname);
			saveConfig();
		}
		if(dotpos == -1)
		{
			return basetag;
		}
		else
		{
			return basetag.getTag(tagname.substring(dotpos+1), createnew);
		}
	}
	
	public ConfigTag getTag(String tagname)
	{
		return getTag(tagname, true);
	}
	
	public boolean containsTag(String tagname)
	{
		return getTag(tagname, false) != null;
	}
	
	/**
	 * @param tagname the Tag to remove
	 * @return true if this tag existed and was removed
	 */
	public boolean removeTag(String tagname)
	{
		ConfigTag tag = getTag(tagname, false);
		if(tag == null)return false;

		int dotpos = tagname.lastIndexOf(".");
		String lastpart = dotpos == -1 ? tagname : tagname.substring(dotpos+1, tagname.length());
		if(tag.parenttag != null)
		{
			return tag.parenttag.childtags.remove(lastpart) != null;
		}
		else if(tag.parentfile != null)	
		{
			return tag.parentfile.maintags.remove(lastpart) != null;
		}
		else
		{
			return false;
		}
	}
	
	public ConfigTag getNewTag(String tagname)
	{
		return new ConfigTag(this, tagname);
	}
	
	public void saveConfig()
	{
		if(loading)
		{
			return;
		}
		
		PrintWriter writer;
		try
		{
			writer = new PrintWriter(file);
		}
		catch(FileNotFoundException e)
		{
            FMLCommonHandler.instance().raiseException(e, "Code Chicken Core", true);
			return;
		}
		
		if(comment != null && !comment.equals(""))
		{
			String doubledelimcomment = comment.replace("::", "~|~");//that way a double will still work
			String[] comments = doubledelimcomment.split(":");
			for(int i = 0; i < comments.length; i++)
			{
				comments[i] = comments[i].replace("~|~", ":");
				ConfigFile.writeLine(writer, "#"+comments[i], 0);
			}
		}
		ConfigFile.writeLine(writer, "", 0);
		saveTagTree(writer, maintags, 0, "", sortMode);
		writer.flush();
		writer.close();
	}
	
	public ConfigFile setComment(String header)
	{
		comment = header;
		saveConfig();
		return this;
	}
	
	public static void saveTagTree(PrintWriter writer, TreeMap<String, ConfigTag> tagtree, int braces, String bracequalifier, int sortMode)
	{
		ArrayList<ConfigTag> taglist = new ArrayList<ConfigTag>(tagtree.size());
		for(Entry<String, ConfigTag> tag : tagtree.entrySet())
		{
			taglist.add(tag.getValue());
		}
		
		Collections.sort(taglist, new TagOrderComparator(sortMode));

		for(ConfigTag tag : taglist)
		{
			tag.save(writer, braces, bracequalifier);
		}
	}
	
	public static ArrayList<ConfigTag> getSortedTagList(TreeMap<String, ConfigTag> tagtree, int sortMode)
	{
		ArrayList<ConfigTag> taglist = new ArrayList<ConfigTag>(tagtree.size());
		for(Entry<String, ConfigTag> tag : tagtree.entrySet())
		{
			taglist.add(tag.getValue());
		}
		
		Collections.sort(taglist, new TagOrderComparator(sortMode));
		return taglist;
	}
	
	public ConfigFile setNewLineMode(int mode)
	{
		newlinemode = mode;
		for(Entry<String, ConfigTag> entry : maintags.entrySet())
		{
			ConfigTag tag = entry.getValue();
			if(newlinemode == 0)
			{
				tag.newline = false;
			}
			else if(newlinemode == 1)
			{
				tag.newline = tag.brace;
			}
			else if(newlinemode == 2)
			{
				tag.newline = true;
			}
		}
		saveConfig();
		return this;
	}
	
	public ConfigFile setSortMode(int mode)
	{
		sortMode = mode;
		saveConfig();
		return this;
	}
	
	public File file;
	public TreeMap<String, ConfigTag> maintags;
	public String comment;
	public int newlinemode = 2;
	public int sortMode = 0;
	public static boolean loading;
	
	public static final byte[] lineend = new byte[]{0xD, 0xA};
}
