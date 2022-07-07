GX-DUPAPOST.
    MOVE 0 TO GX-DA-OUT-L
    MOVE 0 TO GX-DA-F
    PERFORM VARYING GX-DA-I FROM 1 BY 1
            UNTIL GX-DA-I > GX-DA-IN-L OR
                  GX-DA-OUT-L > 2999
       EVALUATE GX-DA-IN-C (GX-DA-I:1) ALSO GX-DA-F
          WHEN X'00' ALSO ANY
             COMPUTE GX-DA-F = 1 - GX-DA-F
          WHEN '''' ALSO 1
             PERFORM VARYING GX-DA-J FROM GX-DA-IN-M BY -1
                     UNTIL GX-DA-J < 1
                     AFTER GX-DA-K FROM 2 BY -1
                     UNTIL GX-DA-K < 1 OR
                           GX-DA-OUT-L > 2999
                ADD 1 TO GX-DA-OUT-L
                MOVE '''' TO GX-DA-OUT-C ( GX-DA-OUT-L:1)
             END-PERFORM
          WHEN OTHER
             ADD 1 TO GX-DA-OUT-L
             MOVE GX-DA-IN-C (GX-DA-I:1) TO GX-DA-OUT-C ( GX-DA-OUT-L:1)
       END-EVALUATE
    END-PERFORM.
