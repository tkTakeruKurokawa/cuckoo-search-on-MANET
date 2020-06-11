set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "../result100/pareto/occupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"../result100/pareto/occupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"../result100/pareto/occupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"../result100/pareto/occupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

set output '../result100/pareto/eps/cumulative_occupancy.eps'
set terminal postscript eps color
replot
