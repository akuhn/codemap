/*-- sphere.c --*/
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

#include <math.h>
#include <stdlib.h>

#include <gtk/gtk.h>
#include "vars.h"
#include "externs.h"


/*-------------------------------------------------------------------------*/
/*      dynamic memory allocation routines                                 */
/*-------------------------------------------------------------------------*/

void
sphere_init (GGobiData * d)
{
  vectori_init_null (&d->sphere.vars);
  vectori_init_null (&d->sphere.pcvars);
  vectorf_init_null (&d->sphere.eigenval);

  arrayd_init_null (&d->sphere.eigenvec);
  arrayf_init_null (&d->sphere.vc);

  vectorf_init_null (&d->sphere.tform_mean);
  vectorf_init_null (&d->sphere.tform_stddev);

  d->sphere.vars_stdized = TRUE;
}

void
sphere_free (GGobiData * d)
{
  /*-- don't free d->sphere.pcvars, because I need it to check history --*/

  vectori_free (&d->sphere.vars);
  vectorf_free (&d->sphere.eigenval);

  arrayd_free (&d->sphere.eigenvec, 0, 0);
  arrayf_free (&d->sphere.vc, 0, 0);

  vectorf_free (&d->sphere.tform_mean);
  vectorf_free (&d->sphere.tform_stddev);
}

void
sphere_malloc (gint nc, GGobiData * d, ggobid * gg)
{
  if (d->sphere.vars.nels != 0)
    sphere_free (d);

  if (nc > 0) {
    vectori_alloc_zero (&d->sphere.vars, nc);
    vectorf_alloc_zero (&d->sphere.eigenval, nc);

    arrayd_alloc_zero (&d->sphere.eigenvec, nc, nc);
    arrayf_alloc_zero (&d->sphere.vc, nc, nc);

    vectorf_alloc_zero (&d->sphere.tform_mean, nc);
    vectorf_alloc_zero (&d->sphere.tform_stddev, nc);
  }
}

/*-------------------------------------------------------------------------*/
/*         test the number of variables sphered last time                  */
/*-------------------------------------------------------------------------*/

