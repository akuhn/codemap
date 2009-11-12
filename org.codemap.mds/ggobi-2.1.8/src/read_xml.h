/* read_xml.h */
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

#ifndef READ_XML_H
#define READ_XML_H

#include "vars.h"

#include <libxml/parser.h>

enum xmlDataState {
  TOP = 0,
  DATASET, DESCRIPTION,
  RECORD, RECORDS, VARIABLES, VARIABLE,
  COLORMAP, COLOR,
  REAL_VARIABLE, CATEGORICAL_VARIABLE,
  INTEGER_VARIABLE, COUNTER_VARIABLE, UNIFORM_VARIABLE,
  CATEGORICAL_LEVELS, CATEGORICAL_LEVEL,
  COLORSCHEME,
  BRUSHSTYLE,
  REAL, INTEGER, STRING, NA,
  QUICK_HELP,
  EDGES, EDGE,
/* HELP, DESCRIPTION */
  UNKNOWN
};

typedef enum xmlDataState XmlTagType;

typedef struct {

  int color;
  int glyphType;
  int glyphSize;
  int edgeWidth;  /*-- this has no home in ggobi --*/
  int hidden;

} DataOptions;

typedef struct _XMLUserData {
  enum xmlDataState state;
  gint current_variable;        /* Indexes the current variable. */
  gint current_record;          /* Indexes the record we are currently working on. */
  gint current_element;         /* Indexes the values within a record. */
  gint current_level;           /* */

  gint current_color;           /* The index of the current element
                                   being processed in the colormap */

  xmlChar *recordString;
  int recordStringLength;

  /* Flag that says we are reading color entries from another file via a
   * sub-parser.  This allows us to reuse the same instance of user data
   * and the same handlers.
   */
  gboolean reading_colormap_file_p;

  /* A boolean indicating whether the transformation name of a variable
     was stored as an attribute.
   */
  gboolean variable_transform_name_as_attribute;

  /* The ggobi instance that is being initialized. */
  ggobid *gg;

  InputDescription *input;

  /* The current data object to which new records are added. */
  GGobiData *current_data;
  // The list of all datad's read.
  GSList *dlist; 

  /* Flag indicating whether we should convert
     char arrays into null-terminated strings
     before passing them to the sub-handlers
     (e.g. setColorValue, setVariableName).
   */
  gboolean terminateStrings_p;

  /* The datasets global missing value identifier. */
  gchar *NA_identifier;
  /* The identifier for a missing value that is currently in effect.
     This might be specified per record and will be discarded
     at the end of that record.
     We could also do this columnwise.
   */
  gchar *current_NA_identifier;

  /* A set of values that apply to records when an attribute
     is not specified for that specific record but is set
     in the ggobidata tag.
   */
  DataOptions defaults;

  /* Local set of record identifiers that are used here
     for matching purposes when specifying edges.
     These are not set in the datad structure and 
     are different from the record's label attribute.
     Currently these are used to verify that the id's are unique
     within a dataset.
   */
  GHashTable *idNamesTable;

  GHashTable *idTable;
  gboolean usesStringIds;  /* this is now unused; it's always true */

  gint recordLabelsVariable;

  GHashTable **autoLevels;

  gint counterVariableIndex;

  /* Reference to the handlers being used as callbacks.
     Need this so that we can re-specify it when creating
     new sub-parsers.
   */
  xmlSAXHandlerPtr handlers;


  /*
     The number of datasets to expect within the file.
   */
  int expectedDatasetCount;

  xmlParserCtxtPtr parser;

} XMLParserData;


#ifdef __cplusplus
extern "C" {
#endif

  enum xmlDataState tagType(const xmlChar * name, gboolean endTag);
  gboolean newVariable(const xmlChar ** attrs, XMLParserData * data,
                       const xmlChar * tagName);
  gboolean newEdgeVariable(const xmlChar ** attrs, XMLParserData * data);
  gboolean setDatasetInfo(const xmlChar ** attrs, XMLParserData * data);
  gboolean setGeneralInfo(const xmlChar ** attrs, XMLParserData * data);
  gboolean allocVariables(const xmlChar ** attrs, XMLParserData * data);
  gboolean newRecord(const xmlChar ** attrs, XMLParserData * data);
  gboolean setDataset(const xmlChar ** attrs, XMLParserData * parserData, enum xmlDataState);
  gboolean setBrushStyle(const xmlChar ** attrs, XMLParserData * parserData);

  gboolean setRecordValues(XMLParserData * data, const xmlChar * line,
                           gint len, gint ncols);
  gboolean setVariableName(XMLParserData * data, const xmlChar * name,
                           gint len);

  gboolean setDefaultDatasetValues(const xmlChar ** attrs,
                                   XMLParserData * data);

  const xmlChar *skipWhiteSpace(const xmlChar * ch, gint * len);

  const gchar *getAttribute(const xmlChar ** attrs, gchar * name);


  void xml_warning(const gchar * attribute, const gchar * value,
                   const gchar * msg, XMLParserData * data);

  void initParserData(XMLParserData * data, xmlSAXHandlerPtr handler,
                      ggobid * gg);

  gboolean setGlyph(const xmlChar ** attrs, XMLParserData * data, gint i);
  gboolean setColor(const xmlChar ** attrs, XMLParserData * data, gint i);



  void categoricalLevels(const xmlChar ** attrs, XMLParserData * data);
  int setLevelIndex(const xmlChar ** attrs, XMLParserData * data);
  void addLevel(XMLParserData * data, const char *c, int len);

  GSList * data_xml_read(InputDescription * desc, ggobid * gg);

  gboolean setHidden(const xmlChar ** attrs, XMLParserData * data, gint i);

  int asInteger(const gchar * tmp);
  double asNumber(const char *sval);
  gboolean asLogical(const gchar * sval);

  GGobiData *getCurrentXMLData(XMLParserData * parserData);

  gboolean readXMLRecord(const xmlChar ** attrs, XMLParserData * data);

  gchar * intern(XMLParserData *, const char * el);
  
  gboolean isXMLFile (const gchar * fileName, ggobid * gg, GGobiPluginInfo * info);

#ifdef __cplusplus
}
#endif
#endif
