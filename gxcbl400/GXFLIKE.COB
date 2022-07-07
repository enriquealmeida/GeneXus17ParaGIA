GX-CALL-LIKE.

    INSPECT GX-LIKE-PTRN REPLACING ALL ' ' BY '*'
    IF GX-LIKE-PTRN NOT EQUAL ALL '*'
       CALL 'GXFLIKE2' USING GX-LIKE-STRING
                             GX-LIKE-STRING-LEN
                             GX-LIKE-PTRN
                             GX-LIKE-PTRN-LEN
                             GX-LIKE-RESULT
    ELSE
       MOVE 1 TO GX-LIKE-RESULT
    END-IF
    CONTINUE.
