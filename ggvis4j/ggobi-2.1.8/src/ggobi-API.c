/*-- ggobi-API.c --*/
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
#include <string.h>
#include <stdlib.h>
#include <gtk/gtk.h>

#include "GGobiAPI.h"

#include "ggobi.h"
#include "ggobi-data.h"
#include "types.h"
#include "vars.h"
#include "externs.h"
#include "display.h"
#include "plugin.h"

extern const gchar *const GlyphNames[];

void warning (const char *msg);

#ifdef __cplusplus
extern "C"
{
#endif
  void GGOBI (displays_release) (ggobid * gg);
  void GGOBI (display_release) (displayd * display, ggobid * gg);
  void GGOBI (splot_release) (splotd * sp, displayd * display, ggobid * gg);
  void GGOBI (data_release) (GGobiData *, ggobid * gg);
  void GGOBI (vartable_free) (GGobiData *, ggobid * gg);
  void GGOBI (vardatum_free) (vartabled * var, ggobid * gg);
#ifdef __cplusplus
}
#endif

const gchar *GGOBI (getFileName) (ggobid * gg)
{
  return (gg->input->fileName);
}

const gchar *GGOBI (setFileName) (const gchar * fileName, DataMode data_mode,
                                  ggobid * gg)
{
  const gchar *old = g_strdup (GGOBI (getFileName) (gg));
  fileset_read_init (fileName, NULL, NULL, gg);
  display_menu_build (gg);
  return (old);
}


DataMode GGOBI (getDataMode) (ggobid * gg)
{
  return (gg->input->mode);
}

DataMode GGOBI (setDataMode) (DataMode newMode, ggobid * gg)
{
  DataMode old = gg->input->mode;
  sessionOptions->data_mode = newMode;
  gg->input->mode = newMode;
  return (old);
}

/**
 Caller should now free the return value, but not the elements of the
 array.
 This is computed dynamically by querying each of the input plugins 
 for its collection of mode names.
*/
const gchar **GGOBI (getDataModeNames) (int *n)
{
  int ctr = 0, num, k, i;
  GList *plugins;
  const gchar **ans;
  GGobiPluginInfo *plugin;

  plugins = sessionOptions->info->inputPlugins;
  num = g_list_length (plugins);
  for (i = 0; i < num; i++) {
    plugin = g_list_nth_data (plugins, i);
    ctr += plugin->info.i->numModeNames;
  }

  ans = (const gchar **) g_malloc (sizeof (gchar *) * ctr);
  ctr = 0;
  for (i = 0; i < num; i++) {
    plugin = g_list_nth_data (plugins, i);
    for (k = 0; k < plugin->info.i->numModeNames; k++) {
      ans[ctr++] = plugin->info.i->modeNames[k];
    }
  }

  if (n)
    *n = ctr;

  return (ans);
}


gchar **GGOBI (getVariableNames) (gint transformed, GGobiData * d,
                                  ggobid * gg)
{
  gchar **names;
  gint nc = d->ncols, j;

  names = (gchar **) g_malloc (sizeof (gchar *) * nc);

  for (j = 0; j < nc; j++) {
    names[j] = transformed ? ggobi_data_get_transformed_col_name(d, j) : ggobi_data_get_col_name(d, j);
  }

  return (names);
}


void
GGOBI (setVariableName) (gint j, gchar * name, gboolean transformed,
                         GGobiData * d, ggobid * gg)
{

  if (transformed)
    ggobi_data_set_transformed_col_name(d, j, name);
  else 
    ggobi_data_set_col_name(d, j, name);

}


/*
  Closes the specified display
 */
void GGOBI (destroyCurrentDisplay) (ggobid * gg)
{
  display_free (gg->current_display, false, gg);
}


MissingValue_p GGobiMissingValue;

MissingValue_p
GGobi_setMissingValueIdentifier (MissingValue_p f)
{
  MissingValue_p old = GGobiMissingValue;
  GGobiMissingValue = f;
  return (old);
}


static const gchar *const DefaultRowNames = NULL;

const gchar *const *
getDefaultRowNamesPtr ()
{
  return ((const gchar * const *) &DefaultRowNames);
}

void
setRowNames (GGobiData * d, gchar ** rownames)
{
  int i;
  gchar *lbl;
  for (i = 0; i < d->nrows; i++) {
    lbl = (rownames != (gchar **) & DefaultRowNames
           && rownames != (gchar **) NULL
           && rownames[i] !=
           (gchar *) NULL) ? g_strdup (rownames[i]) : g_strdup_printf ("%d",
                                                                       i + 1);
    g_array_append_val (d->rowlab, lbl);
  }
}

/*
  An initial attempt to allow new data to be introduced
  to the Ggobi session, replacing the existing contents.
 
  There are still a few details remaining regarding the scaling
  on the axes, etc. (See ruler_ranges_set in scatterplot.c)
  The reverse pipeline data has not been established correctly
  and the computation is incorrect.
  Specifically, the routine splot_screen_to_tform() is not
  

  When this works, we will take the calls for the different stages 
  and put them in separate routines.
 */
