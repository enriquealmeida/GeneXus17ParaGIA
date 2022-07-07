/*-------------------------------------------------------------------*/
/* DATA BASE REORGANIZATION: CLEAR PREVIOUS REORG. PROGRAMS          */
/*-------------------------------------------------------------------*/
 DBRCLR:     PGM        PARM(&PGMLIB)          
/*-------------------------------------------------------------------*/
/* PARAMETERS: &PGMLIB  (PROGRAMS LIBRARY)                           */
/*-------------------------------------------------------------------*/
             DCL        VAR(&PGMLIB) TYPE(*CHAR) LEN(10)
/*-------------------------------------------------------------------*/
/* Standard error-handling variables                                 */
/*-------------------------------------------------------------------*/
             DCL        VAR(&ERRORSW) TYPE(*LGL)
             DCL        VAR(&MSG)     TYPE(*CHAR) LEN(200)
             DCL        VAR(&MSGID)   TYPE(*CHAR) LEN(7)
             DCL        VAR(&MSGDTA)  TYPE(*CHAR) LEN(79)
             DCL        VAR(&MSGF)    TYPE(*CHAR) LEN(10)
             DCL        VAR(&MSGFLIB) TYPE(*CHAR) LEN(10)

/*-------------------------------------------------------------------*/
/* Standard error-handling monitoring                                */
/*-------------------------------------------------------------------*/
             MONMSG     MSGID(CPF0000 GXM0000) +
                        EXEC(GOTO CMDLBL(STDERR1))

             DLTPGM     PGM(&PGMLIB/DBRALC)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRDLC)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRTMP)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRPGM)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBREXC)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRRNM)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRJRN)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRSAV)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/DBRRDN)
             MONMSG     CPF2105

             DLTPGM     PGM(&PGMLIB/C0*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C1*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C2*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C3*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C4*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C5*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C6*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C7*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C8*)
             MONMSG     CPF2125

             DLTPGM     PGM(&PGMLIB/C9*)
             MONMSG     CPF2125
/*-------------------------------------------------------------------*/
/* Normal end of program                                             */
/*-------------------------------------------------------------------*/
             RETURN

/*-------------------------------------------------------------------*/
/* Standard error-handling routine                                   */
/*-------------------------------------------------------------------*/
 STDERR1:
             IF         COND(&ERRORSW) +
                        THEN(SNDPGMMSG MSGID(CPF9999) +
                                       MSGF(QCPFMSG) +
                                       MSGTYPE(*ESCAPE))

             CHGVAR     VAR(&ERRORSW) +
                        VALUE('1')
 STDERR2:
             RCVMSG     MSGTYPE(*DIAG) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             IF         COND(&MSGID *EQ '       ') +
                        THEN(GOTO CMDLBL(STDERR3))

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*DIAG)

             GOTO       CMDLBL(STDERR2)
 STDERR3:
             RCVMSG     MSGTYPE(*EXCP) +
                        MSGDTA(&MSGDTA) +
                        MSGID(&MSGID) +
                        MSGF(&MSGF) +
                        MSGFLIB(&MSGFLIB)

             SNDPGMMSG  MSGID(&MSGID) +
                        MSGF(&MSGFLIB/&MSGF) +
                        MSGDTA(&MSGDTA) +
                        MSGTYPE(*ESCAPE)
/*-------------------------------------------------------------------*/
/* END OF PROGRAM                                                    */
/*-------------------------------------------------------------------*/
             ENDPGM


