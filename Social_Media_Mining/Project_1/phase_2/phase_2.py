import networkx as nx
import snap
import matplotlib.pyplot as plt

def main():

    G1 = snap.TUNGraph.New()
    G2 = nx.Graph() 
    file = open('../phase_1_output/anonymized_edge_list.txt', 'r')
    s = set()
    
    for line in file:
        nodes = line.split(',')
        srcNode = int(nodes[0].strip())
        destNode = int(nodes[1].strip())
        if srcNode not in s:
           G1.AddNode(srcNode)
           G2.add_node(srcNode)
           s.add(srcNode)
        if destNode not in s:
           G1.AddNode(destNode)
           G2.add_node(destNode)
           s.add(destNode)
    
    file1 = open('../phase_1_output/anonymized_edge_list.txt', 'r')
    edge_count = 0
    for line1 in file1:
        nodes = line1.split(',')
        srcNode = int(nodes[0].strip())
        destNode = int(nodes[1].strip())
        G1.AddEdge(srcNode,destNode)
        G2.add_edge(srcNode,destNode)
        edge_count += 1   

    degree_distribution(G1)    

    compute_bridge_count(G1)

    count_3_cycles(G1)    

    get_diameter(G1)
    
    plot_x_versus_mod_S(G2)

def get_diameter(G1):
    diam = snap.GetBfsFullDiam(G1,G1.GetNodes(),'false')
    print "Diameter of the graph " + str(diam)

def count_3_cycles(G1):
    no_of_cycles = 0
    for NI in G1.Nodes():
        no_of_cycles += snap.GetNodeTriads(G1, NI.GetId())
    print "No of 3-cycles " + str(no_of_cycles)

def compute_bridge_count(G1):
    EdgeV = snap.TIntPrV()
    snap.GetEdgeBridges(G1, EdgeV)
    count = 0
    for edge in EdgeV:
        print "Bridge Edge: (%d, %d)" % (edge.GetVal1(), edge.GetVal2())
        count += 1
    print "No. of bridges " + str(count)

def degree_distribution(G1):
    CntV = snap.TIntPrV()
    # get degree distribution pairs (count, out-degree):
    snap.GetOutDegCnt(G1, CntV)
    print " Node  Count n Degree"
    for p in CntV:
        print "%d     %d" % (p.GetVal2(), p.GetVal1())
    # Plot graph using gnuPlot

    # Plotting using Snapy
    snap.PlotInDegDistr(G1, "degree_dist", "Degree Distribution of Graph")    

def power_law_exponent(G2):
    in_degrees = nx.degree(G2) # dictionary node:degree
    in_values = sorted(set(in_degrees.values()))
    in_hist = [in_degrees.values().count(x) for x in in_values]
    plt.loglog(in_values,in_hist, basex=np.e, basey=np.e(-2))
    plt.show(block=False)

def plot_x_versus_mod_S(G2):
    array = []
    test = range(1,101)
    for x in test:
	G = G2.copy()
	count_edges = G.number_of_edges()
	number_edges_removed = int((x*count_edges)/100)
	for itr in G.edges() :
	    if number_edges_removed == 0 :
	       break
	    G.remove_edge(itr[0],itr[1])
	    number_edges_removed -= 1
	cal = sorted(nx.connected_components(G))
	largest = len(cal[0])
	cont = cal[0]
	for i in cal:
	    if(len(i) > largest):
		largest = len(i)
		cont = i
	if largest > 1 :
	    array.append(largest)
	else:
	    array.append(0)
    plt.plot(test,array)
    plt.title("x versus |S|")
    plt.xlabel("x")
    plt.ylabel("|S|")
    plt.show()    

if __name__=="__main__":
    main()
