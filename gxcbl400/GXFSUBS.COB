GX-SUBSTR.

    IF GX-SUBSTR-START > 0 AND GX-SUBSTR-START < 257 AND
       GX-SUBSTR-LEN > 0 AND GX-SUBSTR-LEN < 257
       IF GX-SUBSTR-START + GX-SUBSTR-LEN > 256
          COMPUTE GX-SUBSTR-LEN = 256 - GX-SUBSTR-START
       END-IF
       MOVE GX-SUBSTR-STRING ( GX-SUBSTR-START : GX-SUBSTR-LEN) TO GX-SUBSTR-OUT
    ELSE
       MOVE SPACES TO GX-SUBSTR-OUT
    END-IF.

