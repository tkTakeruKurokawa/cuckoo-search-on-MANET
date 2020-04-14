#####   search cost   #####
# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "searchCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lw 1 lc rgb "blue"

# set output 'eps/searchCost_owner.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "searchCost_path.tsv" every ::2 using 1:3 with lines title "Path" lw 1 lc rgb "magenta"

# set output 'eps/searchCost_path.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "searchCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
# set output 'eps/searchCost_relate.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "searchCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lw 1 lc rgb "red"

# set output 'eps/searchCost_cuckoo.eps'
# set terminal postscript eps color
# replot

set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "searchCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"searchCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"searchCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"searchCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output 'eps/search_cost.eps'
set terminal postscript eps color
replot


#####   replication cost   #####
# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "replicationCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lw 1 lc rgb "blue"

# set output 'eps/replicationCost_owner.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "replicationCost_path.tsv" every ::2 using 1:3 with lines title "Path" lw 1 lc rgb "magenta"

# set output 'eps/replicationCost_path.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "replicationCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lw 1 lc rgb "dark-green"
	
# set output 'eps/replicationCost_relate.eps'
# set terminal postscript eps color
# replot


# set xrange [0:500]
# set xlabel 'Num of cycle'
# set autoscale y
# set ylabel 'Network Cost'
# set key left top

# plot "replicationCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lw 1 lc rgb "red"

# set output 'eps/replicationCost_cuckoo.eps'
# set terminal postscript eps color
# replot

set xrange [0:500]
set xlabel 'Num of cycle'
set autoscale y
set ylabel 'Network Cost'
set key left top

plot "replicationCost_owner.tsv" every ::2 using 1:3 with lines title "Owner" lc rgb "blue",\
	"replicationCost_path.tsv" every ::2 using 1:3 with lines title "Path" lc rgb "magenta",\
	"replicationCost_relate.tsv" every ::2 using 1:3 with lines title "Kageyama" lc rgb "dark-green",\
	"replicationCost_cuckoo.tsv" every ::2 using 1:3 with lines title "Proposed" lc rgb "red",\

set output 'eps/replication_cost.eps'
set terminal postscript eps color
replot