set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'highCounter_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'highCounter_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'highCounter_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lw 1 lc rgb "red"

set output 'highCounter_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"highCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"highCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"highCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'highCounter_comp.eps'
set terminal postscript eps color
replot

set xrange [350:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "highCounter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"highCounter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"highCounter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"highCounter_cuckoo.tsv" every ::2 using 1:2 with lines title "Cuckoo" lc rgb "red",\

set output 'highCounter_comp_expansion.eps'
set terminal postscript eps color
replot