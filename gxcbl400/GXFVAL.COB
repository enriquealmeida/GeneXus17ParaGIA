GX-VAL.
    MOVE 0 TO GX-VAL-INT
    MOVE 0 TO GX-VAL-DEC
    MOVE 0 TO GX-VAL-DECCNT
    IF GX-VAL-STR NOT = SPACES
       MOVE 1 TO GX-VAL-IDX
       PERFORM TEST AFTER
       UNTIL GX-VAL-W-STR NOT = SPACES

          UNSTRING GX-VAL-STR
             DELIMITED BY ALL ' '
             INTO GX-VAL-W-STR
             POINTER GX-VAL-IDX
          END-UNSTRING
       END-PERFORM
       UNSTRING GX-VAL-W-STR
          INTO GX-VAL-SIGN
       END-UNSTRING
       MOVE 1 TO GX-VAL-PTR
       IF GX-VAL-SIGN = '+' OR
          GX-VAL-SIGN = '-'
          MOVE 2 TO GX-VAL-PTR
       END-IF
       MOVE GX-VAL-PTR TO GX-VAL-IDX
       PERFORM TEST AFTER
          UNTIL GX-VAL-CHAR NOT NUMERIC
          UNSTRING GX-VAL-W-STR
             INTO GX-VAL-CHAR
             POINTER GX-VAL-IDX
          END-UNSTRING
       END-PERFORM
       UNSTRING GX-VAL-W-STR
          DELIMITED BY GX-VAL-CHAR
          INTO GX-VAL-INT
          POINTER GX-VAL-PTR
       END-UNSTRING
       IF GX-VAL-CHAR = '.' OR
          GX-VAL-CHAR = ','
          MOVE GX-VAL-PTR TO GX-VAL-IDX
          PERFORM TEST AFTER
             UNTIL GX-VAL-CHAR NOT NUMERIC
             UNSTRING GX-VAL-W-STR
                INTO GX-VAL-CHAR
                POINTER GX-VAL-IDX
             END-UNSTRING
          END-PERFORM
          UNSTRING GX-VAL-W-STR
             DELIMITED BY GX-VAL-CHAR
             INTO GX-VAL-DEC
             COUNT IN GX-VAL-DECCNT
             POINTER GX-VAL-PTR
          END-UNSTRING
       END-IF
       IF GX-VAL-SIGN = '-'
          COMPUTE GX-VAL-INT = GX-VAL-INT * -1
          COMPUTE GX-VAL-DEC = GX-VAL-DEC * -1
       END-IF
    END-IF
    COMPUTE GX-VAL-VAL = GX-VAL-INT +
                         GX-VAL-DEC /
                         10 ** GX-VAL-DECCNT
    CONTINUE.
