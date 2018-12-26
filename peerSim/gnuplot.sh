#!/bin/bash

bash runSimulator.bash > numOfData.csv
gnuplot "comparison.plt"
open comparison.eps

