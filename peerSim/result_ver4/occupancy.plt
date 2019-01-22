set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "owner_occupancy.csv" every ::2 with lines lc rgb "magenta" title "Owner"

set output 'owner_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "path_occupancy.csv" every ::2 with lines lc rgb "blue" title "Path"

set output 'path_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "relate_occupancy.csv" every ::2 with lines lc rgb "dark-green" title "Relate"

set output 'relate_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "cuckoo_occupancy.csv" every ::2 with lines lc rgb "red" title "Cuckoo"

set output 'cuckoo_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "owner_occupancy.csv" every ::2 with lines lw 2 lc rgb "magenta" title "Owner",\
"path_occupancy.csv" every ::2 with lines lw 2 lc rgb "blue" title "Path",\
"relate_occupancy.csv" every ::2 with lines lw 2 lc rgb "dark-green" title "Relate",\
"cuckoo_occupancy.csv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'occupancy_comp.eps'
set terminal postscript eps color
replot