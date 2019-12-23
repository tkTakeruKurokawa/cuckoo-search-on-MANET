#!/bin/bash

javac distribution.java
java distribution
gnuplot "distribution.plt"