/*-- need two of these now, one to replace and one to append --*/
void
GGOBI (setData) (gdouble * values, gchar ** rownames, gchar ** colnames,
                 gint nr, gint nc, GGobiData * d, gboolean cleanup,
                 ggobid * gg, gchar ** ids, gboolean duplicate,
                 InputDescription * desc)
{
  gint i, j;
  gchar *lbl;
  gchar *varname;

  if (cleanup) {
    /* Release all the displays associated with this datad
       and then release all the GUI components and memory
       for this datad.

       This may need some reworking in order to release
       exactly the right things, no more and no less.
     */
    GGOBI (displays_release) (gg);
    varpanel_clear (d, gg);
    GGOBI (data_release) (d, gg);
    /* ?? */
    gtk_ui_manager_remove_ui (gg->main_menu_manager, gg->mode_merge_id);
    /*submenu_destroy (gg->pmode_item);
       submenu_destroy (gg->imode_item); */
  }

  d->input = desc;
  if (d->name == NULL)
    d->name = g_strdup (desc->fileName);
  if (gg->input == NULL)
    gg->input = desc;

  d->ncols = nc;
  d->nrows = nr;

  vectori_init_null (&d->rows_in_plot);
  d->nrows_in_plot = d->nrows;  /*-- for now --*/

  arrayf_alloc (&d->raw, nr, nc);

  if (ids) {
    datad_record_ids_set (d, ids, duplicate);
  }

  rowlabels_alloc (d);

  vartable_alloc (d);
  vartable_init (d);

  br_glyph_ids_alloc (d);
  br_glyph_ids_init (d);

  br_color_ids_alloc (d);
  br_color_ids_init (d);

  br_hidden_alloc (d);
  br_hidden_init (d);

  if (values && d->vartable) {
    /* the person who created the datad is taking care of populating it. */
    for (j = 0; j < nc; j++) {
      varname = (colnames != NULL && colnames[j] != NULL) ? colnames[j] : NULL;
      ggobi_data_set_col_name(d, j, varname);

      for (i = 0; i < nr; i++) {
        if (j == 0) {
          lbl = (rownames != NULL && rownames[i] != NULL) ?
            g_strdup (rownames[i]) : g_strdup_printf ("%d", i + 1);
          g_array_append_val (d->rowlab, lbl);
          /* g_free (lbl); */
        }

        if (values)
          ggobi_data_set_raw_value(d, i, j, values[i + j * nr]);
      }
    }
  }

  if (rownames && d->rowlab->len == 0)
    setRowNames (d, rownames);


  /* Now recompute and display the top plot. */
  if (nc > 0 && datad_init (d, gg, cleanup) != NULL) {
    /* Have to patch up the displays list since we removed
       every entry and that makes for meaningless entries.

       IS THIS TRUE? Only if cleanup was specified!
       This looks very dangerous. Should use g_list_remove();
     */
    gg->displays->next = NULL;
  }
  display_menu_build (gg);
}


/* These are all for freeing the currently held data. */
void GGOBI (displays_release) (ggobid * gg)
{
  GList *dlist;
  displayd *display;

  /* We have to be careful here as we are removing all the elements
     of the singly-linked list. When we remove the last one,
     the ->next value of the dlist becomes non-NULL. Hence
     we are getting garbage. Accordingly, we count down from the total
     number to remove using num and when this is 0, we exit.
     This should leave the slist allocated, but empty.

     We have to patch the list up afterwards.
   */
  gint num = g_list_length (gg->displays);

  for (dlist = gg->displays; dlist != NULL; dlist = dlist->next, num--) {
    if (num == 0)
      break;
    display = (displayd *) dlist->data;
    /*  display_release(display, gg); */
    display_free (display, true, gg);
  }
}

void GGOBI (display_release) (displayd * display, ggobid * gg)
{
  display_free (display, true, gg);
}


void GGOBI (splot_release) (splotd * sp, displayd * display, ggobid * gg)
{
  splot_free (sp, display, gg);
}

/* Not in the API for the moment. A "protected" routine. */
void GGOBI (data_release) (GGobiData * d, ggobid * gg)
{
  void vartable_free (GGobiData * d);

  if (d == NULL)
    return;
  if (d->rowlab) {
    rowlabels_free (d);
    d->rowlab = NULL;
  }

  vartable_free (d);
}


/**XX*/
const gchar *const *GGOBI (getViewTypes) (int *n)
{
  *n = NDISPLAYTYPES;
  return (ViewTypes);
}

const gint *GGOBI (getViewTypeIndices) (gint * n)
{
  extern const gint ViewTypeIndices[];
  *n = NDISPLAYTYPES;
  return (ViewTypeIndices);
}


displayd *GGOBI (newScatterplot) (gint ix, gint iy, gboolean use_window, 
                                  GGobiData * d, ggobid * gg)
{
  displayd *display = NULL;
  gint vars[2];

  vars[0] = ix;
  vars[1] = iy;
  display = scatterplot_new_with_vars (use_window, false, 2, vars, d, gg);
#ifdef FORCE_ADD_DISPLAY
  display_add (display, gg);
#endif


  return (display);
}

displayd *GGOBI (newScatmat) (gint * rows, gint * columns, gint nr, gint nc,
                              gboolean use_window, GGobiData * d, ggobid * gg)
{
  displayd *display;

  display = scatmat_new (NULL, use_window, false, nr, rows, nc, columns, d, gg);
#ifdef FORCE_ADD_DISPLAY
  display_add (display, gg);    /*XX  the caller should add this display. */
#endif

  return (display);
}

displayd *GGOBI (newParCoords) (gint * vars, gint numVars, gboolean use_window, 
                                GGobiData * d, ggobid * gg)
{
  displayd *display = NULL;

  display = parcoords_new (display, use_window, false, numVars, vars, d, gg);
#ifdef FORCE_ADD_DISPLAY
  display_add (display, gg);    /*XX  the caller should add this display. */
#endif


  return (display);
}

displayd *GGOBI (newTimeSeries) (gint * yvars, gint numVars, gboolean use_window, 
                                 GGobiData * d, ggobid * gg)
{
  displayd *display = NULL;

  display = tsplot_new (display, use_window, false, numVars, yvars, d, gg);
#ifdef FORCE_ADD_DISPLAY
  display_add (display, gg);    /*XX  the caller should add this display. */
#endif

  return (display);
}

