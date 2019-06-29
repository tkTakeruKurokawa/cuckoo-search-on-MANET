#!/bin/bash

rm result/*.tsv
rm result/*.eps
java -cp "src:peersim-1.0.5.jar:jep-2.3.0.jar:djep-1.0.0.jar" peersim.Simulator src/research/config.txt
cd result
gnuplot "counter.plt"
gnuplot "occupancy.plt"
cd ../