set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_owner.tsv" every ::2 with lines lc rgb "blue" title "Owner"

set output 'eps/occupancy_owner.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_path.tsv" every ::2 with lines lc rgb "magenta" title "Path"

set output 'eps/occupancy_path.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_relate.tsv" every ::2 with lines lc rgb "dark-green" title "Kageyama"

set output 'eps/occupancy_relate.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_cuckoo.tsv" every ::2 with lines lc rgb "red" title "Cuckoo"

set output 'eps/occupancy_cuckoo.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_owner.tsv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"occupancy_path.tsv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"occupancy_relate.tsv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"occupancy_cuckoo.tsv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'eps/occupancy_comp.eps'
set terminal postscript eps color
replot

set xrange[300:500]
set xlabel "Num of cycle"
set yrange[28000000:33000000]
set ylabel "Cumulative usage"
set key left top

plot "occupancy_owner.tsv" every ::2 with lines lw 2 lc rgb "blue" title "Owner",\
"occupancy_path.tsv" every ::2 with lines lw 2 lc rgb "magenta" title "Path",\
"occupancy_relate.tsv" every ::2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"occupancy_cuckoo.tsv" every ::2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'eps/occupancy_comp_expansion.eps'
set terminal postscript eps color
replot