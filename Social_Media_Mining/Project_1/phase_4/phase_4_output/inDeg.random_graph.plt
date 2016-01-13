#
# Degree Dist. of Random Graph. G(1547, 367109). 787 (0.5087) nodes with in-deg > avg deg (474.6), 0 (0.0000) with >2*avg.deg (Wed Oct 14 21:02:15 2015)
#

set title "Degree Dist. of Random Graph. G(1547, 367109). 787 (0.5087) nodes with in-deg > avg deg (474.6), 0 (0.0000) with >2*avg.deg"
set key bottom right
set logscale xy 10
set format x "10^{%L}"
set mxtics 10
set format y "10^{%L}"
set mytics 10
set grid
set xlabel "In-degree"
set ylabel "Count"
set tics scale 2
set terminal png size 1000,800
set output 'inDeg.random_graph.png'
plot 	"inDeg.random_graph.tab" using 1:2 title "" with linespoints pt 6