displayd *GGOBI (createPlot) (int type, char **varnames)
{
  displayd *display = NULL;
  /*
     display_new(type);
   */
  return (display);
}


const gchar *GGOBI (getCurrentDisplayType) (ggobid * gg)
{
/*XX */
  return (GGOBI (getViewTypeName) (gg->current_display));
}

const gchar *GGOBI (getViewTypeName) (displayd * dpy)
{
  gchar *val;

  if (!GGOBI_IS_EXTENDED_DISPLAY (dpy))
    return (NULL);

/*
 or use gtk_type_name(GTK_OBJECT_TYPE(dpy))
 */
  val = GGOBI_EXTENDED_DISPLAY_GET_CLASS (dpy)->treeLabel;

  return (val);
}


/*
  Pointer to the raw data managed by GGobi.
  Don't touch this.
 */
const gfloat **GGOBI (getRawData) (GGobiData * d, ggobid * gg)
{
  return ((const gfloat **) d->raw.vals);
}

/*
  Pointer to the second transformation of the data managed by GGobi.
  Don't touch this.
 */
const gfloat **GGOBI (getTFormData) (GGobiData * d, ggobid * gg)
{
  return ((const gfloat **) d->tform.vals);
}


/*
  Returns a reference to the labels used to identify
  the observations.
  Do not change this as it is not a copy.
 */
const gchar **GGOBI (getCaseNames) (GGobiData * d, ggobid * gg)
{
  gchar **rowlab = (gchar **) g_malloc (sizeof (gchar *) * d->nrows);
  gint i;
  for (i = 0; i < d->nrows; i++)
    rowlab[i] = (gchar *) g_array_index (d->rowlab, gchar *, i);

  return ((const gchar **) rowlab);
}

/*
 This does not copy the label, so it is assumed
 that the caller has already allocated the space
 using the appropriate GTK memory model.
 If it is apparent that this is being called from 
 contexts that use a very different memory model (e.g. S/R),
 we can change the behaviour to copy this.

 Similarly, this can be modified to return the previous
 value, but the caller will have to free that pointer.
 */
void
GGOBI (setCaseName) (gint index, const gchar * label, GGobiData * d,
                     ggobid * gg)
{
  gchar *old;
  if (index < 0 || index >= d->nrows) {
    warning ("Index is out of range of observations in setCaseName");
    return;
  }

  old = g_array_index (d->rowlab, gchar *, index);
  g_free (old);

  g_array_insert_val (d->rowlab, index, label);
}


void
warning (const gchar * msg)
{
  fprintf (stderr, "%s\n", msg);
  fflush (stderr);
}


/*-------------------------------------------------------------------------*/
/*     setting and getting point glyph types and sizes                     */
/*-------------------------------------------------------------------------*/

gint *GGOBI (getGlyphTypes) (int *n)
{
  static gint *glyphIds = NULL;
  *n = UNKNOWN_GLYPH - 1;       /* -1 since we start at 1 */

  if (glyphIds == NULL) {
    gint i;
    glyphIds = (gint *) g_malloc (*n * sizeof (gint));
    for (i = 0; i < *n; i++) {
      glyphIds[i] = mapGlyphName (GlyphNames[i]);
    }
  }

  return (glyphIds);
}

const gchar *const *GGOBI (getGlyphTypeNames) (gint * n)
{
  *n = UNKNOWN_GLYPH - 1;       /* -1 since we start at 1; starting at 0 now */
  return ((const gchar * const *) GlyphNames);
}


gchar const *GGOBI (getGlyphTypeName) (gint type)
{
  gchar const *ans;
  ans = GlyphNames[type];

  return (ans);
}


gint *GGOBI (getCaseGlyphTypes) (gint * ids, gint n, GGobiData * d,
                                 ggobid * gg)
{
  gint i;
  gint *ans = (gint *) g_malloc (n * sizeof (gint));

  for (i = 0; i < n; i++)
    ans[i] = GGOBI (getCaseGlyphType) (ids[i], d, gg);

  return (ids);
}

gint GGOBI (getCaseGlyphType) (gint id, GGobiData * d, ggobid * gg)
{
  gint index = d->rows_in_plot.els[id];
  return (d->glyph_now.els[index].type);
}

gint *GGOBI (getCaseGlyphSizes) (gint * ids, gint n, GGobiData * d,
                                 ggobid * gg)
{
  gint i;
  gint *ans = (gint *) g_malloc (n * sizeof (gint));

  for (i = 0; i < n; i++)
    ans[i] = GGOBI (getCaseGlyphSize) (ids[i], d, gg);

  return (ids);
}

gint GGOBI (getCaseGlyphSize) (gint id, GGobiData * d, ggobid * gg)
{
  gint index = d->rows_in_plot.els[id];

  return (d->glyph_now.els[index].size);
}

void
GGOBI (setCaseGlyph) (gint index, gint type, gint size, GGobiData * d,
                      ggobid * gg)
{
  if (type > -1) {
    if (type >= NGLYPHTYPES)
      g_printerr ("Illegal glyph type: %d\n", type);
    else
      d->glyph.els[index].type = d->glyph_now.els[index].type = type;
  }

  if (size > -1) {
    if (size >= NGLYPHSIZES)
      g_printerr ("Illegal glyph size: %d\n", size);
    else
      d->glyph.els[index].size = d->glyph_now.els[index].size = size;
  }
}

