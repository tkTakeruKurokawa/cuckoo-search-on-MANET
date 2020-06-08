set xrange [0:50]
set xlabel 'Data Identifier'
set yrange [0.0:1.0]
set ylabel 'Max Rate of Data Request'

plot "normal_distribution.tsv"  with boxes notitle lw 1 lc rgb "blue",\
"normal_distribution.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'normal_distribution.eps'
set terminal postscript eps color
replot

set xrange [0:50]
set xlabel 'Data Identifier'
set yrange [0.0:1.0]
set ylabel 'Max Rate of Data Request'

plot "pareto_distribution.tsv"  with boxes notitle  lw 1 lc rgb "blue",\
"pareto_distribution.tsv"  with lines notitle  lw 1 lc rgb "red"

unset table
set output 'pareto_distribution.eps'
set terminal postscript eps color
replot