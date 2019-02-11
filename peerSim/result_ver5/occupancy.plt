set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:2650000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.csv" every ::2 with lines lc rgb "blue" title "Owner"

set output 'owner_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:2650000]
set ylabel "Cumulative usage"
set key left top

plot "path_occupancy.csv" every ::2 with lines lc rgb "magenta" title "Path"

set output 'path_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:2650000]
set ylabel "Cumulative usage"
set key left top

plot "relate_occupancy.csv" every ::2 with lines lc rgb "dark-green" title "Kageyama"

set output 'relate_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:2650000]
set ylabel "Cumulative usage"
set key left top

plot "cuckoo_occupancy.csv" every ::2 with lines lc rgb "red" title "Cuckoo"

set output 'cuckoo_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:2650000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.csv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"path_occupancy.csv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"relate_occupancy.csv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"cuckoo_occupancy.csv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'occupancy_comp.eps'
set terminal postscript eps color
replot

set xrange[300:500]
set xlabel "Num of cycle"
set yrange[2350000:2650000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.csv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"path_occupancy.csv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"relate_occupancy.csv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"cuckoo_occupancy.csv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'occupancy_expansion.eps'
set terminal postscript eps color
replot