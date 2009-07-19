/* write_xml.c */
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

#include "write_xml.h"
/*
  Takes the current data in the specified ggobid structure
  and writes the XML file corresponding to this configuration.
  This can be used to convert the original data formats to XML, if
  that is desired.  (More likely to be correct than writing a Perl
  script!)  Alas, it can't be relied upon for all file conversions,
  because ggobi doesn't read all xgobi-style data files, with
  .lines as a prominent example.  ggobi also doesn't read
  xgobi-style .colors files.
 */
#include <string.h>
#include <strings.h>
#include "writedata.h"


XmlWriteInfo *updateXmlWriteInfo(GGobiData *d, ggobid *gg, XmlWriteInfo *info);

/* if a string contains an ampersand, write it as &amp; ... etc ... --*/
static void
write_xml_string(FILE *f, gchar *str)
{
  gchar *fmtstr = g_markup_printf_escaped("%s", str);
  fprintf(f, fmtstr);
  g_free(fmtstr);
}
static void
write_xml_string_fmt(FILE *f, gchar *fmt, gchar *str)
{
  gchar *fmtstr = g_markup_printf_escaped(fmt, str);
  fprintf(f, fmtstr);
  g_free(fmtstr);
}

gboolean
write_xml (const gchar *filename,  ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
  FILE *f;
  gboolean ok = false;
/*
 *GGobiData *d;
 *GSList *tmp = gg->d;
*/
  f = fopen (filename,"w");
  if (f == NULL) {
   return (false);
  }

  write_xml_stream (f, gg, filename, xmlWriteInfo);

  fclose(f);
  return ok;
}

gboolean
write_xml_stream (FILE *f, ggobid *gg, const gchar *filename, XmlWriteInfo *xmlWriteInfo)
{
 gint numDatasets, i;
 GGobiData *d;
 numDatasets = g_slist_length(gg->d);
 // g_printerr ("numDatasets %d\n", numDatasets);

 write_xml_header (f, -1, gg, xmlWriteInfo);

  for(i = 0; i < numDatasets; i++) {
    d = (GGobiData *) g_slist_nth_data(gg->d, i);
    if(xmlWriteInfo->useDefault)
      updateXmlWriteInfo(d, gg, xmlWriteInfo);
    write_xml_dataset(f, d, gg, xmlWriteInfo);
  }

  write_xml_footer(f, gg, xmlWriteInfo);
  return(true);
}

gboolean
write_xml_dataset(FILE *f, GGobiData *d, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
  if (d->edge.n && !d->ncols) {
    write_xml_edges(f, d, gg, xmlWriteInfo);
  } else {
    write_dataset_header (f, d, gg, xmlWriteInfo);
    write_xml_description (f, gg, xmlWriteInfo);
    write_xml_variables (f, d, gg, xmlWriteInfo);
    write_xml_records (f, d, gg, xmlWriteInfo);
    write_dataset_footer(f, gg, xmlWriteInfo);
  }

  return(true);
}

gboolean
write_xml_header (FILE *f, int numDatasets, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
 fprintf(f, "<?xml version=\"1.0\"?>");
 fprintf(f, "\n");
 fprintf(f, "<!DOCTYPE ggobidata SYSTEM \"ggobi.dtd\">");
 fprintf(f, "\n\n");

 if(numDatasets < 0)
    numDatasets = g_slist_length(gg->d);

 fprintf(f, "<ggobidata count=\"%d\">\n", numDatasets);

/* fflush(f);*/

 return(true);
}

gboolean
write_xml_description (FILE *f, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
 fprintf(f,"<description>\n");
/*XXX*/
 fprintf(f, "This is XML created by GGobi\n");

 fprintf(f,"</description>\n");

 return(true);
}

gboolean
write_xml_variables (FILE *f, GGobiData *d, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
  gint j;

  if (gg->save.column_ind == ALLCOLS) {
    fprintf(f,"<variables count=\"%d\">\n", d->ncols); 
    for(j = 0; j < d->ncols; j++) {
      write_xml_variable (f, d, gg, j, xmlWriteInfo);
      fprintf(f,"\n");
    }
  } else if (gg->save.column_ind == SELECTEDCOLS) {
    /*-- work out which columns to save --*/
    gint *cols = (gint *) g_malloc (d->ncols * sizeof (gint));
    gint ncols = selected_cols_get (cols, d, gg);
    if (ncols == 0)
      ncols = plotted_cols_get (cols, d, gg);
    fprintf(f,"<variables count=\"%d\">\n", ncols); 
    for(j = 0; j < ncols; j++) {
      write_xml_variable (f, d, gg, cols[j], xmlWriteInfo);
      fprintf(f,"\n");
    }

    g_free (cols);
  }

  fprintf(f,"</variables>\n"); 

  return(true);
}

