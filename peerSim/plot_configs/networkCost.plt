set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "../result300/pareto/searchCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"../result300/pareto/searchCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"../result300/pareto/searchCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"../result300/pareto/searchCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output '../result300/pareto/eps/search_cost.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "../result300/pareto/replicationCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"../result300/pareto/replicationCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"../result300/pareto/replicationCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"../result300/pareto/replicationCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output '../result300/pareto/eps/replication_cost.eps'
set terminal postscript eps color
replot