void
GGOBI (setCaseGlyphs) (gint * ids, gint n, gint type, gint size,
                       GGobiData * d, ggobid * gg)
{
  gint i, doit = 1;
  /*
   * Make sure glyphs are legal before assigning.  (It's safe to
   * ignore values of -1, because those are likely to be deliberately
   * set by the calling routine as an indication to do nothing.)
   * Do the check here to avoid generating large numbers of
   * error messages.
   */
  if (type >= NGLYPHTYPES) {
    g_printerr ("Illegal glyph type: %d\n", type);
    doit = 0;
  }
  if (size >= NGLYPHSIZES) {
    g_printerr ("Illegal glyph size: %d\n", size);
    doit = 0;
  }

  if (doit)
    for (i = 0; i < n; i++)
      GGOBI (setCaseGlyph) (ids[i], type, size, d, gg);
}

/*-------------------------------------------------------------------------*/
/*               setting and getting point colors                          */
/*-------------------------------------------------------------------------*/

void
GGOBI (setCaseColor) (gint pt, gint colorIndex, GGobiData * d, ggobid * gg)
{
  colorschemed *scheme = gg->activeColorScheme;

  /*-- temporary fix --*/
  if (colorIndex < 0 || colorIndex > scheme->n - 1)
    colorIndex = 0;
  d->color.els[pt] = d->color_now.els[pt] = colorIndex;
}

void
GGOBI (setCaseColors) (gint * pts, gint howMany, gint colorIndex,
                       GGobiData * d, ggobid * gg)
{
  gint i;
  for (i = 0; i < howMany; i++)
    d->color.els[pts[i]] = d->color_now.els[pts[i]] = colorIndex;
}


gint GGOBI (getCaseColor) (gint pt, GGobiData * d, ggobid * gg)
{
  return (d->color_now.els[pt]);
}

gint *GGOBI (getCaseColors) (gint * pts, gint howMany, GGobiData * d,
                             ggobid * gg)
{
  gint i;
  gint *ans = (gint *) g_malloc (howMany * sizeof (gint));

  for (i = 0; i < howMany; i++)
    ans[i] = GGOBI (getCaseColor) (pts[i], d, gg);

  return (ans);
}

/*-------------------------------------------------------------------------*/
/*        setting and getting the point hidden state                       */
/*-------------------------------------------------------------------------*/

void
GGOBI (setCaseHidden) (gint pt, gboolean hidden_p, GGobiData * d, ggobid * gg)
{
  d->hidden.els[pt] = d->hidden_now.els[pt] = hidden_p;
  /*-- don't replot --*/
}

void
GGOBI (setCaseHiddens) (gint * pts, gint howMany, gboolean hidden_p,
                        GGobiData * d, ggobid * gg)
{
  gint i;
  for (i = 0; i < howMany; i++)
    GGOBI (setCaseHidden) (pts[i], hidden_p, d, gg);
  displays_plot (NULL, FULL, gg);
}

gboolean GGOBI (getCaseHidden) (gint pt, GGobiData * d, ggobid * gg)
{
  return (d->hidden_now.els[pt]);
}

gboolean *GGOBI (getCaseHiddens) (gint * pts, gint howMany, GGobiData * d,
                                  ggobid * gg)
{
  gint i;
  gboolean *ans = (gboolean *) g_malloc (howMany * sizeof (gboolean));

  for (i = 0; i < howMany; i++)
    ans[i] = GGOBI (getCaseHidden) (pts[i], d, gg);

  return (ans);
}

/*-------------------------------------------------------------------------*/
/*        setting and getting edges                                        */
/*-------------------------------------------------------------------------*/

gboolean GGOBI (getShowLines) ()
{
  return (GGOBI (getDefaultDisplayOptions) ()->edges_undirected_show_p);
}

/* uh.. this takes a boolean value but always shows lines... what's up */
gboolean GGOBI (setShowLines) (displayd * dsp, gboolean val)
{
  GtkAction *action;
  gboolean old = GGOBI (getShowLines) ();
  /*GGOBI(getDefaultDisplayOptions)()->edges_undirected_show_p = val; */
  dsp->options.edges_undirected_show_p = true;

  action = gtk_ui_manager_get_action (dsp->menu_manager,
                                      "/menubar/Edges/ShowUndirectedEdges");
  if (action)
    gtk_toggle_action_set_active (GTK_TOGGLE_ACTION (action), true);

  return (old);
}

/* setShowAxes  will require this code:

{
  GtkWidget *topmenu, *menu, *item;
  topmenu = widget_find_by_name (dspnew->menubar, "DISPLAY:options_topmenu");
  if (topmenu) {
    menu = GTK_MENU_ITEM(topmenu)->submenu;
    if (menu) {
      item = widget_find_by_name (menu, "DISPLAY:show_axes");
      if (item) {
        gtk_check_menu_item_set_active ((GtkCheckMenuItem *) item, false);
      }
    }
  }
}


*/

DisplayOptions *GGOBI (getDefaultDisplayOptions) ()
{
  return (&DefaultDisplayOptions);
}


displayd *GGOBI (getDisplay) (gint which, ggobid * gg)
{
  displayd *display = NULL;

  if (which < g_list_length (gg->displays))
    display = (displayd *) g_list_nth_data (gg->displays, which);

  return (display);
}

DisplayOptions *GGOBI (getDisplayOptions) (int displayNum, ggobid * gg)
{
  DisplayOptions *options = NULL;
  if (displayNum < 0)
    options = GGOBI (getDefaultDisplayOptions) ();
  else {
    displayd *display;
    display = GGOBI (getDisplay) (displayNum, gg);
    if (display)
      options = &(display->options);
  }

  return (options);
}


displayd *GGOBI (getCurrentDisplay) (ggobid * gg)
{
  return (gg->current_display);
}

gint GGOBI (getCurrentDisplayIndex) (ggobid * gg)
{
  return (g_list_index (gg->displays, gg->current_display));
}

