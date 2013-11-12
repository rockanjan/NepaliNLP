#!/bin/bash
#awk -F" " '{if(NF>4 && NF<=50) print $0}' corpus_clean.txt > /tmp/tmp1
#sort /tmp/tmp1 | uniq > /tmp/tmp2
#sort -R /tmp/tmp2 > corpus_clean.processed.txt;

#merge manually tagged pos corpus, EXCEPT the test dataset (no transductive learning)
cat /data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/00ne_pos.txt.txtonly \
/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/01ne_pos.txt.txtonly \
corpus_clean.processed.txt.fixed > /tmp/corpus_clean.combined.txt;

#randomize
shuf /tmp/corpus_clean.combined.txt > corpus_clean.combined.txt;

sed -n 1,10000p corpus_clean.combined.txt > nepali_dev.txt;
sed -n 10001,20000p corpus_clean.combined.txt > nepali_test.txt;
sed -n '200001,$p' corpus_clean.combined.txt > nepali_train.txt;
