GX-FILL-LOW.
       PERFORM VARYING GX-I FROM 256 BY -1
           UNTIL GX-FILL-LOW-A (GX-I) NOT EQUAL SPACE
           MOVE LOW-VALUE TO GX-FILL-LOW-A (GX-I)
       END-PERFORM.
