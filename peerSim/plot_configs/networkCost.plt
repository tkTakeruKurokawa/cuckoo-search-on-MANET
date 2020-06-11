set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "../result100/pareto/searchCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"../result100/pareto/searchCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"../result100/pareto/searchCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"../result100/pareto/searchCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output '../result100/pareto/eps/search_cost.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "../result100/pareto/replicationCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"../result100/pareto/replicationCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"../result100/pareto/replicationCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"../result100/pareto/replicationCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output '../result100/pareto/eps/replication_cost.eps'
set terminal postscript eps color
replot