gint GGOBI (getCurrentPlotIndex) (ggobid * gg)
{
  int val = -1;
  displayd *d;
  if (gg->current_splot) {
    d = GGOBI (getCurrentDisplay) (gg);
    val = g_list_index (d->splots, gg->current_splot);
  }

  return (val);
}

displayd *GGOBI (setCurrentDisplay) (int which, ggobid * gg)
{
  displayd *d;

  d = GGOBI (getDisplay) (which, gg);

  if (d != NULL)
    display_set_current (d, gg);

  return (d);
}


splotd *GGOBI (getPlot) (displayd * display, int which)
{
  splotd *sp = (splotd *) g_list_nth_data (display->splots, which);
  return (sp);
}


gint GGOBI (getNumGGobis) ()
{
  extern gint num_ggobis;
  return (num_ggobis);
}

/*
  Whether to destory the window or not.  If this is being called from an
  event handler in response to the window being destroyed, we would get
  a circularity. However, when called programmatically from within the
  process (or from e.g. R) we need to force it to be closed.
 */
gboolean GGOBI (close) (ggobid * gg, gboolean closeWindow)
{
  gboolean val = true;

  if (gg->close_pending)
    return (false);

  gg->close_pending = true;

  /* close plugin instances */
  closePlugins (gg);

  procs_activate (off, gg->pmode, gg->current_display, gg);

  display_free_all (gg);

  if (closeWindow && gg->main_window)
    gtk_widget_destroy (gg->main_window);

  if (gg->display_tree.window)
    gtk_widget_destroy (gg->display_tree.window);
  if (gg->vartable_ui.window)
    gtk_widget_destroy (gg->vartable_ui.window);

  if (gg->color_ui.symbol_window)
    gtk_widget_destroy (gg->color_ui.symbol_window);

  if (gg->wvis.window)
    gtk_widget_destroy (gg->wvis.window);
  if (gg->svis.window)
    gtk_widget_destroy (gg->svis.window);

  gg->close_pending = false;
  /* Now fix up the list of ggobi's */
  val = ggobi_remove (gg) != -1;

  if (GGobi_getNumGGobis () == 0 && sessionOptions->info->quitWithNoGGobi &&
      gtk_main_level () > 0) {
    gtk_main_quit ();
  }

  return (val);
}

#ifdef EXPLICIT_IDENTIFY_HANDLER
void GGOBI (setIdentifyHandler) (IdentifyProc proc, void *data, ggobid * gg)
{
  gg->identify_handler.handler = proc;
  gg->identify_handler.user_data = data;
}
#endif

void GGOBI (getBrushSize) (gint * w, gint * h, ggobid * gg)
{
  splotd *sp = gg->current_splot;

  *w = ABS (sp->brush_pos.x1 - sp->brush_pos.x2);
  *h = ABS (sp->brush_pos.y1 - sp->brush_pos.y2);
}

void GGOBI (getBrushLocation) (gint * x, gint * y, ggobid * gg)
{
  splotd *sp = gg->current_splot;

  *x = MIN (sp->brush_pos.x1, sp->brush_pos.x2);
  *y = MIN (sp->brush_pos.y1, sp->brush_pos.y2);
}

void
redraw (splotd * sp, ggobid * gg)
{
  brush_once (true, sp, gg);
  display_plot (sp->displayptr, FULL, gg);
}


void GGOBI (setBrushSize) (int w, int h, ggobid * gg)
{
  splotd *sp = gg->current_splot;
  displayd *display = sp->displayptr;

  sp->brush_pos.x1 = MIN (sp->brush_pos.x1, sp->brush_pos.x2);
  sp->brush_pos.y1 = MIN (sp->brush_pos.y1, sp->brush_pos.y2);

  sp->brush_pos.x2 = sp->brush_pos.x1 + w;
  sp->brush_pos.y2 = sp->brush_pos.y1 + h;

  brush_once (true, sp, gg);
  redraw (sp, gg);
  display_plot (display, FULL, gg);
}


void GGOBI (setBrushLocation) (gint x, gint y, ggobid * gg)
{
  gint wd, ht;
  splotd *sp = gg->current_splot;

  GGOBI (getBrushSize) (&wd, &ht, gg);

  sp->brush_pos.x1 = x;
  sp->brush_pos.y1 = y;
  sp->brush_pos.x2 = x + wd;
  sp->brush_pos.y2 = y + ht;

  brush_once (true, sp, gg);

  redraw (sp, gg);
}

gboolean GGOBI (setBrushGlyph) (gint type, gint size, ggobid * gg)
{
  if (type > -1)
    gg->glyph_id.type = type;
  if (size > -1)
    gg->glyph_id.size = size;

  return (true);                /* Should be true iff there is a change. */
}

void GGOBI (getBrushGlyph) (gint * type, gint * size, ggobid * gg)
{
  *type = gg->glyph_id.type;
  *size = gg->glyph_id.size;
}


/*
  Returns the dimensions of the specified
  splot in pixels which can then be used for
  specifying.
 */
void GGOBI (getPlotPixelSize) (gint * w, gint * h, splotd * sp)
{
  /* Temp */
  *w = -1;
  *h = -1;
}


splotd *GGOBI (getSPlot) (gint which, displayd * display)
{
  splotd *sp = (splotd *) g_list_nth_data (display->splots, which);
  return (sp);
}

gint GGOBI (setPMode) (const gchar * name, ggobid * gg)
{
  ProjectionMode old = pmode_get (gg->current_display, gg);
  ProjectionMode newMode = (ProjectionMode) GGOBI (getPModeId) (name);
  if (newMode != NULL_PMODE)
    GGOBI (full_viewmode_set) (newMode, DEFAULT_IMODE, gg);
  return (old);
}

