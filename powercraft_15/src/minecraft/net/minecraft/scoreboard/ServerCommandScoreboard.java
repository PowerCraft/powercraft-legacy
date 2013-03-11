package net.minecraft.scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

public class ServerCommandScoreboard extends CommandBase
{
    public String getCommandName()
    {
        return "scoreboard";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1)
        {
            if (par2ArrayOfStr[0].equalsIgnoreCase("objectives"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    this.func_96336_d(par1ICommandSender);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length < 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }

                    this.func_96350_b(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }

                    this.func_96337_e(par1ICommandSender, par2ArrayOfStr[2]);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("setdisplay"))
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 3 && par2ArrayOfStr.length != 4)
                    {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }

                    this.func_96347_j(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }

            if (par2ArrayOfStr[0].equalsIgnoreCase("players"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    if (par2ArrayOfStr.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }

                    this.func_96341_k(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }

                    this.func_96339_l(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }

                    this.func_96339_l(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("set"))
                {
                    if (par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }

                    this.func_96339_l(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("reset"))
                    {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }

                    this.func_96351_m(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }

            if (par2ArrayOfStr[0].equalsIgnoreCase("teams"))
            {
                if (par2ArrayOfStr.length == 1)
                {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("list"))
                {
                    if (par2ArrayOfStr.length > 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }

                    this.func_96344_f(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length < 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }

                    this.func_96340_c(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }

                    this.func_96343_e(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("empty"))
                {
                    if (par2ArrayOfStr.length != 3)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }

                    this.func_96346_i(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("join"))
                {
                    if (par2ArrayOfStr.length < 4 && (par2ArrayOfStr.length != 3 || !(par1ICommandSender instanceof EntityPlayer)))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }

                    this.func_96342_g(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("leave"))
                {
                    if (par2ArrayOfStr.length < 3 && !(par1ICommandSender instanceof EntityPlayer))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }

                    this.func_96349_h(par1ICommandSender, par2ArrayOfStr, 2);
                }
                else
                {
                    if (!par2ArrayOfStr[1].equalsIgnoreCase("option"))
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }

                    if (par2ArrayOfStr.length != 4 && par2ArrayOfStr.length != 5)
                    {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }

                    this.func_96348_d(par1ICommandSender, par2ArrayOfStr, 2);
                }

                return;
            }
        }

        throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
    }

    protected Scoreboard func_96334_d()
    {
        return MinecraftServer.getServer().worldServerForDimension(0).func_96441_U();
    }

    protected ScoreObjective func_96345_a(String par1Str, boolean par2)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScoreObjective scoreobjective = scoreboard.func_96518_b(par1Str);