gboolean
write_xml_variable(FILE *f, GGobiData *d, ggobid *gg, gint j,
  XmlWriteInfo *xmlWriteInfo)
{
  vartabled *vt = vartable_element_get (j, d);
  gchar* varname = g_strstrip(
    (gg->save.stage == TFORMDATA) ? 
      ggobi_data_get_transformed_col_name(d, j) : 
      ggobi_data_get_col_name(d, j)
  );
  
  if (vt->vartype == categorical) {
    gint k;
    write_xml_string_fmt(f, "  <categoricalvariable name=\"%s\"", varname);
    if (vt->nickname)
      write_xml_string_fmt(f, " nickname=\"%s\"", vt->nickname);
    fprintf(f, ">\n");
    fprintf(f, "    <levels count=\"%d\">\n", vt->nlevels);
    for (k=0; k<vt->nlevels; k++) {
      fprintf(f, "      <level value=\"%d\">",
              vt->level_values[k]);
      /* Add any needed html/xml markup to level names */
      write_xml_string(f, vt->level_names[k]);
      fprintf(f, "</level>\n");
    }

    fprintf(f, "    </levels>\n");
    fprintf(f, "  </categoricalvariable>");
  } else {
    fprintf(f, "   <");
    if (vt->vartype == real)    fprintf(f, "realvariable");
    if (vt->vartype == integer) fprintf(f, "integervariable");
    if (vt->vartype == counter) fprintf(f, "countervariable");

    write_xml_string_fmt(f, " name=\"%s\"", varname);
    if (vt->nickname) 
      write_xml_string_fmt(f, " nickname=\"%s\"", vt->nickname);
    fprintf(f, "/>");
  } 

  return(true);
}

/*
gboolean
write_edge_record_p (gint i, GGobiData *e, ggobid *gg)
{
 * If e is an edge set, then
 * loop over all other datads and test their rowids to decide
 * whether this case should be drawn.  Use sampled and hidden.
 * XXX  We can't really do this, because we don't know what 
 *      edgeset may have been associated with what nodeset.
  gboolean save_case = true;
  GGobiData *d;
  GSList *l;
  gint a, b;

  if (e->edge.n == e->nrows) {
    for (l = gg->d; l; l=l->next) {
      d = (GGobiData *) l->data;
      endpointsd *endpoints = resolveEdgePoints(e, d);
      if (endpoints) {
        if (!edge_endpoints_get (i, &a, &b, d, endpoints, e) ||
            !d->sampled.els[a] || !d->sampled.els[b] ||
            d->excluded.els[a] || d->excluded.els[b])
        {
          save_case = false;
          break;
        }
      }
    }
  }
  return save_case;
}
*/

static void
writeFloat(FILE *f, double value)
{
  fprintf(f, "<real>%g</real>", value); 
}
static void
writeInt(FILE *f, int value)
{
  fprintf(f, "<int>%d</int>", value); 
}

static void
writeEntry(FILE *f, vartyped vtype, double value) {
  switch (vtype) {
  case categorical:
  case integer:
  case counter:
    writeInt(f, (int) value);
  break;

  default:
    writeFloat (f, value);
  }
}

gboolean
write_xml_records(FILE *f, GGobiData *d, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
  gint i, j, m, n = 0;
  vartyped *vartypes;
  vartabled *vt;

  vartypes = (vartyped *) g_malloc(d->ncols * sizeof(vartyped));
  for (j=0; j<d->ncols; j++) {
    vt = vartable_element_get (j, d);
    vartypes[j] = vt->vartype;
  }


  /*-- figure out how many records we're about to save.  --*/
  if (gg->save.row_ind == ALLROWS)
    n = d->nrows;
  else if (gg->save.row_ind == DISPLAYEDROWS)
    n = d->nrows_in_plot;

  fprintf(f, "<records ");
  fprintf(f, "count=\"%d\"", n);
  if(xmlWriteInfo->useDefault) {
/*
   fprintf(f, " glyphSize=\"%s\"", xmlWriteInfo->defaultGlyphSizeName);
   fprintf(f, " glyphType=\"%s\"", xmlWriteInfo->defaultGlyphTypeName);
*/
    fprintf(f, " glyph=\"%s %s\"",
      xmlWriteInfo->defaultGlyphTypeName,
      xmlWriteInfo->defaultGlyphSizeName);
    fprintf(f, " color=\"%s\"", xmlWriteInfo->defaultColorName);
  }

  if (ggobi_data_has_missings(d)) {
    if (gg->save.missing_ind == MISSINGSNA)
      fprintf(f, " missingValue=\"%s\"", "na");
    /*-- otherwise write the "imputed" value --*/
  }
  fprintf(f, ">\n");


  if (gg->save.row_ind == ALLROWS) {
    for (i = 0; i < d->nrows; i++) {
      fprintf(f, "<record");
      write_xml_record (f, d, gg, i, vartypes, xmlWriteInfo);
      fprintf(f, "\n</record>\n");
    }
  } else {  /*-- if displaying visible rows only --*/
    for (i=0; i<d->nrows_in_plot; i++) {
      m = d->rows_in_plot.els[i];
      fprintf(f, "<record");
      write_xml_record (f, d, gg, m, vartypes, xmlWriteInfo);
      fprintf(f, "\n</record>\n");
    }
  }

  fprintf(f, "</records>\n");
  g_free (vartypes);
  return(true);
}


