set xrange [0:50]
set xlabel 'Data Identifier'
set yrange [0.0:1.0]
set ylabel 'Max Rate of Data Request'

plot "normal_distribution.tsv"  with boxes title "normal distribution" lw 1 lc rgb "blue",\
"normal_distribution.tsv"  with lines title "normal distribution" lw 1 lc rgb "red"

unset table
set output 'normal.eps'
set terminal postscript eps color
replot

set xrange [0:50]
set xlabel 'Data Identifier'
set yrange [0.0:1.0]
set ylabel 'Max Rate of Data Request'

plot "power_distribution.tsv"  with boxes title "Based on Power law" lw 1 lc rgb "blue",\
"power_distribution.tsv"  with lines title "Based on Power law" lw 1 lc rgb "red"

unset table
set output 'Based on Power law.eps'
set terminal postscript eps color
replot