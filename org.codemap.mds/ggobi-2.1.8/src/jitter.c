/* jitter.c */
/*
 * ggobi
 * Copyright (C) AT&T, Duncan Temple Lang, Dianne Cook 1999-2005
 *
 * ggobi is free software; you may use, redistribute, and/or modify it
 * under the terms of the Common Public License, which is distributed
 * with the source code and displayed on the ggobi web site, 
 * www.ggobi.org.  For more information, contact the authors:
 *
 *   Deborah F. Swayne   dfs@research.att.com
 *   Di Cook             dicook@iastate.edu
 *   Duncan Temple Lang  duncan@wald.ucdavis.edu
 *   Andreas Buja        andreas.buja@wharton.upenn.edu
*/

/*
 * Contains jittering routines for tform; see missing.c for the
 * jittering routines for missing data.
*/

#include <stdlib.h>
#include <math.h>

#include <gtk/gtk.h>
#include "vars.h"
#include "externs.h"

gfloat
jitter_randval (gint type)
{
/*
 * generate a random value.
*/
  gdouble drand;
  static gdouble dsave;
  static gboolean isave = false;

  if (type == UNIFORM) {
    drand = randvalue ();
    /*
     * Center and scale to [-1, 1]
     */
    drand = (drand - .5) * 2;

  }
  else if (type == NORMAL) {

    gboolean check = true;
    gdouble d, dfac;

    if (isave) {
      isave = false;
      /* prepare to return the previously saved value */
      drand = dsave;
    }
    else {
      isave = true;
      while (check) {

        rnorm2 (&drand, &dsave);
        d = drand * drand + dsave * dsave;

        if (d < 1.0) {
          check = false;
          dfac = sqrt (-2. * log (d) / d);
          drand = drand * dfac;
          dsave = dsave * dfac;
        }
      }                         /* end while */
    }                           /* end else */

    /*
     * Already centered; scale to approximately [-1, 1]
     */
    drand = (drand / 3.0);
  }
  return ((gfloat) drand);
}

void
rejitter (gint * selected_cols, gint nselected_cols, GGobiData * d,
          ggobid * gg)
{
  gint i, j, k, m;
  greal frand, fworld, fjit;
  greal precis = (gfloat) PRECISION1;
  vartabled *vt;

  g_assert (d->jitdata.nrows == d->nrows);
  g_assert (d->jitdata.ncols == d->ncols);

  for (j = 0; j < nselected_cols; j++) {
    k = selected_cols[j];
    vt = vartable_element_get (k, d);

    for (i = 0; i < d->nrows_in_plot; i++) {
      m = d->rows_in_plot.els[i];
      /*-- jitter_one_value (m, k); --*/

      frand = (greal) jitter_randval (d->jitter.type) * precis;

      /*
       * The world.vals used here is already jittered:
       * subtract out the previous jittered value ...
       */
      if (d->jitter.convex) {
        fworld = d->world.vals[m][k] - d->jitdata.vals[m][k];
        fjit = (greal) vt->jitter_factor * (frand - fworld);
      }
      else
        fjit = vt->jitter_factor * frand;

      d->jitdata.vals[m][k] = fjit;
    }
  }
  tform_to_world (d, gg);
  displays_tailpipe (FULL, gg);
}


void
jitter_value_set (gfloat value, GGobiData * d, ggobid * gg)
{
  GtkWidget *tree_view =
    get_tree_view_from_object (G_OBJECT (gg->jitter_ui.window));
  gint *vars;                   // = (gint *) g_malloc (d->ncols * sizeof(gint));
  gint nvars;
  gint j;
  vartabled *vt;

  vars = get_selections_from_tree_view (tree_view, &nvars);

  for (j = 0; j < nvars; j++) {
    vt = vartable_element_get (vars[j], d);
    vt->jitter_factor = value;
  }

  g_free ((gpointer) vars);
}
