set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "owner_occupancy.csv" every ::2 with lines title "Owner"

set output 'owner_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "path_occupancy.csv" every ::2 with lines title "Path"

set output 'path_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "relate_occupancy.csv" every ::2 with lines title "Relate"

set output 'relate_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "cuckoo_occupancy.csv" every ::2 with lines title "Cuckoo"

set output 'cuckoo_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:1.0]
set ylabel "Occupancy"
set key right bottom

plot "owner_occupancy.csv" every ::2 with lines title "Owner",\
"path_occupancy.csv" every ::2 with lines title "Path",\
"relate_occupancy.csv" every ::2 with lines title "Relate",\
"cuckoo_occupancy.csv" every ::2 with lines title "Cuckoo"

set output 'occupancy_comp.eps'
set terminal postscript eps color
replot
