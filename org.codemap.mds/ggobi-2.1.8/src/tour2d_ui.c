/* tour2d_ui.c */
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

#include <gdk/gdkkeysyms.h>
#include <gtk/gtk.h>
#ifdef USE_STRINGS_H
#include <strings.h>
#endif

#include "vars.h"
#include "externs.h"
#include "tour.h"

extern TourPPIndex StandardPPIndices[];


/* This function initializes the tour variables - it should only be
   called more than once, when a new tour is started since a new
   subset of variable might be used, or when there is new data. */

void 
cpanel_t2d_init (cpaneld *cpanel, ggobid *gg) {
  cpanel->t2d.paused = false;
  cpanel->t2d.local_scan = false;
  cpanel->t2d.stepping = false;
  cpanel->t2d.backtracking = false;
  cpanel->t2d.step = TOURSTEP0;
  cpanel->t2d.ls_dir = TOUR_LS_IN;
  cpanel->t2d.path_len = 1.;
  cpanel->t2d.slidepos = sessionOptions->defaultTourSpeed;/* If this is changed, it needs to be 
     changed in th cpanel_tour2d_make routine also. */

  cpanel->t2d.manip_mode = MANIP_OBLIQUE;

  cpanel->t2d.pp_indx = 0; /* Corresponds to the HOLES */
  cpanel->t2d.ppindex = StandardPPIndices[0];
}

void
cpanel_tour2d_set (displayd *display, cpaneld *cpanel, ggobid* gg)
/*
 * To handle the case where there are multiple scatterplots
 * which may have different tour options and parameters selected
*/
{
  GtkWidget *w, *btn;
  GtkWidget *pnl = mode_panel_get_by_name(GGOBI(getPModeName)(TOUR2D), gg);
  GtkAdjustment *adj;

  /*-- speed --*/
  w = widget_find_by_name (pnl, "TOUR2D:speed_bar");
  adj = gtk_range_get_adjustment (GTK_RANGE (w));
  gtk_adjustment_set_value (GTK_ADJUSTMENT (adj),
    cpanel->t2d.slidepos);

  /*-- paused --*/
  btn = widget_find_by_name (pnl, "TOUR2D:pause_button");
  gtk_toggle_button_set_active (GTK_TOGGLE_BUTTON (btn), cpanel->t2d.paused);

  /*-- manual manip --*/
  w = widget_find_by_name (pnl, "TOUR2D:manip");

  if (w)
    gtk_combo_box_set_active (GTK_COMBO_BOX (w), cpanel->t2d.manip_mode);
  
  /*-- PC axes --*/
  /*-- backtracking --*/
  /*-- local scan --*/
  /*-- path len... --*/
}


static void speed2d_set_cb (GtkAdjustment *adj, ggobid *gg) {

  tour2d_speed_set(adj->value, gg);
}

static void tour2d_pause_cb (GtkToggleButton *button, ggobid *gg)
{
  displayd *dsp = gg->current_display;

  tour2d_pause (&dsp->cpanel, button->active, dsp, gg);
}

static void reinit_cb (GtkWidget *w, ggobid *gg) {
  tour2d_reinit(gg);
}

static void scramble_cb (GtkWidget *w, ggobid *gg) {

  tour2d_scramble(gg);
}

/*static void pcaxes_cb (GtkToggleButton *button)
{
  g_printerr ("pcaxes: %d\n", button->active);
}*/

static void t2d_snap_cb (GtkWidget *w, ggobid *gg) {

  tour2d_snap(gg);
}

static void t2d_video_cb (GtkToggleButton *button, ggobid *gg)
{
  tour2d_video(gg);
}

/*static void pcaxes_cb (GtkToggleButton *button)
{
  g_printerr ("pcaxes: %d\n", button->active);
}*/

static void tour2dpp_cb (GtkWidget *w, ggobid *gg) 
{
  tour2dpp_window_open (gg);
}

#ifdef TOUR_ADV_IMPLEMENTED
static void tour2dadv_cb (GtkWidget *w, ggobid *gg) {
  tour2dadv_window_open (gg);
}
#endif

static gchar *manip_lbl[] = {"Off", "Oblique", "Vert", "Horiz", "Radial",
                             "Angular"};
static void manip_cb (GtkWidget *w, ggobid *gg)
{
  displayd *dsp = gg->current_display;
  cpaneld *cpanel = &dsp->cpanel;
  splotd *sp = gg->current_splot;

  cpanel->t2d.manip_mode = gtk_combo_box_get_active(GTK_COMBO_BOX(w));

  if (cpanel->t2d.manip_mode == MANIP_OFF)
    splot_cursor_set ((gint) NULL, sp);
  else
    splot_cursor_set (GDK_HAND2, sp);
}

