package ch.akuhn.com.mathworks.matlab;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/** <b>Partial implementation!</b>
 * <P>
 * In MATLAB Version 5, a MAT-file is made up of a 128-byte header followed by
 * one or more data elements. Each data element is composed of an 8-byte tag
 * followed by the data in the element. The tag specifies the number of bytes in
 * the data element and how these bytes should be interpreted; that is, should
 * the bytes be read as 16-bit values, 32-bit values, floating point values or
 * some other data type.
 * <P>
 * By using tags, the Version 5 MAT-file format provides quick access to
 * individual data elements within a MAT-file. You can move through a MAT-file
 * by finding a tag and then skipping ahead the specified number of bytes until
 * the next tag.
 * 
 * @author akuhn
 * 
 */
public class OpenMatlab5 {

    public static void main(String[] args) throws IOException {
        File file = new File("swiss_roll_data.matlab5");
        FileChannel channel = new FileInputStream(file).getChannel();
        ByteBuffer scan = channel.map(MapMode.READ_ONLY,0,channel.size());
        scan.order(ByteOrder.BIG_ENDIAN);
        /*
         * MATLAB Version 5 MAT-files begin with a 128-byte header made up of a
         * 124 byte text field and two, 16-bit flag fields. <P> The first 124
         * bytes of the header can contain text data in human-readable form.
         * This text typically provides information that describes how the
         * MAT-file was created. For example, MAT-files created by MATLAB
         * include the following information in their headers: <UL> <LI>MATLAB
         * version <LI>Platform on which the file was created <LI>Date and time
         * the file was created </UL> <STRONG>Programming Note</STRONG> When
         * creating a MAT-file, you must write data in the first four bytes of
         * this header. MATLAB uses these bytes to determine if a MAT-file uses
         * a Version 5 format or a Version 4 format. If any of these bytes
         * contain a zero, MATLAB will incorrectly assume the file is a Version
         * 4 MAT-file.
         */
        byte[] chars = new byte[124];
        scan.get(chars);
        String text = new String(chars);
        System.out.println(text);
        /* Header Flag Fields 
The last four bytes in the header are divided into two, 16-bit flag fields (int16). 
Programming Note  Programs that create MAT-files always write data in 
their native machine format. Programs that read MAT-files are responsible for 
byte-swapping. 
Field Value 
Version Identifies the version of the MATLAB software used to 
create the MAT-file format. When creating a MAT-file, set 
this field to 0x0100. 
Endian 
Indicator 
Contains the two characters, M and I, written to the 
MAT-file in this order, as a 16-bit value. If, when read 
from the MAT-file as a 16-bit value, the characters appear 
in reversed order (IM rather than MI), it indicates that the 
program reading the MAT-file must perform 
byte-swapping to interpret the data in the MAT-file 
correctly. 
         * 
         */
        short version = scan.getShort();
        System.out.println(version);
        short letters = scan.getShort();
        char shouldBeM = (char) (letters >> 8);
        if (shouldBeM == 'I') scan.order(ByteOrder.LITTLE_ENDIAN);
            
        /* Data Element Format 
Each data element begins with an 8-byte tag followed immediately by the data 
in the element. Figure2 shows this format. (MATLAB also supports a 
compressed data element format. See page -9 for more information.) 
        *
        */
        /* Tag 
The 8-byte data element tag is composed of two, 32-bit fields: 
•Data Type 
•Number of Bytes 
Data Type. The Data Type field specifies how the data in the element should be 
interpreted, that is, its size and format. The MAT-file format supports many 
data types including signed and unsigned, 8-bit, 16-bit, 32-bit, and 64-bit data 
types and a special data type that represents MATLAB arrays. Table1 lists all 
these data types with the values used to specify them. The table also includes 
symbols that are used to represent these data types in the examples in this 
document. 
Table 1:  MAT-File Data Types 
MAT-File Data Type Value Symbol 
8 bit, signed 1 miINT8 
8 bit, unsigned 2 miUINT8 
16-bit, signed 3 miINT16 
16-bit, unsigned 4 miUINT16 
32-bit, signed 5 miINT32 
32-bit, unsigned 6 miUINT32 
IEEE 754 single format 7 miSINGLE 
Reserved 8 -- 
IEEE 754 double format 9 miDOUBLE 
Reserved 10 -- 
Reserved 11 -- 
64-bit, signed 12 miINT64
          Number of Bytes. The Number of Bytes field is a 32-bit value that specifies the 
number of bytes of data in the element. This value does not include the eight 
bytes of the data element’s tag. 
64-bit, unsigned 13 miUINT64 
MATLAB array 14 miMATRIX 
(For more information about this 
data type, see “Version 5 
MATLAB Array Data Element 
Formats” on page -10.) 
Table 1:  MAT-File Data Types 
MAT-File Data Type Value Symbol 
          * 
          */
        int type = scan.getInt();
        System.out.println(type);
        int size = scan.getInt();
        System.out.println(size-(8+8)-(8+8)-(8+8)-(8+8*3*20000));
        {
            assert 6 == scan.getInt() : "type = miUINT32";
            assert 8 == scan.getInt() : "size";
            int tag = scan.getInt();
            byte flags = (byte) (tag >> 8);
            byte class0 = (byte) tag;
            assert 0 == flags : "flags = none";
            assert 6 == class0 : "class = mxDOUBLE_CLASS";
            scan.getInt(); // undefined
        }
        {
            assert 5 == scan.getInt() : "type = miINT32";
            assert 8 == scan.getInt() : "size";
            int[] dim = new int[8/4];
            for (int i = 0; i < dim.length; i++) dim[i] = scan.getInt();
        }
        {
            assert 1 == scan.getInt() : "type = miINT8";
            assert 6 == scan.getInt() : "size";
            byte[] name0 = new byte[6];
            scan.get(name0);
            String name = new String(name0);
            System.out.println(name);
            scan.position(scan.position() + 2);
        }
        {
            assert 9 == scan.getInt() : "type = miDOUBLE";
            assert 3*20000*8 == scan.getInt() : "size";
            System.out.println(scan.remaining());
            double[][] data = new double[3][20000];
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = scan.getDouble();
                }
            }
        }
        {
            System.out.println(scan.remaining());
            System.out.println(scan.getInt());
            System.out.println(scan.getInt());

            System.out.println(scan.getInt());
            System.out.println(scan.getInt());
            System.out.println(scan.getInt());
            System.out.println(scan.getInt());

            System.out.println(scan.getInt());
            System.out.println(scan.getInt());
            System.out.println(scan.getInt());
            System.out.println(scan.getInt());

            System.out.println(scan.getInt());
            System.out.println(scan.getInt());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            System.out.println((char) scan.get());
            
        }
    }
    
}
