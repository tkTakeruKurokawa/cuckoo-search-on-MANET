set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "owner_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 1 lc rgb "red",\
	"owner_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 1 lc rgb "blue",\
	"owner_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 1 lc rgb "dark-green",\
	"owner_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 1 lc rgb "brown",\
	"owner_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 1 lc rgb "orange"


set output 'owner_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "path_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 1 lc rgb "red",\
	"path_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 1 lc rgb "blue",\
	"path_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 1 lc rgb "dark-green",\
	"path_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 1 lc rgb "brown",\
	"path_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 1 lc rgb "orange"


set output 'path_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "relate_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 1 lc rgb "red",\
	"relate_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 1 lc rgb "blue",\
	"relate_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 1 lc rgb "dark-green",\
	"relate_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 1 lc rgb "brown",\
	"relate_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 1 lc rgb "orange"


set output 'relate_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "cuckoo_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 1 lc rgb "red",\
	"cuckoo_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 1 lc rgb "blue",\
	"cuckoo_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 1 lc rgb "dark-green",\
	"cuckoo_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 1 lc rgb "brown",\
	"cuckoo_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 1 lc rgb "orange"


set output 'cuckoo_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:600]
set ylabel 'Num of Replica'
plot "owner_counter.csv" every ::1 using 1:5 with lines title "Owner" lc rgb "blue",\
	"path_counter.csv" every ::1 using 1:5 with lines title "Path" lc rgb "magenta",\
	"relate_counter.csv" every ::1 using 1:5 with lines title "Relate" lc rgb "dark-green",\
	"cuckoo_counter.csv" every ::1 using 1:5 with lines title "Cuckoo" lc rgb "red",\


set output 'counter_comp.eps'
set terminal postscript eps color
replot