        if (scoreobjective == null)
        {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] {par1Str});
        }
        else if (par2 && scoreobjective.func_96680_c().func_96637_b())
        {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] {par1Str});
        }
        else
        {
            return scoreobjective;
        }
    }

    protected ScorePlayerTeam func_96338_a(String par1Str)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScorePlayerTeam scoreplayerteam = scoreboard.func_96508_e(par1Str);

        if (scoreplayerteam == null)
        {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] {par1Str});
        }
        else
        {
            return scoreplayerteam;
        }
    }

    protected void func_96350_b(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3++];
        String s1 = par2ArrayOfStr[par3++];
        Scoreboard scoreboard = this.func_96334_d();
        ScoreObjectiveCriteria scoreobjectivecriteria = (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get(s1);

        if (scoreobjectivecriteria == null)
        {
            String[] astring1 = (String[])ScoreObjectiveCriteria.field_96643_a.keySet().toArray(new String[0]);
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] {joinNiceString(astring1)});
        }
        else if (scoreboard.func_96518_b(s) != null)
        {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else
        {
            ScoreObjective scoreobjective = scoreboard.func_96535_a(s, scoreobjectivecriteria);

            if (par2ArrayOfStr.length > par3)
            {
                String s2 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

                if (s2.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] {s2, Integer.valueOf(32)});
                }

                if (s2.length() > 0)
                {
                    scoreobjective.func_96681_a(s2);
                }
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.add.success", new Object[] {s});
        }
    }

    protected void func_96340_c(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3++];
        Scoreboard scoreboard = this.func_96334_d();

        if (scoreboard.func_96508_e(s) != null)
        {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] {s});
        }
        else if (s.length() > 16)
        {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] {s, Integer.valueOf(16)});
        }
        else
        {
            ScorePlayerTeam scoreplayerteam = scoreboard.func_96527_f(s);

            if (par2ArrayOfStr.length > par3)
            {
                String s1 = func_82360_a(par1ICommandSender, par2ArrayOfStr, par3);

                if (s1.length() > 32)
                {
                    throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] {s1, Integer.valueOf(32)});
                }

                if (s1.length() > 0)
                {
                    scoreplayerteam.func_96664_a(s1);
                }
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.add.success", new Object[] {s});
        }
    }

    protected void func_96348_d(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        ScorePlayerTeam scoreplayerteam = this.func_96338_a(par2ArrayOfStr[par3++]);
        String s = par2ArrayOfStr[par3++].toLowerCase();

        if (!s.equalsIgnoreCase("color") && !s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles"))
        {
            throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
        }
        else if (par2ArrayOfStr.length == 4)
        {
            if (s.equalsIgnoreCase("color"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(EnumChatFormatting.func_96296_a(true, false))});
            }
            else if (!s.equalsIgnoreCase("friendlyfire") && !s.equalsIgnoreCase("seeFriendlyInvisibles"))
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else
            {
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
            }
        }
        else
        {
            String s1 = par2ArrayOfStr[par3++];

            if (s.equalsIgnoreCase("color"))
            {
                EnumChatFormatting enumchatformatting = EnumChatFormatting.func_96300_b(s1);

                if (s1 == null)
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(EnumChatFormatting.func_96296_a(true, false))});
                }

                scoreplayerteam.func_96666_b(enumchatformatting.toString());
                scoreplayerteam.func_96662_c(EnumChatFormatting.RESET.toString());
            }
            else if (s.equalsIgnoreCase("friendlyfire"))
            {
                if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
                }

                scoreplayerteam.func_96660_a(s1.equalsIgnoreCase("true"));
            }
            else if (s.equalsIgnoreCase("seeFriendlyInvisibles"))
            {
                if (!s1.equalsIgnoreCase("true") && !s1.equalsIgnoreCase("false"))
                {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] {s, func_96333_a(Arrays.asList(new String[]{"true", "false"}))});
                }

                scoreplayerteam.func_98300_b(s1.equalsIgnoreCase("true"));
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.option.success", new Object[] {s, scoreplayerteam.func_96661_b(), s1});
        }
    }

    protected void func_96343_e(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScorePlayerTeam scoreplayerteam = this.func_96338_a(par2ArrayOfStr[par3++]);
        scoreboard.func_96511_d(scoreplayerteam);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.remove.success", new Object[] {scoreplayerteam.func_96661_b()});
    }

    protected void func_96344_f(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();

        if (par2ArrayOfStr.length > par3)
        {
            ScorePlayerTeam scoreplayerteam = this.func_96338_a(par2ArrayOfStr[par3++]);
            Collection collection = scoreplayerteam.func_96670_d();

            if (collection.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] {scoreplayerteam.func_96661_b()});
            }

            par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString("commands.scoreboard.teams.list.player.count", new Object[] {Integer.valueOf(collection.size()), scoreplayerteam.func_96661_b()}));
            par1ICommandSender.sendChatToPlayer(joinNiceString(collection.toArray()));
        }
        else
        {
            Collection collection1 = scoreboard.func_96525_g();

            if (collection1.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }

            par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString("commands.scoreboard.teams.list.count", new Object[] {Integer.valueOf(collection1.size())}));
            Iterator iterator = collection1.iterator();

            while (iterator.hasNext())
            {
                ScorePlayerTeam scoreplayerteam1 = (ScorePlayerTeam)iterator.next();
                par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.scoreboard.teams.list.entry", new Object[] {scoreplayerteam1.func_96661_b(), scoreplayerteam1.func_96669_c(), Integer.valueOf(scoreplayerteam1.func_96670_d().size())}));
            }
        }
    }

    protected void func_96342_g(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScorePlayerTeam scoreplayerteam = scoreboard.func_96508_e(par2ArrayOfStr[par3++]);
        HashSet hashset = new HashSet();
        String s;

        if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length)
        {
            s = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();
            scoreboard.func_96521_a(s, scoreplayerteam);
            hashset.add(s);
        }
        else
        {
            while (par3 < par2ArrayOfStr.length)
            {
                s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
                scoreboard.func_96521_a(s, scoreplayerteam);
                hashset.add(s);
            }
        }

        if (!hashset.isEmpty())
        {
            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.join.success", new Object[] {Integer.valueOf(hashset.size()), scoreplayerteam.func_96661_b(), joinNiceString(hashset.toArray(new String[0]))});
        }
    }

    protected void func_96349_h(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        HashSet hashset = new HashSet();
        HashSet hashset1 = new HashSet();
        String s;

        if (par1ICommandSender instanceof EntityPlayer && par3 == par2ArrayOfStr.length)
        {
            s = getCommandSenderAsPlayer(par1ICommandSender).getEntityName();

            if (scoreboard.func_96524_g(s))
            {
                hashset.add(s);
            }
            else
            {
                hashset1.add(s);
            }
        }
        else
        {
            while (par3 < par2ArrayOfStr.length)
            {
                s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);

                if (scoreboard.func_96524_g(s))
                {
                    hashset.add(s);
                }
                else
                {
                    hashset1.add(s);
                }
            }
        }

        if (!hashset.isEmpty())
        {
            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.leave.success", new Object[] {Integer.valueOf(hashset.size()), joinNiceString(hashset.toArray(new String[0]))});
        }

        if (!hashset1.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] {Integer.valueOf(hashset1.size()), joinNiceString(hashset1.toArray(new String[0]))});
        }
    }

    protected void func_96346_i(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScorePlayerTeam scoreplayerteam = this.func_96338_a(par2ArrayOfStr[par3++]);
        ArrayList arraylist = new ArrayList(scoreplayerteam.func_96670_d());

        if (arraylist.isEmpty())
        {
            throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] {scoreplayerteam.func_96661_b()});
        }
        else
        {
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                scoreboard.func_96512_b(s, scoreplayerteam);
            }

            notifyAdmins(par1ICommandSender, "commands.scoreboard.teams.empty.success", new Object[] {Integer.valueOf(arraylist.size()), scoreplayerteam.func_96661_b()});
        }
    }

    protected void func_96337_e(ICommandSender par1ICommandSender, String par2Str)
    {
        Scoreboard scoreboard = this.func_96334_d();
        ScoreObjective scoreobjective = this.func_96345_a(par2Str, false);
        scoreboard.func_96519_k(scoreobjective);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.remove.success", new Object[] {par2Str});
    }

    protected void func_96336_d(ICommandSender par1ICommandSender)
    {
        Scoreboard scoreboard = this.func_96334_d();
        Collection collection = scoreboard.func_96514_c();

        if (collection.size() <= 0)
        {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        else
        {
            par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString("commands.scoreboard.objectives.list.count", new Object[] {Integer.valueOf(collection.size())}));
            Iterator iterator = collection.iterator();

            while (iterator.hasNext())
            {
                ScoreObjective scoreobjective = (ScoreObjective)iterator.next();
                par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.scoreboard.objectives.list.entry", new Object[] {scoreobjective.func_96679_b(), scoreobjective.func_96678_d(), scoreobjective.func_96680_c().func_96636_a()}));
            }
        }
    }

    protected void func_96347_j(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        String s = par2ArrayOfStr[par3++];
        int j = Scoreboard.func_96537_j(s);
        ScoreObjective scoreobjective = null;

        if (par2ArrayOfStr.length == 4)
        {
            scoreobjective = this.func_96345_a(par2ArrayOfStr[par3++], false);
        }

        if (j < 0)
        {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] {s});
        }
        else
        {
            scoreboard.func_96530_a(j, scoreobjective);

            if (scoreobjective != null)
            {
                notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successSet", new Object[] {Scoreboard.func_96517_b(j), scoreobjective.func_96679_b()});
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.scoreboard.objectives.setdisplay.successCleared", new Object[] {Scoreboard.func_96517_b(j)});
            }
        }
    }

    protected void func_96341_k(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();

        if (par2ArrayOfStr.length > par3)
        {
            String s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
            Map map = scoreboard.func_96510_d(s);

            if (map.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] {s});
            }

            par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString("commands.scoreboard.players.list.player.count", new Object[] {Integer.valueOf(map.size()), s}));
            Iterator iterator = map.values().iterator();

            while (iterator.hasNext())
            {
                Score score = (Score)iterator.next();
                par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.scoreboard.players.list.player.entry", new Object[] {Integer.valueOf(score.func_96652_c()), score.func_96645_d().func_96678_d(), score.func_96645_d().func_96679_b()}));
            }
        }
        else
        {
            Collection collection = scoreboard.func_96526_d();

            if (collection.size() <= 0)
            {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }

            par1ICommandSender.sendChatToPlayer(EnumChatFormatting.DARK_GREEN + par1ICommandSender.translateString("commands.scoreboard.players.list.count", new Object[] {Integer.valueOf(collection.size())}));
            par1ICommandSender.sendChatToPlayer(joinNiceString(collection.toArray()));
        }
    }

    protected void func_96339_l(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        String s = par2ArrayOfStr[par3 - 1];
        String s1 = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
        ScoreObjective scoreobjective = this.func_96345_a(par2ArrayOfStr[par3++], true);
        int j = s.equalsIgnoreCase("set") ? parseInt(par1ICommandSender, par2ArrayOfStr[par3++]) : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[par3++], 1);
        Scoreboard scoreboard = this.func_96334_d();
        Score score = scoreboard.func_96529_a(s1, scoreobjective);

        if (s.equalsIgnoreCase("set"))
        {
            score.func_96647_c(j);
        }
        else if (s.equalsIgnoreCase("add"))
        {
            score.func_96649_a(j);
        }
        else
        {
            score.func_96646_b(j);
        }

        notifyAdmins(par1ICommandSender, "commands.scoreboard.players.set.success", new Object[] {scoreobjective.func_96679_b(), s1, Integer.valueOf(score.func_96652_c())});
    }

    protected void func_96351_m(ICommandSender par1ICommandSender, String[] par2ArrayOfStr, int par3)
    {
        Scoreboard scoreboard = this.func_96334_d();
        String s = func_96332_d(par1ICommandSender, par2ArrayOfStr[par3++]);
        scoreboard.func_96515_c(s);
        notifyAdmins(par1ICommandSender, "commands.scoreboard.players.reset.success", new Object[] {s});
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"objectives", "players", "teams"});
        }
        else
        {
            if (par2ArrayOfStr[0].equalsIgnoreCase("objectives"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"list", "add", "remove", "setdisplay"});
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("add"))
                {
                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, ScoreObjectiveCriteria.field_96643_a.keySet());
                    }
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96335_a(false));
                    }
                }
                else if (par2ArrayOfStr[1].equalsIgnoreCase("setdisplay"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"list", "sidebar", "belowName"});
                    }

                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96335_a(false));
                    }
                }
            }
            else if (par2ArrayOfStr[0].equalsIgnoreCase("players"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"set", "add", "remove", "reset", "list"});
                }

                if (!par2ArrayOfStr[1].equalsIgnoreCase("set") && !par2ArrayOfStr[1].equalsIgnoreCase("add") && !par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                {
                    if ((par2ArrayOfStr[1].equalsIgnoreCase("reset") || par2ArrayOfStr[1].equalsIgnoreCase("list")) && par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96334_d().func_96526_d());
                    }
                }
                else
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (par2ArrayOfStr.length == 4)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96335_a(true));
                    }
                }
            }
            else if (par2ArrayOfStr[0].equalsIgnoreCase("teams"))
            {
                if (par2ArrayOfStr.length == 2)
                {
                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"add", "remove", "join", "leave", "empty", "list", "option"});
                }

                if (par2ArrayOfStr[1].equalsIgnoreCase("join"))
                {
                    if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96334_d().func_96531_f());
                    }

                    if (par2ArrayOfStr.length >= 4)
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }
                }
                else
                {
                    if (par2ArrayOfStr[1].equalsIgnoreCase("leave"))
                    {
                        return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
                    }

                    if (!par2ArrayOfStr[1].equalsIgnoreCase("empty") && !par2ArrayOfStr[1].equalsIgnoreCase("list") && !par2ArrayOfStr[1].equalsIgnoreCase("remove"))
                    {
                        if (par2ArrayOfStr[1].equalsIgnoreCase("option"))
                        {
                            if (par2ArrayOfStr.length == 3)
                            {
                                return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96334_d().func_96531_f());
                            }

                            if (par2ArrayOfStr.length == 4)
                            {
                                return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"color", "friendlyfire", "seeFriendlyInvisibles"});
                            }

                            if (par2ArrayOfStr.length == 5)
                            {
                                if (par2ArrayOfStr[3].equalsIgnoreCase("color"))
                                {
                                    return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, EnumChatFormatting.func_96296_a(true, false));
                                }

                                if (par2ArrayOfStr[3].equalsIgnoreCase("friendlyfire") || par2ArrayOfStr[3].equalsIgnoreCase("seeFriendlyInvisibles"))
                                {
                                    return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"true", "false"});
                                }
                            }
                        }
                    }
                    else if (par2ArrayOfStr.length == 3)
                    {
                        return getListOfStringsFromIterableMatchingLastWord(par2ArrayOfStr, this.func_96334_d().func_96531_f());
                    }
                }
            }

            return null;
        }
    }

    protected List func_96335_a(boolean par1)
    {
        Collection collection = this.func_96334_d().func_96514_c();
        ArrayList arraylist = new ArrayList();
        Iterator iterator = collection.iterator();

        while (iterator.hasNext())
        {
            ScoreObjective scoreobjective = (ScoreObjective)iterator.next();

            if (!par1 || !scoreobjective.func_96680_c().func_96637_b())
            {
                arraylist.add(scoreobjective.func_96679_b());
            }
        }

        return arraylist;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par1ArrayOfStr[0].equalsIgnoreCase("players") ? par2 == 2 : (!par1ArrayOfStr[0].equalsIgnoreCase("teams") ? false : par2 == 2 || par2 == 3);
    }
}