gint GGOBI (getPModeId) (const gchar * name)
{
  gint n, i;
  const gchar *const *names = GGOBI (getPModeNames) (&n);

  for (i = 0; i < n; i++) {
    if (strcmp (names[i], name) == 0)
      return (i);
  }

  return (-1);
}

const gchar *GGOBI (getPModeName) (int which)
{
  int n;
  const gchar *const *names;

  names = GGOBI (getPModeNames) (&n);
  return (names[which]);
}

const gchar *GGOBI (getPModeScreenName) (int which, displayd * display)
{
  if (which == EXTENDED_DISPLAY_PMODE) {
    gchar *name;
    GGOBI_EXTENDED_DISPLAY_GET_CLASS (display)->imode_control_box (display,
                                                                   &name,
                                                                   display->
                                                                   ggobi);
    return name;
  }
  return (GGOBI (getPModeName) (which));
}

gint GGOBI (setIMode) (const gchar * name, ggobid * gg)
{
  InteractionMode old = imode_get (gg);
  InteractionMode newMode = (InteractionMode) GGOBI (getIModeId) (name);
  if (newMode != NULL_PMODE)
    GGOBI (full_viewmode_set) (NULL_PMODE, newMode, gg);
  return (old);
}

gint GGOBI (getIModeId) (const gchar * name)
{
  gint n, i;
  const gchar *const *names = GGOBI (getIModeNames) (&n);

  for (i = 0; i < n; i++) {
    if (strcmp (names[i], name) == 0)
      return (i);
  }

  return (-1);
}

const gchar *GGOBI (getIModeName) (int which)
{
  int n;
  const gchar *const *names;

  names = GGOBI (getIModeNames) (&n);
  return (names[which]);
}

const gchar *GGOBI (getIModeScreenName) (int which, displayd * display)
{
  if (which == DEFAULT_IMODE)
    return (GGOBI (getPModeScreenName) (display->cpanel.pmode, display));
  return (GGOBI (getIModeName) (which));
}

const gchar *GGOBI (getPModeKey) (int which)
{
  int n;
  const gchar *const *keys = GGOBI (getPModeKeys) (&n);
  return (keys[which]);
}

/*
gint
GGOBI(getModeId)(const gchar *name)
{
  gint n, i;
  const gchar *const *names = GGOBI(getOpModeNames)(&n);
 
  for(i = 0; i < n; i++) {
    if(strcmp(names[i],name) == 0)
      return(i);
  }

  return(-1);
}

const gchar *
GGOBI(getModeName)(int which)
{ 
  int n;
  const gchar *const *names = GGOBI(getOpModeNames)(&n);
  return(names[which]);
}
*/

gint GGOBI (setBrushColor) (gint cid, ggobid * gg)
{
  gint old = gg->color_id;
  if (cid > -1 && cid < gg->activeColorScheme->n)
    gg->color_id = cid;

  return (old);
}

gint GGOBI (getBrushColor) (ggobid * gg)
{
  return (gg->color_id);
}

const gchar *GGOBI (getColorName) (gint cid, ggobid * gg, gboolean inDefault)
{
  if (cid >= 0 && cid < gg->activeColorScheme->n) {
    return ((gchar *) g_array_index (gg->activeColorScheme->colorNames,
                                     gchar *, cid));
  }

  return (NULL);
}


static gint
addVariableInternal (gdouble * vals, gint num, gchar * name,
                     gchar ** levels, gint * values, gint * counts,
                     gint numLevels, gboolean update, GGobiData * d,
                     ggobid * gg)
{
  /*if (d->ncols < 1) {
     gchar ** rnames = (gchar **) &DefaultRowNames; 
     GGOBI(setData)(NULL, rnames, &name, num, d->ncols, d, false, gg, NULL, false, d->input); 
     datad_init(d, gg, false);
     } */

  if (num > d->nrows && d->ncols > 0) {
    num = d->nrows;
    g_warning ("Variable length (%d) exceeds the number of dataset rows (%d)",
               num, d->nrows);
  }
  newvar_add_with_values (vals, num, name,
                          numLevels > 0 ? categorical : real,
                          numLevels, levels, values, counts, d);

  if (update)
    gdk_flush ();

  return (d->ncols - 1);
}

/*-- this is really addRealVariable --*/
gint
GGOBI (addVariable) (gdouble * vals, gint num, gchar * name,
                     gboolean update, GGobiData * d, ggobid * gg)
{
  return (addVariableInternal
          (vals, num, name, NULL, NULL, NULL, 0, update, d, gg));
}

/*
It's hard to get the sequence right of adding a variable, updating its
data, updating the variable table structure, setting the variable's type,
emitting an event saying that a variable has been added, and updating the
GUI.   ....  This works for now, but the code is not pretty.  We're making
only very limted use of the event we emit.    dfs  2/7
*/
gint
GGOBI (addCategoricalVariable) (gdouble * vals, gint num, gchar * name,
                                gchar ** levels, gint * values, gint * counts,
                                gint numLevels, gboolean update,
                                GGobiData * d, ggobid * gg)
{

  return (addVariableInternal
          (vals, num, name, levels, values, counts, numLevels, update, d,
           gg));
}


/*
  The idea of the update argument is that we can defer recomputing
  the statistics for all the variables and then the transformations.
  This is useful when we know we will be adding more variables before
  redisplaying.
  For example,
    for(i = 0; i < n; i++)
      GGOBI(setVariableValues)(i, values[i], gg->nrows, i == n-1, gg);
  causes the update to be done only for the last variable.
 */
