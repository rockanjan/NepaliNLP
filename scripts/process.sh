#!/bin/bash
#awk -F" " '{if(NF>4 && NF<=50) print $0}' corpus_clean.txt > /tmp/tmp1
#sort /tmp/tmp1 | uniq > /tmp/tmp2
#sort -R /tmp/tmp2 > corpus_clean.processed.txt;

#merge manually tagged pos corpus, EXCEPT the test dataset (no transductive learning)
cat /data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/00ne_pos.txt.txtonly \
/data/nepalicorpus/new_submissions_parallel_corpus_project_Nepal/processed/01ne_pos.txt.txtonly \
corpus_clean.processed.txt > /tmp/corpus_clean.combined.txt;

#randomize
shuf /tmp/corpus_clean.combined.txt > corpus_clean.combined.txt;

echo "after corpus_clean.combined.txt, use java program CleanCorpus to create corpus_clean.combined.txt.cleaned"

