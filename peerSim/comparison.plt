set datafile separator ","
set xrange [0:2000]
set xlabel 'Num of cycles'
set yrange [0:1.1]
set ylabel 'Num of data'
plot './result/owner.csv' with lines

set output 'comp.eps'
set terminal postscript eps color
replot