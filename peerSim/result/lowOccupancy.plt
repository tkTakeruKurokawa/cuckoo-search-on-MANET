set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'eps/lowOccupancy_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'eps/lowOccupancy_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'eps/lowOccupancy_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lw 1 lc rgb "red"

set output 'eps/lowOccupancy_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"lowOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"lowOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"lowOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'eps/lowOccupancy_comp.eps'
set terminal postscript eps color
replot

set xrange [350:500]
set xlabel 'Num of cycle'
set yrange [2000000:13000000]
set ylabel 'Cumulative Occupancy'
plot "lowOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"lowOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"lowOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"lowOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'eps/lowOccupancy_comp_expansion.eps'
set terminal postscript eps color
replot