/*
 * I want this to write <edge> records as well as <record> records.
*/
gboolean
write_xml_record (FILE *f, GGobiData *d, ggobid *gg, gint i, 
  vartyped *vartypes, XmlWriteInfo *xmlWriteInfo)
{
  gint j;
  gchar *gstr, *gtypestr = NULL;

  /*-- ids if present --*/
  if (d->rowIds) {
     write_xml_string_fmt(f, " id=\"%s\"", d->rowIds[i]);
  }

  /*-- if the record is hidden, indicate that --*/
  if (d->hidden_now.els[i]) {
    fprintf(f, " hidden=\"true\"");
  }

  /*-- edges if present and requested --*/
  if (gg->save.edges_p && d->edge.n && i < d->edge.n) {
    write_xml_string_fmt(f, " source=\"%s\"", d->edge.sym_endpoints[i].a);
    write_xml_string_fmt(f, " destination=\"%s\"", d->edge.sym_endpoints[i].b);
  }

  if (d->rowlab && d->rowlab->data
      && (gstr = (gchar *) g_array_index (d->rowlab, gchar *, i))) 
  {
    fprintf(f, " label=\"");
    write_xml_string(f, gstr);
    fprintf(f, "\"");
  }

  if (!xmlWriteInfo->useDefault ||
      xmlWriteInfo->defaultColor != d->color.els[i])
  {
    fprintf(f, " color=\"%d\"", d->color.els[i]);
  }

/*
  fprintf(f, " glyphSize=\"%d\"", d->glyph[i].size);
  fprintf(f, " glyphType=\"%d\"", d->glyph[i].type);
*/
  if (!xmlWriteInfo->useDefault ||
     xmlWriteInfo->defaultGlyphType != d->glyph.els[i].type ||
     xmlWriteInfo->defaultGlyphSize != d->glyph.els[i].size)
  {
    switch (d->glyph.els[i].type) {
      case PLUS:
        gtypestr = "plus";
      break;
      case X:
        gtypestr = "x";
      break;
      case OR:
        gtypestr = "or";
      break;
      case FR:
        gtypestr = "fr";
      break;
      case OC:
        gtypestr = "oc";
      break;
      case FC:
        gtypestr = "fc";
      break;
      case DOT_GLYPH:
        gtypestr = ".";
      break;
      case UNKNOWN_GLYPH:
      default:
        gtypestr=NULL;
      break;
    }

    fprintf (f, " glyph=\"%s %d\"", gtypestr, d->glyph.els[i].size);
  }

  fprintf(f, ">\n");

  if (gg->save.column_ind == ALLCOLS && d->ncols > 0) {
    for(j = 0; j < d->ncols; j++) {
      /*-- if missing, figure out what to write --*/
      if (ggobi_data_has_missings(d) && 
          ggobi_data_is_missing(d, i, j) && 
          gg->save.missing_ind != MISSINGSIMPUTED)
      {
          fprintf (f, "<na/>");
      } else {  /*-- if not missing, just write the data --*/
        writeEntry (f, vartypes[j],
          (gg->save.stage == TFORMDATA) ? d->tform.vals[i][j] :
                                          d->raw.vals[i][j]);
      }
      if (j < d->ncols-1 )
        fprintf(f, " ");
     }
  } else if (gg->save.column_ind == SELECTEDCOLS && d->ncols > 0) {
    /*-- work out which columns to save --*/
    gint *cols = (gint *) g_malloc (d->ncols * sizeof (gint));
    gint k, ncols = selected_cols_get (cols, d, gg);
    if (ncols == 0)
      ncols = plotted_cols_get (cols, d, gg);
    for(k = 0; k < ncols; k++) {
      j = cols[k];
      if (ggobi_data_is_missing(d, i, j) &&
        gg->save.missing_ind != MISSINGSIMPUTED)
      {
          fprintf (f, "<na/>");
      } else {

        writeEntry (f, vartypes[j],
          (gg->save.stage == TFORMDATA) ? d->tform.vals[i][j] :
                                          d->raw.vals[i][cols[j]]);
      } 
      if (j < ncols-1 )
        fprintf(f, " ");
     }
     g_free (cols);
   }

 return (true);
}

