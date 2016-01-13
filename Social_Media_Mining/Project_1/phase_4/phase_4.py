import networkx as nx
import snap

def main():

    G1 = snap.TUNGraph.New()
    G2 = nx.Graph()
    file = open('../phase_1_output/anonymized_edge_list.txt', 'r')
    s = set()
    no_of_nodes = 0
    no_of_edges = 0
    for line in file:
        nodes = line.split(',')
        srcNode = int(nodes[0].strip())
        destNode = int(nodes[1].strip())
        if srcNode not in s:
           no_of_nodes += 1
           G1.AddNode(srcNode)
           G2.add_node(srcNode)
           s.add(srcNode)
        if destNode not in s:
           no_of_nodes += 1
           G1.AddNode(destNode)
           G2.add_node(destNode)
           s.add(destNode)

    file1 = open('../phase_1_output/anonymized_edge_list.txt', 'r')
    for line1 in file1:
        nodes = line1.split(',')
        srcNode = int(nodes[0].strip())
        destNode = int(nodes[1].strip())
        G1.AddEdge(srcNode,destNode)
        G2.add_edge(srcNode,destNode)
        no_of_edges += 1
    
    print "No of nodes " + str(no_of_nodes)
    print "No of edges " + str(no_of_edges)

    average_path_length(G1)
    
    average_degree = 0.0
    total = 0
    for NI in G1.Nodes(): 
        total += NI.GetInDeg()
    average_degree = total/(no_of_nodes)
    print "Average_degree " + str(average_degree)
        
    generate_random_graph(no_of_nodes, no_of_edges) 
    generate_small_world(G1, no_of_nodes, average_degree)
    generate_pref_attach(G1, no_of_nodes, average_degree)
    
def generate_random_graph(no_of_nodes, no_of_edges):
    UGraph = snap.GenRndGnm(snap.PUNGraph, no_of_nodes, no_of_edges)
    print "\n"
    print "Random Graph Model"
    print "\n"
    average_path_length(UGraph)
    local_clustering_coefficient(UGraph)
    global_clustering_coefficient(UGraph)
    snap.PlotInDegDistr(UGraph, "random_graph", "Degree Dist. of Random Graph")
    
def generate_pref_attach(G1, no_of_nodes, avg_deg):
    print "\n"
    print "Preferential Attachment Model"
    print "\n"
    Rnd = snap.TRnd()
    UGraph = snap.GenPrefAttach(no_of_nodes, 10, Rnd)
    average_path_length(UGraph)
    local_clustering_coefficient(UGraph)
    global_clustering_coefficient(UGraph)
    snap.PlotInDegDistr(UGraph, "pref_attach", "Degree Dist. of Preferential Attch. Model")

def generate_small_world(G1, no_of_nodes, avg_deg):
    print "\n"
    print "Small World Model"
    print "\n"
    p = (float)(3*(avg_deg-2))/(4*(avg_deg-1))
    print p
    Rnd = snap.TRnd(1,0)
    UGraph = snap.GenSmallWorld(no_of_nodes, avg_deg, p, Rnd)
    average_path_length(UGraph)
    local_clustering_coefficient(UGraph)
    global_clustering_coefficient(UGraph)
    snap.PlotInDegDistr(UGraph, "small_world", "Degree Dist. of Small World Model")
 
def average_path_length(G1):
    diam = snap.GetBfsEffDiam(G1,G1.GetNodes(),'false')
    print "Average Path Lenth" + str(diam)

 
def degree_distribution(G1):
    CntV = snap.TIntPrV()
    # get degree distribution pairs (count, out-degree):
    snap.GetOutDegCnt(G1, CntV)
    for p in CntV:
        print "%d     %d" % (p.GetVal2(), p.GetVal1())
    # Plot graph using gnuPlot

def local_clustering_coefficient(G1):
    NIdCCfH = snap.TIntFltH()
    snap.GetNodeClustCf(G1, NIdCCfH)
    total = 0
    count = 0
    for item in NIdCCfH:
        count = count + 1
        total = total + NIdCCfH[item]
    print "Average Local Clustering Coefficient" + str(total/count)

def global_clustering_coefficient(G1):
    cf = snap.GetClustCf(G1)
    print "Global Clustering Coefficient", cf

if __name__=="__main__":
        main()
