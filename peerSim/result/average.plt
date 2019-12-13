set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0.0:1.0]
set ylabel 'Data Availability'
set table "average_Availability.tsv"

plot "< paste highCounter_owner.tsv lowCounter_owner.tsv" every::2 using 1:(($2+$6)/2) with lines lc rgb "blue" title "Owner",\
"< paste highCounter_path.tsv lowCounter_path.tsv" every::2 using 1:(($2+$6)/2) with lines lc rgb "magenta" title "Path",\
"< paste highCounter_relate.tsv lowCounter_relate.tsv" every::2 using 1:(($2+$6)/2) with lines lc rgb "dark-green" title "Kageyama",\
"< paste highCounter_cuckoo.tsv lowCounter_cuckoo.tsv" every::2 using 1:(($2+$6)/2) with lines lc rgb "red" title "Cuckoo"

unset table
set output 'eps/average_Availability.eps'
set terminal postscript eps color
replot


set xrange[0:500]
set xlabel "Num of cycle"
set yrange[0:100]
set ylabel "Avarage Storage Remaining"
set key left bottom
set table "average_Remaining.tsv"


plot "< paste highRemaining_owner.tsv lowRemaining_owner.tsv" every::2 using 1:(($2+$5)/2) with lines lc rgb "blue" title "Owner",\
"< paste highRemaining_path.tsv lowRemaining_path.tsv" every::2 using 1:(($2+$5)/2) with lines lc rgb "magenta" title "Path",\
"< paste highRemaining_relate.tsv lowRemaining_relate.tsv" every::2 using 1:(($2+$5)/2) with lines lc rgb "dark-green" title "Kageyama",\
"< paste highRemaining_cuckoo.tsv lowRemaining_cuckoo.tsv" every::2 using 1:(($2+$5)/2) with lines lc rgb "red" title "Cuckoo"

unset table
set output 'eps/average_Remaining.eps'
set terminal postscript eps color
replot


set xrange [0:500]
set xlabel 'Num of cycle'
set yrange [0:3000000]
set ylabel 'Cumulative Occupancy'
set key left top
set table "average_Occupancy.tsv"

plot "< paste highOccupancy_owner.tsv lowOccupancy_owner.tsv" every::2 using 1:(($2+$4)/2) with lines lc rgb "blue" title "Owner",\
"< paste highOccupancy_path.tsv lowOccupancy_path.tsv" every::2 using 1:(($2+$4)/2) with lines lc rgb "magenta" title "Path",\
"< paste highOccupancy_relate.tsv lowOccupancy_relate.tsv" every::2 using 1:(($2+$4)/2) with lines lc rgb "dark-green" title "Kageyama",\
"< paste highOccupancy_cuckoo.tsv lowOccupancy_cuckoo.tsv" every::2 using 1:(($2+$4)/2) with lines lc rgb "red" title "Cuckoo"

unset table
set output 'eps/average_Occupancy.eps'
set terminal postscript eps color
replot