/*
 * before sphering 
 * if (npcvars > 0 && npcvars != npcs) {
 *   delete the variables in sphere.pcvars (or just as many as needed?)
 *   add npcs new variables (or ditto?); populate pcvars 
 * } else if (sphere.npcvars == 0) {
 *   add npcs new variables; populate pcvars
 * }
*/
gboolean
spherize_set_pcvars (GGobiData * d, ggobid * gg)
{
  gint ncols_prev = d->ncols;
  gint j, k;
  gchar *lbl;
  /*-- for newvar_add.. the variable notebooks --*/
  gchar *vname;
  gdouble *dtmp;
  gboolean succeeded = true;

  /*-- for updating the tree view --*/
  GtkWidget *tree_view = gg->sphere_ui.tree_view;
  GtkTreeModel *model = gtk_tree_view_get_model (GTK_TREE_VIEW (tree_view));
  GtkTreeIter iter;
  /*-- --*/

  if (d->sphere.npcs == 0)
    return false;

  /*-- the null case: sphering for the first time --*/
  if (d->sphere.pcvars.els == NULL || d->sphere.pcvars.nels == 0) {
    vectori_realloc (&d->sphere.vars_sphered, d->sphere.vars.nels);
    vectori_copy (&d->sphere.vars, &d->sphere.vars_sphered);  /* from, to */

    vectori_realloc (&d->sphere.pcvars, d->sphere.npcs);

    dtmp = (gdouble *) g_malloc0 (d->nrows * sizeof (gdouble));
    for (j = 0; j < d->sphere.npcs; j++) {
      vname = g_strdup_printf ("PC%d", j + 1);
      newvar_add_with_values (dtmp, d->nrows, vname,
                              real, 0, (gchar **) NULL, (gint *) NULL,
                              (gint *) NULL, d);
      g_free (vname);
    }
    g_free (dtmp);

    for (j = ncols_prev, k = 0; j < d->ncols; j++) {
      d->sphere.pcvars.els[k++] = j;
    }

/*
 * sphering after the first time
*/

  /*-- if the number hasn't changed --*/
  }
  else if (d->sphere.pcvars.nels == d->sphere.npcs) {

    if (d->sphere.vars_sphered.nels != d->sphere.vars.nels)
      vectori_realloc (&d->sphere.vars_sphered, d->sphere.vars.nels);
    vectori_copy (&d->sphere.vars, &d->sphere.vars_sphered);  /* from, to */

  /*-- if the number has grown --*/
  }
  else if (d->sphere.pcvars.nels < d->sphere.npcs) {
    /*-- add just the additional required variables? --*/

    /*-- try deleting them all and starting fresh? --*/
    if (delete_vars (d->sphere.pcvars.els, d->sphere.pcvars.nels, d)) {

      ncols_prev = d->ncols;

      vectori_realloc (&d->sphere.vars_sphered, d->sphere.vars.nels);
      vectori_copy (&d->sphere.vars, &d->sphere.vars_sphered);  /* from, to */

      vectori_realloc (&d->sphere.pcvars, d->sphere.npcs);

      /*-- variable labels updated at the end of this function;
            data updated when sphere_apply_cb calls spherize_data --*/
      clone_vars (d->sphere.vars.els, d->sphere.npcs, d);


      for (j = ncols_prev, k = 0; j < d->ncols; j++)
        d->sphere.pcvars.els[k++] = j;

    }
    else {
      succeeded = false;
    }

  /*-- if the number has decreased --*/
  }
  else if (d->sphere.pcvars.nels > d->sphere.npcs) {
    /*-- delete the last few variables --*/
    gint ncols = d->sphere.pcvars.nels - d->sphere.npcs;
    gint *cols = (gint *) g_malloc (ncols * sizeof (gint));
    for (j = d->sphere.pcvars.nels - 1, k = ncols - 1; j >= d->sphere.npcs;
         j--)
      cols[k--] = d->sphere.pcvars.els[j];

    if (delete_vars (cols, ncols, d)) {

      /*-- then behave as above, when the lengths were the same --*/
      if (d->sphere.vars_sphered.nels != d->sphere.vars.nels)
        vectori_realloc (&d->sphere.vars_sphered, d->sphere.vars.nels);

      /*-- should I also realloc pcvars? --*/
      vectori_realloc (&d->sphere.pcvars, d->sphere.npcs);

      vectori_copy (&d->sphere.vars, &d->sphere.vars_sphered);  /* from, to */
    }
    else
      succeeded = false;

    g_free (cols);
  }

  if (succeeded) {

    /*-- update the variable labels --*/
    for (k = 0; k < d->sphere.pcvars.nels; k++) {
      j = d->sphere.pcvars.els[k];
      lbl = g_strdup_printf ("PC%d", (k + 1));
      
      ggobi_data_set_col_name (d, j, lbl);
      
      varpanel_label_set (j, d);   /*-- checkboxes --*/
      varcircle_label_set (j, d);  /*-- variable circles --*/
      g_free (lbl);
    }

    /*-- clear the model --*/
    gtk_list_store_clear (GTK_LIST_STORE (model));
    /*-- add the new labels to the 'sphered variables' tree view --*/
    for (j = 0; j < d->sphere.vars_sphered.nels; j++) {
      gtk_list_store_append (GTK_LIST_STORE (model), &iter);
      gtk_list_store_set (GTK_LIST_STORE (model), &iter, 0, ggobi_data_get_col_name(d, d->sphere.vars_sphered.els[j]), -1);
    }
  }

  return succeeded;
}

/*-------------------------------------------------------------------------*/
/*      get and set the number of principal components;                    */
/*      changed when the spin button is reset                              */
/*-------------------------------------------------------------------------*/

