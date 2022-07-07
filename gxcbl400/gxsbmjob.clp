PGM PARM( +
         #2 +
        )
DCL VAR(#2) TYPE(*CHAR) LEN(#3)
SBMJOB CMD( CALL PGM(#1) PARM( +
                              #2 +
                             )) +
#4
ENDPGM