gboolean
GGOBI (setVariableValues) (gint whichVar, gdouble * vals, gint num,
                           gboolean update, GGobiData * d, ggobid * gg)
{
  gint i;
  for (i = 0; i < num; i++) {
    d->raw.vals[i][whichVar] = d->tform.vals[i][whichVar] = vals[i];
  }

  if (update) {
    GGOBI (update_data) (d, gg);
  }

  return (true);
}

void GGOBI (update_data) (GGobiData * d, ggobid * gg)
{
  limits_set (d, true, true, gg->lims_use_visible);
  vartable_limits_set (d);
  vartable_stats_set (d);

  tform_to_world (d, gg);
}

gint GGOBI (removeVariable) (gchar * name, GGobiData * d, ggobid * gg)
{
  gint which = GGOBI (getVariableIndex) (name, d, gg);
  if (which > -1 && which < d->ncols)
    return (GGOBI (removeVariableByIndex) (which, d, gg));

  return (-1);
}

gint GGOBI (removeVariableByIndex) (gint which, GGobiData * d, ggobid * gg)
{
  gint i, j;
  for (i = 0; i < d->nrows; i++) {
    for (j = which + 1; j < d->ncols; j++) {
/*XXX Fill in */
    }
  }

  d->ncols--;

  return (-1);
}


gint GGOBI (getVariableIndex) (const gchar * name, GGobiData * d, ggobid * gg)
{
  gint j;

  for (j = 0; j < d->ncols; j++) {
    if (strcmp (ggobi_data_get_col_name(d, j), name) == 0)
      return (j);
  }

  return (-1);
}

void
GGOBI (setPlotRange) (double *x, double *y, int plotNum, displayd * display,
                      gboolean pixels, ggobid * gg)
{
  splotd *sp;

  sp = GGOBI (getPlot) (display, plotNum);

  if (pixels) {

  }
  else {

    splot_zoom (sp, *x, *y);
  }

  /*
     fcoords tfmin, tfmax;
     tfmin.x = x[0];
     tfmin.y = y[0];
     tfmax.x = x[1];
     tfmax.y = y[1];

     if (GTK_WIDGET_VISIBLE (display->hrule)) {
     if (((gfloat) GTK_RULER (display->hrule)->lower != tfmin.x) ||
     ((gfloat) GTK_RULER (display->hrule)->upper != tfmax.x))
     {
     GTK_RULER_set_range (GTK_RULER (display->hrule),
     (gdouble) tfmin.x, (gdouble) tfmax.x);
     }
     }

     if (GTK_WIDGET_VISIBLE (display->vrule)) {
     if (((gfloat) GTK_RULER (display->vrule)->upper != tfmin.y) ||
     ((gfloat) GTK_RULER (display->vrule)->lower != tfmax.y))
     {
     GTK_RULER_set_range (GTK_RULER (display->vrule),
     (gdouble) tfmax.y, (gdouble) tfmin.y);
     }
     }
   */
}


/*
  This handles the raising and lowering or the iconifying or
  de-iconifying of one or more windows.  If which is negative,
  the operation applies to all the displays with the ggobid
  instance.  Otherwise, the operation applies just to the
  display indexed by which.

  The two logical arguments indicate whether to raise/lower
  or iconify/deiconify.  Within these two operation types, the
  up argument indicates whether to raise or lower, an iconify
  or deiconify.
 */
gboolean
GGOBI (raiseWindow) (int which, gboolean raiseOrIcon, gboolean up,
                     ggobid * gg)
{
  windowDisplayd *display;
  gboolean ok = false;
  int start, end, i;

  if (which < 0) {
    start = 0;
    end = g_list_length (gg->displays);
  }
  else {
    end = which + 1;
    start = which;
  }

  for (i = start; i < end; i++) {
    display = (windowDisplayd *) g_list_nth_data (gg->displays, i);
    if (GGOBI_IS_WINDOW_DISPLAY (display) == false)
      continue;
    if (raiseOrIcon) {
      if (up)
        gdk_window_raise (display->window->window);
      else
        gdk_window_lower (display->window->window);
    }
    else {
      if (up)
        gtk_widget_hide_all (display->window);
      else
        gtk_widget_show_all (display->window);

    }
  }

  ok = true;


  gdk_flush ();
  return (ok);
}

gchar *GGOBI (getDescription) (ggobid * gg)
{
  if (!gg->input)
    return (NULL);

  return (g_strdup (gg->input->fileName));
}

/*
  Finds the index of the dataset named `name'
  in the specified ggobid object.
 */
int GGOBI (datasetIndex) (const char *name, const ggobid * const gg)
{
  GGobiData *d;
  int ctr = 0;
  GSList *tmp = gg->d;

  while (tmp) {
    d = (GGobiData *) tmp->data;
    if (strcmp (name, d->name) == 0)
      return (ctr);
    ctr++;
    tmp = tmp->next;
  }

  return (-1);
}

/*
  Returns the names of the different datasets
  maintained in the specified ggobid object.
 */
gchar **GGOBI (getDatasetNames) (gint * n, ggobid * gg)
{
  gint i;
  GGobiData *d;
  gchar **names;
  GSList *tmp = gg->d;
  *n = g_slist_length (gg->d);
  names = (gchar **) g_malloc (sizeof (gchar *) * (*n));
  for (i = 0; i < *n; i++) {
    d = (GGobiData *) tmp->data;
    names[i] = g_strdup (d->name);
    tmp = tmp->next;
  }

  return (names);
}

/*
 Added to the API and to avoid breaking code (e.g. in RSggobi)
 we add it here with a new name GGOBI(ggobi_get).
*/
ggobid *GGOBI (ggobi_get) (gint which)
{
  return (ggobi_get (which));
}

