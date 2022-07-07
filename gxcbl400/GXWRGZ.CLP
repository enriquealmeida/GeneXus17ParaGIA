       PGM PARM(&PGMLIB &DTALIB &APPNAME &GXLIB)

       DCL VAR(&PGMLIB)     TYPE(*CHAR)     LEN(10)
       DCL VAR(&DTALIB)     TYPE(*CHAR)     LEN(10)
       DCL VAR(&APPNAME)    TYPE(*CHAR)     LEN(30)
       DCL VAR(&GXLIB)      TYPE(*CHAR)     LEN(10)

/* Standard error-handling variables                                 */
       DCL       VAR(&ERRORSW)       TYPE(*LGL)
       DCL       VAR(&MSGID)         TYPE(*CHAR)         LEN(7)
       DCL       VAR(&MSGDTA)        TYPE(*CHAR)         LEN(79)
       DCL       VAR(&MSGF)          TYPE(*CHAR)         LEN(10)
       DCL       VAR(&MSGFLIB)       TYPE(*CHAR)         LEN(10)

       MONMSG    MSGID(CPF0000 GXM0000) +
                 EXEC(GOTO CMDLBL(STDERR1))

MAIN:
       ADDLIBLE &GXLIB
       MONMSG CPF2103

       ADDLIBLE &PGMLIB
       MONMSG CPF2103

       ADDLIBLE &DTALIB
       MONMSG CPF2103

       CALL GX/RRGZBTC PARM(&DTALIB &PGMLIB '00')

PGMEND:
       RETURN
/* Standard error-handling routine */
STDERR1:

       IF        COND(&ERRORSW) +
                 THEN(SNDPGMMSG MSGID(CPF9999) +
                           MSGF(QCPFMSG) +
                           MSGTYPE(*ESCAPE))

       CHGVAR    VAR(&ERRORSW) +
                 VALUE('1')
STDERR2:
       RCVMSG    MSGTYPE(*DIAG) +
                 MSGDTA(&MSGDTA) +
                 MSGID(&MSGID) +
                 MSGF(&MSGF) +
                 MSGFLIB(&MSGFLIB)

       IF        COND(&MSGID *EQ '       ') +
                 THEN(GOTO CMDLBL(STDERR3))

       SNDPGMMSG MSGID(&MSGID) +
                 MSGF(&MSGFLIB/&MSGF) +
                 MSGDTA(&MSGDTA) +
                 MSGTYPE(*DIAG)

       GOTO      CMDLBL(STDERR2)
STDERR3:
       RCVMSG    MSGTYPE(*EXCP) +
                 MSGDTA(&MSGDTA) +
                 MSGID(&MSGID) +
                 MSGF(&MSGF) +
                 MSGFLIB(&MSGFLIB)

       SNDPGMMSG MSGID(&MSGID) +
                 MSGF(&MSGFLIB/&MSGF) +
                 MSGDTA(&MSGDTA) +
                 MSGTYPE(*ESCAPE)

/* End of program */
       ENDPGM

