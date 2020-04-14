set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "occupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

set output 'eps/occupancy_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "occupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

set output 'eps/occupancy_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "occupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'eps/occupancy_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "occupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lw 1 lc rgb "red"

set output 'eps/occupancy_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Cumulative Occupancy'
set key left top

plot "occupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"occupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"occupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"occupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

set output 'eps/occupancy_comp.eps'
set terminal postscript eps color
replot

# set xrange [350:500]
# set xlabel 'Num of cycle'
# set yrange [3000000:13000000]
set autoscale y
# set ylabel 'Cumulative Occupancy'
# plot "occupancy_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
# 	"occupancy_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
# 	"occupancy_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
# 	"occupancy_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

# set output 'eps/occupancy_comp_expansion.eps'
# set terminal postscript eps color
# replot