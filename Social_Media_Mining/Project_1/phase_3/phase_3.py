import snap
import networkx as nx
import operator

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

    for line1 in file1:
        nodes = line1.split(',')
        srcNode = int(nodes[0].strip())
        destNode = int(nodes[1].strip())
        G1.AddEdge(srcNode,destNode)
        G2.add_edge(srcNode,destNode)
 
    local_clustering_coefficient(G1)
 
    global_clustering_coefficient(G1)    
  
    page_rank(G1)    

    eigen_centrality(G1)

    degree_centrality(G1)
   
    jaccard_similarity(G2)

def local_clustering_coefficient(G1):
    NIdCCfH = snap.TIntFltH()
    snap.GetNodeClustCf(G1, NIdCCfH)
    total = 0
    count = 0
    for item in NIdCCfH:
        count = count + 1
        total = total + NIdCCfH[item]
    print "Average Local Clustering Coefficient" + str(total/count)

def page_rank(G1):
    PRankH = snap.TIntFltH()
    snap.GetPageRank(G1, PRankH)
    values = {}
    for item in PRankH:
        values[item] = PRankH[item]
    arr = sorted(values, key=values.get, reverse=True)[:10]
    print "Page Rank - Top 10 Nodes"
    for i in arr:
        print i, PRankH[i]
    

def eigen_centrality(G1):
    NIdEigenH = snap.TIntFltH()
    snap.GetEigenVectorCentr(G1, NIdEigenH)
    values = {}
    for item in NIdEigenH:
        values[item] = NIdEigenH[item]
    arr = sorted(values, key=values.get, reverse=True)[:10]
    print "Eigen Vector Centrality - Top 10 Nodes"
    for i in arr:
        print i, NIdEigenH[i]    

def degree_centrality(G1):
    values = {}
    for NI in G1.Nodes():
        DegCentr = snap.GetDegreeCentr(G1, NI.GetId())
        values[NI.GetId()] = DegCentr
    arr = sorted(values, key=values.get, reverse=True)[:10]
    print "Degree Centralty - Top 10 Nodes"
    for i in arr:
        degree = snap.GetDegreeCentr(G1, i)
        print i, degree

def global_clustering_coefficient(G1):
    cf = snap.GetClustCf(G1)
    print "Global Clustering Coefficient", cf

def jaccard_similarity(G1):
	preds = nx.jaccard_coefficient(G1,G1.edges())
	values = {}
	for n1, n2, p in preds:
            values[str(n1) + ',' + str(n2)] = p
	print "Nodes having maximum Jaccard similarity " + max(values.iteritems(), key=operator.itemgetter(1))[0]

if __name__=="__main__":
        main()
