GX-CTOT.

    SET GX-CTOT-VALID TO TRUE
    MOVE 1 TO GX-CTOT-POINTER
    MOVE '0000' TO YY OF GX-CTOT-DT
    MOVE '00'   TO MM OF GX-CTOT-DT
    MOVE '00'   TO DD OF GX-CTOT-DT
    MOVE '00'   TO HH OF GX-CTOT-DT
    MOVE '00'   TO MIN OF GX-CTOT-DT
    MOVE '00'   TO SS OF GX-CTOT-DT

    IF GX-CTOT-DLEN GREATER 0
       UNSTRING GX-CTOT-C DELIMITED BY '/' OR
                                       '-' OR
                                       ALL ' '
          INTO GX-CTOT-BUFFER
               DELIMITER IN GX-CTOT-DELIM
               COUNT IN GX-CTOT-COUNT
          POINTER GX-CTOT-POINTER

       IF GX-CTOT-COUNT GREATER 4
          MOVE 2 TO GX-CTOT-MM-L
          MOVE 2 TO GX-CTOT-DD-L
          EVALUATE TRUE
             WHEN GX-CTOT-YMD
                EVALUATE GX-CTOT-COUNT
                   WHEN 6
                      MOVE GX-CTOT-BUFFER(1:2) TO YY OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(3:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:2) TO DD OF GX-CTOT-DT
                      MOVE 2 TO GX-CTOT-YY-L
                   WHEN 8
                      MOVE GX-CTOT-BUFFER(1:4) TO YY OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(7:2) TO DD OF GX-CTOT-DT
                      MOVE 4 TO GX-CTOT-YY-L
                   WHEN OTHER
                      SET GX-CTOT-DATE-LENGTH TO TRUE
                END-EVALUATE
             WHEN GX-CTOT-MDY
                EVALUATE GX-CTOT-COUNT
                   WHEN 6
                      MOVE GX-CTOT-BUFFER(1:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(3:2) TO DD OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:2) TO YY OF GX-CTOT-DT
                      MOVE 2 TO GX-CTOT-YY-L
                   WHEN 8
                      MOVE GX-CTOT-BUFFER(1:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(3:2) TO DD OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:4) TO YY OF GX-CTOT-DT
                      MOVE 4 TO GX-CTOT-YY-L
                   WHEN OTHER
                      SET GX-CTOT-DATE-LENGTH TO TRUE
                END-EVALUATE
             WHEN GX-CTOT-DMY
                EVALUATE GX-CTOT-COUNT
                   WHEN 6
                      MOVE GX-CTOT-BUFFER(1:2) TO DD OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(3:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:2) TO YY OF GX-CTOT-DT
                      MOVE 2 TO GX-CTOT-YY-L
                   WHEN 8
                      MOVE GX-CTOT-BUFFER(1:2) TO DD OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(3:2) TO MM OF GX-CTOT-DT
                      MOVE GX-CTOT-BUFFER(5:4) TO YY OF GX-CTOT-DT
                      MOVE 4 TO GX-CTOT-YY-L
                   WHEN OTHER
                      SET GX-CTOT-DATE-LENGTH TO TRUE
                END-EVALUATE
          END-EVALUATE
       ELSE
          MOVE 1 TO GX-CTOT-POINTER
          MOVE 0 TO GX-CTOT-YY-L
                    GX-CTOT-MM-L
                    GX-CTOT-DD-L
          EVALUATE TRUE
              WHEN GX-CTOT-YMD
                 UNSTRING GX-CTOT-C DELIMITED BY '/' OR '-'
                    INTO YY OF GX-CTOT-DT COUNT IN GX-CTOT-YY-L
                         MM OF GX-CTOT-DT COUNT IN GX-CTOT-MM-L
                    POINTER GX-CTOT-POINTER
     
                 UNSTRING GX-CTOT-C DELIMITED BY ALL ' '
                    INTO DD OF GX-CTOT-DT COUNT IN GX-CTOT-DD-L
                    POINTER GX-CTOT-POINTER
     
              WHEN GX-CTOT-MDY
                 UNSTRING GX-CTOT-C DELIMITED BY '/' OR '-'
                    INTO MM OF GX-CTOT-DT COUNT IN GX-CTOT-MM-L
                         DD OF GX-CTOT-DT COUNT IN GX-CTOT-DD-L
                    POINTER GX-CTOT-POINTER
     
                 UNSTRING GX-CTOT-C DELIMITED BY ALL ' '
                    INTO YY OF GX-CTOT-DT COUNT IN GX-CTOT-YY-L
                    POINTER GX-CTOT-POINTER
     
              WHEN GX-CTOT-DMY
                 UNSTRING GX-CTOT-C DELIMITED BY '/' OR '-'
                    INTO DD OF GX-CTOT-DT COUNT IN GX-CTOT-DD-L
                         MM OF GX-CTOT-DT COUNT IN GX-CTOT-MM-L
                    POINTER GX-CTOT-POINTER
     
                 UNSTRING GX-CTOT-C DELIMITED BY ALL ' '
                    INTO YY OF GX-CTOT-DT COUNT IN GX-CTOT-YY-L
                    POINTER GX-CTOT-POINTER
          END-EVALUATE
          IF GX-CTOT-YY-L LESS 1 OR
             GX-CTOT-YY-L GREATER 4 OR
             (GX-CTOT-DLEN = 8 AND GX-CTOT-YY-L GREATER 2)
             SET GX-CTOT-YY-LENGTH TO TRUE
          END-IF
          IF GX-CTOT-MM-L GREATER 2 OR GX-CTOT-MM-L LESS 1
             SET GX-CTOT-MM-LENGTH TO TRUE
          END-IF
          IF GX-CTOT-DD-L GREATER 2 OR GX-CTOT-DD-L LESS 1
             SET GX-CTOT-DD-LENGTH TO TRUE
          END-IF
       END-IF
    END-IF

    MOVE 0 TO GX-CTOT-COUNT
    UNSTRING GX-CTOT-C DELIMITED BY ':' OR
                                    'A' OR
                                    'P' OR
                                    ALL ' '
       INTO GX-CTOT-BUFFER
            DELIMITER IN GX-CTOT-DELIM
            COUNT IN GX-CTOT-COUNT
       POINTER GX-CTOT-POINTER

    IF GX-CTOT-COUNT GREATER 2
       EVALUATE GX-CTOT-COUNT
          WHEN 4
             MOVE GX-CTOT-BUFFER(1:2) TO HH OF GX-CTOT-DT
             MOVE GX-CTOT-BUFFER(3:2) TO MIN OF GX-CTOT-DT
          WHEN 6
             MOVE GX-CTOT-BUFFER(1:2) TO HH OF GX-CTOT-DT
             MOVE GX-CTOT-BUFFER(3:2) TO MIN OF GX-CTOT-DT
             MOVE GX-CTOT-BUFFER(5:2) TO SS OF GX-CTOT-DT
          WHEN OTHER
             SET GX-CTOT-HH-LENGTH TO TRUE
       END-EVALUATE
    ELSE
       IF GX-CTOT-COUNT GREATER 0
          MOVE GX-CTOT-BUFFER(1:GX-CTOT-COUNT) TO HH OF GX-CTOT-DT

          IF GX-CTOT-DELIM EQUAL ':'
             MOVE 0 TO GX-CTOT-COUNT
             UNSTRING GX-CTOT-C DELIMITED BY ':' OR
                                             'A' OR
                                             'P' OR
                                             ALL ' '
                INTO MIN OF GX-CTOT-DT 
                        DELIMITER IN GX-CTOT-DELIM
                        COUNT IN GX-CTOT-COUNT
                POINTER GX-CTOT-POINTER
             IF GX-CTOT-COUNT LESS 3
                IF GX-CTOT-DELIM EQUAL ':'
                   MOVE 0 TO GX-CTOT-COUNT
                   UNSTRING GX-CTOT-C DELIMITED BY 'A' OR
                                                   'P' OR
                                                   ALL ' '
                      INTO SS OF GX-CTOT-DT
                           DELIMITER IN GX-CTOT-DELIM
                           COUNT IN GX-CTOT-COUNT
                      POINTER GX-CTOT-POINTER
                   IF GX-CTOT-COUNT GREATER 2
                      SET GX-CTOT-SS-LENGTH TO TRUE
                   END-IF
                ELSE
                   MOVE '00' TO SS OF GX-CTOT-DT
                END-IF
             ELSE
                SET GX-CTOT-MIN-LENGTH TO TRUE
             END-IF
          ELSE
             MOVE '00' TO MIN OF GX-CTOT-DT
             MOVE '00' TO SS OF GX-CTOT-DT
          END-IF
       END-IF
    END-IF

    INSPECT HH OF GX-CTOT-DT REPLACING ALL ' ' BY '0'
    IF HH OF GX-CTOT-DT NUMERIC
       MOVE HH OF GX-CTOT-DT TO GX-CTOT-HH
    ELSE
       MOVE 0 TO GX-CTOT-HH
       SET GX-CTOT-HH-VALUE TO TRUE
    END-IF

    IF GX-CTOT-POINTER NOT GREATER LENGTH OF GX-CTOT-C AND
       GX-CTOT-DELIM = SPACES
       MOVE GX-CTOT-C (GX-CTOT-POINTER:1) TO GX-CTOT-DELIM
    END-IF

    EVALUATE GX-CTOT-DELIM
       WHEN 'P'
          EVALUATE TRUE
             WHEN GX-CTOT-HH LESS 12
                ADD 12 TO GX-CTOT-HH
             WHEN GX-CTOT-HH GREATER 12
                SET GX-CTOT-HH-VALUE TO TRUE
          END-EVALUATE
       WHEN 'A'
          EVALUATE TRUE
             WHEN GX-CTOT-HH = 12
                MOVE 0 TO GX-CTOT-HH
             WHEN GX-CTOT-HH GREATER 12
                SET GX-CTOT-HH-VALUE TO TRUE
          END-EVALUATE
    END-EVALUATE

    MOVE GX-CTOT-HH TO HH OF GX-CTOT-DT

    INSPECT GX-CTOT-DT REPLACING ALL ' ' BY '0'

    IF GX-CTOT-TLEN LESS 8
       MOVE '00' TO SS OF GX-CTOT-DT
    END-IF

    IF GX-CTOT-TLEN LESS 5
       MOVE '00' TO MIN OF GX-CTOT-DT
    END-IF

    MOVE CORR GX-CTOT-DT TO GX-INT-FMT-DATE
    PERFORM GX-ADJUST-INPUT-DATE
    MOVE CORR GX-INT-FMT-DATE TO GX-CTOT-DT

    MOVE CORR GX-CTOT-DT TO GX-TDTIME-INDT
    PERFORM GX-TDTIME
    IF GX-TDTIME-OUTNUM = 0
       SET GX-CTOT-NOT-VALID TO TRUE
    END-IF

    IF NOT GX-CTOT-VALID
       MOVE '0001' TO YY OF GX-CTOT-DT
       MOVE '01'   TO MM OF GX-CTOT-DT
       MOVE '01'   TO DD OF GX-CTOT-DT
       MOVE '00'   TO HH OF GX-CTOT-DT
       MOVE '00'   TO MIN OF GX-CTOT-DT
       MOVE '00'   TO SS OF GX-CTOT-DT
    ELSE IF YY OF GX-CTOT-DT EQUAL '0000' AND
            MM OF GX-CTOT-DT EQUAL '00' AND
            DD OF GX-CTOT-DT EQUAL '00'
            
       MOVE '0001' TO YY OF GX-CTOT-DT
       MOVE '01' TO MM OF GX-CTOT-DT
       MOVE '01' TO DD OF GX-CTOT-DT
    END-IF
    END-IF.