gint GGOBI (ncols) (GGobiData * data)
{
  return (data->ncols);
}

gint GGOBI (nrecords) (GGobiData * data)
{
  return (data->nrows);
}


/*
 This is the routine one uses to register a handler for key press events
 for the numbered keys, i.e. 0, 1, ..., 9
 One can specify null values for each of these to remove the handler and have
 these events discarded.

 See notes/NumberedKeys.*, splot.c and ggobi.h also for more details 
 */
KeyEventHandler *GGOBI (registerNumberedKeyEventHandler) (KeyEventHandlerFunc
                                                          routine,
                                                          void *userData,
                                                          char *description,
                                                          ReleaseData *
                                                          releaseData,
                                                          ggobid * gg,
                                                          ProgrammingLanguage
                                                          lang)
{
  KeyEventHandler *old = gg->NumberedKeyEventHandler;
  KeyEventHandler *newValue;
  if (routine == NULL)
    newValue = NULL;
  else {
    newValue = g_malloc (1 * sizeof (KeyEventHandler));
    newValue->handlerRoutine = routine;
    newValue->userData = userData;
    newValue->description = g_strdup (description);
    newValue->language = lang;
    newValue->releaseData = releaseData;
  }

  gg->NumberedKeyEventHandler = newValue;

  return (old);
}

KeyEventHandler *GGOBI (removeNumberedKeyEventHandler) (ggobid * gg)
{
  return (GGOBI
          (registerNumberedKeyEventHandler (NULL, NULL, NULL, NULL, gg, C)));
}


#include "config.h"
static const gchar *version_date = GGOBI_RELEASE_DATE;
static const int GgobiVersionNumbers[] =
  { MAJOR_VERSION, MINOR_VERSION, MICRO_VERSION };
static const gchar *version_string = PACKAGE_VERSION;

const char *GGOBI (getVersionDate) ()
{
  return (version_date);
}

const char *GGOBI (getVersionString) ()
{
  return (version_string);
}

const int *GGOBI (getVersionNumbers) ()
{
  return (GgobiVersionNumbers);
}


GGobiData *GGOBI (data_get) (gint which, const ggobid * const gg)
{
  GGobiData *data = NULL;

  if (gg->d != NULL)
    data = g_slist_nth_data (gg->d, which);

  return (data);
}

GGobiData *GGOBI (data_get_by_name) (const gchar * const name,
                                     const ggobid * const gg)
{
  gint which;
  GGobiData *data = NULL;

  which = GGOBI (datasetIndex) (name, gg);
  if (which > -1) {
    data = GGOBI (data_get) (which, gg);
  }

  return (NULL);
}

void
GGobi_setSessionOptions (GGobiOptions * opts)
{
  sessionOptions = opts;
}

const gchar *
GGobi_getLevelName (vartabled * vt, double value)
{
  int which = 0;
  for (which = 0; which < vt->nlevels; which++) {
    if (vt->level_values[which] == (int) value)
      return (vt->level_names[which]);
  }

  return (NULL);
}


void
GGobi_setDataName (const char *const name, GGobiData * d)
{
  if (d->name)
    g_free ((gchar *) d->name);

  d->name = g_strdup (name);
  /* Update the different labels. */
}


/* sets the tour projection matrix, F */
gboolean
GGOBI (setTour2DProjectionMatrix) (gdouble * Fvalues, gint ncols, gint ndim,
                                   gboolean vals_scaled, ggobid * gg)
{
  ProjectionMode vm = pmode_get (gg->current_display, gg);
  displayd *dsp = gg->current_display;
  cpaneld *cpanel = &dsp->cpanel;
  GGobiData *d = dsp->d;
  gboolean candoit = true;
  gint i, j;

  if ((ncols != d->ncols) || ndim != 2)
    candoit = false;

  if (candoit) {
    /* Set the scatterplot display mode to be tour2d */
    if (vm != TOUR2D) {
      /* Needs to be filled in */
    }

    /* Pause the tour */
    if (!cpanel->t2d.paused)
      tour2d_pause (cpanel, true, dsp, gg);

    /* Set the projection vector F */
    for (i = 0; i < ndim; i++)
      for (j = 0; j < ncols; j++)
        dsp->t2d.F.vals[i][j] = Fvalues[i + j * 2];

    /* If the values are scaled, then we need to multiply
       them by the tform data, else we multiply them by the 
       world data */
    if (vals_scaled) {
      /* Needs to be filled in */
    }
    else {
      display_tailpipe (dsp, FULL, gg);
      varcircles_refresh (d, gg);
    }
  }

  return (candoit);
}

const gdouble **GGOBI (getTour2DProjectionMatrix) (gint ncols, gint ndim,
                                                   gboolean vals_scaled,
                                                   ggobid * gg)
{
  displayd *dsp = gg->current_display;
  GGobiData *d = dsp->d;
  gdouble **Fvals;
  gint i, j;

  ncols = d->ncols;
  ndim = 2;

  Fvals = (gdouble **) g_malloc (sizeof (gdouble *) * ncols);

  if (vals_scaled) {
    /* run the F values through the reverse pipeline */
  }
  else {
    for (i = 0; i < ndim; i++)
      for (j = 0; j < ncols; j++)
        Fvals[i][j] = dsp->t2d.F.vals[i][j];
  }

  return ((const gdouble **) Fvals);
}


guint
getGGobiSignal (GGobiSignalType which)
{
  /*
     XXX  assert(which > -1 && which < MAX_GGOBI_SIGNALS);
   */
  return (GGobiSignals[which]);
}


GSList *GGOBI (getExtendedDisplayTypes) ()
{
  return (ExtendedDisplayTypes);
}