gboolean
write_xml_edges (FILE *f, GGobiData *d, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
  gint i, j;
  vartyped *vartypes;
  vartabled *vt;

  if (d->edge.n < 1)
    return(true);
  /*
  fprintf(f, "<edges count=\"%d\" name=\"%s\">\n", d->edge.n,
  d->name); There seems to be a need to write the defaults in the case
  where we started with ascii data and added edges; in this case the
  edges are written as <edges> rather than as a new datad, and maybe
  that's the problem.   dfs
  */
  fprintf(f, "<edges count=\"%d\" ", d->edge.n);
  write_xml_string_fmt(f, "name=\"%s\" ", (gchar *)d->name);
  fprintf(f, "color=\"%d\" glyphType=\"%s\" glyphSize=\"%s\">\n",
    xmlWriteInfo->defaultColor,
    xmlWriteInfo->defaultGlyphTypeName, 
    xmlWriteInfo->defaultGlyphSizeName);

  vartypes = (vartyped *) g_malloc(d->ncols * sizeof(vartyped));
  for (j=0; j<d->ncols; j++) {
    vt = vartable_element_get (j, d);
    vartypes[j] = vt->vartype;
  }

  for(i = 0; i < d->edge.n; i++) {
    fprintf(f, "<edge");
    write_xml_record (f, d, gg, i, vartypes, xmlWriteInfo);
    fprintf(f, "</edge>\n");
  }
  fprintf(f, "</edges>\n");

  g_free (vartypes);
 return(true);
}

gboolean
write_dataset_header (FILE *f, GGobiData *d, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{


  fprintf(f,"<data ");
  write_xml_string_fmt(f, "name=\"%s\"", (gchar *)d->name);
  fprintf(f,">\n");

  return(true);
}

gboolean
write_dataset_footer(FILE *f, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
 fprintf(f,"</data>\n");
 return(true);
}

gboolean
write_xml_footer(FILE *f, ggobid *gg, XmlWriteInfo *xmlWriteInfo)
{
 fprintf(f,"</ggobidata>\n");
 return(true);
}

XmlWriteInfo *
updateXmlWriteInfo(GGobiData *d, ggobid *gg, XmlWriteInfo *info)
{
  int i, n, numGlyphSizes;
  gint *colorCounts, *glyphTypeCounts, *glyphSizeCounts, count;
  gchar *str;
  gint ncolors = gg->activeColorScheme->n;

  colorCounts = g_malloc0(sizeof(gint) * ncolors);
  glyphTypeCounts = g_malloc0(sizeof(gint) * UNKNOWN_GLYPH);
  numGlyphSizes = NGLYPHSIZES;
  glyphSizeCounts = g_malloc0(sizeof(gint) * numGlyphSizes);

  n = GGOBI(nrecords)(d);
  for(i = 0 ; i < n ; i++) {
    colorCounts[d->color.els[i]]++;
    glyphSizeCounts[d->glyph.els[i].size]++;
    glyphTypeCounts[d->glyph.els[i].type]++;
  }

  count = -1;
  for(i = 0 ; i < ncolors; i++) {
    if(colorCounts[i] > count) {
      info->defaultColor = i;
      count= colorCounts[i];
    }
  }

  count = -1;
  for(i = 0 ; i < UNKNOWN_GLYPH; i++) {
    if(glyphTypeCounts[i] > count) {
      info->defaultGlyphType = i;
      count= glyphTypeCounts[i];
    }
  }

  count = -1;
  for(i = 0 ; i < numGlyphSizes; i++) {
    if(glyphSizeCounts[i] > count) {
      info->defaultGlyphSize = i;
      count= glyphSizeCounts[i];
    }
  }

/*
  if(gg->colorNames && (str = gg->colorNames[info->defaultColor]) && str[0])
    info->defaultColorName = g_strdup(str);
  else {
*/
    info->defaultColorName = str = g_malloc(5 * sizeof(char));
    sprintf(str, "%d", info->defaultColor);
/*
  }
*/

  info->defaultGlyphSizeName = str = g_malloc(5 * sizeof(char));
  sprintf(str, "%d", info->defaultGlyphSize);

  str = (gchar *) GGOBI(getGlyphTypeName)(info->defaultGlyphType);  
  info->defaultGlyphTypeName = g_strdup(str);

  return(info);
}
