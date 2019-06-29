set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:35000000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.tsv" every ::2 with lines lc rgb "blue" title "Owner"

set output 'owner_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:35000000]
set ylabel "Cumulative usage"
set key left top

plot "path_occupancy.tsv" every ::2 with lines lc rgb "magenta" title "Path"

set output 'path_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:35000000]
set ylabel "Cumulative usage"
set key left top

plot "relate_occupancy.tsv" every ::2 with lines lc rgb "dark-green" title "Kageyama"

set output 'relate_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:35000000]
set ylabel "Cumulative usage"
set key left top

plot "cuckoo_occupancy.tsv" every ::2 with lines lc rgb "red" title "Cuckoo"

set output 'cuckoo_occupancy.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:35000000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.tsv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"path_occupancy.tsv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"relate_occupancy.tsv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"cuckoo_occupancy.tsv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'occupancy_comp.eps'
set terminal postscript eps color
replot

set xrange[300:500]
set xlabel "Num of cycle"
set yrange[28000000:35000000]
set ylabel "Cumulative usage"
set key left top

plot "owner_occupancy.tsv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"path_occupancy.tsv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"relate_occupancy.tsv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"cuckoo_occupancy.tsv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'occupancy_expansion.eps'
set terminal postscript eps color
replot