void
pca_diagnostics_set (GGobiData * d, ggobid * gg)
{
/*
 * Compute and set the total variance and the condition number
 * of the principal components analysis
*/
  gint j;
  gfloat ftmp1 = 0.0, ftmp2 = 0.0;
  gfloat firstpc, lastpc;

  if (d == NULL || d->sphere.npcs <= 0
      || d->sphere.eigenval.nels < d->sphere.npcs)
    return;

  firstpc = d->sphere.eigenval.els[0];
  lastpc = d->sphere.eigenval.els[d->sphere.npcs - 1];

  for (j = 0; j < d->sphere.npcs; j++)
    ftmp1 += d->sphere.eigenval.els[j];
  for (j = 0; j < d->sphere.vars.nels; j++)
    ftmp2 += d->sphere.eigenval.els[j];

  if (ftmp2 != 0)
    sphere_variance_set (ftmp1 / ftmp2, d, gg);
  else
    sphere_variance_set (-999.0, d, gg);
  if (lastpc != 0)
    sphere_condnum_set (firstpc / lastpc, gg);
  else
    sphere_condnum_set (-999.0, gg);
}

void
sphere_npcs_set (gint n, GGobiData * d, ggobid * gg)
{
  d->sphere.npcs = n;
  if (!scree_mapped_p (gg)) {
    return;
  }

  if (d->sphere.npcs < 1) {
    quick_message ("Need to choose at least 1 PC.", false);
    sphere_enable (false, gg);

  }
  else if (d->sphere.npcs > d->sphere.vars.nels) {
    gchar *msg = g_strdup_printf ("Need to choose at most %d PCs.\n",
                                  d->sphere.vars.nels);
    quick_message (msg, false);
    sphere_enable (false, gg);
    g_free (msg);

  }
  else {
    pca_diagnostics_set (d, gg);
    sphere_enable (true, gg);
  }
}

gint
npcs_get (GGobiData * d, ggobid * gg)
{
  return d->sphere.npcs;
}

/*-------------------------------------------------------------------------*/
/*    get and set the sphered variables:                                   */
/*    the user chooses the first npcs of these as principal components     */
/*-------------------------------------------------------------------------*/

void
spherevars_set (ggobid * gg)
{
  gint j, nvars, *vars;
  GGobiData *d;
  GtkWidget *tree_view;

  if (gg->sphere_ui.window == NULL) {
    d = gg->current_display->d;
    if (d == NULL)
      return;
    vars = (gint *) g_malloc (d->ncols * sizeof (gint));
    nvars = 0;  /*-- initially, no variables are selected --*/
  }
  else {
    tree_view = get_tree_view_from_object (G_OBJECT (gg->sphere_ui.window));
    if (tree_view == NULL)
      return;
    d = (GGobiData *) g_object_get_data (G_OBJECT (tree_view), "datad");
    //vars = (gint *) g_malloc (d->ncols * sizeof(gint));
    vars = get_selections_from_tree_view (tree_view, &nvars);
  }

  if (d->sphere.vars.els == NULL || d->sphere.vars.nels != nvars) {
    sphere_malloc (nvars, d, gg);
  }

  for (j = 0; j < nvars; j++)
    d->sphere.vars.els[j] = vars[j];

  /*-- update the "vars stdized?" text entry --*/
  /*  vars_stdized_send_event (d, gg); */

  /*-- reset the spinner so that its max is the number of sphered vars --*/
  sphere_npcs_range_set (nvars, gg);

  g_free (vars);
}

/*-------------------------------------------------------------------------*/
/*    eigenvector and eigenvalues routines                                 */
/*-------------------------------------------------------------------------*/

void
eigenvals_get (gfloat * els, GGobiData * d)
{
  gint j;
  for (j = 0; j < d->sphere.vars.nels; j++)
    els[j] = d->sphere.eigenval.els[j];
}

void
eigenval_zero (GGobiData * d)
{
  vectorf_zero (&d->sphere.eigenval);
}

void
eigenvec_zero (GGobiData * d, ggobid * gg)
{
  arrayd_zero (&d->sphere.eigenvec);
}

