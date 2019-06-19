set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.2]
set ylabel 'Data Availavility'
plot "owner_counter.csv" every ::2 with lines title "Owner" lw 1 lc rgb "red"

set output 'owner_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.2]
set ylabel 'Data Availavility'
plot "path_counter.csv" every ::2 with lines title "Path" lw 1 lc rgb "blue"

set output 'path_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.2]
set ylabel 'Data Availavility'
plot "relate_counter.csv" every ::2 with lines title "Kageyama" lw 1 lc rgb "brown"
	
set output 'relate_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.2]
set ylabel 'Data Availavility'
plot "cuckoo_counter.csv" every ::2 with lines title "Cuckoo" lw 1 lc rgb "magenta"

set output 'cuckoo_counter.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:0.2]
set ylabel 'Data Availavility'
plot "owner_counter.csv" every ::2 with lines title "Owner" lc rgb "blue",\
	"path_counter.csv" every ::2 with lines title "Path" lc rgb "magenta",\
	"relate_counter.csv" every ::2 with lines title "Kageyama" lc rgb "dark-green",\
	"cuckoo_counter.csv" every ::2 with lines title "Cuckoo" lc rgb "red",\

set output 'counter_comp.eps'
set terminal postscript eps color
replot


set xrange [143:300]
set xlabel 'Num of cycle'
set yrange [0:300]
set ylabel 'Data Availavility'
plot "owner_counter.csv" every ::1 using 1:3 with lines title "Owner" lc rgb "blue",\
	"path_counter.csv" every ::1 using 1:3 with lines title "Path" lc rgb "magenta",\
	"relate_counter.csv" every ::1 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"cuckoo_counter.csv" every ::1 using 1:3 with lines title "Cuckoo" lc rgb "red",\


set output 'counter_expansion.eps'
set terminal postscript eps color
replot

set xrange [160:165]
set yrange [0:50]
plot "owner_counter.csv" every ::1 using 1:3 with lines title "Owner" lw 3 lc rgb "blue",\
	"path_counter.csv" every ::1 using 1:3 with lines title "Path"  lw 3 lc rgb "magenta"

set output 'counter_owner_path.eps'
set terminal postscript eps color
replot

set xrange [245:265]
set yrange [0:10]
plot "relate_counter.csv" every ::1 using 1:3 with lines title "Kageyama"  lw 3 lc rgb "dark-green",\
	"cuckoo_counter.csv" every ::1 using 1:3 with lines title "Cuckoo" lw 3 lc rgb "red",\


set output 'counter_relate_cuckoo.eps'
set terminal postscript eps color
replot