void
cpanel_tour2d_make (ggobid *gg) {
  modepaneld *panel;
  GtkWidget *box, *btn, *sbar, *lbl, *vb;
  GtkObject *adj;
  GtkWidget *manip_opt;
  /*GtkWidget *tgl;*/
  
  panel = (modepaneld *) g_malloc(sizeof(modepaneld));
  gg->control_panels = g_list_append(gg->control_panels, (gpointer) panel);
  panel->name = g_strdup(GGOBI(getPModeName)(TOUR2D));
  panel->w = gtk_vbox_new (false, VBOX_SPACING);
  gtk_container_set_border_width (GTK_CONTAINER (panel->w), 5);

/*
 * speed scrollbar
*/
  /* value, lower, upper, step_increment, page_increment, page_size */
  /* Note that the page_size value only makes a difference for
   * scrollbar widgets, and the highest value you'll get is actually
   * (upper - page_size). */
  adj = gtk_adjustment_new (sessionOptions->defaultTourSpeed, 0.0, MAX_TOUR_SPEED, 1.0, 1.0, 0.0);

  g_signal_connect (G_OBJECT (adj), "value_changed",
                      G_CALLBACK (speed2d_set_cb), (gpointer) gg);

  sbar = gtk_hscale_new (GTK_ADJUSTMENT (adj));
  gtk_widget_set_name (sbar, "TOUR2D:speed_bar");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), sbar,
    "Adjust speed of tour motion", NULL);
  scale_set_default_values (GTK_SCALE (sbar));

  gtk_box_pack_start (GTK_BOX (panel->w), sbar,
    false, false, 1);

/*
 * Box to hold 'pause' toggle button
*/
  box = gtk_hbox_new (true, 1);

  btn = gtk_check_button_new_with_mnemonic ("_Pause");
  gtk_widget_set_name (btn, "TOUR2D:pause_button");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Stop tour motion temporarily (keyboard shortcut: w)", NULL);
  g_signal_connect (G_OBJECT (btn), "toggled",
                     G_CALLBACK (tour2d_pause_cb), (gpointer) gg);
  gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

  gtk_box_pack_start (GTK_BOX (panel->w), box, false, false, 1);

/*
 * Box to hold 'Reinit' toggle and 'Scramble' button
*/
  box = gtk_hbox_new (true, 2);

  btn = gtk_button_new_with_mnemonic("_Reinit");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Reset projection to first two active variables", NULL);
  g_signal_connect (G_OBJECT (btn), "clicked",
                     G_CALLBACK (reinit_cb), (gpointer) gg);
  gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

  btn = gtk_button_new_with_mnemonic ("Scr_amble");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Reset projection to random value", NULL);
  g_signal_connect (G_OBJECT (btn), "clicked",
                     G_CALLBACK (scramble_cb), (gpointer) gg);
  gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

  gtk_box_pack_start (GTK_BOX (panel->w), box, false, false, 1);

/*
 * snapshot and video stream controls
  box = gtk_hbox_new (true, 2);

  btn = gtk_button_new_with_mnemonic ("_Snap");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Take a snapshot of this frame to re-generate plot outside ggobi", NULL);
  g_signal_connect (G_OBJECT (btn), "clicked",
                     G_CALLBACK (t2d_snap_cb), (gpointer) gg);
  gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

  btn = gtk_check_button_new_with_mnemonic ("Vid_eo");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Save sequence of projection frames out to file", NULL);
  g_signal_connect (G_OBJECT (btn), "toggled",
                     G_CALLBACK (t2d_video_cb), (gpointer) gg);
  gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

  gtk_box_pack_start (GTK_BOX (panel->w), box, false, false, 1);
 */

/*
 * manipulation option menu and label inside vbox
*/

  vb = gtk_vbox_new (false, 0);
  gtk_box_pack_start (GTK_BOX (panel->w), vb, false, false, 0);

  lbl = gtk_label_new_with_mnemonic ("_Manual manipulation:");
  gtk_misc_set_alignment (GTK_MISC (lbl), 0, 0.5);
  gtk_box_pack_start (GTK_BOX (vb), lbl, false, false, 0);

  manip_opt = gtk_combo_box_new_text ();
  gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), manip_opt);
  gtk_widget_set_name (manip_opt, "TOUR2D:manip");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), manip_opt,
    "Set the manual manipulation method", NULL);
  gtk_box_pack_end (GTK_BOX (vb), manip_opt, false, false, 0);
  populate_combo_box (manip_opt, manip_lbl, G_N_ELEMENTS(manip_lbl),
    G_CALLBACK(manip_cb), (gpointer) gg);