/*
 * this routine overwrites the var-cov matrix of the old active variables
 * with the new active variables. then this matrix is put through
 * singular value decomposition and overwritten with the matrix of
 * eigenvectors, and the eigenvalues are returned in a.
*/
void
eigenvec_set (GGobiData * d, ggobid * gg)
{
  gint i, j;
  gint nels = d->sphere.vars.nels;
  gdouble **eigenvec = d->sphere.eigenvec.vals;
  gfloat **vc = d->sphere.vc.vals;

  for (i = 0; i < nels; i++)
    for (j = 0; j < nels; j++)
      eigenvec[i][j] = (gdouble) vc[i][j];

}


/*-------------------------------------------------------------------------*/
/*       variance-covariance matrix routines                               */
/*-------------------------------------------------------------------------*/

void
sphere_varcovar_set (GGobiData * d, ggobid * gg)
{
  gint i, j, k, m, var;
  gfloat tmpf = 0.;
  gint n = d->nrows_in_plot;
  gfloat *tform_mean = d->sphere.tform_mean.els;
  gfloat *tform_stddev = d->sphere.tform_stddev.els;

  for (k = 0; k < d->sphere.vars.nels; k++) {
    var = d->sphere.vars.els[k];

    g_assert (var < d->ncols);
    g_assert (k < d->sphere.tform_mean.nels);

/*
 * This may not be necessary:  isn't tform_mean already
 * stored in vartabled?  dfs ...  Yes, but Andreas thinks
 * maybe it shouldn't be.
*/
    tmpf = 0.;
    for (i = 0; i < n; i++)
      tmpf += d->tform.vals[d->rows_in_plot.els[i]][var];
    tform_mean[k] = tmpf / ((gfloat) n);
  }

  for (k = 0; k < d->sphere.vc.ncols; k++) {
    for (j = 0; j < d->sphere.vc.ncols; j++) {
      tmpf = 0.;
      for (m = 0; m < n; m++) {
        i = d->rows_in_plot.els[m];
        tmpf = tmpf +
          (d->tform.vals[i][d->sphere.vars.els[k]] - tform_mean[k]) *
          (d->tform.vals[i][d->sphere.vars.els[j]] - tform_mean[j]);
      }
      tmpf /= ((gfloat) (n - 1));
      d->sphere.vc.vals[j][k] = tmpf;
      if (j == k)
        tform_stddev[k] = (gfloat) sqrt ((gdouble) tmpf);
    }
  }

  if (d->sphere.vars_stdized) {
    for (k = 0; k < d->sphere.vc.ncols; k++)
      for (j = 0; j < d->sphere.vc.ncols; j++)
        d->sphere.vc.vals[j][k] = d->sphere.vc.vals[j][k] /
          (tform_stddev[j] * tform_stddev[k]);
  }
}

/*
 * returns true if the nxn variance-covariance matrix 'matrx'
 * is within tol of being equal to the identity matrix
*/
gboolean
vc_identity_p (gdouble ** matrx, gint n)
{
  gint i, j;
  gboolean retn_val = true;
  gfloat tol = 0.001;

  for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
      if ((i == j) && (fabs ((1.000 - matrx[i][j])) > tol)) {
        retn_val = false;
        break;
      }
      else if ((i != j) && (fabs (matrx[i][j]) > tol)) {
        retn_val = false;
        break;
      }
    }
  }
  return (retn_val);
}

/*-------------------------------------------------------------------------*/
/*       Here's the beef:  sphering routines                               */
/*-------------------------------------------------------------------------*/

