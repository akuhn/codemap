package edu.berkeley.guir.prefuse.graph.io;

import java.io.*;
import java.util.*;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * Reads in trees from HDir formatted files.
 * 
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class HDirTreeReader extends AbstractTreeReader {

	private Vector fieldNameList;
	private String textFieldName = "label";
	private int levels;
	private double sizes;
	private double isizes;
	private String HREFs;
	private String types;
	private String TEXTs;
	private boolean isdir;
	private int dircnt;
	private double dirtotal;
	private double areimages;
	private double areaudio;
	private double arehtml;
	private String dirfile;
	private int yvals;
	private int dirvals;
	private boolean istext;
	private boolean isdisplayed;
	private boolean isopen;
	private boolean searchHit;
	private StreamTokenizer t = null;
	private double totalsize = 0; // ** size on HDIR line
	private String HREFserver;
	private String HREFbase;
	private int index = 0;
	private int count = 0;

	private DefaultTreeNode root;

    /**
     * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.io.InputStream)
     */
    public Tree loadTree(InputStream is) throws IOException {
    	count = 0;
		fieldNameList = new Vector();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		readDataFile(br);
		System.out.println("Read in tree with "+(root.getDescendantCount()+1)+" nodes.");
        return new DefaultTree(root);
    } //

    //// =====================================================================
    //// == FILE PARSING METHODS =============================================

    public void readDataFile(BufferedReader d) {
        String line, value;
        String aTEXT;
        int currentLevel = 0;
        fieldNameList.addElement(textFieldName);

        //Restrictions on data format:
        //  <R> and <D> items must not be broken into multiple lines

        index = -1; // initialize index value
        DefaultTreeNode node = null;
        boolean firstTime = true;
        int c, c2;

        try {
            t = new StreamTokenizer(d);
            t.resetSyntax();
            t.whitespaceChars(0, ' ');
            t.quoteChar('"');
            t.wordChars('a', 'z');
            t.wordChars('A', 'Z');
            t.wordChars('\'', '\'');
            t.wordChars('\\', '\\');
            t.wordChars('/', '/');
            t.wordChars('(', '(');
            t.wordChars(')', ')');
            t.wordChars('0', '9');
            t.wordChars('.', '.');
            t.wordChars('+', '+');
            t.wordChars('-', '-');
            t.wordChars(':', ':');
            t.wordChars(';', ';');
            t.wordChars('~', '~');
            t.wordChars('*', '*');
            t.wordChars('#', '#');
            t.wordChars('<', '<');
            t.ordinaryChars('>', '>');
            t.ordinaryChars(',', ',');
            out : while (true) {
                switch (c = t.nextToken()) {
                    case StreamTokenizer.TT_EOF :
                        break out;
                    case '"' :
                    case StreamTokenizer.TT_WORD :
                        String Tag = t.sval.toLowerCase();
                        if (Tag.equals("<hdir")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (c2 == '<') {
                                t.pushBack();
                                break;
                            } else if (c2 == StreamTokenizer.TT_WORD) {
                                if (t.sval.startsWith("<"))
                                    t.pushBack();
                                else
                                    totalsize =
                                        Double.valueOf(t.sval).doubleValue();
                            } else {
                                t.pushBack();
                                break;
                            }
                        } else if (Tag.equals("</hdir")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                        } else if (Tag.equals("<server")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            if (((c2 = t.nextToken())
                                == StreamTokenizer.TT_WORD)
                                || (c2 == '"'))
                                HREFserver = t.sval;
                        } else if (Tag.equals("<base")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            if (((c2 = t.nextToken())
                                == StreamTokenizer.TT_WORD)
                                || (c2 == '"'))
                                HREFbase = t.sval;
                        } else if (Tag.equals("<directorytree")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            //directorytree=true;
                            //dironside=true;
                        } else if (Tag.equals("<r")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            index += 1;
                            levels = currentLevel;
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (c2 == StreamTokenizer.TT_WORD)
                                sizes = Double.valueOf(t.sval).doubleValue();
                            else {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) != ',') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (
                                (c2 == StreamTokenizer.TT_WORD) || (c2 == '"'))
                                HREFs = t.sval;
                            else {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) != ',') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (
                                (c2 == StreamTokenizer.TT_WORD) || (c2 == '"'))
                                types = t.sval;
                            else {
                                t.pushBack();
                                break;
                            }
                            aTEXT = HREFs;
                            if ((c2 = t.nextToken()) != ',')
                                t.pushBack();
                            else if (
                                (c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                t.pushBack();
                            else if (
                                (c2 == StreamTokenizer.TT_WORD)
                                    || (c2 == '"')) {
                                if (t.sval.startsWith("<"))
                                    t.pushBack();
                                else if (!t.sval.equals(""))
                                    aTEXT = t.sval;
                            } else
                                t.pushBack();
                            TEXTs = aTEXT;
                            //TEXTs
                            node = new DefaultTreeNode();
							node.setAttribute("id", String.valueOf(count++));
                            node.setAttribute(textFieldName, TEXTs);
                            isizes = 0;
                            if ((c2 = t.nextToken()) == ',') {
                                if ((c2 = t.nextToken())
                                    == StreamTokenizer.TT_EOF)
                                    break out;
                                else if (c2 == StreamTokenizer.TT_EOL)
                                    break;
                                else if (c2 == StreamTokenizer.TT_WORD) {
                                    isizes =
                                        Double.valueOf(t.sval).doubleValue();
                                }
                            } else
                                t.pushBack();

                        } else if (Tag.equals("<hl")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }                            
                            readLevel(d, currentLevel, t, node);
                            if (firstTime) {
                                firstTime = false;
                                root = node;                                
                            }
                        } else {
                            //System.err.println ("readDataFile -- unexpected input : "+Tag);
                            }
                        break;
                    default :
                        //System.err.println ("readDataFile -- unexpected token : "+c);
                        } //end switch
            } //end while
        } catch (Exception e) {
            System.err.println("Caught exception in readDataFile " + e);
            e.printStackTrace();
        }
    }

    private void readLevel(
        BufferedReader d,
        int currentLevel,
        StreamTokenizer t,
        DefaultTreeNode parent) {
        String aTEXT;
        String line, value;
        String dirprefix = "";
        int c, c2;

        currentLevel++; // Increment level
        isdir = true;
        try {
            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF) {
                t.pushBack();
                return;
            }
            if (c2 != StreamTokenizer.TT_EOL)
                if ((c2 == StreamTokenizer.TT_WORD) || (c2 == '"'))
                    dirprefix = t.sval;
            
            DefaultTreeNode node = null;
            out : while (true) {
                switch (c = t.nextToken()) {
                    case StreamTokenizer.TT_EOF :
                        break out;
                    case '"' :
                    case StreamTokenizer.TT_WORD :
                        String Tag = t.sval.toLowerCase();
                        if (Tag.equals("<r")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            index += 1;
                            levels = currentLevel;
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (c2 == StreamTokenizer.TT_WORD)
                                sizes = Double.valueOf(t.sval).doubleValue();
                            else {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) != ',') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (
                                (c2 == StreamTokenizer.TT_WORD) || (c2 == '"'))
                                HREFs = t.sval;
                            else {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) != ',') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                break;
                            else if (
                                (c2 == StreamTokenizer.TT_WORD) || (c2 == '"'))
                                types = t.sval;
                            else {
                                t.pushBack();
                                break;
                            }
                            aTEXT = HREFs;
                            if ((c2 = t.nextToken()) != ',')
                                t.pushBack();
                            else if (
                                (c2 = t.nextToken()) == StreamTokenizer.TT_EOF)
                                break out;
                            else if (c2 == StreamTokenizer.TT_EOL)
                                t.pushBack();
                            else if (
                                (c2 == StreamTokenizer.TT_WORD)
                                    || (c2 == '"')) {
                                if (t.sval.startsWith("<"))
                                    t.pushBack();
                                else if (!t.sval.equals(""))
                                    aTEXT = t.sval;
                            } else
                                t.pushBack();
                            TEXTs = aTEXT;

                            node = new DefaultTreeNode(); 
							node.setAttribute("id", String.valueOf(count++));                           
                            node.setAttribute(textFieldName, TEXTs);
                            isizes = 0;

                            if ((c2 = t.nextToken()) == ',') {
                                if ((c2 = t.nextToken())
                                    == StreamTokenizer.TT_EOF)
                                    break out;
                                else if (c2 == StreamTokenizer.TT_EOL)
                                    break;
                                else if (c2 == StreamTokenizer.TT_WORD) {
                                    isizes =
                                        Double.valueOf(t.sval).doubleValue();
                                }
                            } else
                                t.pushBack();

                            parent.addChild(new DefaultEdge(parent, node));

                        } else if (Tag.equals("<hl")) {

                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            readLevel(d, currentLevel, t, node);

                        } else if (Tag.equals("</hl")) {
                            if ((c2 = t.nextToken()) != '>') {
                                t.pushBack();
                                break;
                            }
                            if ((c2 = t.nextToken())
                                == StreamTokenizer.TT_EOF) {
                                t.pushBack();
                                return;
                            }
                            if (c2 != StreamTokenizer.TT_EOL) {
                                if ((c2 == StreamTokenizer.TT_WORD)
                                    || (c2 == '"')) {
                                    String dirprefix2 = t.sval;
                                    if (dirprefix2.startsWith("<"))
                                        t.pushBack();
                                    else if (!dirprefix.equals(dirprefix2)) {
                                        //System.err.println("readLevel -- after / HL unexpected string : "+dirprefix2);
                                        }
                                }
                            }
                            return; // End this level
                        } else {
                            //System.err.println ("readLevel -- unexpected input : "+Tag);
                            }
                        break;
                    default :
                        //System.err.println ("readLevel -- unexpected token : "+c);
                        }
            }
        } catch (Exception e) {
            System.err.println(
                "readLevel ** Exception: " + e + " " + currentLevel);
            e.printStackTrace();
        }
    } //

    //// == END FILE PARSING METHODS =========================================
    //// =====================================================================

} // end of class HDirTreeReader
