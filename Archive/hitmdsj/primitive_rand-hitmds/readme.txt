
            High Througput Multi-Dimensional Scaling II (HiT-MDS-2)
            =======================================================


Package written in ANSI-C implementing multidimensional scaling 
according to best distance matrix reconstruction, based on 
signs of derivatives (previously: based on Fisher's z').

Copyright (C) 2007  Marc Strickert (stricker@ipk-gatersleben.de)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.



Installation and Requirements
-----------------------------

You need to have a command line such as bash available.
After extraction, go to destination directory type make.
This should compile the c source, exhibiting one or two 
warnings that can be safely ignored.

Consider makefile optimization like, such as (for pentium4)
  CFLAGS=-Wall -Wpadded -O4 -ffast-math -mcpu=pentium4
Also make sure that AWK points to gawk,
gawk version >= 3.1 needed for asort() function.

HiT-MDS has been successfully tested on 
Suse 10 Linux 64 bit,
Solaris 5.9,
MacOS X with Fink extensions (caution: Fink's gawk 3.1.2 has a memory bug),
Windows XP with cygwin.

A system with 512MB RAM should suffice for processing
data sets up to 5,000 genes.



Quick Start
-----------

The following make targets are available.
For example, if You type 
> make test_genes_cor8_2d
this produces the embedding shown in the BMC Bioinformatics manuscript
and should run on most platforms. This will take some time (minutes).
Note that PCA rotation of scatter plot must be done externally;
see stdscatter.[rm] for R and MATLAB/Octave implementations.
All other targets require gawk, Spearman correlation gawk version >= 3.1.
The environment LC_ALL=C sets locale to C standard, important
for consistent decimal point handling, for example, on German systems.

The list of available target is:

# Euclidean embedding of 14 experiments (endosperm 0DAF-26DAF in steps of 2DAF)
make test_experiments_euc_2d

# Pearson corralation
#   Embed experiments, i.e. the transposed gene expression list.
#   Point order is 0DAF to 26DAF in steps of 2DAF. Embedding differs
#   from publication because only one experimental series is considered.
make test_experiments_cor_2d

# Pearson corralation (exponent 8 applied) to gene profiles (takes some minutes)
make test_genes_cor8_2d

# Pearson corralation applied to modulo-8 pruned list of gene profiles (3d)
make test_genes_cor_p3d

# Spearman rank corralation for modulo-8 pruned list of gene profiles (2d)
make test_genes_spr_p2d

# General Minkowski metrik :exponent (here exponent=6) (mod-8 pruned gene list)
make test_genes_mink6_2d



Example Run
-----------

> make test_genes_cor8_2d
cat genes_endo_4824.dat | ./hitmds2 50 1 0 1:8 > genes_endo_4824_2d_cor8.dat
10.00%: 0.693252        (r = 0.768492)
20.00%: 0.422655        (r = 0.838398)
30.00%: 0.410739        (r = 0.841931)
40.00%: 0.40626         (r = 0.843271)
50.00%: 0.404366        (r = 0.843839)
60.00%: 0.392854        (r = 0.847302)
70.00%: 0.386037        (r = 0.849401)
80.00%: 0.381142        (r = 0.850905)
90.00%: 0.37823         (r = 0.851803)
100.00%: 0.377287       (r = 0.852094)

The three output columns are:
progress[%]   stress-value   r-correlation(original,reconstruction)

For 'difficult' data or inappropriate parameters,
NaN values (not a number) might occur and slow
down computing time. In that case, stop, change parameters, and restart.



Input Data
----------

The input matrix is a text file, separated by space characters (not tabs).
Numerics might be scientific like 1.23e-4; MATLAB ASCII files can
thus be processed, for example.

The header with optional leading # contains number of subsequent 
data vectors (lines) and the dimension of data vectors.

The footer usually contains a negative number, indicating the target
dimension. For example, -2 says 2D embedding, randomly initialized.
Again, an optional # can precede this number, for comment based masking.
If a positive number d is given, a set of >preinitialized< points
of dimension d must follow, containing same number as input vectors.

The general data structure is
  [#]number_data_vectors number_dimensions
  Space-separated-text-file,containing_data_vectors_in_rows
  [#][-]target_dimension
Such stream can be piped to the hitmds2 binary, as demonstrated in 
the makefile, can be assembled at runtime by awk or perl scripts.

Kill (CTRL-C) will be caught by program to still write current results.


Trouble Shooting
----------------

Generally, no problems should occur, but in case...

Nowadays the text input not be sensitive to dos/unix file format,
however, if problems occur, consider carriage return (CR) and
line feed (LF) characters to be a reason. 
Consider tools like dos2unix, unix2dos, tr [-d], or 
a text editor like vim with command :set fileformat.

Data colum separator must be one or more blank (space) characters.

kill command against hitmds2 and CTRL-C works in cygwin/Linux;
results, so far, are written to disk in case of SIGTERM.
Windows task manager just terminates without saving intermediate
results, cygwin's kill works fine.



That's it. If You encounter severe problems, please let me know.

Good luck,
Marc


(stricker@ipk-gatersleben.de)
(Thu May  3 14:25:37    2007)
// EOF
