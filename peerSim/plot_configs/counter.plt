set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "../result300/pareto/counter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"../result300/pareto/counter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"../result300/pareto/counter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"../result300/pareto/counter_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

set output '../result300/pareto/eps/data_availability.eps'
set terminal postscript eps color
replot
