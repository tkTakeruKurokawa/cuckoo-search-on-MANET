#!/bin/bash

rm result/*.tsv
rm result/*.eps
rm result/eps/*.eps
rm result/*.csv
java -cp "src:peersim-1.0.5.jar:jep-2.3.0.jar:djep-1.0.0.jar" peersim.Simulator src/research/config.txt
cd result
gnuplot "highCounter.plt"
gnuplot "lowCounter.plt"
gnuplot "highOccupancy.plt"
gnuplot "lowOccupancy.plt"
gnuplot "highRemaining.plt"
gnuplot "lowRemaining.plt"
gnuplot "average.plt"
cd ../