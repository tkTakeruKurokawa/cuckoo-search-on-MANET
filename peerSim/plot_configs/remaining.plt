set xrange[0:500]
set xlabel "Num of cycle"
set autoscale y
set ylabel "Avarage Storage Remaining"
set key right bottom

plot "../result300/pareto/remaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"../result300/pareto/remaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"../result300/pareto/remaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"../result300/pareto/remaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Proposed"

set output '../result300/pareto/eps/average_storage_remaining.eps'
set terminal postscript eps color
replot
