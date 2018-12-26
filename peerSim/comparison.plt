set datafile separator ","
set xrange [0:500]
set xlabel 'Num of cycles'
set yrange [0:2000]
set ylabel 'Num of data'
plot 'numOfData.csv'

set output 'comparison.eps'
set terminal postscript eps color
replot
