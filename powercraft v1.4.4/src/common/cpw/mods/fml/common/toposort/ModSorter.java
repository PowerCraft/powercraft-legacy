package cpw.mods.fml.common.toposort;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.toposort.TopologicalSort.DirectedGraph;
import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModSorter
{
    private DirectedGraph<ModContainer> modGraph;

    private ModContainer beforeAll = new DummyModContainer();
    private ModContainer afterAll = new DummyModContainer();
    private ModContainer before = new DummyModContainer();
    private ModContainer after = new DummyModContainer();

    public ModSorter(List<ModContainer> modList, Map<String, ModContainer> nameLookup)
    {
        buildGraph(modList, nameLookup);
    }

    private void buildGraph(List<ModContainer> modList, Map<String, ModContainer> nameLookup)
    {
        modGraph = new DirectedGraph<ModContainer>();
        modGraph.addNode(beforeAll);
        modGraph.addNode(before);
        modGraph.addNode(afterAll);
        modGraph.addNode(after);
        modGraph.addEdge(before, after);
        modGraph.addEdge(beforeAll, before);
        modGraph.addEdge(after, afterAll);

        for (ModContainer mod : modList)
        {
            modGraph.addNode(mod);
        }

        for (ModContainer mod : modList)
        {
            if (mod.isImmutable())
            {
                modGraph.addEdge(beforeAll, mod);
                modGraph.addEdge(mod, before);
                continue;
            }

            boolean preDepAdded = false;
            boolean postDepAdded = false;

            for (ArtifactVersion dep : mod.getDependencies())
            {
                preDepAdded = true;
                String modid = dep.getLabel();

                if (modid.equals("*"))
                {
                    modGraph.addEdge(mod, afterAll);
                    modGraph.addEdge(after, mod);
                    postDepAdded = true;
                }
                else
                {
                    modGraph.addEdge(before, mod);

                    if (Loader.isModLoaded(modid))
                    {
                        modGraph.addEdge(nameLookup.get(modid), mod);
                    }
                }
            }

            for (ArtifactVersion dep : mod.getDependants())
            {
                postDepAdded = true;
                String modid = dep.getLabel();

                if (modid.equals("*"))
                {
                    modGraph.addEdge(beforeAll, mod);
                    modGraph.addEdge(mod, before);
                    preDepAdded = true;
                }
                else
                {
                    modGraph.addEdge(mod, after);

                    if (Loader.isModLoaded(modid))
                    {
                        modGraph.addEdge(mod, nameLookup.get(modid));
                    }
                }
            }

            if (!preDepAdded)
            {
                modGraph.addEdge(before, mod);
            }

            if (!postDepAdded)
            {
                modGraph.addEdge(mod, after);
            }
        }
    }

    public List<ModContainer> sort()
    {
        List<ModContainer> sortedList = TopologicalSort.topologicalSort(modGraph);
        sortedList.removeAll(Arrays.asList(new ModContainer[] {beforeAll, before, after, afterAll}));
        return sortedList;
    }
}
