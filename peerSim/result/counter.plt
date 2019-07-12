set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "counter_owner.tsv" every ::2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'counter_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "counter_path.tsv" every ::2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'counter_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "counter_relate.tsv" every ::2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'counter_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "counter_cuckoo.tsv" every ::2 with lines title "Cuckoo" lw 1 lc rgb "red"

set output 'counter_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "counter_owner.tsv" every ::2 with lines title "Owner" lc rgb "blue",\
	"counter_path.tsv" every ::2 with lines title "Path" lc rgb "magenta",\
	"counter_relate.tsv" every ::2 with lines title "Kageyama" lc rgb "dark-green",\
	"counter_cuckoo.tsv" every ::2 with lines title "Cuckoo" lc rgb "red",\

set output 'counter_comp.eps'
set terminal postscript eps color
replot

set xrange [350:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.01]
set ylabel 'Data Availavility'
plot "counter_owner.tsv" every ::2 with lines title "Owner" lc rgb "blue",\
	"counter_path.tsv" every ::2 with lines title "Path" lc rgb "magenta",\
	"counter_relate.tsv" every ::2 with lines title "Kageyama" lc rgb "dark-green",\
	"counter_cuckoo.tsv" every ::2 with lines title "Cuckoo" lc rgb "red",\

set output 'counter_comp_expansion.eps'
set terminal postscript eps color
replot