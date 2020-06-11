#!/bin/bash

type_index=1
stop_cycle=100
type="pareto"
if [ $type_index = 0 ]; then
    type="normal"
fi

# rm -rf "result$stop_cycle/"
rm "result$stop_cycle/$type/"*.tsv
rm "result$stop_cycle/$type/eps/"*.eps
java ChangeResultName $type_index $stop_cycle
java -cp "src:peersim-1.0.5.jar:jep-2.3.0.jar:djep-1.0.0.jar" peersim.Simulator src/research/config.txt
cd ./plot_configs
gnuplot "counter.plt"
gnuplot "occupancy.plt"
gnuplot "remaining.plt"
gnuplot "networkCost.plt"
cd ../