/*
 * As long as eigenvec is not equal to the identity matrix, 
 * perform a singular value decomposition.
*/
gboolean
sphere_svd (GGobiData * d, ggobid * gg)
{
  gint i, j, k, rank;
  gint nels = d->sphere.vars.nels;
  gdouble **eigenvec = d->sphere.eigenvec.vals;
  /*  gfloat **eigenvec = d->sphere.vc.vals; */
  gfloat *eigenval = d->sphere.eigenval.els;

  gboolean vc_equals_I = vc_identity_p (eigenvec, nels);
  paird *pairs = (paird *) g_malloc (nels * sizeof (paird));
  /*-- scratch vector and array --*/
  gfloat *e = (gfloat *) g_malloc (nels * sizeof (gfloat));
  gdouble **b = (gdouble **) g_malloc (nels * sizeof (gdouble *));

  for (j = 0; j < nels; j++)
    b[j] = (gdouble *) g_malloc0 (nels * sizeof (gdouble));

  if (!vc_equals_I) {
    eigenval_zero (d);  /*-- zero out the vector of eigenvalues --*/
    dsvd (eigenvec, nels, nels, d->sphere.eigenval.els, b);
    for (j = 0; j < nels; j++) {
      eigenval[j] = (gfloat) sqrt ((gdouble) eigenval[j]);
    }
  }

  /*-- obtain ranks to use in sorting eigenvals and eigenvec --*/
  for (i = 0; i < d->sphere.vars.nels; i++) {
    pairs[i].f = (gfloat) eigenval[i];
    pairs[i].indx = i;
  }
  qsort ((gchar *) pairs, nels, sizeof (paird), pcompare);

  /*-- sort the eigenvalues and eigenvectors into temporary arrays --*/
  for (i = 0; i < nels; i++) {
    k = (nels - i) - 1;  /*-- to reverse the order --*/
    rank = pairs[i].indx;
    e[k] = eigenval[rank];
    for (j = 0; j < nels; j++)
      b[j][k] = eigenvec[j][rank];
  }

  /*-- copy the sorted eigenvalues and eigenvectors back --*/
  for (i = 0; i < nels; i++) {
    eigenval[i] = e[i];
    for (j = 0; j < nels; j++) {
      eigenvec[j][i] = b[j][i];
    }
  }
  for (i = 0; i < nels; i++)
    if (eigenvec[0][i] < 0)
      for (j = 0; j < nels; j++)
        eigenvec[j][i] = -eigenvec[j][i];

  /*-- free temporary variables --*/
  g_free ((gpointer) pairs);
  for (j = 0; j < nels; j++)
    g_free (b[j]);
  g_free (b);
  g_free (e);

  return (!vc_equals_I);
}

/*-- sphere data from svars[] into pcvars[] --*/
void
spherize_data (vector_i * svars, vector_i * pcvars, GGobiData * d,
               ggobid * gg)
{
  gint i, j, k, m;
  gfloat tmpf;
  gfloat *b = (gfloat *) g_malloc (svars->nels * sizeof (gfloat));

  gfloat *tform_mean = d->sphere.tform_mean.els;
  gfloat *tform_stddev = d->sphere.tform_stddev.els;
  gdouble **eigenvec = d->sphere.eigenvec.vals;
  gfloat *eigenval = d->sphere.eigenval.els;

  for (m = 0; m < d->nrows_in_plot; m++) {
    i = d->rows_in_plot.els[m];

    for (j = 0; j < pcvars->nels; j++) {
      tmpf = 0.;
      for (k = 0; k < svars->nels; k++) {
        if (d->sphere.vars_stdized) {
          tmpf = tmpf + (gfloat) eigenvec[k][j] *
            (d->tform.vals[i][svars->els[k]] -
             tform_mean[k]) / tform_stddev[k];
        }
        else {
          tmpf = tmpf + (gfloat) eigenvec[k][j] *
            (d->tform.vals[i][svars->els[k]] - tform_mean[k]);
        }
      }
      b[j] = tmpf / eigenval[j];
    }
    for (j = 0; j < pcvars->nels; j++)
      d->raw.vals[i][pcvars->els[j]] = d->tform.vals[i][pcvars->els[j]] =
        b[j];
  }

  g_free (b);
}

/*-------------------------------------------------------------------------*/
/*          Principal components analysis routines:                        */
/*  executed when the sphere button is pressed and the scree plot opened   */
/*-------------------------------------------------------------------------*/

gboolean
pca_calc (GGobiData * d, ggobid * gg)
{
  gboolean svd_ok = false;

  eigenvec_zero (d, gg);

  sphere_varcovar_set (d, gg);

  /* If nspherevars > 1 use svd routine, otherwise just standardize */
  if (d->sphere.vars.nels > 1) {
    eigenvec_set (d, gg);
    svd_ok = sphere_svd (d, gg);
  }

  return svd_ok;
}
