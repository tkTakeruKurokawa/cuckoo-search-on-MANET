#!/bin/bash

bash runSimulator.bash > numOfData.csv

gnuplot "./result/owner.plt"
gnuplot "./result/path.plt"
gnuplot "./result/relate.plt"
gnuplot "./result/cuckoo.plt"

open ./result/owner.eps
open ./result/path.eps
open ./result/relate.eps
open ./result/cuckoo.eps

