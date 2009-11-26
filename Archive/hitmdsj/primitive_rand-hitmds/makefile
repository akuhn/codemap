# makefile for hitmds2
#
CC=gcc
#CFLAGS=-Wall -Wpadded -O4 -ffast-math -march=pentium4
CFLAGS=-Wall -pedantic -Wpadded -O2

# maby gawk on solaris and other systems
AWK=awk

LFLAGS=-lm

TARGET=hitmds2

# default target
# 
it: info mds

info:
	@echo "Compiling. Stand by. 'not initialized' warnings are alright." && echo ""

# binary compilations
# 
mds:
	$(CC) $(CFLAGS) -o $(TARGET) $(TARGET).c $(LFLAGS)


# test suite

# Euclidean
test_experiments_euc_2d:
	LC_ALL=C $(AWK) -f transpose.awk genes_endo_4824.dat | $(AWK)  'BEGIN{print 14,4824}1;END{print -2}' | ./$(TARGET) 1000 5 0 0 > genes_endo_4824T_2d_euc.dat

# Pearson corralation
test_experiments_cor_2d:
	LC_ALL=C $(AWK) -f transpose.awk genes_endo_4824.dat | $(AWK)  'BEGIN{print 14,4824}1;END{print -2}' | ./$(TARGET) 1000 5 0 1 > genes_endo_4824T_2d_cor.dat
	
# Pearson corralation (exponent 8 applied) to gene profiles (takes some minutes)
test_genes_cor8_2d:
	cat genes_endo_4824.dat | ./$(TARGET) 50 1 0 1:8 > genes_endo_4824_2d_cor8.dat

# Pearson corralation applied to modulo-8 pruned list of gene profiles (3d)
test_genes_cor_p3d:
	LC_ALL=C $(AWK) 'NF>4 && NR%8==0{nf=NF;l[++nr]=$$0}END{print nr,nf;for(i=1;i<=nr;i++)print l[i]; print -3}' genes_endo_4824.dat | ./$(TARGET) 50 1 0 1 > genes_endo_4824_p3d_cor.dat

# Spearman rank corralation for modulo-8 pruned list of gene profiles (2d)
test_genes_spr_p2d:
	echo Rank conversion takes a little before starting...; \
	LC_ALL=C $(AWK) -f tornk.awk genes_endo_4824.dat | $(AWK) 'NF>4 && NR%8==0{nf=NF;l[++nr]=$$0}END{print nr,nf;for(i=1;i<=nr;i++)print l[i]; print -2}' | ./$(TARGET) 50 1 0 2 > genes_endo_4824_p2d_spr.dat

# General Minkowski metrik :exponent (here exponent=6) (mod-8 pruned gene list)
test_genes_mink6_2d:
	LC_ALL=C $(AWK) 'NF>4 && NR%8==0{nf=NF;l[++nr]=$$0}END{print nr,nf;for(i=1;i<=nr;i++)print l[i]; print -2}' genes_endo_4824.dat | ./$(TARGET) 50 10 0 :6 > genes_endo_4824_p2d_mink6.dat
	
