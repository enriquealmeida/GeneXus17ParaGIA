GX-TTOC.
    IF YY OF GX-TTOC-DT EQUAL 1 AND
       MM OF GX-TTOC-DT EQUAL 1 AND
       DD OF GX-TTOC-DT EQUAL 1

       MOVE ZERO TO YY OF GX-TTOC-DT
                    MM OF GX-TTOC-DT
                    DD OF GX-TTOC-DT
    END-IF

    MOVE 1 TO GX-TTOC-POINTER
    MOVE SPACES TO GX-TTOC-C
    EVALUATE GX-TTOC-DLEN
       WHEN 0
           CONTINUE
       WHEN 8
          EVALUATE TRUE
             WHEN GX-TTOC-YMD
                STRING YY OF GX-TTOC-DT(3:2)
                       '/'
                       MM OF GX-TTOC-DT
                       '/'
                       DD OF GX-TTOC-DT
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER
                       
             WHEN GX-TTOC-MDY
                STRING MM OF GX-TTOC-DT
                       '/'
                       DD OF GX-TTOC-DT
                       '/'
                       YY OF GX-TTOC-DT(3:2)
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER

             WHEN GX-TTOC-DMY
                STRING DD OF GX-TTOC-DT
                       '/'
                       MM OF GX-TTOC-DT
                       '/'
                       YY OF GX-TTOC-DT(3:2)
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER
          END-EVALUATE
          ADD 1 TO GX-TTOC-POINTER
       WHEN 10
          EVALUATE TRUE
             WHEN GX-TTOC-YMD
                STRING YY OF GX-TTOC-DT
                       '/'
                       MM OF GX-TTOC-DT
                       '/'
                       DD OF GX-TTOC-DT
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER
                       
             WHEN GX-TTOC-MDY
                STRING MM OF GX-TTOC-DT
                       '/'
                       DD OF GX-TTOC-DT
                       '/'
                       YY OF GX-TTOC-DT
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER

             WHEN GX-TTOC-DMY
                STRING DD OF GX-TTOC-DT
                       '/'
                       MM OF GX-TTOC-DT
                       '/'
                       YY OF GX-TTOC-DT
                   DELIMITED BY SIZE
                   INTO GX-TTOC-C
                   POINTER GX-TTOC-POINTER
          END-EVALUATE
          ADD 1 TO GX-TTOC-POINTER
    END-EVALUATE

    IF GX-TTOC-12H
       IF HH OF GX-TTOC-DT NOT LESS 12
          MOVE ' PM' TO GX-TTOC-AMPM-SYM
          IF HH OF GX-TTOC-DT NOT EQUAL 12
             SUBTRACT 12 FROM HH OF GX-TTOC-DT
          END-IF
       ELSE
          MOVE ' AM' TO GX-TTOC-AMPM-SYM
       END-IF
    ELSE
       MOVE '   ' TO GX-TTOC-AMPM-SYM
    END-IF

    EVALUATE GX-TTOC-TLEN
       WHEN 2
          STRING HH OF GX-TTOC-DT
                 GX-TTOC-AMPM-SYM
             DELIMITED BY SIZE
             INTO GX-TTOC-C
             POINTER GX-TTOC-POINTER
       WHEN 5
          STRING HH OF GX-TTOC-DT
                 ':'
                 MIN OF GX-TTOC-DT
                 GX-TTOC-AMPM-SYM
             DELIMITED BY SIZE
             INTO GX-TTOC-C
             POINTER GX-TTOC-POINTER
       WHEN 8
          STRING HH OF GX-TTOC-DT
                 ':'
                 MIN OF GX-TTOC-DT
                 ':'
                 SS OF GX-TTOC-DT
                 GX-TTOC-AMPM-SYM
             DELIMITED BY SIZE
             INTO GX-TTOC-C
             POINTER GX-TTOC-POINTER
    END-EVALUATE.
