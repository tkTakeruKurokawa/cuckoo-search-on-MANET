# set xrange[0:500]
# set xlabel "Num of cycle"
# set autoscale y
# set ylabel "Avarage Storage Remaining"
# set key right bottom

# plot "remaining_owner.tsv" every ::2 using 1:2 with lines lc rgb "blue" title "Owner"
# # plot "remaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


# set output 'eps/remaining_owner.eps'
# set terminal postscript eps color
# replot


# set xrange[0:500]
# set xlabel "Num of cycle"
# set autoscale y
# set ylabel "Avarage Storage Remaining"
# set key right bottom

# plot "remaining_path.tsv" every ::2 using 1:2 with lines lc rgb "magenta" title "Path"
# # plot "remaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

# set output 'eps/remaining_path.eps'
# set terminal postscript eps color
# replot


# set xrange[0:500]
# set xlabel "Num of cycle"
# set autoscale y
# set ylabel "Avarage Storage Remaining"
# set key right bottom

# plot "remaining_relate.tsv" every ::2 using 1:2 with lines lc rgb "dark-green" title "Kageyama"
# # plot "remaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

# set output 'eps/remaining_relate.eps'
# set terminal postscript eps color
# replot


# set xrange[0:500]
# set xlabel "Num of cycle"
# set autoscale y
# set ylabel "Avarage Storage Remaining"
# set key right bottom

# plot "remaining_cuckoo.tsv" every ::2 using 1:2 with lines lc rgb "red" title "Proposed"
# # plot "remaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

# set output 'eps/remaining_cuckoo.eps'
# set terminal postscript eps color
# replot


set xrange[0:500]
set xlabel "Num of cycle"
set autoscale y
set ylabel "Avarage Storage Remaining"
set key right bottom

plot "remaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"remaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"remaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"remaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Proposed"
# plot "remaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "remaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "remaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "remaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


set output 'eps/average_storage_remaining.eps'
set terminal postscript eps color
replot

# set xrange[300:500]
# set xlabel "Num of cycle"
# set autoscale y
# set ylabel "Avarage Storage Remaining"
# set key right bottom

# plot "remaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
# "remaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
# "remaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
# "remaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Proposed"
# # plot "remaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# # "remaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# # "remaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# # "remaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


# set output 'eps/remaining_comp_expansion.eps'
# set terminal postscript eps color
# replot