/*
 * PC Axes toggle
*/
  /*  tgl = gtk_check_button_new_with_label ("PC axes");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
    "Show principal component axes or plain variable axes", NULL);
  g_signal_connect (G_OBJECT (tgl), "toggled",
                      G_CALLBACK (pcaxes_cb), (gpointer) NULL);
  gtk_box_pack_start (GTK_BOX (panel->w),
                      tgl, false, false, 1);
  */
/*
 * projection pursuit button
*/
  btn = gtk_button_new_with_mnemonic ("Pro_jection pursuit ...");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Open panel for grand tour projection pursuit. Requires selected variables to be sphered (see the tools menu).", NULL);
  gtk_box_pack_start (GTK_BOX (panel->w),
                      btn, false, false, 1);
  g_signal_connect (G_OBJECT (btn), "clicked",
                      G_CALLBACK (tour2dpp_cb), gg);

/*
 * advanced features button
*/
#ifdef TOUR_ADV_IMPLEMENTED
  btn = gtk_button_new_with_mnemonic ("Advanced _features ...");
  gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
    "Open panel for additional grand tour features", NULL);
  gtk_box_pack_start (GTK_BOX (panel->w),
                      btn, false, false, 1);
  g_signal_connect (G_OBJECT (btn), "clicked",
                      G_CALLBACK (tour2dadv_cb), gg);
#endif

  gtk_widget_show_all (panel->w);
}


/*----------------------------------------------------------------------*/
/*               Advanced features panel and callbacks                  */
/*----------------------------------------------------------------------*/

/*

The following are considered advanced features for now:
  local tour
  step/go tour
  interpolation methods (geodesic, HH, Givens)
  path length
  history

  section tour
*/


#ifdef TOUR_ADV_IMPLEMENTED
static void tour2dadv_window_open (ggobid *gg);
static GtkWidget *window = NULL;

static gchar *pathlen_lbl[] = {"1/10", "1/5", "1/4", "1/3", "1/2", "1",
                               "2", "10", "Infinite"};
static void pathlen_cb (GtkWidget *w, ggobid *gg)
{
  gint indx = gtk_combo_box_get_active(GTK_COMBO_BOX(w));
  g_printerr ("cbd: %s\n", pathlen_lbl[indx]);
}

static gchar *interp_lbl[] = {"Geodesic", "Householder", "Givens"};
static void interp_cb (GtkWidget *w, ggobid *gg)
{
  gint indx = gtk_combo_box_get_active(GTK_COMBO_BOX(w));
  g_printerr ("cbd: %s\n", interp_lbl[indx]);
}

static void localscan_cb (GtkToggleButton *button)
{
  g_printerr ("local scan: %d\n", button->active);
}

static void step_cb (GtkToggleButton *tgl, GtkWidget *btn)
{
  g_printerr ("step: %d\n", tgl->active);
  gtk_widget_set_sensitive (btn, tgl->active);
}
static void go_cb (GtkButton *button, ggobid *gg)
{
  displayd *dsp = gg->current_display; 
  tour2d_do_step (dsp, gg);
}

static void storebases_cb (GtkToggleButton *button)
{
  g_printerr ("store bases: %d\n", button->active);
}

/* 
 * Section callbacks
*/
static void section_cb (GtkToggleButton *button)
{
  g_printerr ("local scan: %d\n", button->active);
}
static void epsilon_cb (GtkAdjustment *adj, gpointer cbd) {
  g_printerr ("epsilon %f\n", adj->value);
}

static void hide_cb (GtkWidget *w ) {
  gtk_widget_hide (w);
}
#endif

