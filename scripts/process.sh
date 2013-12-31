#!/bin/bash
shuf corpus_clean.processed.txt.cleaned.fixed.cleanedbyvocab > /tmp/nepali_shuf.txt
sed -n 1,10000p /tmp/nepali_shuf.txt > nepali_dev.txt;
sed -n 10001,20000p /tmp/nepali_shuf.txt > nepali_test.txt;
sed -n '20001,$p' /tmp/nepali_shuf.txt > /tmp/nepali_rem.txt;
cat /tmp/nepali_rem.txt wsj.txt > nepali_train.txt;

