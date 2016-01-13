#
# Degree Dist. of Preferential Attch. Model. G(1547, 15415). 410 (0.2650) nodes with in-deg > avg deg (19.9), 118 (0.0763) with >2*avg.deg (Wed Oct 14 21:03:39 2015)
#

set title "Degree Dist. of Preferential Attch. Model. G(1547, 15415). 410 (0.2650) nodes with in-deg > avg deg (19.9), 118 (0.0763) with >2*avg.deg"
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
set output 'inDeg.pref_attach.png'
plot 	"inDeg.pref_attach.tab" using 1:2 title "" with linespoints pt 6
