/*
 * hitmds2.h
 *
 * Multidimensional Scaling according to best distance matrix reconstruction.
 * Based on Fisher's z
 * 
 *
 *   Copyright (C) 2006  Marc Strickert (stricker@ipk-gatersleben.de)
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   A copy of the GNU General Public License (gp_license.txt) comes
 *   along with this program. (Free Software Foundation, Inc.)
 *
 */ 

#ifndef __MDS_H
#define __MDS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <limits.h>
#include <math.h>
#include <float.h>
#include <signal.h>


#define SCANFMT "%lf"

/* linear learning rate annealing at . times cycles */
#define START_ANNEALING_RATIO .5

#define EPS 1e-16

#define frand() ((double) rand() / (RAND_MAX+1.0))
#define irand(X) ((int)((double)(X) * frand()))

#define error(X) fprintf(stderr, (X)), main_bye(), exit(1)

#define ABS(X) (((X) > 0) ? (X) : -(X))

/* address matrix components */
#define D(mat,i,j) (*(mat + ( \
  (i<j) ? (j - 1 + ((((pattern_length<<1) - i - 3) * i) >> 1)) \
        : ((i==j) ? matsize \
                  : (i - 1 + ((((pattern_length<<1) - j - 3) * j) >> 1))) \
    )))

void main_bye(void);

static int stop_calculation = 0;
static double (*distance)(int dim, double *d1, double *d2);

#define inline __inline

/* type of distance matrix elements */
typedef float FLT;

int correlation_exponent,/* (1/r^2)^k */
    pattern_length,      /* Length of data series */
    pattern_dimension,   /* pattern dimension     */
    matsize,             /* number of distance matrix elements */
    target_dim,          /* dimension of target space */
    printqual,           /* flag to rate embedding qualities (last column) */
    matrix_input;       /* != 0 if data given as distance matrix */

double **pattern,        /* contains float data from STDIN */
       **points,
       points_distmat_mean,
       points_distmat_mixed,
       points_distmat_mono,
       pattern_distmat_var_sum,
       pattern_distmat_mean,
       correps;          /* correlation epsilon avoiding 1/0 */

/* save memory space */
FLT  *pattern_distmat,
     *points_distmat;


int *shuffle_index;      /* helper for data shuffling */


unsigned randomize() {

  int srd, tmp;  /* auto-variable: not initialized -> random */
                 /* Compiler Warnings are welcome here ! */
  time_t t;
  struct tm *ts;

  time(&t);
  ts = localtime(&t);

  tmp = ts->tm_mday | (ts->tm_min << 6) | (ts->tm_sec << 12);

  if(srd == 0)
    srd |= tmp;
  else
    srd *= tmp;

  srand((unsigned)srd);

  return (unsigned)srd;
}

#endif
