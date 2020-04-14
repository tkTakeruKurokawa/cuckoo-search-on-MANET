set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "networkCost_owner.tsv" every ::2  with lines title "Owner" lw 1 lc rgb "blue"

set output 'eps/networkCost_owner.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "networkCost_path.tsv" every ::2  with lines title "Path" lw 1 lc rgb "magenta"

set output 'eps/networkCost_path.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "networkCost_relate.tsv" every ::2  with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
set output 'eps/networkCost_relate.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "networkCost_cuckoo.tsv" every ::2  with lines title "Proposed" lw 1 lc rgb "red"

set output 'eps/networkCost_cuckoo.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "networkCost_owner.tsv" every ::2  with lines title "Owner" lc rgb "blue",\
	"networkCost_path.tsv" every ::2  with lines title "Path" lc rgb "magenta",\
	"networkCost_relate.tsv" every ::2  with lines title "Kageyama" lc rgb "dark-green",\
	"networkCost_cuckoo.tsv" every ::2  with lines title "Proposed" lc rgb "red",\

set output 'eps/networkCost_comp.eps'
set terminal postscript eps color
replot

# set xrange [350:500]
# set xlabel 'Num of cycle'
# set yrange [3000000:13000000]
set autoscale y
# set ylabel 'Network Cost'
# plot "networkCost_owner.tsv" every ::2  with lines title "Owner" lc rgb "blue",\
# 	"networkCost_path.tsv" every ::2  with lines title "Path" lc rgb "magenta",\
# 	"networkCost_relate.tsv" every ::2  with lines title "Kageyama" lc rgb "dark-green",\
# 	"networkCost_cuckoo.tsv" every ::2  with lines title "Proposed" lc rgb "red",\

# set output 'eps/networkCost_comp_expansion.eps'
# set terminal postscript eps color
# replot