#
# Degree Dist. of Small World Model. G(1547, 609831). 748 (0.4835) nodes with in-deg > avg deg (788.4), 0 (0.0000) with >2*avg.deg (Wed Oct 14 21:03:38 2015)
#

set title "Degree Dist. of Small World Model. G(1547, 609831). 748 (0.4835) nodes with in-deg > avg deg (788.4), 0 (0.0000) with >2*avg.deg"
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
set output 'inDeg.small_world.png'
plot 	"inDeg.small_world.tab" using 1:2 title "" with linespoints pt 6
