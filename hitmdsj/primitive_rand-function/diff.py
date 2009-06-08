"""
diff the two output files and check if every line is the same ...
"""
if __name__ == "__main__":
    output_c = open('output_c')
    output_java = open('output_java')

    for line in output_c:
        assert line.strip() == output_java.next().strip()
        
    print "all ok ..."