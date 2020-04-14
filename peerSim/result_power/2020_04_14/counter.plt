# set xrange [0:500]
# set xlabel 'Num of cycle'
# set yrange [0.0:1.0]
# set ylabel 'Data Availability'
# plot "counter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lw 1 lc rgb "blue"

# set output 'eps/counter_owner.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set yrange [0.0:1.0]
# set ylabel 'Data Availability'
# plot "counter_path.tsv" every ::2 using 1:2 with lines title "Path" lw 1 lc rgb "magenta"

# set output 'eps/counter_path.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set yrange [0.0:1.0]
# set ylabel 'Data Availability'
# plot "counter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
# set output 'eps/counter_relate.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set yrange [0.0:1.0]
# set ylabel 'Data Availability'
# plot "counter_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lw 1 lc rgb "red"

# set output 'eps/counter_cuckoo.eps'
# set terminal postscript eps color
# replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
plot "counter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
	"counter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
	"counter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
	"counter_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

set output 'eps/data_availability.eps'
set terminal postscript eps color
replot

# set xrange [350:500]
# set xlabel 'Num of cycle'
# set yrange [0.0:1.0]
# set ylabel 'Data Availability'
# plot "counter_owner.tsv" every ::2 using 1:2 with lines title "Owner" lc rgb "blue",\
# 	"counter_path.tsv" every ::2 using 1:2 with lines title "Path" lc rgb "magenta",\
# 	"counter_relate.tsv" every ::2 using 1:2 with lines title "Kageyama" lc rgb "dark-green",\
# 	"counter_cuckoo.tsv" every ::2 using 1:2 with lines title "Proposed" lc rgb "red",\

# set output 'eps/counter_comp_expansion.eps'
# set terminal postscript eps color
# replot