#ifdef TOUR_ADV_IMPLEMENTED
static void tour2dadv_window_open (ggobid *gg) {
  GtkWidget *vbox, *box, *btn, *opt, *tgl, *entry;
  GtkWidget *pathlen_opt, *vb, *hb, *lbl, *sbar, *notebook;
  GtkObject *adj;

  if (window == NULL) {
    
    window = gtk_window_new (GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title (GTK_WINDOW (window), "Advanced Tour");
    
    gtk_container_set_border_width (GTK_CONTAINER (window), 10);

    /* Create a new notebook, place the position of the tabs */
    notebook = gtk_notebook_new ();
    gtk_notebook_set_tab_pos (GTK_NOTEBOOK (notebook), GTK_POS_TOP);
    gtk_container_add (GTK_CONTAINER (window), notebook);

/*-- vbox to be placed in the notebook page --*/
    vbox = gtk_vbox_new (false, 2);
    gtk_container_set_border_width (GTK_CONTAINER (vbox), 4);

    /*-- local scan toggle --*/
    tgl = gtk_check_button_new_with_mnemonic ("_Local scan");
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
      "Perform the tour within a small local region", NULL);
    g_signal_connect (G_OBJECT (tgl), "toggled",
                        G_CALLBACK (localscan_cb), (gpointer) NULL);
    gtk_box_pack_start (GTK_BOX (vbox),
                        tgl, false, false, 1);

    /*-- Box to hold 'step' toggle and 'go' button --*/
    box = gtk_hbox_new (true, 2);

    tgl = gtk_check_button_new_with_mnemonic ("_Step");
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
      "Prepare to run the grand tour one step at a time", NULL);
    gtk_box_pack_start (GTK_BOX (box), tgl, true, true, 1);

    btn = gtk_button_new_with_mnemonic ("_Go");
    gtk_widget_set_sensitive (btn, false);
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), btn,
      "Take one step of the grand tour", NULL);
    gtk_box_pack_start (GTK_BOX (box), btn, true, true, 1);

    g_signal_connect (G_OBJECT (btn), "clicked",
                       G_CALLBACK (go_cb), (gpointer) gg);
    g_signal_connect (G_OBJECT (tgl), "toggled",
                       G_CALLBACK (step_cb), GTK_WIDGET (btn));

    gtk_box_pack_start (GTK_BOX (vbox), box, false, false, 1);

    lbl = gtk_label_new_with_mnemonic ("_General");
    gtk_notebook_append_page (GTK_NOTEBOOK (notebook), vbox, lbl);

    /*-- path length option menu inside frame --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vbox), hb, false, false, 0);

    lbl = gtk_label_with_mnemonic ("Path _length:");
    gtk_box_pack_start (GTK_BOX (hb), lbl, false, false, 0);

    pathlen_opt = gtk_combo_box_new_text ();
	gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), pathlen_opt);
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), pathlen_opt,
      "Set the path length", NULL);
    gtk_box_pack_end (GTK_BOX (hb), pathlen_opt, false, false, 0);
    populate_combo_box (pathlen_opt, pathlen_lbl, G_N_ELEMENTS(pathlen_lbl),
                          G_CALLBACK(pathlen_cb), gg);

    /*-- interpolation option menu inside hbox --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vbox), hb, false, false, 0);

    lbl = gtk_label_new_with_mnemonic ("_Interpolation: ");
    gtk_box_pack_start (GTK_BOX (hb), lbl, false, false, 0);

    opt = gtk_combo_box_new_text ();
	gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), opt);
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), opt,
      "Set the interpolation method", NULL);
    gtk_box_pack_end (GTK_BOX (hb), opt, false, false, 0);
    populate_combo_box (opt, interp_lbl, G_N_ELEMENTS(interp_lbl),
                          G_CALLBACK(interp_cb), gg);

/*-- tour history functions: vbox to be placed in the notebook page --*/
    vb = gtk_vbox_new (true, 0);
    gtk_container_set_border_width (GTK_CONTAINER (vb), 4);
    lbl = gtk_label_new_with_mnemonic ("_History");
    gtk_notebook_append_page (GTK_NOTEBOOK (notebook), vb, lbl);

    /*-- Store bases toggle --*/
    tgl = gtk_check_button_new_with_mnemonic ("_Store bases");
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
      "Store basis vectors", NULL);
    g_signal_connect (G_OBJECT (tgl), "toggled",
                        G_CALLBACK (storebases_cb), (gpointer) NULL);
    gtk_box_pack_start (GTK_BOX (vb), tgl, false, false, 0);

    /*-- Number of bases stored; a label and a text entry --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vb), hb, false, false, 0);

    llbl = gtk_label_new_with_mnemonic ("_Number of bases stored:");
    gtk_misc_set_alignment (GTK_MISC (lbl), 0, 0.5);
    gtk_box_pack_start (GTK_BOX (hb), lbl, false, false, 0);

    entry = gtk_entry_new ();
	gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), entry);
    gtk_widget_set_usize (entry,
                          gdk_string_width (entry->style->font, "XXXX"),
                          -1);
    gtk_box_pack_end (GTK_BOX (hb), entry, false, false, 0);

    /*-- Number of bases stored; a label and a text entry --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vb), hb, false, false, 0);

    lbl = gtk_label_new_with_mnemonic ("_Current base pair: ");
    gtk_misc_set_alignment (GTK_MISC (lbl), 0, 0.5);
    gtk_box_pack_start (GTK_BOX (hb), lbl, false, false, 0);

    entry = gtk_entry_new ();
	gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), entry);
    gtk_widget_set_usize (entry,
                          gdk_string_width (entry->style->font, "XXXX"),
                          -1);
    gtk_box_pack_start (GTK_BOX (hb), entry, false, false, 0);
    entry = gtk_entry_new ();
    gtk_widget_set_usize (entry,
                          gdk_string_width (entry->style->font, "XXXX"),
                          -1);
    gtk_box_pack_end (GTK_BOX (hb), entry, false, false, 0);

    /*-- Return to basis x --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vb), hb, false, false, 0);

    tgl = gtk_check_button_new_with_mnemonic ("_Return to basis");
    gtk_box_pack_start (GTK_BOX (hb), tgl, false, false, 0);

    entry = gtk_entry_new ();
    gtk_widget_set_usize (entry,
                          gdk_string_width (entry->style->font, "XXXX"),
                          -1);
    gtk_box_pack_end (GTK_BOX (hb), entry, false, false, 0);

    /*-- Display basis as bitmap --*/
    hb = gtk_hbox_new (false, 0);
    gtk_box_pack_start (GTK_BOX (vb), hb, false, false, 0);

    tgl = gtk_check_button_new_with_mnemonic ("_Display basis");
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
      "Display basis as bitmap", NULL);
    gtk_box_pack_start (GTK_BOX (hb), tgl, false, false, 0);

    entry = gtk_entry_new ();
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), entry,
      "Enter bitmap number", NULL);
    gtk_widget_set_usize (entry,
                          gdk_string_width (entry->style->font, "XXXX"),
                          -1);
    gtk_box_pack_end (GTK_BOX (hb), entry, false, false, 0);

