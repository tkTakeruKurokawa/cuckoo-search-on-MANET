set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "owner_counter.tsv" every ::2 with lines title "Owner" lw 1 lc rgb "red"

set output 'owner_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "path_counter.tsv" every ::2 with lines title "Path" lw 1 lc rgb "blue"

set output 'path_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "relate_counter.tsv" every ::2 with lines title "Kageyama" lw 1 lc rgb "brown"
	
set output 'relate_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "cuckoo_counter.tsv" every ::2 with lines title "Cuckoo" lw 1 lc rgb "magenta"

set output 'cuckoo_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availavility'
plot "owner_counter.tsv" every ::2 with lines title "Owner" lc rgb "blue",\
	"path_counter.tsv" every ::2 with lines title "Path" lc rgb "magenta",\
	"relate_counter.tsv" every ::2 with lines title "Kageyama" lc rgb "dark-green",\
	"cuckoo_counter.tsv" every ::2 with lines title "Cuckoo" lc rgb "red",\

set output 'counter_comp.eps'
set terminal postscript eps color
replot