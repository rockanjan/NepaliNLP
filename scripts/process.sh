#!/bin/bash
awk -F" " '{if(NF>4 && NF<=50) print $0}' corpus_clean.txt > /tmp/tmp1
sort /tmp/tmp1 | uniq > /tmp/tmp2
sort -R /tmp/tmp2 > corpus_clean.processed.txt;
sed -n 1,10000p corpus_clean.processed.txt > nepali_dev.txt;
sed -n 10001,20000p corpus_clean.processed.txt > nepali_test.txt;
sed -n '200001,$p' corpus_clean.processed.txt > nepali_train.txt;