/*-- section tour widgets: vbox to be placed in the notebook page --*/
    box = gtk_vbox_new (false, 0);
    gtk_container_set_border_width (GTK_CONTAINER (box), 4);
    lbl = gtk_label_new ("Section");
    gtk_notebook_append_page (GTK_NOTEBOOK (notebook), box, lbl);

    lbl = gtk_label_new_with_mnemonic ("_Section");
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), tgl,
      "Downlight points that are not within epsilon of the center plane",
      NULL);
    g_signal_connect (G_OBJECT (tgl), "toggled",
                        G_CALLBACK (section_cb), (gpointer) NULL);
    gtk_box_pack_start (GTK_BOX (box), tgl, false, false, 1);

    /*-- vbox for label and rangewidget --*/
    vb = gtk_vbox_new (true, 0);
    gtk_box_pack_start (GTK_BOX (box), vb, false, false, 1);

    lbl = gtk_label_new_with_mnemonic ("_Epsilon:");
    gtk_misc_set_alignment (GTK_MISC (lbl), 0, 0.5);
    gtk_box_pack_start (GTK_BOX (vb), lbl, false, false, 0);

    adj = gtk_adjustment_new (1.0, 0.0, 1.0, 0.01, .01, 0.0);
    g_signal_connect (G_OBJECT (adj), "value_changed",
                        G_CALLBACK (epsilon_cb), NULL);

    sbar = gtk_hscale_new (GTK_ADJUSTMENT (adj));
	gtk_label_set_mnemonic_widget(GTK_LABEL(lbl), sbar);
    gtk_tooltips_set_tip (GTK_TOOLTIPS (gg->tips), sbar,
      "Set the width of the cross-section",
      NULL);
    gtk_range_set_update_policy (GTK_RANGE (sbar), GTK_UPDATE_CONTINUOUS);
    gtk_scale_set_digits (GTK_SCALE (sbar), 2);
    gtk_scale_set_value_pos (GTK_SCALE (sbar), GTK_POS_BOTTOM);
    gtk_box_pack_start (GTK_BOX (vb), sbar, false, false, 0);

    /*-- Close button --*/
    btn = gtk_button_new_from_stock (GTK_STOCK_CLOSE);
    g_signal_connect_swapped (G_OBJECT (btn), "clicked",
                   G_CALLBACK(G_CALLBACK) (hide_cb), (GtkObject*) window);
    gtk_box_pack_start (GTK_BOX (vbox), btn, false, true, 2);
  }

  gtk_widget_show_all (window);

}
#endif

