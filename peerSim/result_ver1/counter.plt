set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "owner_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 3 lc rgb "light-red",\
	"owner_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 0.5 lc rgb "yellow",\
	"owner_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 3 lc rgb "green",\
	"owner_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 0.5 lc rgb "skyblue",\
	"owner_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 0.5 lc rgb "orange"


set output 'owner_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "path_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 3 lc rgb "light-red",\
	"path_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 0.5 lc rgb "yellow",\
	"path_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 3 lc rgb "green",\
	"path_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 0.5 lc rgb "skyblue",\
	"path_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 0.5 lc rgb "orange"


set output 'path_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "relate_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 3 lc rgb "light-red",\
	"relate_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 0.5 lc rgb "yellow",\
	"relate_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 3 lc rgb "green",\
	"relate_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 0.5 lc rgb "skyblue",\
	"relate_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 0.5 lc rgb "orange"


set output 'relate_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "cuckoo_counter.csv" every ::1 using 1:2 with lines title "Data5" lw 3 lc rgb "light-red",\
	"cuckoo_counter.csv" every ::1 using 1:3 with lines title "Data15" lw 0.5 lc rgb "yellow",\
	"cuckoo_counter.csv" every ::1 using 1:4 with lines title "Data25" lw 3 lc rgb "green",\
	"cuckoo_counter.csv" every ::1 using 1:5 with lines title "Data35" lw 0.5 lc rgb "skyblue",\
	"cuckoo_counter.csv" every ::1 using 1:6 with lines title "Data45" lw 0.5 lc rgb "orange"


set output 'cuckoo_counter.eps'
set terminal postscript eps color
replot

set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [-50:1600]
set ylabel 'Num of Replica'
plot "owner_counter.csv" every ::1 using 1:3 with lines title "Owner" lw 3 lc rgb "yellow",\
	"path_counter.csv" every ::1 using 1:3 with lines title "Path" lc rgb "skyblue",\
	"relate_counter.csv" every ::1 using 1:3 with lines title "Relate" lc rgb "green",\
	"cuckoo_counter.csv" every ::1 using 1:3 with lines title "Cuckoo" lc rgb "light-red",\


set output 'counter_comp.eps'
set terminal postscript eps color
replot