set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "lowCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'lowCounter_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "lowCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'lowCounter_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "lowCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'lowCounter_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "lowCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lw 1 lc rgb "red"

set output 'lowCounter_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "lowCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"lowCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"lowCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"lowCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'lowCounter_comp.eps'
set terminal postscript eps color
replot

set xrange [350:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.01]
set ylabel 'Data Availability'
plot "lowCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"lowCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"lowCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"lowCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'lowCounter_comp_expansion.eps'
set terminal postscript eps color
replot