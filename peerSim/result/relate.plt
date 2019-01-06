set datafile separator ","
set xrange [0:500]
set xlabel 'Num of cycles'
set yrange [0:2500]
set ylabel 'Num of data'
plot './result/owner.csv' using 1:3 with points

set output './result/ownereps'
set terminal postscript eps color
replot

set datafile separator ","
set xrange [0:500]
set xlabel 'Num of cycles'
set yrange [0:2500]
set ylabel 'Num of data'
plot './result/path.csv' using 1:3 with points

set output './result/path.eps'
set terminal postscript eps color
replot

set datafile separator ","
set xrange [0:500]
set xlabel 'Num of cycles'
set yrange [0:2500]
set ylabel 'Num of data'
plot './result/relate.csv' using 1:3 with points

set output './result/relate.eps'
set terminal postscript eps color
replot

set datafile separator ","
set xrange [0:500]
set xlabel 'Num of cycles'
set yrange [0:2500]
set ylabel 'Num of data'
plot './result/cuckoo.csv' using 1:3 with points

set output './result/cuckoo.eps'
set terminal postscript eps color
replot
