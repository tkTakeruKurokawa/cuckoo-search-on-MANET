set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_owner.tsv" every ::2 using 1:2 with lines lc rgb "blue" title "Owner"
# plot "highRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


set output 'highRemaining_owner.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_path.tsv" every ::2 using 1:2 with lines lc rgb "magenta" title "Path"
# plot "highRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

set output 'highRemaining_path.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_relate.tsv" every ::2 using 1:2 with lines lc rgb "dark-green" title "Kageyama"
# plot "highRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

set output 'highRemaining_relate.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lc rgb "red" title "Cuckoo"
# plot "highRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\

set output 'highRemaining_cuckoo.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"highRemaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"highRemaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"highRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Cuckoo"
# plot "highRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


set output 'highRemaining_comp.eps'
set terminal postscript eps color
replot

set xrange[300:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

plot "highRemaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"highRemaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"highRemaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"highRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Cuckoo"
# plot "highRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "highRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\


set output 'highRemaining_comp_expansion.eps'
set terminal postscript eps color
replot