/*----------------------------------------------------------------------*/
/*                              I/O events                              */
/*----------------------------------------------------------------------*/

/*-- called from the Options menu --*/
void tour2d_io_cb (GtkWidget *w, gpointer *cbd) {
/*
  gchar *lbl = (gchar *) cbd;
  g_printerr ("cbd: %s\n", lbl);
*/
}

/*--------------------------------------------------------------------*/
/*      Handling keyboard and mouse events in the plot window         */
/*--------------------------------------------------------------------*/

static gint
key_press_cb (GtkWidget *w, GdkEventKey *event, splotd *sp)
{
  ggobid *gg = GGobiFromSPlot(sp);
  cpaneld *cpanel = &gg->current_display->cpanel;
  
/*-- add a key_press_cb in each mode, and let it begin with these lines --*/
  if (splot_event_handled (w, event, cpanel, sp, gg))
    return true;

  /*-- insert mode-specific key presses (if any) here --*/
  if (event->keyval == GDK_w || event->keyval == GDK_W) {
    /*-- turn pause on and off --*/
    GtkWidget *pnl = mode_panel_get_by_name(GGOBI(getPModeName)(TOUR2D), gg);
    GtkWidget *pause_button = NULL;

    pause_button = widget_find_by_name (pnl, "TOUR2D:pause_button");
    gtk_toggle_button_set_active (GTK_TOGGLE_BUTTON (pause_button),
      !cpanel->t2d.paused);
	return true;
  }

  return false;
}

static gint
motion_notify_cb (GtkWidget *w, GdkEventMotion *event, splotd *sp)
{
  ggobid *gg = GGobiFromSPlot(sp);
  gboolean button1_p, button2_p;

  mousepos_get_motion (w, event, &button1_p, &button2_p, sp);

  /*-- if neither button is pressed, we shouldn't have gotten the event --*/
  if (!button1_p && !button2_p)
    return false;

  tour2d_manip(sp->mousepos.x, sp->mousepos.y, sp, gg);
  return true;
}

static gint
button_press_cb (GtkWidget *w, GdkEventButton *event, splotd *sp)
{
  ggobid *gg = GGobiFromWidget(w, true);
  displayd *dsp = gg->current_display;
  cpaneld *cpanel = &dsp->cpanel;
  gboolean button1_p, button2_p;

  mousepos_get_pressed (w, event, &button1_p, &button2_p, sp);
  if (cpanel->t2d.manip_mode != MANIP_OFF) 
  {
    sp->motion_id = g_signal_connect (G_OBJECT (sp->da),
                                      "motion_notify_event",
                                      G_CALLBACK(motion_notify_cb),
                                      (gpointer) sp);
    tour2d_manip_init(sp->mousepos.x, sp->mousepos.y, sp);
  }

  return true;
}
static gint
button_release_cb (GtkWidget *w, GdkEventButton *event, splotd *sp)
{
  gboolean retval = true;
  GdkModifierType state;

  gdk_window_get_pointer (w->window, &sp->mousepos.x, &sp->mousepos.y, &state);

  tour2d_manip_end(sp);

  gdk_pointer_ungrab (event->time);

  return retval;
}

void
tour2d_event_handlers_toggle (splotd *sp, gboolean state) {
  displayd *display = (displayd *) sp->displayptr;

  if (state == on) {
    if(GGOBI_IS_WINDOW_DISPLAY(display) && GGOBI_WINDOW_DISPLAY(display)->useWindow)
      sp->key_press_id = g_signal_connect (G_OBJECT (GGOBI_WINDOW_DISPLAY(display)->window),
        "key_press_event",
        G_CALLBACK(key_press_cb),
        (gpointer) sp);
    sp->press_id = g_signal_connect (G_OBJECT (sp->da),
                                       "button_press_event",
                                       G_CALLBACK(button_press_cb),
                                       (gpointer) sp);
    sp->release_id = g_signal_connect (G_OBJECT (sp->da),
                                         "button_release_event",
                                         G_CALLBACK(button_release_cb),
                                         (gpointer) sp);
  } else {
    disconnect_key_press_signal (sp);
    disconnect_button_press_signal (sp);
    disconnect_button_release_signal (sp);
  }
}
