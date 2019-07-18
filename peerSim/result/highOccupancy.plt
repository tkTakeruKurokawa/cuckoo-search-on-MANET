set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'highOccupancy_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'highOccupancy_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'highOccupancy_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lw 1 lc rgb "red"

set output 'highOccupancy_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"highOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"highOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"highOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'highOccupancy_comp.eps'
set terminal postscript eps color
replot

set xrange [350:500]
set xlabel 'Num of cycle'
set yrange [3000000:13000000]
set ylabel 'Cumulative Occupancy'
plot "highOccupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"highOccupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"highOccupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"highOccupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'highOccupancy_comp_expansion.eps'
set terminal postscript eps color
replot