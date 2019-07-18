set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_owner.tsv" every ::2 using 1:2 with lines lc rgb "blue" title "Owner"

set output 'lowRemaining_owner.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_path.tsv" every ::2 using 1:2 with lines lc rgb "magenta" title "Path"

set output 'lowRemaining_path.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_relate.tsv" every ::2 using 1:2 with lines lc rgb "dark-green" title "Kageyama"

set output 'lowRemaining_relate.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lc rgb "red" title "Cuckoo"

set output 'lowRemaining_cuckoo.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"lowRemaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"lowRemaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"lowRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Cuckoo"


set output 'lowRemaining_comp.eps'
set terminal postscript eps color
replot

set xrange[300:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom

# plot "lowRemaining_owner.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_path.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_relate.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
# "lowRemaining_cuckoo.tsv" every ::2 using 1:2:3 with errorbars lc rgb "gray70" notitle,\
plot "lowRemaining_owner.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "blue" title "Owner",\
"lowRemaining_path.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "magenta" title "Path",\
"lowRemaining_relate.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "dark-green" title "Kageyama",\
"lowRemaining_cuckoo.tsv" every ::2 using 1:2 with lines lw 2 lc rgb "red" title "Cuckoo"

set output 'lowRemaining_comp_expansion.eps'
set terminal postscript eps color
replot